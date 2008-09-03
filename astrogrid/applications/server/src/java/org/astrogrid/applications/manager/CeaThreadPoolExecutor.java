/*$Id: CeaThreadPoolExecutor.java,v 1.2 2008/09/03 14:18:55 pah Exp $
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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import junit.framework.Test;

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.springframework.stereotype.Service;


/** customization of the standard pooled executor - to fit in better with our component system, and to provide default configuration.
 * default configuration is an unbounded queue, max of 4 worker threads, minimum of 4 concurrent threads.
 * @author Noel Winstanley nw@jb.man.ac.uk 14-Sep-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 21 Apr 2008
 * @TODO is this really necessary - could just be incorporated into {@link ThreadPoolExecutionController}
 *
 */
@Service
public class CeaThreadPoolExecutor extends ThreadPoolExecutor implements
            ComponentDescriptor {
    
        /** Construct a new CeaThreadPool
     * 
     */
    public CeaThreadPoolExecutor(ExecutionPolicy policy) {
	//IMPL the keep alive threads is set to the max number of threads 
	super(policy.getMaxConcurrent(),policy.getMaxConcurrent(),0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
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
//FIXME - need to get the servlet lifecycle hooked in here again...perhaps this done in ThreadPoolExecutionController now
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
$Log: CeaThreadPoolExecutor.java,v $
Revision 1.2  2008/09/03 14:18:55  pah
result of merge of pah_cea_1611 branch

Revision 1.1.2.2  2008/05/13 15:57:32  pah
uws with full app running UI is working

Revision 1.1.2.1  2008/04/23 14:14:30  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.3.206.2  2008/04/08 14:45:10  pah
Completed move to using spring as container for webapp - replaced picocontainer

ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708

Revision 1.3.206.1  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.3  2004/09/22 10:52:50  pah
getting rid of some unused imports

Revision 1.2  2004/09/17 01:21:49  nw
implemented execution controller that uses a threadpool

Revision 1.1.2.1  2004/09/14 13:45:22  nw
implemented thread pooling
 
*/