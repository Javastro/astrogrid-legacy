/*$Id: WhileMultipleFeatureTest.java,v 1.2 2004/12/09 16:39:12 clq2 Exp $
 * Created on 09-Dec-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Dec-2004
 *
 */
public class WhileMultipleFeatureTest extends AbstractTestForFeature {

    /** Construct a new WhileMultipleFeatureTest
     * @param name
     */
    public WhileMultipleFeatureTest(String name) {
        super(name);
    }
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set x = new Set();
        x.setVar("x");
        x.setValue("${10}");
        wf.getSequence().addActivity(x);
        
        While w = new While();
        w.setTest("${x > 0}");
        wf.getSequence().addActivity(w);
        
        Sequence whileBody = new Sequence();
        w.setActivity(whileBody);
        
        Script sc = new Script();
        sc.setBody("x=x-1");
        whileBody.addActivity(sc);

        
        Script sc1 = new Script();
        sc1.setBody("x=x+1");
        whileBody.addActivity(sc1);
        
        
        Script sc2 = new Script();
        sc2.setBody("x=x-2");
        whileBody.addActivity(sc2);        
        
        Script end = new Script();
        end.setBody("print (x == 0)");
        wf.getSequence().addActivity(end);
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Script body = (Script)((Sequence)((While)result.getSequence().getActivity(1)).getActivity()).getActivity(2);

        assertEquals(5,body.getStepExecutionRecordCount());
        for (int i = 0; i < body.getStepExecutionRecordCount(); i++) {
            StepExecutionRecord rec = body.getStepExecutionRecord(i);
            assertEquals(ExecutionPhase.COMPLETED,rec.getStatus());
            assertTrue(rec.getMessageCount() > 0);        
        }
        
        Script end = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(end,"true");
    }

}


/* 
$Log: WhileMultipleFeatureTest.java,v $
Revision 1.2  2004/12/09 16:39:12  clq2
nww_jes_panic

Revision 1.1.2.1  2004/12/09 16:11:03  nw
fixed for and while loops
 
*/