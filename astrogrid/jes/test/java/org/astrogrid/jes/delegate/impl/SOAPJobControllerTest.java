/*$Id: SOAPJobControllerTest.java,v 1.4 2004/03/15 00:06:57 nw Exp $
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

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.component.ComponentManagerFactory;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.jes.delegate.v1.jobcontroller.JesFault;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.testutils.io.FileResourceLoader;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.axis.client.AdminClient;

import EDU.oswego.cs.dl.util.concurrent.Mutex;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/** Test soap transport of controller.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Mar-2004
 *
 */
public class SOAPJobControllerTest extends AbstractTestForSOAPService {
    /**
     * Constructor for SOAPJobControlerTest.
     * @param arg0
     */
    public SOAPJobControllerTest(String arg0) {
        super(arg0);
    }
    
    /** custom suite - sets up soap server and components only once */
    public static Test suite() {
        Test rawSuite = new TestSuite(SOAPJobControllerTest.class);
        return new SOAPTestSetup(rawSuite);
    }
    
    
    private static class SOAPTestSetup extends TestSetup {
        /** Construct a new SOAPTestSetup
         * @param arg0
         */
        public SOAPTestSetup(Test arg0) {
            super(arg0);
        }
        // create test fixture, once
        protected void setUp() throws Exception{   
            ComponentManager cm = new TestComponentManager(new Mutex());        
            ComponentManagerFactory._setInstance(cm);
            assertTrue(ComponentManagerFactory.getInstance().getNotifier() instanceof MyMockJobScheduler); 
            deployLocalController();
        }
    }
    protected void setUp() throws Exception {

        
        delegate = JesDelegateFactory.createJobController(CONTROLLER_ENDPOINT);
        assertNotNull(delegate);
        assertEquals(delegate.getTargetEndPoint(),CONTROLLER_ENDPOINT);

        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/workflow1.xml"));
        assertNotNull(reader);
        wf = Workflow.unmarshalWorkflow(reader);
        assertNotNull(wf);
        reader.close(); 
        unknownURN = new JobURN();
        unknownURN.setContent("jes:unknown:urn");
        
     }
     
    private JobController delegate;
    public static final String CONTROLLER_ENDPOINT = "local:///JobControllerService";
    private Workflow wf;
    private static JobURN unknownURN;
        
     public void testSubmitWorkflow() throws Exception {
         JobURN urn = delegate.submitWorkflow(wf);
         assertNotNull(urn);
         assertNotNull(urn.getContent());
         storedURN = urn;
     }
     
     protected static JobURN storedURN;
     
     /** check it doesn't barf */
     public void testCancelJob() throws Exception {
             delegate.cancelJob(unknownURN);

     }
     /** check it doesn't barf */
     public void testDeleteJob() throws Exception {
         delegate.deleteJob(unknownURN);
     }
     /** try to get list for workflow we just submitted */
     public void testReadJobList() throws Exception {
         assertNotNull(storedURN);
         Account acc = wf.getCredentials().getAccount();
         assertNotNull(acc);
         JobSummary[] arr = delegate.readJobList(acc);
         assertNotNull(arr);
         assertTrue(arr.length > 0);
         JobSummary i = arr[0];
         assertNotNull(i);
         assertEquals(storedURN.getContent(),i.getJobURN().getContent());
     }
     
     public void testUnknownReadJobList() throws Exception {
         
         Account acc = new Account();
         acc.setCommunity("unknown");
         acc.setName("unknown");
         JobSummary[] arr = delegate.readJobList(acc);
         assertNotNull(arr);
         assertEquals(0,arr.length);
     }
     
     public void testReadJob() throws Exception{
         System.out.println("in test read job");
         assertNotNull(storedURN);
         JobFactory fac = ComponentManagerFactory.getInstance().getFacade().getJobFactory();
         Workflow sanityCheck = fac.findJob(storedURN);
         assertNotNull(sanityCheck);
         
         Workflow w = delegate.readJob(storedURN);
         assertNotNull(w);
         assertEquals(storedURN.getContent(),w.getJobExecutionRecord().getJobId().getContent());
     }
     
     public void testFaultyReadJob() throws Exception {
         try {
             delegate.readJob(unknownURN);
             fail("should have barfed");
         } catch (JesDelegateException e) {
             Throwable t = e.getCause();
             assertNotNull(t);
             assertTrue(t instanceof JesFault);
         }
     }
    
    public static void deployLocalController() throws Exception {
        InputStream is = SOAPJobControllerTest.class.getResourceAsStream("/wsdd/JobController-deploy.wsdd");
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
$Log: SOAPJobControllerTest.java,v $
Revision 1.4  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.3  2004/03/09 15:04:42  nw
renamed JobInfo to JobSummary

Revision 1.2  2004/03/09 14:23:36  nw
tests that exercise the soap transport

Revision 1.1  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/