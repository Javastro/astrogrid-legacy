/*$Id: ScriptFeatureTest.java,v 1.1 2004/07/09 09:32:12 nw Exp $
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
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/** unit test for &lt;script&gt; tag.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Jul-2004
 *
 */
public class ScriptFeatureTest extends AbstractTestForFeature {
    /** Construct a new ScriptFeatureTest
     * @param name
     */
    public ScriptFeatureTest(String name) {
        super(name);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForScriptingFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Script script = new Script();       
        script.setBody("x = 1 + 1 \nprint x");
        wf.getSequence().addActivity(script);
        return wf;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForScriptingFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        Script script =(Script)result.getSequence().getActivity(0);
        assertEquals(1,script.getStepExecutionRecordCount());
        StepExecutionRecord rec = script.getStepExecutionRecord(0);
        assertEquals(ExecutionPhase.COMPLETED, rec.getStatus());
        assertTrue(rec.getMessageCount() > 0);
        //@todo more here.
    }
}


/* 
$Log: ScriptFeatureTest.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/