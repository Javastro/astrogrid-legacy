/*$Id: BackgroundExecutor.java,v 1.5 2008/11/04 14:35:49 nw Exp $
 * Created on 30-Nov-2005
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

import EDU.oswego.cs.dl.util.concurrent.Executor;

/** Execute background tasks.
 * assumed that the implementation executes tasks in parallel if possible.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 30-Nov-2005

 */
public interface BackgroundExecutor extends Executor {

    public void executeWorker(BackgroundWorker worker);
    public void interrupt(Runnable r) ;
    
    /** request to execute a runnable on the swing event dispatch thread
     * implementations may choose to ignore this (for testing)
     * 
     * called from backgroundWorker.
     * @param r
     */
    public void executeLaterEDT(Runnable r);

}


/* 
$Log: BackgroundExecutor.java,v $
Revision 1.5  2008/11/04 14:35:49  nw
javadoc polishing

Revision 1.4  2008/04/25 08:59:01  nw
refactored to ease testing.

Revision 1.3  2007/01/29 11:11:37  nw
updated contact details.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.28.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.28.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.28.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.1  2005/12/02 13:43:18  nw
new compoent that manages a pool of threads to execute background processes on
 
*/