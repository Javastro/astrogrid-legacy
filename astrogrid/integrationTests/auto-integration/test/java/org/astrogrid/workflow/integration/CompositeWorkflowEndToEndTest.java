/*$Id: CompositeWorkflowEndToEndTest.java,v 1.1 2004/04/23 00:27:56 nw Exp $
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
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.integration.*;
import org.astrogrid.io.Piper;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.ToolValidationException;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Date;

/** end-to-end test of workfow - usecase of creating and submitting a workflow.
 * <p>
 * involves jes, cea, registry and myspace, all orchestrated through the workflow library.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class CompositeWorkflowEndToEndTest extends AbstractTestForIntegration {
    /**
     * Constructor for WorkflowManagerIntegrationTest.
     * @param arg0
     */
    public CompositeWorkflowEndToEndTest(String arg0) {
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


    /** use case of a complex workflow containing more than one step 
     * @todo add more checking of the final document here.*/
    public void testCompositeWorkflow() throws Exception {
        buildComplexWorkflowDocument();
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
        Thread.sleep(20000);
        Workflow w1 = jes.readJob(urn);
               assertNotNull("null workflow returned",w1);
                assertEquals("workflow does not have expected name",w1.getName(),wf.getName());
            // dump it to myspace store - then we can look at it later.
           w1.setName("CompositeWorkflowEndToEndTest" + "-" + System.currentTimeMillis());
           Ivorn ivorn = new Ivorn(MYSPACE,user.getUserId() +"/" + w1.getName() + ".saved-workflow.xml"); 
           store.saveWorkflow(user,ivorn,w1);
        assertEquals("Workflow not completed",ExecutionPhase.COMPLETED,w1.getJobExecutionRecord().getStatus());
    }        
    
//-------------------------------------------------------------------------------


    /** build a multi-step workflow r*/
    private void buildComplexWorkflowDocument() throws Exception {
        wf.setName("Complex Workflow");
        // build step that queries datacenter
        ApplicationDescription datacenterDescription = reg.getDescriptionFor(TESTDSA);
        assertNotNull("Could not find description for datacenter " + TESTDSA,datacenterDescription);
        Tool datacenterTool = datacenterDescription.createToolFromDefaultInterface();

        ParameterValue query= (ParameterValue)datacenterTool.findXPathValue("input/parameter[name='Query']");
        assertNotNull(query);
        InputStream is = this.getClass().getResourceAsStream("WorkflowEndToEndTest-query.xml");
        assertNotNull(is);
        StringWriter out = new StringWriter();
        Piper.pipe(new InputStreamReader(is),out); 
        query.setValue(out.toString());
                       
        ParameterValue target = (ParameterValue)datacenterTool.findXPathValue("output/parameter[name='Target']");
        assertNotNull(target);
        Ivorn targetIvorn = new Ivorn(MYSPACE,user.getUserId() + "/WorkflowEndToEnd-complexDocument-votable.xml");
        target.setValue(targetIvorn.toString());
        
        
        datacenterDescription.validate(datacenterTool);
        Step s = new Step();
        s.setDescription("Datacenter query");
        s.setName(TESTDSA);
        s.setTool(datacenterTool);
        wf.getSequence().addActivity(s);
        // build step that consumes result of the query.
        ApplicationDescription consumerToolDescription = reg.getDescriptionFor(TESTAPP);
        assertNotNull("Could not find descriptioni for votable consumer " + TESTAPP,consumerToolDescription);
        Tool consumerTool = consumerToolDescription.createToolFromDefaultInterface();
        // set parameters here.
       consumerToolDescription.validate(consumerTool);
        s = new Step();
        s.setDescription("step that consumes result of datacenter query");
        s.setName(TESTAPP);
        s.setTool(consumerTool);
        wf.getSequence().addActivity(s);
        
        // finally.
        assertTrue("workflow document not valid",wf.isValid());
        
    }    

    
}


/* 
$Log: CompositeWorkflowEndToEndTest.java,v $
Revision 1.1  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel

Revision 1.9  2004/04/22 08:58:38  nw
improved

Revision 1.8  2004/04/21 13:43:43  nw
tidied imports

Revision 1.7  2004/04/20 14:48:21  nw
simplified myspace storepoint.

Revision 1.6  2004/04/19 09:35:24  nw
added constants for ivorns of services.
added test query

Revision 1.5  2004/04/15 23:11:20  nw
tweaks

Revision 1.4  2004/04/14 16:42:37  nw
fixed tests to break more sensibly

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