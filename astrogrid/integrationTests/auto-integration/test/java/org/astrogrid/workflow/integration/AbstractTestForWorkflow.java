/*$Id: AbstractTestForWorkflow.java,v 1.23 2004/11/24 19:49:22 clq2 Exp $
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
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.Marshaller;
import org.w3c.dom.Document;

import java.io.StringReader;

/** Abstract base class for all tests that fire workflows into jes.
 * <p>
 * 
 * There's a lot of commonality involved in constructing a workflow document, submitting it to the service, polling and waiting, and then inspecting the results. This class
 * captures the common behaviour.
 * {@link #testWorkflow()} is a  skeleton tet method that performs the test procedure -
 *  subclasses should normally only need to override {@link #checkExecutionResults(Workflow)} and {@link #buildWorkflow()}
 * <p />
 * This class also defines a bunch of helper methods that can be used to assert properties of workflows in{@link #checkExecutionResults(Workflow)} 
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Jun-2004
 *
 */
public abstract class AbstractTestForWorkflow extends AbstractTestForIntegration {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(AbstractTestForWorkflow.class);

    /** Construct a new AbstractTestForWorkflow
     *  Construct a new AbstractTestForWorkflow
     * @param applications - list of registry keys of applications involved in this workflow. At an early stage in the test, it will be verifed that these keys do exist in the registry.
     * @param arg0
     */
    public AbstractTestForWorkflow(String[] applications,String arg0) {
        super(arg0);
        this.applications = applications;
    }
    /** keys of applications that must appear in the registry */
    protected final String[] applications;

    protected void setUp() throws Exception {
        super.setUp();
        urn = null;
        WorkflowManager manager = ag.getWorkflowManager();
        jes = manager.getJobExecutionService();
        reg = manager.getToolRegistry();
    }
    /** @NWW - test addition. Added a cleanup routine in the tearDown - will try to cancel / remove the job - so that it doesn't interfere with subsequent tests*/
    protected void tearDown() throws Exception{
        try {
            jes.cancelJob(urn);
        } catch (WorkflowInterfaceException e) {
            logger.info("Exception when attempting to cancel job",e);
        }              
        super.tearDown();
    }
    
  /** an instance of the jes client, initialized in {@link #setup} */
    protected JobExecutionService jes;
    /** an instance of the application registry client, initialized in {@link #setup}*/
    protected ApplicationRegistry reg;
    
    /** abstract method that should be implemented in subclasses to build the workflow document
     * The parent class provides a pre-initialized workflow document in the member variable {@link AbstractTestForIntegration#wf}
     * @throws Exception if something goes wrong.
     */
    protected abstract void buildWorkflow() throws  Exception;
    /** timout is ten minutes for each test. can be overridden in subclasses. */
   public  long WAIT_TIME = 10 * 60 * 1000;
    protected JobURN urn; 
/** checks the results of a JES execution. 
 * this implementation just asserts that the workflow completed. Override to check the workflow document more thoroughly
 * <p>
 * Soft assertions are a good idea here..
 * @param result - resulting workflow, with execution annotations, retreived from the jes server
 * @throws Exception
 */
    public void checkExecutionResults(Workflow result) throws Exception{
        assertWorkflowCompleted(result);
    }

