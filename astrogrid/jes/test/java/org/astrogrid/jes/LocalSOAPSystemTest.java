/*$Id: LocalSOAPSystemTest.java,v 1.3 2004/03/07 21:04:39 nw Exp $
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

import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.component.ComponentManagerFactory;
import org.astrogrid.jes.delegate.impl.SOAPJobControllerTest;
import org.astrogrid.jes.delegate.impl.SOAPJobMonitorTest;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceLocator;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitorServiceLocator;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.ShortCircuitDispatcher;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ConstantParameter;

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

    protected class SOAPTestComponentManager extends TestComponentManager {
        public SOAPTestComponentManager() throws Exception {
            super();    
            JobMonitor monitorDelegate = 
                (new JobMonitorServiceLocator()).getJobMonitorService(new URL(SOAPJobMonitorTest.MONITOR_ENDPOINT));
            MutablePicoContainer pico = super.getContainer();
            // disptcher that short-circuits back to a monitor - set to call soap monitor delegate
            pico.unregisterComponent(Dispatcher.class);
            pico.registerComponentImplementation(Dispatcher.class,ShortCircuitDispatcher.class,
                new Parameter[] {
                    new ConstantParameter(monitorDelegate)
                }
            );
        }     
    
    }
    
    protected JobController getController() throws Exception {
        return  (new JobControllerServiceLocator()).getJobControllerService(new URL(SOAPJobControllerTest.CONTROLLER_ENDPOINT));
    }
    
    protected void setUp() throws Exception {
        // this creates job monitor and job controller services..
        SOAPJobControllerTest.deployLocalController();
        SOAPJobMonitorTest.deployLocalMonitor();
        
        // create store
        ComponentManager cm = new SOAPTestComponentManager();        
        ComponentManagerFactory._setInstance(cm);
        
        WAIT_SECONDS = 20;
    }

    

    

}


/* 
$Log: LocalSOAPSystemTest.java,v $
Revision 1.3  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.2.4.1  2004/03/07 20:42:31  nw
updated tests to work with picocontainer

Revision 1.2  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/