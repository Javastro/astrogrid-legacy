/*$Id: ThreadPoolExecutionController.java,v 1.2 2004/09/17 01:21:49 nw Exp $
 * Created on 14-Sep-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.workflow.beans.v1.Tool;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

import junit.framework.Test;

/** implementation of {@link org.astrogrid.applications.manager.ExecutionController} that manages a pool of workr threads.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-Sep-2004
 *
 */
public class ThreadPoolExecutionController extends DefaultExecutionController {


    /** Construct a new ThreadPoolExecutionController
     * @param library
     * @param executionHistory
     */
    public ThreadPoolExecutionController(ApplicationDescriptionLibrary library, ExecutionHistory executionHistory, PooledExecutor executor) {
        super(library, executionHistory);
        this.executor = executor;
    }
    protected final PooledExecutor executor;


    protected boolean startRunnable(Runnable r) {
        try {
            executor.execute(r);
            return true;
        } catch (InterruptedException e) {
            logger.debug("InterruptedException",e);
            return false;
        }
    }

     
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Thread Pool Execution Controller";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Thread pool info " + executor.getClass().getName() + "\n" + executor.toString();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }


}


/* 
$Log: ThreadPoolExecutionController.java,v $
Revision 1.2  2004/09/17 01:21:49  nw
implemented execution controller that uses a threadpool

Revision 1.1.2.1  2004/09/14 13:45:22  nw
implemented thread pooling
 
*/