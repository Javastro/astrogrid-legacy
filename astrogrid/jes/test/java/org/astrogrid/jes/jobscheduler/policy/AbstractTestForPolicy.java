/*$Id: AbstractTestForPolicy.java,v 1.1 2004/03/18 10:56:19 nw Exp $
 * Created on 18-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.policy;

import org.astrogrid.jes.AbstractTestWorkflowInputs;

import java.io.InputStream;

import junit.framework.TestCase;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import java.io.InputStream;
import java.io.InputStreamReader;

/** Abstract test - can be extended to verify any particular implementation confirms to the general policy contract.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2004
 *
 */
public abstract class AbstractTestForPolicy extends AbstractTestWorkflowInputs {
    /**
     * Constructor for AbstractTestForPolicy.
     * @param arg0
     */
    public AbstractTestForPolicy(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        policy = createPolicy();
    }
    protected AbstractPolicy policy;
    protected abstract AbstractPolicy createPolicy();

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
$Log: AbstractTestForPolicy.java,v $
Revision 1.1  2004/03/18 10:56:19  nw
factored out an abstract test class
 
*/