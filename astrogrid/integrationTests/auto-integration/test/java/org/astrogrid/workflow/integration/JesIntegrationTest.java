/*$Id: JesIntegrationTest.java,v 1.2 2004/04/08 14:50:54 nw Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;

import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.scripting.Service;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.util.List;

/** Test jes service is happy.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class JesIntegrationTest extends AbstractTestForIntegration {
    /**
     * Constructor for JesIntegrationTest.
     * @param arg0
     */
    public JesIntegrationTest(String arg0) throws Exception{
        super(arg0);
    }
    protected void setUp() throws Exception {
        super.setUp();
        List apps = ag.getJes();
        assertNotNull(apps);
        assertTrue(apps.size() > 0);
        serv = (Service)apps.get(0);
        delegate = (JobController)serv.createDelegate();
    }
    protected Service serv;
    protected JobController delegate;

    
    public void testSubmitListReadDeleteJob() throws Exception {
        // wf is an empty workflow - no steps. jes should be able to handle these.
        JobURN urn = delegate.submitWorkflow(wf);
        assertNotNull("null urn returned by submission",urn);        

        //list
        JobSummary[] arr = delegate.readJobList(acc);
        assertNotNull("null job list returned",arr);
        assertTrue("empty job list returned",arr.length > 0);
        
        boolean found = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getJobURN().getContent().equals(urn.getContent())) {
                found = true;
            }
        }        
        assertTrue("newly submitted job not in list",found);
        
        //read
        Workflow wf1 = delegate.readJob(urn);
        assertNotNull("read workflow is null",wf1);
        assertEquals("read workflow does not have expected urn",urn.getContent(),wf1.getJobExecutionRecord().getJobId().getContent());
    

       delegate.deleteJob(urn);
       Thread.sleep(2000); // wait for the request to be processed
       try {
            Workflow wf = delegate.readJob(urn);
            assertNull(wf);
            fail("Expected to barf, when reading a deleted job");
       } catch (JesDelegateException e) {
                // ok
            }
    }
    

}


/* 
$Log: JesIntegrationTest.java,v $
Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/