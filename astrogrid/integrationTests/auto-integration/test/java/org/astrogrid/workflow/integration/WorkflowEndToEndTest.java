/*$Id: WorkflowEndToEndTest.java,v 1.3 2004/04/14 15:28:47 nw Exp $
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
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** grand test of workfow - usecase of creating and submitting a workflow.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 * @todo add test of workflow with multiple steps - datacenter and command-line apps, for example.
 *
 */
public class WorkflowEndToEndTest extends AbstractTestForIntegration {
    /**
     * Constructor for WorkflowManagerIntegrationTest.
     * @param arg0
     */
    public WorkflowEndToEndTest(String arg0) {
        super(arg0);
    }
    /*
     * @see AbstractTestForIntegration#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        WorkflowManager manager = ag.getWorkflowManager();
        jes = manager.getJobExecutionService();
        reg = manager.getToolRegistry();
        store = manager.getWorkflowStore();
    }
    
    protected JobExecutionService jes;    
    protected ApplicationRegistry reg;    
    protected WorkflowStore store;

    public void testSimpleWorkflow() throws Exception {
        buildSimpleWorkflowDocument();
        JobURN urn = submitWorkflowDocument();
        Thread.sleep(10000);
        // try retreiving the workflow.
        Workflow w1 = readWorkflowFromJesAndSave(urn, "Simple workflow execution results: ");
       assertEquals("Workflow not completed",ExecutionPhase.COMPLETED,w1.getJobExecutionRecord().getStatus()); // i.e. its not in error
              
    }
    /** use case of a complex workflow containing more than one step 
     * @todo add more checking of the final document here.*/
    public void testComplexWorkflow() throws Exception {
        buildComplexWorkflowDocument();
        JobURN urn = submitWorkflowDocument();
        Thread.sleep(20000);
        Workflow w1 = readWorkflowFromJesAndSave(urn,"Complex workflow execution results :");
        assertEquals("Workflow not completed",ExecutionPhase.COMPLETED,w1.getJobExecutionRecord().getStatus());
    }        
    
//-------------------------------------------------------------------------------

    /** build a simple one-step workflow */
    private void buildSimpleWorkflowDocument() throws WorkflowInterfaceException, ToolValidationException {
            // create a tool
           ApplicationDescription descr = reg.getDescriptionFor(reg.listApplications()[0]);
           assertNotNull("could not get application description",descr);
           Tool tool = descr.createToolFromDefaultInterface();
           assertNotNull("tool is null",tool);
           descr.validate(tool); // shouold be ready to go, with no further config.
           // add a step to the workflow.
           Step step = new Step();
           step.setDescription("single step");
           step.setName("test step");
           step.setTool(tool);
           wf.getSequence().addActivity(step);
           assertTrue("workflow is not valid",wf.isValid());
           // save it in the store,
          // store.saveWorkflow(acc,wf); 
    }

    /** the name / registry key of the dataset provided by the local datacenter 
     * @todo set this correctly 
     */
    public final static String DATASET_NAME = "dunno what to put here ";
    /** the name / registry key of an application that will consume / process the VOTABLE result of a datacenter query
     * @todo set this correctly */
    public final static String VOTABLE_CONSUMER_NAME = "dunno what to put here either";
    /** build a multi-step workflow 
     * @todo add correct parameters to tools*/
    private void buildComplexWorkflowDocument() throws WorkflowInterfaceException, ToolValidationException {
        // build step that queries datacenter
        ApplicationDescription datacenterDescription = reg.getDescriptionFor(DATASET_NAME);
        assertNotNull("Could not find description for datacenter " + DATASET_NAME,datacenterDescription);
        Tool datacenterTool = datacenterDescription.createToolFromDefaultInterface();
        // set up parameters here -- need an input query and an output myspace location at least.
        datacenterDescription.validate(datacenterTool);
        Step s = new Step();
        s.setDescription("Datacenter query");
        s.setName(DATASET_NAME);
        s.setTool(datacenterTool);
        wf.getSequence().addActivity(s);
        // build step that consumes result of the query.
        ApplicationDescription consumerToolDescription = reg.getDescriptionFor(VOTABLE_CONSUMER_NAME);
        assertNotNull("Could not find descriptioni for votable consumer " + VOTABLE_CONSUMER_NAME,consumerToolDescription);
        Tool consumerTool = consumerToolDescription.createToolFromDefaultInterface();
        // set parameters here.
       consumerToolDescription.validate(consumerTool);
        s = new Step();
        s.setDescription("step that consumes result of datacenter query");
        s.setName(VOTABLE_CONSUMER_NAME);
        s.setTool(consumerTool);
        wf.getSequence().addActivity(s);
        
        // finally.
        assertTrue("workflow document not valid",wf.isValid());
       // store.saveWorkflow(acc,wf);
        
    }    

    /** submit the workflow document to JES */
    private JobURN submitWorkflowDocument() throws WorkflowInterfaceException, InterruptedException {
          JobURN urn = jes.submitWorkflow(wf);
           assertNotNull("submitted workflow produced null urn",urn);
           //check its in the list.
           JobSummary summaries[] = jes.readJobList(acc);
           assertNotNull("null job list returned",summaries);
           assertTrue("empty job list returned",summaries.length > 0);
           boolean found = false;
           for (int i = 0; i < summaries.length; i++) {
               if (summaries[i].getJobURN().getContent().equals(urn.getContent())) {
                   found=true;
               }
           }
           assertTrue("job not found in list",found);
        return urn;
    }
    /** read executed workflow document back from jes, save to myspace */
    private Workflow readWorkflowFromJesAndSave(JobURN urn, String workflowName) throws WorkflowInterfaceException {        
           Workflow w1 = jes.readJob(urn);
           assertNotNull("null workflow returned",w1);
            assertEquals("workflow does not have expected name",w1.getName(),wf.getName());
        // dump it to myspace store - then we can look at it later.
       // w1.setName(workflowName + urn.getContent());
       // store.saveWorkflow(acc,w1);       
        return w1;
    }    

    
}


/* 
$Log: WorkflowEndToEndTest.java,v $
Revision 1.3  2004/04/14 15:28:47  nw
updated tests to fit with new WorkspaceStore interface

Revision 1.2  2004/04/14 10:16:40  nw
added to the workflow integration tests

Revision 1.1  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.4  2004/04/06 15:35:28  nw
altered order of things happining

Revision 1.3  2004/04/06 12:08:30  nw
fixes

Revision 1.2  2004/03/17 01:14:37  nw
removed possible infinite loop

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/