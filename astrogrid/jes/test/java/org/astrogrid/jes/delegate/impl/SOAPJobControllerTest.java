/*$Id: SOAPJobControllerTest.java,v 1.1 2004/03/05 16:16:55 nw Exp $
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

import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.jes.testutils.io.FileResourceLoader;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.axis.client.AdminClient;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

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
    protected void setUp() throws Exception {
        super.setUp();
        deployLocalController();
        
        delegate = JesDelegateFactory.createJobController(CONTROLLER_ENDPOINT);
        assertNotNull(delegate);
        assertEquals(delegate.getTargetEndPoint(),CONTROLLER_ENDPOINT);

        Reader reader = new InputStreamReader(this.getClass().getResourceAsStream("/workflow1.xml"));
        assertNotNull(reader);
        wf = Workflow.unmarshalWorkflow(reader);
        assertNotNull(wf);
        reader.close(); 
     }
     
    private JobController delegate;
    public static final String CONTROLLER_ENDPOINT = "local:///JobControllerService";
    private Workflow wf;
        
     public void testSubmitJob() throws Exception {
         JobURN urn = delegate.submitJob(wf);
        // assertTrue("notification times out",notifier.barrier.attempt(Sync.ONE_SECOND * 10));
        // assertEquals(1,notifier.getCallCount());
         assertNotNull(urn);
         assertNotNull(urn.getContent());
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
Revision 1.1  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/