/*$Id: WorkflowEndToEndTest.java,v 1.1 2004/04/08 14:50:54 nw Exp $
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
import org.astrogrid.portal.workflow.intf.WorkflowManager;
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
        manager = ag.getWorkflowManager();    
    }
    protected WorkflowManager manager;

    
    public void testSimpleWorkflow() throws Exception {
        ApplicationRegistry reg = manager.getToolRegistry();
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
       manager.getWorkflowStore().saveWorkflow(acc,wf); 
       // now submit it.

       JobExecutionService jes = manager.getJobExecutionService();
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
           Thread.sleep(10000); // 10 seconds should be enough
        // try retreiving the workflow.
           Workflow w1 = jes.readJob(urn);
           assertNotNull("null workflow returned",w1);
            assertEquals("workflow does not have expected name",w1.getName(),wf.getName());
        // dump it to myspace store - then we can look at it later.
        w1.setName("execution results:" + urn.getContent());
        manager.getWorkflowStore().saveWorkflow(acc,w1);       
       assertEquals("Workflow not completed",ExecutionPhase.COMPLETED,w1.getJobExecutionRecord().getStatus()); // i.e. its not in error
              
    }
    
}


/* 
$Log: WorkflowEndToEndTest.java,v $
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