/*$Id: WhileNeverFeatureTest.java,v 1.1 2004/08/05 10:57:03 nw Exp $
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

import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Aug-2004
 *
 */
public class WhileNeverFeatureTest extends AbstractTestForFeature {

    /** Construct a new WhileFalseFeatureTest
     * @param name
     */
    public WhileNeverFeatureTest(String name) {
        super(name);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set x = new Set();
        x.setVar("x");
        x.setValue("${false}");
        wf.getSequence().addActivity(x);
        
        While w = new While();
        w.setTest("${x}");
        Script sc = new Script();
        sc.setBody("x=!x");
        w.setActivity(sc);
        wf.getSequence().addActivity(w);
        
        Script end = new Script();
        end.setBody("print (!x)");
        wf.getSequence().addActivity(end);
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Script body = (Script)((While)result.getSequence().getActivity(1)).getActivity();
        //assertScriptCompleted(body);
        assertEquals(0,body.getStepExecutionRecordCount());
        
        Script end = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(end,"true");
    }
}


/* 
$Log: WhileNeverFeatureTest.java,v $
Revision 1.1  2004/08/05 10:57:03  nw
tests for while construct
 
*/