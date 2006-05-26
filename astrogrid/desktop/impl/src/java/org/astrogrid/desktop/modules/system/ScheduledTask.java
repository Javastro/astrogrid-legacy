/*$Id: ScheduledTask.java,v 1.3 2006/05/26 15:19:31 nw Exp $
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
 * each 'period' seconds, and the new worker then executed.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2005
 *
 */
public interface ScheduledTask{
	/** create a new worker task - single-user object */
	public BackgroundWorker createWorker();
   public long getPeriod();
   
}


/* 
$Log: ScheduledTask.java,v $
Revision 1.3  2006/05/26 15:19:31  nw
reworked scheduled tasks,

Revision 1.2  2006/05/17 23:57:46  nw
documentation improvements.

Revision 1.1  2005/11/10 12:05:53  nw
big change around for vo lookout
 
*/