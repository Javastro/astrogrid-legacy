/*$Id: SchedulerInternal.java,v 1.11 2008/06/06 13:43:43 nw Exp $
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

import org.joda.time.Duration;



/** Internal interface to a low-level scheduler.
 * 
 * can also be used as a single-threaded background worker.
 * @todo not used in this module - maybe move to background module
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Oct-2005
 *
 */
public interface SchedulerInternal {
    /** execute a periodic task - runs the first time immediately, and then after task.getPeriod() milliseconds*/
    public void schedule(ScheduledTask task);
    
    /** execute a delayed continuation */
    public void schedule(DelayedContinuation task);

    /** execute a simple task after a delay - the task is executed on the timer
     * thread, and should be a simple task */
    public void executeAfterDelay(Duration delay, Runnable task);
    
    /** execute a simple task some time in the future - the task is executed on
     * the timer thread and should be a simple task. */
    public void executeAt(Date d, Runnable task);
    
    
    
	/** a process which should be run after a number of seconds have elapsed, 
	 * does something, and who's product is another delayedContinuation, to run subsequently. */
	public static interface DelayedContinuation {
		/** the computation to execute
		 * 
		 * @return another delayed continuation, or null if no more repetitions are to happen 
		 */
		public DelayedContinuation execute();
		/** the period to wait before executing
		 * 
		 * @return a duration to wait for. {@link Duration.ZERO} 0 indicates 'as soon as possible'
		 */ 
		public Duration getDelay();
		
		/** specify the session to run this continuation as
		 * 
		 * @return the user's session. if null, will run under default session 
		 */
		public Principal getPrincipal();
		
		public String getTitle();
		
	}

}


/* 
$Log: SchedulerInternal.java,v $
Revision 1.11  2008/06/06 13:43:43  nw
corrected documentation.

Revision 1.10  2008/05/09 11:32:34  nw
Incomplete - task 392: joda time

Revision 1.9  2007/11/26 14:44:46  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.8  2007/07/13 23:14:55  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

Revision 1.7  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.6  2007/01/29 16:45:07  nw
cleaned up imports.

Revision 1.5  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.4  2006/05/26 15:19:31  nw
reworked scheduled tasks,

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.30.1  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.1.2.1  2005/11/23 04:44:14  nw
improved interface.

Revision 1.1  2005/11/10 12:05:53  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/