/*$Id: LocalSOAPSystemTest.java,v 1.1 2004/03/03 01:13:42 nw Exp $
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

import org.astrogrid.config.Config;
import org.astrogrid.jes.comm.MemoryQueueSchedulerNotifier;
import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceLocator;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitorServiceLocator;
import org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.CastorBeanFacade;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.jobscheduler.policy.RoughPolicy;
import org.astrogrid.jes.testutils.io.FileResourceLoader;

import org.apache.axis.client.AdminClient;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
            protected org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler buildScheduler() {
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
        deployLocalController();
        deployLocalMonitor();
        
        JobMonitor jm = (new JobMonitorServiceLocator()).getJobMonitorService(new URL("local:///JobMonitorService"));
        jc = (new JobControllerServiceLocator()).getJobControllerService(new URL("local:///JobControllerService"));

        // close the loop, pass the monitor to the dispatcher
        disp.setMonitor(jm); // this time, the job monitor is a delegate that communicates via local soap.

    }

    
    protected void deployLocalMonitor() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/wsdd/JobMonitor-deploy.wsdd");
        assertNotNull(is);
        String wsdd = FileResourceLoader.streamToString(is);
        assertNotNull(wsdd);
        PrintWriter pw = new PrintWriter(new FileWriter("JobMonitor-deploy.wsdd"));
        pw.print(wsdd);
        pw.close();
     // deploy our 'server' locally     
        String[] args = {"-l",
                         "local:///AdminService",
                         "JobMonitor-deploy.wsdd"};
        AdminClient.main(args);                
    }
    
    protected void deployLocalController() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/wsdd/JobController-deploy.wsdd");
        assertNotNull(is);
        String wsdd = FileResourceLoader.streamToString(is);
        assertNotNull(wsdd);
        PrintWriter pw = new PrintWriter(new FileWriter("JobController-deploy.wsdd"));
        pw.print(wsdd);
        pw.close();
     // deploy our 'server' locally     
        String[] args = {"-l",
                         "local:///AdminService",
                         "JobController-deploy.wsdd"};
        AdminClient.main(args);        
    }
}


/* 
$Log: LocalSOAPSystemTest.java,v $
Revision 1.1  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model
 
*/