/*$Id: WhileManyFeatureTest.java,v 1.2 2005/07/27 15:35:08 clq2 Exp $
 * Created on 05-Aug-2004
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
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Aug-2004
 *
 */
public class WhileManyFeatureTest extends AbstractTestForFeature {

    /** Construct a new WhileManyFeatureTest
     * @param name
     */
    public WhileManyFeatureTest(String name) {
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
        Script sc = new Script();
        sc.setBody("x=x-1");
        w.setActivity(sc);
        wf.getSequence().addActivity(w);
        
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
        Script body = (Script)((While)result.getSequence().getActivity(1)).getActivity();

        assertEquals(10,body.getStepExecutionRecordCount());
        assertAllScriptRunsCompleted(body);
 
        
        Script end = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(end,"true");
    }

}


/* 
$Log: WhileManyFeatureTest.java,v $
Revision 1.2  2005/07/27 15:35:08  clq2
jes_nww_review_unit_tests

Revision 1.1.146.1  2005/07/19 15:38:06  nw
fixed unit tests -100% pass rate now.

Revision 1.1  2004/08/05 10:57:03  nw
tests for while construct
 
*/