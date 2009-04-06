/*$Id: ClockDaemonScheduler.java,v 1.22 2009/04/06 11:43:20 nw Exp $
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

import java.security.Principal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.builtin.ShutdownListener;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.joda.time.Duration;

import EDU.oswego.cs.dl.util.concurrent.ClockDaemon;
import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;

/** Implementation of the scheduler using the <a href='http://g.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html'>oswego clock daemon</a>.
 * 
 * Just creatres a new backgroundWorker for each task every period seconds..
 * takes a list of services to schedule.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Oct-2005

 */
public class ClockDaemonScheduler implements SchedulerInternal , ShutdownListener{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ClockDaemonScheduler.class);

    /**  
     * 
     */
    public ClockDaemonScheduler(final List tasks, final UIContext context,final SessionManagerInternal session) {
        super();
        this.defaultSession = session.findSessionForKey(session.getDefaultSessionId());
        this.context = context;
        // make shceduled tasks run at low priority, but above the backgound executor tasks.
        this.daemon.setThreadFactory(new ThreadFactory() {
            private final ThreadFactory wrapped = daemon.getThreadFactory();

            public Thread newThread(final Runnable arg0) {
                final Thread t = wrapped.newThread(arg0);
                t.setDaemon(true);
                t.setName("Scheduled Tasks Thread - " + t.getName());
                t.setPriority(Thread.MIN_PRIORITY+3);
                
                return t;
            }
        });
        // start the daemon running.
        daemon.restart();
        // register all known scheduled tasks.
        if (tasks != null) {
        for (final Iterator i = tasks.iterator(); i.hasNext(); ) {
           final Object o = i.next();
           if (o instanceof DelayedContinuation ) {
               final DelayedContinuation d = (DelayedContinuation)o;
               this.schedule(d);
           } else if (o instanceof ScheduledTask){
               final ScheduledTask st = (ScheduledTask)o;
               this.schedule(st);
           } else {
               logger.error("List of services to schedule contains something that isn't a ScheduledTask or DelayedContinuation " + o);
           }
        }          
        }
    }
    // the implementation of the clock.
    final ClockDaemon daemon =  new ClockDaemon();
    final Principal defaultSession;
    final UIContext context;
    
    public void schedule(final ScheduledTask task) {
        daemon.executePeriodically(task.getPeriod(), new Runnable() {
        	public void run() {
        		final BackgroundWorker<Void> worker = new BackgroundWorker<Void>(context,task.getName(),BackgroundWorker.VERY_LONG_TIMEOUT,Thread.MIN_PRIORITY) {

					@Override
                    protected Void construct() throws Exception {
						task.execute(this);
						return null;
					}
        		};
				worker.getControl().setPrincipal(task.getPrincipal() == null ? defaultSession : task.getPrincipal());
				worker.start();
        	}
        }
        		,true); // start it right now.
    }
    
	public void schedule(final DelayedContinuation task) {
		if (task.getDelay().getMillis() < 0) {
			return;
		}
		daemon.executeAfterDelay(task.getDelay().getMillis(),new Runnable() {

			public void run() {// rund on scheduler thread. just submits a new backgroundWorker for execution.
				final BackgroundWorker<Void> worker = new BackgroundWorker<Void>(context,task.getTitle(),BackgroundWorker.LONG_TIMEOUT) {

					@Override
                    protected Void construct() throws Exception {
						final DelayedContinuation next =  task.execute();
						if (next != null) {
							schedule(next); // recursive call.
						}
						return null;
					}
				};
				worker.getControl().setPrincipal(task.getPrincipal() == null ? defaultSession : task.getPrincipal());
				worker.start();
			}
		});
	}

    public void executeAfterDelay(final Duration delay, final Runnable task) {
    	daemon.executeAfterDelay(delay.getMillis(),task);
    }
    
    public void executeAt(final Date d, final Runnable task) {
    	daemon.executeAt(d,task);
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
Revision 1.22  2009/04/06 11:43:20  nw
Complete - taskConvert all to generics.

Incomplete - taskVOSpace VFS integration

Revision 1.21  2009/03/26 18:04:11  nw
source code improvements - cleaned imports, @override, etc.

Revision 1.20  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.19  2008/05/28 12:28:15  nw
Incomplete - task 400: Alternate caching strategy.

Revision 1.18  2008/05/09 11:32:34  nw
Incomplete - task 392: joda time

Revision 1.17  2008/04/25 08:58:04  nw
tested

Revision 1.16  2008/04/23 11:10:19  nw
marked as needing test.

Revision 1.15  2008/03/30 14:44:28  nw
Complete - task 363: race condition at startup.

Revision 1.14  2008/03/05 10:57:59  nw
fix to handle stricter compiler in new eclipse.

Revision 1.13  2007/11/27 07:09:51  nw
integrate commons.io

Revision 1.12  2007/11/26 14:44:46  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.11  2007/11/26 12:01:48  nw
added framework for progress indication for background processes

Revision 1.10  2007/07/13 23:14:55  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

Revision 1.9  2007/04/18 15:47:07  nw
tidied up voexplorer, removed front pane.

Revision 1.8  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.7  2007/01/29 16:45:07  nw
cleaned up imports.

Revision 1.6  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.5  2006/06/15 09:53:16  nw
improvements coming from unit testing

Revision 1.4  2006/05/26 15:19:31  nw
reworked scheduled tasks,

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