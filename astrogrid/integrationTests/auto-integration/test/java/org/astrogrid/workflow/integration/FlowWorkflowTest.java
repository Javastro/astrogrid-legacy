/*$Id: FlowWorkflowTest.java,v 1.1 2004/04/23 00:27:56 nw Exp $
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
import org.astrogrid.portal.workflow.design.activity.Activity;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.ActivityContainer;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

/** test of a workflow containing a flow. in particular, going to check execution times of steps, to prove steps in flows are executed in parallel
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Apr-2004
 *
 */
public class FlowWorkflowTest extends SimpleCommandlineWorkflowEndToEndTest {
    /** Construct a new FlowWorkflowTest
     * @param arg0
     */
    public FlowWorkflowTest(String arg0) {
        super(arg0);
    }
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        targetApplication = TESTAPP2;
    }
    /**
     * @see org.astrogrid.workflow.integration.SimpleCommandlineWorkflowEndToEndTest#testCheckExecutionResults()
     */
    public void testCheckExecutionResults() throws Exception {
        super.testCheckExecutionResults();
        // now examine execution times for each step, both starts should be before both stops.
        AbstractActivity act = wf.getSequence().getActivity(0);
        assertNotNull(act);
        assertTrue(act instanceof Flow);
        Flow theFlow= (Flow)act;
        assertEquals(2,theFlow.getActivityCount());
        Step stepA = (Step)theFlow.getActivity(0);
        Step stepB = (Step)theFlow.getActivity(1);
        // check both steps have been executed once.
        assertEquals(1,stepA.getStepExecutionRecordCount());
        assertEquals(1,stepB.getStepExecutionRecordCount());
        // and they've completed
        assertEquals(ExecutionPhase.COMPLETED,stepA.getStepExecutionRecord(0).getStatus());
        assertEquals(ExecutionPhase.COMPLETED,stepB.getStepExecutionRecord(0).getStatus());
        Date startA = stepA.getStepExecutionRecord(0).getStartTime();
        Date startB = stepB.getStepExecutionRecord(0).getStartTime();
        Date endA = stepA.getStepExecutionRecord(0).getFinishTime();
        Date endB = stepB.getStepExecutionRecord(0).getFinishTime();
        assertNotNull(startA);
        assertNotNull(startB);
        assertNotNull(endA);
        assertNotNull(endB);
        
       assertTrue(startA.before(endA));
       assertTrue(startB.before(endB));
       
       // the important fact - both started before either finished.
       assertTrue("one step started after other finished - suggests they were executed sequentially",startA.before(endB));
       assertTrue("one step started after other finished - suggests they were executed sequentially",startB.before(endA));
    }    

    /** build a workflow containing a flow calling 2 test apps.
     * @see org.astrogrid.workflow.integration.SimpleCommandlineWorkflowEndToEndTest#buildWorkflow()
     */
    protected void buildWorkflow() throws Exception {
        wf.setName("Flow Workflow Test");
        Flow flow = new Flow();
        wf.getSequence().addActivity(flow);
        // now add two steps to the flow.
        ApplicationDescription app = reg.getDescriptionFor(TESTAPP2);
        Step s1 = createStep(app);
        s1.setName("A");
        Step s2 = createStep(app);
        s2.setName("B");
        flow.addActivity(s1);
        flow.addActivity(s2);
         
        
    }
    
    private Step createStep(ApplicationDescription descr) {
        Step step = new Step();
        Tool tool = descr.createToolFromDefaultInterface();      
         step.setTool(tool);   
        ParameterValue param = (ParameterValue)tool.findXPathValue("input/parameter[name='p2']");
        assertNotNull(param);
        param.setValue("10"); // think this means it will wait for 10 secs
        return step;
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(FlowWorkflowTest.class.getName());
        
        suite.addTest(new FlowWorkflowTest("verifyRequiredRegistryEntries"));
        suite.addTest(new FlowWorkflowTest("testSubmitWorkflow"));
        suite.addTest(new FlowWorkflowTest("testExecutionProgress"));
        suite.addTest(new FlowWorkflowTest("testCheckExecutionResults"));
        suite.addTest(new FlowWorkflowTest("tidyUp"));
        
        return suite;
    }


}


/* 
$Log: FlowWorkflowTest.java,v $
Revision 1.1  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel
 
*/