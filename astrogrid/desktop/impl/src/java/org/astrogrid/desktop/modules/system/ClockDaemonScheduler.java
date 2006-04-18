/*$Id: ClockDaemonScheduler.java,v 1.3 2006/04/18 23:25:44 nw Exp $
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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.builtin.ShutdownListener;

import EDU.oswego.cs.dl.util.concurrent.ClockDaemon;
import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;

/** implmentation of the scheduler using the oswego clock daemon.
 * 
 * takes a list of services to schedule.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Oct-2005
 *
 */
public class ClockDaemonScheduler implements SchedulerInternal , ShutdownListener{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ClockDaemonScheduler.class);

    /** 
     * 
     */
    public ClockDaemonScheduler(final List tasks) {
        super();
        this.daemon = new ClockDaemon();
        // make shceduled tasks run at low priority, but above the backgound executor tasks.
        this.daemon.setThreadFactory(new ThreadFactory() {
            private final ThreadFactory wrapped = daemon.getThreadFactory();

            public Thread newThread(Runnable arg0) {
                Thread t = wrapped.newThread(arg0);
                t.setDaemon(true);
                t.setName("Scheduled Tasks Thread - " + t.getName());
                t.setPriority(Thread.MIN_PRIORITY+3);
                return t;
            }
        });
        // start the daemon running.
        daemon.restart();
        // register all known scheduled tasks.
        for (Iterator i = tasks.iterator(); i.hasNext(); ) {
           Object o = i.next();
           if (! (o instanceof ScheduledTask)) {
               logger.error("List of services to schedule contains something that isn't a ScheduledTask - " + o);
           } else {
               ScheduledTask st = (ScheduledTask)o;
               this.executePeriodically(st.getPeriod(),st);
           }
        }          
    }
    // the implementation of the clock.
    final ClockDaemon daemon;


    public void executePeriodically(long milliseconds, Runnable task) {
        daemon.executePeriodically(milliseconds,task,false);
    }

       
    public void runNow(Runnable key) {
        // assume key is the runnable..
        daemon.executeAfterDelay(1L,key); // execute as soon as possible
    }

    public void halting() {
        daemon.shutDown();        
    }

    public String lastChance() {
        return null; // don't care.
    }

}


/* 
$Log: ClockDaemonScheduler.java,v $
Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.30.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2.30.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2.30.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.2  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.1.2.1  2005/11/23 04:43:57  nw
tuned threads to run at low priority.

Revision 1.1  2005/11/10 12:05:53  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/