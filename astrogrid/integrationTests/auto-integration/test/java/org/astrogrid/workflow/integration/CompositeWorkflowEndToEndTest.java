/*$Id: CompositeWorkflowEndToEndTest.java,v 1.12 2005/03/14 22:03:53 clq2 Exp $
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
import org.astrogrid.io.Piper;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowManager;
import org.astrogrid.portal.workflow.intf.WorkflowStore;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/** end-to-end test of workfow - usecase of creating and submitting a workflow.
 * <p> 
 * involves jes, cea, registry and myspace, all orchestrated through the workflow library.
 * <p>
 * This is an old acceptance test, which I don't want to alter - however,it doesn't test things thoroughly, and is liable to fail.
 * 
 * Uses TESTAPP and TESTDSA
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class CompositeWorkflowEndToEndTest extends AbstractTestForWorkflow {
    /**
     * Constructor for WorkflowManagerIntegrationTest.
     * @param arg0
     */
    public CompositeWorkflowEndToEndTest(String arg0) {
        super(new String[]{TESTDSA,TESTAPP}, arg0);
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


    
    public void checkExecutionResults(Workflow w1) throws Exception {
        super.checkExecutionResults(w1);
        softAssertEquals("workflow does not have expected name",w1.getName(),wf.getName());

    softAssertEquals("Workflow not completed",ExecutionPhase.COMPLETED,w1.getJobExecutionRecord().getStatus());        
    }
    
   

    /** build a multi-step workflow r*/
    protected void buildWorkflow() throws Exception {
        wf.setName("Complex Workflow");
        // build step that queries datacenter
        ApplicationDescription datacenterDescription = reg.getDescriptionFor(TESTDSA);
        assertNotNull("Could not find description for datacenter " + TESTDSA,datacenterDescription);
        Tool datacenterTool = datacenterDescription.createToolFromDefaultInterface();

        ParameterValue format = (ParameterValue)datacenterTool.findXPathValue("input/parameter[name='Format']");
        assertNotNull(format);
        format.setIndirect(false);
        format.setValue("VOTABLE");
        
        ParameterValue query= (ParameterValue)datacenterTool.findXPathValue("input/parameter[name='Query']");
        assertNotNull(query);
        InputStream is = this.getClass().getResourceAsStream("simple-query.xml");
        assertNotNull(is);
        StringWriter out = new StringWriter();
        Piper.pipe(new InputStreamReader(is),out);
        query.setIndirect(false); 
        query.setValue(out.toString());
                       
        ParameterValue result = (ParameterValue)datacenterTool.findXPathValue("output/parameter[name='Result']");
        assertNotNull(result);
        Ivorn targetIvorn = new Ivorn(MYSPACE,user.getUserId() + "/WorkflowEndToEnd-complexDocument-votable.xml");
        result.setValue(targetIvorn.toString());
        result.setIndirect(true);
        
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
Revision 1.12  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.11.34.1  2005/03/11 17:17:17  nw
changed bunch of tests to use FileManagerClient instead of VoSpaceClient

Revision 1.11  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.8.40.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.8  2004/09/09 10:46:38  nw
removed explicit call to write workflow to vospace - happens under the hood anyhow.

Revision 1.6  2004/08/19 23:48:06  nw
made a child of standard baseclass

Revision 1.5  2004/07/23 08:08:44  nw
upped the timeout value.

Revision 1.4  2004/07/20 02:00:57  nw
tweaked parameters sent to datacenter application

Revision 1.3  2004/07/01 11:47:39  nw
cea refactor

Revision 1.2  2004/04/26 12:17:33  nw
fixed query name

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