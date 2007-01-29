/*$Id: SchedulerInternal.java,v 1.5 2007/01/29 11:11:37 nw Exp $
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

import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Internal interface to a low-level scheduler.
 * 
 * can also be used as a single-threaded background worker.
 * @todo not used in this module - maybe move to background module
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 21-Oct-2005
 *
 */
public interface SchedulerInternal {
    /** execute a task periodically */
    public void executePeriodically(ScheduledTask task);

}


/* 
$Log: SchedulerInternal.java,v $
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