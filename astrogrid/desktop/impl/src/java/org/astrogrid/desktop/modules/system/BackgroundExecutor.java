/*$Id: BackgroundExecutor.java,v 1.1 2005/12/02 13:43:18 nw Exp $
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

import javax.swing.ListModel;

import EDU.oswego.cs.dl.util.concurrent.Executor;

/** Interface to a component that manages exection of background processes.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Nov-2005

 */
public interface BackgroundExecutor extends Executor {

    public void executeWorker(BackgroundWorker worker);
    public void interrupt(Runnable r) ;

}


/* 
$Log: BackgroundExecutor.java,v $
Revision 1.1  2005/12/02 13:43:18  nw
new compoent that manages a pool of threads to execute background processes on
 
*/