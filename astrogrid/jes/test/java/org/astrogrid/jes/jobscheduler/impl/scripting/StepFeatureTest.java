/*$Id: StepFeatureTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
 * Created on 09-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/** Feature test to test execution of the 'Step' construct. 
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Jul-2004
 *
 */
public class StepFeatureTest extends AbstractTestForFeature {
    /** Construct a new StepFeatureTest
     * @param name
     */
    public StepFeatureTest(String name) {
        super(name);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Step s = new Step();
        s.setName("test step");
        Tool t = new Tool();
        t.setInterface("unknown");
        t.setName("test tool");
        t.setInput(new Input());
        t.setOutput(new Output());
        s.setTool(t);
        wf.getSequence().addActivity(s);
        return wf;
    }
    
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        Step step =(Step)result.getSequence().getActivity(0);
        assertEquals(1,step.getStepExecutionRecordCount());
        StepExecutionRecord rec = step.getStepExecutionRecord(0);
        assertEquals(ExecutionPhase.COMPLETED, rec.getStatus());
        assertTrue(rec.getMessageCount() > 0);
        //@todo more here.        
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForFeature#furtherProcessing()
     */
    protected void furtherProcessing() {
        //@todo add in calls to return messages and result value from the 'application' being called
    }

}


/* 
$Log: StepFeatureTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/