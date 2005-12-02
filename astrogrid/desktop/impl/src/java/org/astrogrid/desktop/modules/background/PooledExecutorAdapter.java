/*$Id: PooledExecutorAdapter.java,v 1.1 2005/12/02 13:43:41 nw Exp $
 * Created on 02-Dec-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.desktop.modules.system.BackgroundExecutor;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/**wraps a BackgroundExecutor to look like a PooledExecutor - which is what is required by the cea system.
 * cea _should_ just depend on the interface Executor - would make this class unnecessary. 
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Dec-2005
 *
 */
public class PooledExecutorAdapter extends PooledExecutor {

    /** Construct a new PooledExecutorAdapter
     * 
     */
    public PooledExecutorAdapter(BackgroundExecutor exec) {
        super();
        this.exec = exec;
    }
    private final BackgroundExecutor exec;

  

    public void execute(Runnable arg0) throws InterruptedException {
        exec.execute(arg0);
    }
}


/* 
$Log: PooledExecutorAdapter.java,v $
Revision 1.1  2005/12/02 13:43:41  nw
linked internal cea into new thread-pool system.
 
*/