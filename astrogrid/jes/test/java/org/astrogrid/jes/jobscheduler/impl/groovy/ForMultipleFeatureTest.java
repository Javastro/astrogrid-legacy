/*$Id: ForMultipleFeatureTest.java,v 1.2 2004/12/09 16:39:12 clq2 Exp $
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
public class ForMultipleFeatureTest extends ForManyFeatureTest {

    /** Construct a new ForMultipleFeatureTest
     * @param name
     */
    public ForMultipleFeatureTest(String name) {
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
        Sequence seq = new Sequence();
        f.setActivity(seq);
        
        Script sc = new Script();
        sc.setBody("acc = acc + i");
        seq.addActivity(sc);
 
        Script sc2 = new Script();
        sc2.setBody("acc = acc + 100");
        seq.addActivity(sc2);        
        
        // need to check that this one is getting executed too.
        Script sc1 = new Script();
        sc1.setBody("acc = acc + 10");
       seq.addActivity(sc1);
        
        wf.getSequence().addActivity(f);
        

        Script end1 = new Script();
        end1.setBody("print (acc)"); 
        wf.getSequence().addActivity(end1);        
        
        return wf;        
    }

    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        /*@todo don't care about this for now. - no time, put in later
        Script body = (Script)((For)result.getSequence().getActivity(1)).getActivity();
        assertEquals(10,body.getStepExecutionRecordCount());
        for (int i = 0; i < body.getStepExecutionRecordCount(); i++) {
            StepExecutionRecord rec = body.getStepExecutionRecord(i);
            assertEquals(ExecutionPhase.COMPLETED,rec.getStatus());
            assertTrue(rec.getMessageCount() > 0);        
        }*/
        
        Script end = (Script)result.getSequence().getActivity(2);
        assertScriptCompletedWithMessage(end,"1155");        
    }
}


/* 
$Log: ForMultipleFeatureTest.java,v $
Revision 1.2  2004/12/09 16:39:12  clq2
nww_jes_panic

Revision 1.1.2.1  2004/12/09 16:11:03  nw
fixed for and while loops
 
*/