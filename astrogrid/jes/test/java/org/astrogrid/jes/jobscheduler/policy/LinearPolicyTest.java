/*$Id: LinearPolicyTest.java,v 1.3 2004/03/18 01:30:34 nw Exp $
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
        if (getInputFileNumber() == EMPTY_WORKFLOW) {
            assertEquals(ExecutionPhase.COMPLETED,policy.currentJobStatus(wf));
            
        } else {
            assertEquals(ExecutionPhase.PENDING,policy.currentJobStatus(wf));
             
        testFindStepInVirginWorkflow(wf);
                
        assertEquals(ExecutionPhase.PENDING,policy.currentJobStatus(wf));
        
        }
        testFillingInWorkflow(wf);
    }
    



    public void testFindStepInVirginWorkflow(Workflow wf) {
        Step s = policy.nextExecutableStep(wf);
        if (getInputFileNumber() == EMPTY_WORKFLOW) { // empty workflow document
            assertNull(s) ;
        } else {
            assertNotNull(s);            
            assertEquals(0,s.getStepExecutionRecordCount()); 
        }
    }
    
    public void testFillingInWorkflow(Workflow wf) {
        Step s = policy.nextExecutableStep(wf);         
        while ( s != null) {
            assertTrue(policy.currentJobStatus(wf).getType() < ExecutionPhase.COMPLETED_TYPE) ;    
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
Revision 1.3  2004/03/18 01:30:34  nw
tidied

Revision 1.2  2004/03/10 14:37:35  nw
adjusted tests to handle an empty workflow

Revision 1.1  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade
 
*/