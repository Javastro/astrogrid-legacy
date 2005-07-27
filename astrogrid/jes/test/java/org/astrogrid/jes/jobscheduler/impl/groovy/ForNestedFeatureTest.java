/*$Id: ForNestedFeatureTest.java,v 1.3 2005/07/27 15:35:08 clq2 Exp $
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
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Dec-2004
 *
 */
public class ForNestedFeatureTest extends AbstractTestForFeature {

    /** Construct a new ForNestedFeatureTest
     * @param name
     */
    public ForNestedFeatureTest(String name) {
        super(name);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set acc = new Set();
        acc.setVar("acc");
        acc.setValue("${0}");
        wf.getSequence().addActivity(acc);
        
        Set innerAcc = new Set();
        innerAcc.setVar("innerAcc");
        innerAcc.setValue("${0}");
        wf.getSequence().addActivity(innerAcc);
        
        For f = new For();
        f.setVar("i");
        f.setItems("${1..10}");
        wf.getSequence().addActivity(f);
        Sequence seq = new Sequence();
        f.setActivity(seq);
        
        
        For f1 = new For();
        f1.setVar("j");
        f1.setItems("${1..3}");
        seq.addActivity(f1);
        
        Script inner = new Script();
        inner.setBody("innerAcc = innerAcc + j + i");
        f1.setActivity(inner);
        
        Script sc = new Script();
        sc.setBody("acc = acc + i");
        seq.addActivity(sc);
        

        
        Script end = new Script();
        //end.setBody("print acc;print i; print j; print innerAcc;");
        end.setBody("print (acc == 55 && i == 10 && j == 3 && innerAcc == 225 )"); 
        wf.getSequence().addActivity(end);
        
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Sequence outerFor = (Sequence)((For)result.getSequence().getActivity(2)).getActivity();
        Script outerScript = (Script)outerFor.getActivity(1);
        assertEquals(10,outerScript.getStepExecutionRecordCount());
        assertAllScriptRunsCompleted(outerScript);
        
        Script innerScript = (Script)((For)outerFor.getActivity(0)).getActivity();
        assertEquals(30,innerScript.getStepExecutionRecordCount());
        assertAllScriptRunsCompleted(innerScript);        
        
        Script end = (Script)result.getSequence().getActivity(3);
        assertScriptCompletedWithMessage(end,"true");        
    }


}


/* 
$Log: ForNestedFeatureTest.java,v $
Revision 1.3  2005/07/27 15:35:08  clq2
jes_nww_review_unit_tests

Revision 1.2.70.1  2005/07/19 15:38:06  nw
fixed unit tests -100% pass rate now.

Revision 1.2  2004/12/09 16:39:12  clq2
nww_jes_panic

Revision 1.1.2.1  2004/12/09 16:11:03  nw
fixed for and while loops
 
*/