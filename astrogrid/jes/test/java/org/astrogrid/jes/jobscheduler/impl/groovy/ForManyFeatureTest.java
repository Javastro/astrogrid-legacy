/*$Id: ForManyFeatureTest.java,v 1.2 2005/07/27 15:35:08 clq2 Exp $
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
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Aug-2004
 *
 */
public class ForManyFeatureTest extends AbstractTestForFeature {

    /** Construct a new ForManyFeatureTest
     * @param name
     */
    public ForManyFeatureTest(String name) {
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
        
        For f = new For();
        f.setVar("i");
        f.setItems("${1..10}");
        
        Script sc = new Script();
        sc.setBody("acc = acc + i");
        f.setActivity(sc);
        
        wf.getSequence().addActivity(f);
        
        Script end = new Script();
        end.setBody("print (acc == 55 && i == 10)"); 
        wf.getSequence().addActivity(end);
        
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Script body = (Script)((For)result.getSequence().getActivity(1)).getActivity();
        assertEquals(10,body.getStepExecutionRecordCount());
        assertAllScriptRunsCompleted(body);
        
        Script end = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(end,"true");        
    }

}


/* 
$Log: ForManyFeatureTest.java,v $
Revision 1.2  2005/07/27 15:35:08  clq2
jes_nww_review_unit_tests

Revision 1.1.146.1  2005/07/19 15:38:06  nw
fixed unit tests -100% pass rate now.

Revision 1.1  2004/08/05 14:38:30  nw
tests for sequential for construct
 
*/