/*$Id: LinearPolicyTest.java,v 1.1 2004/03/05 16:16:55 nw Exp $
 * Created on 04-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.policy;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Mar-2004
 *
 */
public class LinearPolicyTest extends AbstractTestWorkflowInputs {
    /** Construct a new LinearPolicyTest
     * 
     */
    public LinearPolicyTest(String name) {
        super(name);
    }

    protected void setUp() {
        policy = new LinearPolicy();
    }
    protected LinearPolicy policy;

    /**
     * @see org.astrogrid.jes.AbstractTestWorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
        Workflow wf = Workflow.unmarshalWorkflow(new InputStreamReader(is));  
        policy.registerFunctions(wf);
        
        assertEquals(ExecutionPhase.PENDING,policy.currentJobStatus(wf));      
        testFindStepInVirginWorkflow(wf);
        
        testFindStepInInitializedWorkflow(wf);
        assertEquals(ExecutionPhase.PENDING,policy.currentJobStatus(wf));
        
        testFindStepInExecutingWorkflow(wf,resourceNum);
        
        testFillingInWorkflow(wf);
    }
    
    /**
     * @param wf
     */
    public void testFindStepInExecutingWorkflow(Workflow wf, int resourceNum) {
        // first initialize workflow

        Step s = (Step)wf.findXPathValue("//*[jes:isStep()]");
        assertNotNull(s);
        StepExecutionRecord er = JesUtil.getLatestOrNewRecord(s); //initializes job step.
        er.setStatus(ExecutionPhase.COMPLETED);
        
        Step s1 = policy.nextExecutableStep(wf);
        if (resourceNum == 0 || resourceNum == 4 ) { // these workflows only have a sngle step
            assertNull(s1);
        } else {
            assertNotNull(s1);
            assertEquals(ExecutionPhase.PENDING,JesUtil.getLatestOrNewRecord(s1).getStatus());
        }
        
    }


    public void testFindStepInInitializedWorkflow(Workflow wf) {

        Step s = (Step)wf.findXPathValue("//*[jes:isStep() ]");
        assertNotNull(s);
        JesUtil.getLatestOrNewRecord(s); //initializes job step.
        
        Step s1 = policy.nextExecutableStep(wf);
        assertNotNull(s1);
        assertEquals(1,s1.getStepExecutionRecordCount());
        assertEquals(ExecutionPhase.PENDING,s1.getStepExecutionRecord(0).getStatus());
        
    }

    public void testFindStepInVirginWorkflow(Workflow wf) {
        Step s = policy.nextExecutableStep(wf);
        assertNotNull(s);
        assertEquals(0,s.getStepExecutionRecordCount());
    }
    
    public void testFillingInWorkflow(Workflow wf) {
        Step s = policy.nextExecutableStep(wf);         
        while ( s != null) {           
            assertNotNull(s);            
            assertEquals(ExecutionPhase.PENDING,JesUtil.getLatestOrNewRecord(s).getStatus());
            JesUtil.getLatestOrNewRecord(s).setStatus(ExecutionPhase.COMPLETED);
            assertEquals(1,s.getStepExecutionRecordCount());
            s = policy.nextExecutableStep(wf);
        }
        assertEquals(ExecutionPhase.COMPLETED,policy.currentJobStatus(wf));
        // now should not be able to find any that aren't completed..
        assertNull(wf.findXPathValue("//*[jes:isStep() and jes:latestStatus() != '" + ExecutionPhase.COMPLETED +"' ]"));
    }
    
    
}


/* 
$Log: LinearPolicyTest.java,v $
Revision 1.1  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/