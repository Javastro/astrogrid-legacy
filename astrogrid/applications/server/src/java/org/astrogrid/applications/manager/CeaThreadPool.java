/*$Id: CeaThreadPool.java,v 1.2 2004/09/17 01:21:49 nw Exp $
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

import org.astrogrid.component.descriptor.ComponentDescriptor;

import org.apache.commons.collections.UnboundedFifoBuffer;
import org.picocontainer.Startable;

import junit.framework.Test;

import EDU.oswego.cs.dl.util.concurrent.Channel;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

/** customization of the standard pooled executor - to fit in better with our component system, and to provide default configuration.
 * default configuration is an unbounded queue, max of 100 worker threads, minimum of 4 concurrent threads.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-Sep-2004
 *
 */
public class CeaThreadPool extends PooledExecutor implements
        ComponentDescriptor, Startable {

    /** Construct a new CeaThreadPool
     * 
     */
    public CeaThreadPool() {
        super(new LinkedQueue(),100);
        super.setMinimumPoolSize(4);
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "CeaThreadPool";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Current number of worker threads " + super.getPoolSize();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }

    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
    }

    /** tell the threads in the pool to stop processing.
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        this.shutdownNow();
    }


}


/* 
$Log: CeaThreadPool.java,v $
Revision 1.2  2004/09/17 01:21:49  nw
implemented execution controller that uses a threadpool

Revision 1.1.2.1  2004/09/14 13:45:22  nw
implemented thread pooling
 
*/