    /** skeleton tet method - perorms the test procedure - subclasses should normally only need to extend {@link #checkExecutionResults(Workflow)} and {@link #buildWorkflow()}
     * <p>
     * <ul>
     * <li>First verifies that required applications do have registry entries
     * <li>calls {@link #buildWorkflow()}
     * <li>writes copy of workflow document to myspace.
     * <li>submits document to jes.
     * <li>verify that it's in the jes queue
     * <li>polls JES until either job completes, or {@link #WAIT_TIME} is exceeded
     * <li>Retreives result document from jes, writes to system.out (so it can be picked up by maven test reports)
     * <li>calls {@link #checkExecutionResults(Workflow)}
     * @throws Exception
     */
    public void testWorkflow() throws Exception {
        // check registry entries are there. - if not, no point continuing.
        for (int i = 0; i < applications.length; i++) {
            ApplicationDescription description = reg.getDescriptionFor(applications[i]);
            assertNotNull(description);
            assertEquals(applications[i], description.getName());
        }
        try {
            buildWorkflow();
            writeWorkflowToVOSpace(wf);
            assertTrue("workflow is not valid", wf.isValid());
            urn = jes.submitWorkflow(wf);
            assertNotNull("submitted workflow produced null urn", urn);
            System.out.println(this.getClass().getName() + ": assigned URN is " + urn.getContent());
            //check its in the list.
            Thread.sleep(2000); // wait 2 secs, to just be safe.
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
               //NWW writeWorkflowResultToVOSpace(w11);
                if (w11.getJobExecutionRecord() != null
                    && w11.getJobExecutionRecord().getStatus().getType() >= ExecutionPhase.COMPLETED_TYPE) {
                    completed = true;
                    break;
                }  
            }
            catch (WorkflowInterfaceException e1) {
                // doesn't matter - we'll get it next time round.
            }
            Thread.sleep(10 * 1000); // have a little breather - standoff for 10 seconds.   
        }
        softAssertTrue("Job failed to complete in expected time",completed);
        // check results.        
            Workflow result = jes.readJob(urn);
            // dump workflow to output, so we can see whats been going on.
            Document doc = XMLUtils.newDocument();
            Marshaller.marshal(result,doc);

