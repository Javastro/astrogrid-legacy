/*$Id: SOAPResultListenerTest.java,v 1.2 2004/11/05 16:52:42 jdt Exp $
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

import org.astrogrid.jes.component.JesComponentManager;
import org.astrogrid.jes.component.JesComponentManagerFactory;
import org.astrogrid.jes.jobscheduler.impl.MockSchedulerImpl;
import org.astrogrid.jes.service.v1.cearesults.ResultsListener;
import org.astrogrid.jes.service.v1.cearesults.ResultsListenerServiceLocator;
import org.astrogrid.jes.testutils.io.FileResourceLoader;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;

import org.apache.axis.client.AdminClient;

import EDU.oswego.cs.dl.util.concurrent.Mutex;
import EDU.oswego.cs.dl.util.concurrent.Sync;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

/** tests result listener.
 * this test sets up a proper SOAP connection, and checks that what we pass in is what we get out.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Mar-2004
 *
 */
public class SOAPResultListenerTest extends AbstractTestForSOAPService {
    /**
     * Constructor for SOAPJobMonitorTest.
     * @param arg0
     */
    public SOAPResultListenerTest(String arg0) {
        super(arg0);
    }
    protected Sync barrier;  
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        barrier = new Mutex();
        JesComponentManager cm = new TestComponentManager(barrier);        
        JesComponentManagerFactory._setInstance(cm);
        assertTrue(JesComponentManagerFactory.getInstance().getScheduler() instanceof MyMockJobScheduler);
       deployLocalResultsListener();
       
      delegate = 
           (new ResultsListenerServiceLocator()).getResultListener(new URL(SOAPResultListenerTest.RESULTS_LISTENER_ENDPOINT));
       id = new JobIdentifierType("some arbitrary identifier");


    }
    private ResultsListener delegate;
    private JobIdentifierType id;
        
    public void testEmptyData() throws Exception {
        delegate.putResults(id, new ResultListType());
        assertTrue("notification times out",barrier.attempt(Sync.ONE_SECOND * 10));
        MockSchedulerImpl noti = (MockSchedulerImpl)JesComponentManagerFactory.getInstance().getScheduler();
        assertEquals(1,noti.getCallCount());
    }
    
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public static final String RESULTS_LISTENER_ENDPOINT = "local:///ResultListener";
    /** set up local service */
    public static void deployLocalResultsListener() throws Exception {
        InputStream is = SOAPResultListenerTest.class.getResourceAsStream("/wsdd/CEAResultsListener-deploy.wsdd");
        assertNotNull(is);
        String wsdd = FileResourceLoader.streamToString(is);
        assertNotNull(wsdd);
        PrintWriter pw = new PrintWriter(new FileWriter("CEAResultsListener-deploy.wsdd"));
        pw.print(wsdd);
        pw.close();
     // deploy our 'server' locally     
        String[] args = {"-l",
                         "local:///AdminService",
                         "CEAResultsListener-deploy.wsdd"};
        AdminClient.main(args);                
    }
}

