/*$Id: AbstractTestForWorkflow.java,v 1.8 2004/08/12 22:05:22 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
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
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.StringReader;

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
    
    protected abstract void buildWorkflow() throws  Exception;

   public static final long WAIT_TIME = 240 * 1000;
/** override this to do fuller tests */
    public void checkExecutionResults(Workflow result) throws Exception{
        assertWorkflowCompleted(result);
    }

    public void testWorkflow() throws Exception {
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
            Workflow result = jes.readJob(urn);
            assertNotNull("null workflow returned", result);
            checkExecutionResults(result);
 
    }
    
    // helper assertions to use.
    /**
     * @param result
     */
    public void assertWorkflowCompleted(Workflow result) {
        // workflow should have been executed and completed.
        assertNotNull("no execution record",result.getJobExecutionRecord());
        JobExecutionRecord jrec = result.getJobExecutionRecord();
        softAssertNotNull("no finish time recorded",jrec.getFinishTime());
        softAssertEquals("status not completed",ExecutionPhase.COMPLETED,jrec.getStatus());
    }

    protected void assertWorkflowError(Workflow result) {
        // workflow should have been executed and completed.
        assertNotNull(result.getJobExecutionRecord());
        JobExecutionRecord jrec = result.getJobExecutionRecord();
        softAssertNotNull("no finish time recorded",jrec.getFinishTime());
        softAssertEquals("status not completed",ExecutionPhase.ERROR,jrec.getStatus());
    }    
    
    /**
     * @param sc2
     */
    protected void assertScriptCompletedWithMessage(Script sc2, String msg) {
        softAssertEquals("expected a single execution",1,sc2.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc2.getStepExecutionRecord(0);
        softAssertEquals("expected to complete",ExecutionPhase.COMPLETED, rec.getStatus());
        softAssertTrue("expected some messages",rec.getMessageCount() > 0);
        //check stdout and stderr messages.
        MessageType stdout = (MessageType)rec.findXPathValue("message[source='stdout']");
        assertNotNull("no stdout message found",stdout);
        System.out.println(stdout.getContent());
        softAssertEquals("Message was " + stdout.getContent(),msg,stdout.getContent().trim());
    }

    /**
     * @param sc1
     */
    protected void assertScriptCompleted(Script sc1) {
        softAssertEquals("expected a single execution",1,sc1.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc1.getStepExecutionRecord(0);
        softAssertEquals("expected to complete",ExecutionPhase.COMPLETED,rec.getStatus());
        softAssertTrue("expected some messages",rec.getMessageCount() > 0);
    }
    protected void assertScriptError(Script sc1) {
        softAssertEquals("expected a single execution",1,sc1.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc1.getStepExecutionRecord(0);
        softAssertEquals("expected to err",ExecutionPhase.ERROR,rec.getStatus());
        softAssertTrue("expected some messages",rec.getMessageCount() > 0);
    }    
    protected void assertScriptNotRun(Script sc1) {
        softAssertEquals("expected script not to run",0,sc1.getStepExecutionRecordCount());

    }    
    /**
     * @param step
     */
    protected void assertStepCompleted(Step step) {
        softAssertEquals("expected a single execution",1,step.getStepExecutionRecordCount());
        StepExecutionRecord rec = step.getStepExecutionRecord(0);
        softAssertEquals("expected step to complete",ExecutionPhase.COMPLETED, rec.getStatus());
        softAssertTrue("expected some messages",rec.getMessageCount() > 0);
    }
    

    /**
     * @param step
     */
    protected void assertStepRunning(Step step) {
        softAssertEquals("expected a single execution",1,step.getStepExecutionRecordCount());
        StepExecutionRecord rec = step.getStepExecutionRecord(0);
        softAssertEquals("expected script to be running",ExecutionPhase.RUNNING,rec.getStatus());
        softAssertNotNull("expected a start time",rec.getStartTime());
        softAssertNull("didn't expect a finish time",rec.getFinishTime());
    }
    
    protected ResultListType getResultOfStep(Step step) throws Exception {
        StepExecutionRecord rec = step.getStepExecutionRecord(0);
        assertNotNull(rec);

        MessageType result = (MessageType)rec.findXPathValue("message[source='CEA']"); //@todo this is flakey - need to demarcate the result message in a better way.
        assertNotNull(result);
        ResultListType resultList = ResultListType.unmarshalResultListType(new StringReader(result.getContent()));
        assertNotNull(resultList);
        return resultList;
    }
    
    protected String getStdoutOfScript(Script sc2) throws Exception {
        softAssertEquals("expected a single execution",1,sc2.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc2.getStepExecutionRecord(0);
        softAssertEquals("expected to complete",ExecutionPhase.COMPLETED, rec.getStatus());
        softAssertTrue("expected some messages",rec.getMessageCount() > 0);
        MessageType stdout = (MessageType)rec.findXPathValue("message[source='stdout']");
        softAssertNotNull("no stdout available",stdout);
        return stdout.getContent();
    }
        
}


/* 
$Log: AbstractTestForWorkflow.java,v $
Revision 1.8  2004/08/12 22:05:22  nw
added more helper methods

Revision 1.7  2004/08/12 21:29:54  nw
added helper assertion methods

Revision 1.6  2004/08/12 14:29:39  nw
loosened up an exception type

Revision 1.5  2004/08/05 08:14:20  nw
minor improvement

Revision 1.4  2004/08/04 16:49:32  nw
added test for scripting extensions to workflow

Revision 1.3  2004/07/09 15:49:55  nw
commented out cleanup phase - easier to see whats going on for now

Revision 1.2  2004/07/05 18:32:34  nw
fixed tests

Revision 1.1  2004/07/01 11:47:39  nw
cea refactor
 
*/