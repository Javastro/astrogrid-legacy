/*$Id: LocalSOAPSystemTest.java,v 1.2 2004/03/05 16:16:55 nw Exp $
 * Created on 01-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes;

import org.astrogrid.jes.comm.JobScheduler;
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.delegate.impl.SOAPJobControllerTest;
import org.astrogrid.jes.delegate.impl.SOAPJobMonitorTest;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceLocator;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitorServiceLocator;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;

import java.net.URL;

/** Tests same functionality as {@link InMemorySystemTest} but uses local SOAP-transport between services, rather than
 * direct method call. So exercises marshalling / unmarshalling to XML in JobController and JobMonitor
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Mar-2004
 *
 */
public class LocalSOAPSystemTest extends InMemorySystemTest {
    /** Construct a new LocalSOAPSystemTest
     * @param arg
     */
    public LocalSOAPSystemTest(String arg) {
        super(arg);
    }

    private class TestComponentManager extends ComponentManager {
        public TestComponentManager() {
            super();            
        }        

        /** build a job scheduler */
            protected JobScheduler buildScheduler() {
                    Locator locator = buildLocator();  
                     Policy policy = buildPolicy();
                     disp = new MockDispatcher();
                     return  new ObservableJobScheduler(facade,disp,policy,barrier);       
            }        

    }
    
    protected void setUp() throws Exception {
        // create store
        ComponentManager cm = new TestComponentManager();        
        ComponentManager._setInstance(cm);

        BeanFacade facade = cm.getFacade();
        factory = (AbstractJobFactoryImpl)facade.getJobFactory();

        // this creates job monitor and job controller services..
        SOAPJobControllerTest.deployLocalController();
        SOAPJobMonitorTest.deployLocalMonitor();
        
        JobMonitor jm = (new JobMonitorServiceLocator()).getJobMonitorService(new URL(SOAPJobMonitorTest.MONITOR_ENDPOINT));
        jc = (new JobControllerServiceLocator()).getJobControllerService(new URL(SOAPJobControllerTest.CONTROLLER_ENDPOINT));

        // close the loop, pass the monitor to the dispatcher
        disp.setMonitor(jm); // this time, the job monitor is a delegate that communicates via local soap.
        // as we're going by soap, set up a longer time to block.
        WAIT_SECONDS = 20;

    }

    

    

}


/* 
$Log: LocalSOAPSystemTest.java,v $
Revision 1.2  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/