            System.err.println(urn.toString());                        
            System.err.print("***WORKFLOW***");
            XMLUtils.PrettyDocumentToStream(doc,System.err);
            System.err.println("***WORKFLOW-END***");
            assertNotNull("null workflow returned", result);
            checkExecutionResults(result);
 
    }

    /**
     * Save a workflow to vospace, using the current test class name.
     * @param workflow The workflow to store.
     *
     */
    protected void writeWorkflowToVOSpace(Workflow workflow) {
      this.writeWorkflowToVOSpace(
        this.getClass().getName() + "-workflow",
        workflow
        ) ;
    }

    /**
     * Save a workflow to vospace, path defaults to 'workflow'
     * @param name     The filename to store it as.
     * @param workflow The workflow to store.
     *
     */
   protected void writeWorkflowToVOSpace(String name, Workflow workflow) {
      this.writeWorkflowToVOSpace(
        "workflow",
        name,
        workflow
        ) ;
    }

    /**
     * Save a workflow to vospace.
     * @param path     The VoSpace path to store it at.
     * @param name     The VoSpace name to store it as.
     * @param workflow The workflow to store.
     *
     */
    protected void writeWorkflowToVOSpace(String path, String name, Workflow workflow) {
        try {
            ag.getWorkflowManager().getWorkflowStore().saveWorkflow(
                user,
                createUniqueIVORN(
                    path,
                    name,
                    "work"
                    ),
                workflow
                );
        } catch (WorkflowInterfaceException e) {
            System.err.println("Could not save workflow to myspace");
            e.printStackTrace(System.err);
        }
    }

    /**
     * Save a workflow result to vospace, using the current test class name.
     * @param workflow The workflow to store.
     *
     */
    protected void writeWorkflowResultToVOSpace(Workflow workflow) {
      this.writeWorkflowResultToVOSpace(
        this.getClass().getName() + "-result",
        workflow
        ) ;
    }

    /**
     * Save a workflow result to vospace, path defaults to 'workflow'
     * @param name     The filename to store it as.
     * @param workflow The workflow to store.
     *
     */
   protected void writeWorkflowResultToVOSpace(String name, Workflow workflow) {
      this.writeWorkflowResultToVOSpace(
        "workflow",
        name,
        workflow
        ) ;
    }

    /**
     * Save a workflow result to vospace.
     * @param path     The VoSpace path to store it at.
     * @param name     The VoSpace name to store it as.
     * @param workflow The workflow to store.
     *
     */
    protected void writeWorkflowResultToVOSpace(String path, String name, Workflow workflow) {
        try {
            ag.getWorkflowManager().getWorkflowStore().saveWorkflow(
                user,
                createUniqueIVORN(
                    path,
                    name,
                    "job"
                    ),
                workflow
                );
        } catch (WorkflowInterfaceException e) {
            System.err.println("Could not save workflow results to myspace");
            e.printStackTrace(System.err);
        }
    }

    // helper assertions to use.
    /** helper assertion - assert that a workflow completed - ie. it's status is 'completed', and a finish time was recorded
     * @param result
     */
    public void assertWorkflowCompleted(Workflow result) {
        // workflow should have been executed and completed.
        assertNotNull("no execution record",result.getJobExecutionRecord());
        JobExecutionRecord jrec = result.getJobExecutionRecord();
        softAssertNotNull("no finish time recorded",jrec.getFinishTime());
        softAssertEquals("status not completed",ExecutionPhase.COMPLETED,jrec.getStatus());
    }

    /** helper assertion - assert that a workflow is in error - no finish timme, and it's status is 'error'*/
    protected void assertWorkflowError(Workflow result) {
        assertNotNull("no execution record",result.getJobExecutionRecord());
        JobExecutionRecord jrec = result.getJobExecutionRecord();
        softAssertNotNull("no finish time recorded",jrec.getFinishTime());
        softAssertEquals("status not completed",ExecutionPhase.ERROR,jrec.getStatus());
    }    
    
    /** helper assertion - assert that a script element output the expected message
     * <p>
     * calls {@link #assertScriptCompleted(Script)}
     * @param sc2 the script element
     * @param msg expected message to see in Stdout.
     */
    protected void assertScriptCompletedWithMessage(Script sc2, String msg) {
        assertScriptCompleted(sc2);
        //check stdout and stderr messages.
        StepExecutionRecord rec = sc2.getStepExecutionRecord(0);        
        MessageType stdout = (MessageType)rec.findXPathValue("message[source='stdout']");
        assertNotNull("no stdout message found",stdout);
        System.out.println(stdout.getContent());
        softAssertEquals("Message was " + stdout.getContent(),msg,stdout.getContent().trim());
    }

    /** helper assertion - assert that a scirpt element was executed only once, and completed successfully
     * @param sc1 the script element to check.
     */
    protected void assertScriptCompleted(Script sc1) {
        softAssertEquals("expected a single execution",1,sc1.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc1.getStepExecutionRecord(0);
        softAssertEquals("expected to complete",ExecutionPhase.COMPLETED,rec.getStatus());
        softAssertTrue("expected some messages",rec.getMessageCount() > 0);
    }
    /** helper assertion - assert that a script element ended in error
     * 
     * @param sc1 the script expected to err
     */
    protected void assertScriptError(Script sc1) {
        softAssertEquals("expected a single execution",1,sc1.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc1.getStepExecutionRecord(0);
        softAssertEquals("expected to err",ExecutionPhase.ERROR,rec.getStatus());
        softAssertTrue("expected some messages",rec.getMessageCount() > 0);
    }    
    /** helper assertion - assert that a script element was not run at all*/
    protected void assertScriptNotRun(Script sc1) {
        softAssertEquals("expected script not to run",0,sc1.getStepExecutionRecordCount());

    }    
    /** helper assertion - assert that a step element ended in 'COMPLETED', after being executed once.
     * @param step
     */        
    protected void assertStepCompleted(Step step) {
        softAssertEquals("expected a single execution",1,step.getStepExecutionRecordCount());
        if (step.getStepExecutionRecordCount() > 0) {
            StepExecutionRecord rec = step.getStepExecutionRecord(0);
            softAssertEquals("expected step to complete",ExecutionPhase.COMPLETED, rec.getStatus());
            softAssertTrue("expected some messages",rec.getMessageCount() > 0);
        } else {
            softFail("No execution record");
        }
    }
    

    /** helper assertion - assert that a step is still running
     * @param step
     */
    protected void assertStepRunning(Step step) {
        softAssertEquals("expected a single execution",1,step.getStepExecutionRecordCount());
        StepExecutionRecord rec = step.getStepExecutionRecord(0);
        softAssertEquals("expected script to be running",ExecutionPhase.RUNNING,rec.getStatus());
        softAssertNotNull("expected a start time",rec.getStartTime());
        softAssertNull("didn't expect a finish time",rec.getFinishTime());
    }
    
    /** access the results returned by a step */
    protected ResultListType getResultOfStep(Step step) throws Exception {
        StepExecutionRecord rec = step.getStepExecutionRecord(0);
        assertNotNull("step execution record was null",rec);

        MessageType result = (MessageType)rec.findXPathValue("message[source='CEA']"); //@todo this is flakey - need to demarcate the result message in a better way.
        assertNotNull("no result message found",result);
        ResultListType resultList = ResultListType.unmarshalResultListType(new StringReader(result.getContent()));
        assertNotNull("result list was null",resultList);
        return resultList;
    }
    /** access the messages a script executioin wrote to stdout */
    protected String getStdoutOfScript(Script sc2) throws Exception {
        softAssertEquals("expected a single execution",1,sc2.getStepExecutionRecordCount());
        StepExecutionRecord rec = sc2.getStepExecutionRecord(0);
        softAssertEquals("expected to complete",ExecutionPhase.COMPLETED, rec.getStatus());
        softAssertTrue("expected some messages",rec.getMessageCount() > 0);
        MessageType stdout = (MessageType)rec.findXPathValue("message[source='stdout']");
        softAssertNotNull("no stdout available",stdout);
        return stdout.getContent();
    }

    /** clone a parameter
     * @param src
     * @return
     */
    protected ParameterValue copyParameter(ParameterValue src) {
        ParameterValue clone = new ParameterValue();
        clone.setEncoding(src.getEncoding());
        clone.setIndirect(src.getIndirect());
        clone.setName(src.getName());
        clone.setValue(src.getValue());
        return clone;
        
    }
        

}


/* 
$Log: AbstractTestForWorkflow.java,v $
Revision 1.23  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.20.2.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.20  2004/11/09 14:11:18  pah
more rationalization of registering

Revision 1.19  2004/09/16 21:58:06  nw
don'y seem able to write out workflow to myspace - seems to cause no end of problems - so commented out for now.

Revision 1.18  2004/09/09 01:19:50  dave
Updated MIME type handling in MySpace.
Extended test coverage for MIME types in FileStore and MySpace.
Added VM memory data to community ServiceStatusData.

Revision 1.17  2004/09/08 13:10:13  nw
made it possible to override timeout

Revision 1.16.2.6  2004/09/07 17:42:22  dave
Changed the result type to *.job.

Revision 1.16.2.5  2004/09/07 16:07:17  dave
Added save workflow results to VoSpace ...

Revision 1.16.2.4  2004/09/07 15:55:42  dave
Refactored save workflow to VoSpace ...

Revision 1.16.2.3  2004/09/07 15:41:57  dave
Reafctored the VoSpace save methods ....

Revision 1.16.2.2  2004/09/07 13:27:03  dave
Added unique ivorn factory to abstract test.

Revision 1.16.2.1  2004/09/07 02:15:15  dave
Fixed myspace path.

Revision 1.16  2004/09/03 13:10:43  nw
saves workflows before and after execution into myspace

Revision 1.15  2004/08/22 01:40:05  nw
improved assertions

Revision 1.14  2004/08/22 01:32:30  nw
increased standoff time too

Revision 1.13  2004/08/22 01:31:15  nw
increased timeout for tests - give them more chance to finish

Revision 1.12  2004/08/19 23:31:54  nw
improved assertion messages

Revision 1.11  2004/08/19 16:27:26  nw
added delimiters around workflow document

Revision 1.10  2004/08/18 16:17:07  nw
added printout of workflow to system.err, so it gets picked up in tests.

Revision 1.9  2004/08/17 13:33:07  nw
added another helper method

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
