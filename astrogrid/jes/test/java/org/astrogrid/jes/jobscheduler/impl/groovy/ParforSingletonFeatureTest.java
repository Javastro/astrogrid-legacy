/*$Id: ParforSingletonFeatureTest.java,v 1.1 2004/08/09 17:34:10 nw Exp $
 * Created on 09-Aug-2004
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
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Aug-2004
 *
 */
public class ParforSingletonFeatureTest extends AbstractTestForFeature {

    /** Construct a new ParforSingletonFeatureTest
     * @param name
     */
    public ParforSingletonFeatureTest(String name) {
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
        
        Parfor f = new Parfor();
        f.setVar("i");
        f.setItems("${[1]}");
        
        Script sc = new Script();
        sc.setBody("acc = acc + i");
        f.setActivity(sc);
        
        wf.getSequence().addActivity(f);
        
        Script end = new Script();
        end.setBody("print (acc == 1)"); 
        wf.getSequence().addActivity(end);
        
        return wf;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        assertWorkflowCompleted(result);
        Script body = (Script)((Parfor)result.getSequence().getActivity(1)).getActivity();
        assertEquals(1,body.getStepExecutionRecordCount());
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
$Log: ParforSingletonFeatureTest.java,v $
Revision 1.1  2004/08/09 17:34:10  nw
implemented parfor.
removed references to rulestore
 
*/