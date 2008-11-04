/*$Id: ScheduledTask.java,v 1.13 2008/11/04 14:35:49 nw Exp $
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

import java.security.Principal;

import org.astrogrid.desktop.modules.ui.WorkerProgressReporter;

/** A schedulable task.
 * the execute method is called each 'period' milliseconds.
 * after a first initial execution.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 05-Nov-2005
 *
 */
public interface ScheduledTask{

    public String getName();
    
    /** perform the execution of the scheduled task
     * 
     * @param reporter - interface to report progress.
     */
	public void execute(WorkerProgressReporter reporter);
	/** period (milliseconds) after which to repeat this task */
   public long getPeriod();
	/** specify the session to run this continuation as
	 * 
	 * @return the user's session. if null, will run under default session 
	 */   
   public Principal getPrincipal();
   
}


/* 
$Log: ScheduledTask.java,v $
Revision 1.13  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.12  2008/06/06 13:44:50  nw
corrected documentation.

Revision 1.11  2007/11/27 07:09:51  nw
integrate commons.io

Revision 1.10  2007/11/26 14:44:46  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.9  2007/09/21 16:35:13  nw
improved error reporting,
various code-review tweaks.

Revision 1.8  2007/07/13 23:14:55  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

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