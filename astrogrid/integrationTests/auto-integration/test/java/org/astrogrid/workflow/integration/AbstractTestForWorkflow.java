/*$Id: AbstractTestForWorkflow.java,v 1.4 2004/08/04 16:49:32 nw Exp $
 * Created on 30-Jun-2004
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
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/** Abstract base class for all tests that fire workflows into jes.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 *
 */
public abstract class AbstractTestForWorkflow extends AbstractTestForIntegration {
    /** Construct a new AbstractTestForWorkflow
     *  Construct a new AbstractTestForWorkflow
     * @param applications - list of application names involved in this workflow.
     * @param arg0
     */
    public AbstractTestForWorkflow(String[] applications,String arg0) {
        super(arg0);
        this.applications = applications;
    }
    protected final String[] applications;

    protected void setUp() throws Exception {
        super.setUp();
        WorkflowManager manager = ag.getWorkflowManager();
        jes = manager.getJobExecutionService();
        reg = manager.getToolRegistry();
    }


    protected JobExecutionService jes;
    protected ApplicationRegistry reg;
    
    protected abstract void buildWorkflow() throws WorkflowInterfaceException;

   public static final long WAIT_TIME = 240 * 1000;

    public void checkExecutionResults(Workflow result) throws Exception{
        softAssertEquals("Workflow not completed",
            ExecutionPhase.COMPLETED,
            result.getJobExecutionRecord().getStatus());
        // i.e. its not in error             
    }

    public void testWorkflow() throws WorkflowInterfaceException, InterruptedException {
        // check registry entries are there. - if not, no point continuing.
        for (int i = 0; i < applications.length; i++) {
            ApplicationDescription description = reg.getDescriptionFor(applications[i]);
            assertNotNull(description);
            assertEquals(applications[i], description.getName());
        }
        JobURN urn = null;
        try {
            buildWorkflow();
            assertTrue("workflow is not valid", wf.isValid());
            urn = jes.submitWorkflow(wf);
            assertNotNull("submitted workflow produced null urn", urn);
            System.out.println(this.getClass().getName() + ": assigned URN is " + urn.getContent());
            //check its in the list.
            JobSummary summaries[] = jes.readJobList(acc);
            softAssertNotNull("null job list returned", summaries);
            softAssertTrue("empty job list returned", summaries.length > 0);
            boolean found = false;
            for (int i = 0; i < summaries.length; i++) {
                if (summaries[i].getJobURN().getContent().equals(urn.getContent())) {
                    found = true;
                }
            }
            softAssertTrue("job not found in list", found);
        }
        catch (Exception e) {
            softFail("testSubmitWorkflow " + e.getMessage());
        }
        // wait for job to complete.
        long startTime = System.currentTimeMillis();
        boolean completed =false;
        while (System.currentTimeMillis() < startTime + WAIT_TIME) {
            try {
                Workflow w11 = jes.readJob(urn);
                if (w11.getJobExecutionRecord() != null
                    && w11.getJobExecutionRecord().getStatus().getType() >= ExecutionPhase.COMPLETED_TYPE) {
                    completed = true;
                    break;
                }  
            }
            catch (WorkflowInterfaceException e1) {
                // doesn't matter - we'll get it next time round.
            }
            Thread.sleep(1000); // have a little breather.   
        }
        softAssertTrue("Job failed to complete in expected time",completed);
        // check results.        
        try {
            Workflow result = jes.readJob(urn);
            assertNotNull("null workflow returned", result);
            checkExecutionResults(result);
        }
        catch (Exception e) {
            softFail("exception in testCheckExecutionResults " + e.getClass().getName() + "\n" + e.getMessage());
        }
        // tidy up.
        /* bad idea deleting the workflows - goot to keep around, no matter whether the test has succeeded
        Workflow w1 = jes.readJob(urn);
        if (w1.getJobExecutionRecord().getStatus().equals(ExecutionPhase.COMPLETED)) {
            jes.deleteJob(urn);
        }
        */
    }
}


/* 
$Log: AbstractTestForWorkflow.java,v $
Revision 1.4  2004/08/04 16:49:32  nw
added test for scripting extensions to workflow

Revision 1.3  2004/07/09 15:49:55  nw
commented out cleanup phase - easier to see whats going on for now

Revision 1.2  2004/07/05 18:32:34  nw
fixed tests

Revision 1.1  2004/07/01 11:47:39  nw
cea refactor
 
*/