/*$Id: FlowWorkflowTest.java,v 1.8 2005/03/14 22:03:53 clq2 Exp $
 * Created on 22-Apr-2004
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
import org.astrogrid.applications.integration.ServerInfo;
import org.astrogrid.applications.integration.commandline.CommandLineProviderServerInfo;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.util.Date;

/** test of a workflow containing a flow. in particular, going to check execution times of steps, to prove steps in flows are executed in parallel
 * <p>
 * calls the TESTAPP application twice
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Apr-2004
 *
 */
public class FlowWorkflowTest extends AbstractTestForWorkflow {
    /** Construct a new FlowWorkflowTest
     * @param arg0
     */
    public FlowWorkflowTest(String arg0) {
        super(new String[]{info.getApplicationName()},arg0);
    }

    private final static ServerInfo info = new CommandLineProviderServerInfo();

    /**
     * @see org.astrogrid.workflow.integration.SimpleCommandlineWorkflowEndToEndTest#testCheckExecutionResults()
     */
    public void checkExecutionResults(Workflow wf) throws Exception {
        super.checkExecutionResults(wf);
        // now examine execution times for each step, both starts should be before both stops.
        AbstractActivity act = wf.getSequence().getActivity(0);
        assertNotNull(act);
        assertTrue(act instanceof Flow);
        Flow theFlow= (Flow)act;
        assertEquals(2,theFlow.getActivityCount());
        Step stepA = (Step)theFlow.getActivity(0);
        Step stepB = (Step)theFlow.getActivity(1);
        // check both steps have been executed once.
        softAssertEquals(1,stepA.getStepExecutionRecordCount());
        softAssertEquals(1,stepB.getStepExecutionRecordCount());
        // and they've completed
        softAssertEquals(ExecutionPhase.COMPLETED,stepA.getStepExecutionRecord(0).getStatus());
        softAssertEquals(ExecutionPhase.COMPLETED,stepB.getStepExecutionRecord(0).getStatus());
        Date startA = stepA.getStepExecutionRecord(0).getStartTime();
        Date startB = stepB.getStepExecutionRecord(0).getStartTime();
        Date endA = stepA.getStepExecutionRecord(0).getFinishTime();
        Date endB = stepB.getStepExecutionRecord(0).getFinishTime();
        assertNotNull(startA);
        assertNotNull(startB);
        assertNotNull(endA);
        assertNotNull(endB);
        
       softAssertTrue(startA.before(endA));
       softAssertTrue(startB.before(endB));
       
       // the important fact - both started before either finished.
       assertTrue("one step started after other finished - suggests they were executed sequentially",startA.before(endB));
       assertTrue("one step started after other finished - suggests they were executed sequentially",startB.before(endA));
    }    

    /** build a workflow containing a flow calling 2 test apps.
     * @see org.astrogrid.workflow.integration.SimpleCommandlineWorkflowEndToEndTest#buildWorkflow()
     */
    protected void buildWorkflow() {
        wf.setName("Flow Workflow Test");
        wf.setDescription("Execute a flow of two concurrent steps, and verify that the scheduler is executing these in parallel");
        Flow flow = new Flow();
        wf.getSequence().addActivity(flow);
        // now add two steps to the flow.
        ApplicationDescription app = null;      
        try {
            app = reg.getDescriptionFor(info.getApplicationName());
        } catch (Exception e) {
            fail("Cannot get app description for " + info.getApplicationName() + " " + e.getMessage());
        }
        Step s1 = createStep(app,"A");
        Step s2 = createStep(app,"B");
        flow.addActivity(s1);
        flow.addActivity(s2);
         
        
    }
    
    private Step createStep(ApplicationDescription descr,String name) {
        Step step = new Step();
        step.setName(name);
        Tool tool = descr.createToolFromDefaultInterface();
        info.populateDirectTool(tool);   
         step.setTool(tool);   
        ParameterValue param = (ParameterValue)tool.findXPathValue("input/parameter[name='P1']");
        assertNotNull(param);
        param.setValue("10"); // think this means it will wait for 10 secs
        return step;
    }
    


}


/* 
$Log: FlowWorkflowTest.java,v $
Revision 1.8  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.7.34.1  2005/03/11 17:17:17  nw
changed bunch of tests to use FileManagerClient instead of VoSpaceClient

Revision 1.7  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.4.76.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.4  2004/08/04 16:49:32  nw
added test for scripting extensions to workflow

Revision 1.3  2004/07/01 11:47:39  nw
cea refactor

Revision 1.2  2004/04/26 12:17:00  nw
got working.

Revision 1.1  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel
 
*/