/*$Id: AbstractTestForSOAPService.java,v 1.3 2004/03/09 14:23:36 nw Exp $
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

import org.astrogrid.jes.comm.MockSchedulerNotifier;
import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.component.BasicComponentManager;
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.component.ComponentManagerFactory;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import org.picocontainer.MutablePicoContainer;

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
    
    
    /** subclass of componenent manager to configure local service to use a mock.. */
    protected static  class TestComponentManager extends BasicComponentManager {
        public TestComponentManager(Sync barrier) {
            super();    
            MutablePicoContainer pico = super.getContainer();
            // need to remove existing registrations.
            
            // just mock the dispatcher.
            pico.unregisterComponent(Dispatcher.class);
            pico.registerComponentImplementation(Dispatcher.class,MockDispatcher.class);                       
            
            // scheduler that notifies of completion by releasing a barrier
            pico.unregisterComponent(SchedulerNotifier.class);
            pico.registerComponentImplementation(SchedulerNotifier.class,MySchedulerNotifier.class);
            pico.registerComponentInstance(barrier);                  
        }           

    
    }
    /** scheduler notifier that releases barrier when finished */
    protected static class MySchedulerNotifier extends MockSchedulerNotifier {
        public MySchedulerNotifier(Sync barrier) throws InterruptedException {
            this.barrier = barrier;
            barrier.acquire();
        }
        protected final Sync barrier;
     
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
Revision 1.3  2004/03/09 14:23:36  nw
tests that exercise the soap transport

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.4.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.1  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/