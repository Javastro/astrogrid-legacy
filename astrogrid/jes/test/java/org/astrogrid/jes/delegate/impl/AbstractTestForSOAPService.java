/*$Id: AbstractTestForSOAPService.java,v 1.1 2004/03/05 16:16:55 nw Exp $
 * Created on 05-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate.impl;

import org.astrogrid.jes.comm.JobScheduler;
import org.astrogrid.jes.comm.MockSchedulerNotifier;
import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import EDU.oswego.cs.dl.util.concurrent.Mutex;
import EDU.oswego.cs.dl.util.concurrent.Sync;
import junit.framework.TestCase;

/** abstract class for tests that exercise the soap transport.
 * sets up the components needed for a soap test rig.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Mar-2004
 *
 */
public class AbstractTestForSOAPService extends TestCase {
    /**
     * Constructor for AbstractTestForSOAPService.
     * @param arg0
     */
    public AbstractTestForSOAPService(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {        
        notifier = new MySchedulerNotifier();
        ComponentManager cm = new TestComponentManager(notifier);        
        ComponentManager._setInstance(cm);
       // sanity check
        // check we are getting the right kind of notifier.
        assertSame(notifier,ComponentManager.getInstance().getNotifier() );  
    }

    protected MySchedulerNotifier notifier;
    
    /** subclass of componenent manager to configure local service to use a mock.. */
    protected class TestComponentManager extends ComponentManager {
        TestComponentManager(SchedulerNotifier notifier) {
            this.notifier = notifier;
        }
        private SchedulerNotifier notifier; 
        /** build a scheduler notifier, possibly using  the previously-constructed scheduler */     
      protected SchedulerNotifier buildNotifier(JobScheduler scheduler) {
             return notifier;
     }

     protected Dispatcher buildDispatcher(Locator locator) {
         return new MockDispatcher();
     }     
    }
    protected class MySchedulerNotifier extends MockSchedulerNotifier {
        public MySchedulerNotifier() throws InterruptedException {
            barrier = new Mutex(); 
            barrier.acquire();
        }
        public Sync barrier;       
        public void resumeJob(JobIdentifierType id, MessageType mt) throws Exception {
            try {
                assertNotNull(id);
                assertNotNull(mt);
                super.resumeJob(id,mt);
            } finally {
                barrier.release();
            }
        }
    }
}


/* 
$Log: AbstractTestForSOAPService.java,v $
Revision 1.1  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/