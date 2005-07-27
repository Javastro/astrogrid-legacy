/*$Id: WhileNestedFeatureTest.java,v 1.3 2005/07/27 15:35:08 clq2 Exp $
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
public class WhileNestedFeatureTest extends AbstractTestForFeature {

    /** Construct a new WhileNestedFeatureTest
     * @param name
     */
    public WhileNestedFeatureTest(String name) {
        super(name);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set x = new Set();
        x.setVar("x");
        x.setValue("${10}");
        wf.getSequence().addActivity(x);
        
        
        While w = new While();
        w.setTest("${x > 0}");
        wf.getSequence().addActivity(w);
        
        Sequence seq = new Sequence();
        w.setActivity(seq);
        
        Set y = new Set();
        y.setVar("y");
        y.setValue("${3}");
        seq.addActivity(y);
        
        While w1 = new While();
        w1.setTest("${y>0}");
        seq.addActivity(w1);
        
        Script inner = new Script();
        inner.setBody("y=y-1");
        w1.setActivity(inner);
        
        
        Script sc = new Script();
        sc.setBody("x=x-1");
        seq.addActivity(sc);

        
        Script end = new Script();
        end.setBody("print (x == 0 && y == 0)");
        wf.getSequence().addActivity(end);
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Sequence seq= (Sequence)((While)result.getSequence().getActivity(1)).getActivity();
        Script outerBody = (Script)seq.getActivity(2);

        assertEquals(10,outerBody.getStepExecutionRecordCount());
        assertAllScriptRunsCompleted(outerBody);
        
        Script innerBody = (Script)((While)seq.getActivity(1)).getActivity();
        assertEquals(30,innerBody.getStepExecutionRecordCount());
        assertAllScriptRunsCompleted(innerBody);        
        
        Script end = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(end,"true");
    }

}


/* 
$Log: WhileNestedFeatureTest.java,v $
Revision 1.3  2005/07/27 15:35:08  clq2
jes_nww_review_unit_tests

Revision 1.2.70.1  2005/07/19 15:38:06  nw
fixed unit tests -100% pass rate now.

Revision 1.2  2004/12/09 16:39:12  clq2
nww_jes_panic

Revision 1.1.2.1  2004/12/09 16:11:03  nw
fixed for and while loops
 
*/