/*$Id: JesInstallationTest.java,v 1.6 2004/08/17 15:11:50 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.integration.*;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** Test jes service is happy. also exercises the part of workflowManager that talks to jes.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class JesInstallationTest extends AbstractTestForIntegration {
    /**
     * Constructor for JesIntegrationTest.
     * @param arg0
     */
    public JesInstallationTest(String arg0) throws Exception{
        super(arg0);
    }
    protected void setUp() throws Exception {
        super.setUp();
        delegate = ag.getWorkflowManager().getJobExecutionService();
        assertNotNull(delegate);
    }
    protected JobExecutionService delegate;

    
    public void testSubmitListReadDeleteJob() throws Exception {
        // wf is an empty workflow - no steps. jes should be able to handle these.
        JobURN urn = delegate.submitWorkflow(wf);
        assertNotNull("null urn returned by submission",urn);        

        //list
        JobSummary[] arr = delegate.readJobList(acc);
        softAssertNotNull("null job list returned",arr);
        softAssertTrue("empty job list returned",arr.length > 0);
        
        boolean found = false;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getJobURN().getContent().equals(urn.getContent())) {
                found = true;
            }
        }        
        softAssertTrue("newly submitted job not in list",found);
        
        //read
        Thread.sleep(2000); // give it time to process the job.
        Workflow wf1 = delegate.readJob(urn);
        assertNotNull("read workflow is null",wf1);
        softAssertNotNull("read workfow was not executed",wf1.getJobExecutionRecord());
        softAssertEquals("read workfow did not complete",ExecutionPhase.COMPLETED,wf1.getJobExecutionRecord().getStatus());
        softAssertEquals("read workflow does not have expected urn",urn.getContent(),wf1.getJobExecutionRecord().getJobId().getContent());
    

       delegate.deleteJob(urn);
       Thread.sleep(2000); // wait for the request to be processed
       try {
            Workflow wf = delegate.readJob(urn);
            softFail("Expected to barf, when reading a deleted job");
       } catch (WorkflowInterfaceException e) {
                // ok
            }
    }
    

}


/* 
$Log: JesInstallationTest.java,v $
Revision 1.6  2004/08/17 15:11:50  nw
updated some tests

Revision 1.5  2004/07/01 11:47:39  nw
cea refactor

Revision 1.4  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel

Revision 1.3  2004/04/22 08:58:38  nw
improved

Revision 1.2  2004/04/21 13:43:43  nw
tidied imports

Revision 1.1  2004/04/15 23:11:20  nw
tweaks

Revision 1.2  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/