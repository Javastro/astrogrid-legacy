/*$Id: ClockDaemonScheduler.java,v 1.1 2005/11/01 09:19:46 nw Exp $
 * Created on 21-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.picocontainer.Startable;

import EDU.oswego.cs.dl.util.concurrent.ClockDaemon;

/** implmentation of the scheduler using the oswego clock daemon.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Oct-2005
 *
 */
public class ClockDaemonScheduler implements SchedulerInternal, Startable {

    /** Construct a new QuartzScheduler
     * 
     */
    public ClockDaemonScheduler() {
        super();
        daemon = new ClockDaemon();
    }
    private final ClockDaemon daemon;
    

    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        daemon.restart();
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        daemon.shutDown();
    }

    /**
     * @see org.astrogrid.desktop.modules.background.SchedulerInternal#executePeriodically(long, java.lang.Runnable)
     */
    public void executePeriodically(long milliseconds, Runnable task) {
        daemon.executePeriodically(milliseconds,task,false);
    }

}


/* 
$Log: ClockDaemonScheduler.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/