/*$Id: ClockDaemonScheduler.java,v 1.2 2005/11/24 01:13:24 nw Exp $
 * Created on 21-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.desktop.framework.DefaultModule;
import org.astrogrid.desktop.framework.MutableACR;
import org.astrogrid.desktop.framework.NewModuleEvent;
import org.astrogrid.desktop.framework.NewModuleListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.Startable;

import EDU.oswego.cs.dl.util.concurrent.ClockDaemon;
import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;

import java.util.Iterator;
import java.util.List;

/** implmentation of the scheduler using the oswego clock daemon.
 * 
 * on startup automatically registers any available instances of ScheduledTask in any modules.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Oct-2005
 *
 */
public class ClockDaemonScheduler implements SchedulerInternal, Startable, NewModuleListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ClockDaemonScheduler.class);

    /** Construct a new QuartzScheduler
     * 
     */
    public ClockDaemonScheduler(MutableACR reg) {
        super();
        this.daemon = new ClockDaemon();
        // make shceduled tasks run at lowest priority.
        this.daemon.setThreadFactory(new ThreadFactory() {
            private final ThreadFactory wrapped = daemon.getThreadFactory();

            public Thread newThread(Runnable arg0) {
                Thread t = wrapped.newThread(arg0);
                t.setName("Scheduled Tasks Thread - " + t.getName());
                t.setPriority(Thread.MIN_PRIORITY);
                return t;
            }
        });
        reg.addNewModuleListener(this);
    }
    // the implementation of the clock.
    private final ClockDaemon daemon;

    public void start() {      
        daemon.restart();
    }

    public void stop() {
        daemon.shutDown();
    }

    public void executePeriodically(long milliseconds, Runnable task) {
        daemon.executePeriodically(milliseconds,task,false);
    }

    /** listens to the ACR registry - scans new modules for class that implement 'ScheduledTask'
     * and adds them to the scheduler - means that we can inject clock 'ticks' into any other component.
     * @see org.astrogrid.desktop.framework.NewModuleListener#newModuleRegistered(org.astrogrid.desktop.framework.NewModuleEvent)
     */
    public void newModuleRegistered(NewModuleEvent e) {
        DefaultModule nu = (DefaultModule)e.getModule();
        List l = nu.getComponentInstancesOfType(ScheduledTask.class);        
        logger.info("Registering " + l.size() +" background tasks");
        for (Iterator i = l.iterator(); i.hasNext(); ) {
            ScheduledTask st = (ScheduledTask)i.next();
            this.executePeriodically(st.getPeriod(),st);
        }           
    }
    
   
    public void runNow(Runnable key) {
        // assume key is the runnable..
        daemon.executeAfterDelay(1L,key); // execute as soon as possible
    }

}


/* 
$Log: ClockDaemonScheduler.java,v $
Revision 1.2  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.1.2.1  2005/11/23 04:43:57  nw
tuned threads to run at low priority.

Revision 1.1  2005/11/10 12:05:53  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/