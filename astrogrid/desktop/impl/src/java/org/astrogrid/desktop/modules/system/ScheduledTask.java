/*$Id: ScheduledTask.java,v 1.7 2007/04/18 15:47:06 nw Exp $
 * Created on 05-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** interface to a schedulable task.
 * basically iit's a factory for background workers, and a period. The factory is called 
 * each 'period' <b>milliseconds</b>, and the new worker then executed.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 05-Nov-2005
 *
 */
public interface ScheduledTask{
	/** create a new worker task - single-use object
	 * if the worker is to be executed in a particular session, the principal property it should be populated with the principal for that session.
	 * Else it will be executed in the default session.
	 *  
	 *  */
	public BackgroundWorker createWorker();
	/** period after which to repeat this task */
   public long getPeriod();
   
}


/* 
$Log: ScheduledTask.java,v $
Revision 1.7  2007/04/18 15:47:06  nw
tidied up voexplorer, removed front pane.

Revision 1.6  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.5  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.4  2007/01/09 16:24:50  nw
doc fix.

Revision 1.3  2006/05/26 15:19:31  nw
reworked scheduled tasks,

Revision 1.2  2006/05/17 23:57:46  nw
documentation improvements.

Revision 1.1  2005/11/10 12:05:53  nw
big change around for vo lookout
 
*/