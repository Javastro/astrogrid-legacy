/*$Id: SOAPJobMonitorTest.java,v 1.3 2004/03/09 14:23:36 nw Exp $
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
import org.astrogrid.jes.component.ComponentManager;
import org.astrogrid.jes.component.ComponentManagerFactory;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobMonitor;
import org.astrogrid.jes.testutils.io.FileResourceLoader;
import org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.LogLevel;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import org.apache.axis.client.AdminClient;

import EDU.oswego.cs.dl.util.concurrent.Mutex;
import EDU.oswego.cs.dl.util.concurrent.Sync;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Calendar;

/** Have been seeing problem with the job monitor - data is not being transported over SOAP
 * this test sets up a proper SOAP connection, and checks that what we pass in is what we get out.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Mar-2004
 *
 */
public class SOAPJobMonitorTest extends AbstractTestForSOAPService {
    /**
     * Constructor for SOAPJobMonitorTest.
     * @param arg0
     */
    public SOAPJobMonitorTest(String arg0) {
        super(arg0);
    }
    protected Sync barrier;  
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        barrier = new Mutex();
        ComponentManager cm = new TestComponentManager(barrier);        
        ComponentManagerFactory._setInstance(cm);
        assertTrue(ComponentManagerFactory.getInstance().getNotifier() instanceof MySchedulerNotifier);
       deployLocalMonitor();
       delegate = JesDelegateFactory.createJobMonitor(MONITOR_ENDPOINT);
       assertNotNull(delegate);
       assertEquals(delegate.getTargetEndPoint(),MONITOR_ENDPOINT);
       id = new JobIdentifierType("some arbitrary identifier");
       info = new MessageType();
       info.setContent("message");
       info.setLevel(LogLevel.info);
       info.setPhase(ExecutionPhase.RUNNING);
       info.setSource("test");
       info.setTimestamp(Calendar.getInstance());

    }
    private JobMonitor delegate;
    private JobIdentifierType id;
    private MessageType info;
        
    public void testEmptyData() throws Exception {
        delegate.monitorJob(id,info);
        assertTrue("notification times out",barrier.attempt(Sync.ONE_SECOND * 10));
        MockSchedulerNotifier noti = (MockSchedulerNotifier)ComponentManagerFactory.getInstance().getNotifier();
        assertEquals(1,noti.getCallCount());
    }
    
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public static final String MONITOR_ENDPOINT = "local:///JobMonitorService";
    /** set up local service */
    public static void deployLocalMonitor() throws Exception {
        InputStream is = SOAPJobMonitorTest.class.getResourceAsStream("/wsdd/JobMonitor-deploy.wsdd");
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
}


/* 
$Log: SOAPJobMonitorTest.java,v $
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