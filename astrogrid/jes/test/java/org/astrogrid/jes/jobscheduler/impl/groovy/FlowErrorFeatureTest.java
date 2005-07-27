/*$Id: FlowErrorFeatureTest.java,v 1.3 2005/07/27 15:35:08 clq2 Exp $
 * Created on 29-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.groovy;

import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2004
 *
 */
public class FlowErrorFeatureTest extends AbstractTestForFeature {

    /** Construct a new ErrorFlowFeatureTest
     * @param name
     */
    public FlowErrorFeatureTest(String name) {
        super(name);
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Set x = new Set();
        x.setVar("x");
        x.setValue("${1}");
        wf.getSequence().addActivity(x);
        
        Flow f = new Flow();
        wf.getSequence().addActivity(f);
        // add a step
        Step s= new Step();
        s.setResultVar("results");
        s.setName("test step");
        Tool t = new Tool();
        s.setTool(t);
        t.setInterface("unknown");
        t.setName("test tool");
        t.setInput(new Input());
        t.setOutput(new Output());
        f.addActivity(s);
        // add a script in parallel.
        Script sc = new Script();
        sc.setBody("x=x+1;print(x)");
        f.addActivity(sc);
        Script finalScript = new Script();
        finalScript.setBody("print(x)"); // test which value we got.
        wf.getSequence().addActivity(finalScript);
        return wf;
    }

    protected void furtherProcessing(JobURN urn) throws Exception {
        //check call has been made.
        Workflow wf = jobFactory.findJob(urn);        
        Flow flow = (Flow)wf.getSequence().getActivity(1);
        Step step = (Step)flow.getActivity(0);
        assertStepRunning(step);
        
        // send an error back..
        JobIdentifierType jid = JesUtil.createJobId(urn,step.getId());
        MessageType msg= new MessageType();
        msg.setContent("return");
        msg.setPhase(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.ERROR);
        sched.resumeJob(jid,msg);
        
    }
    
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.groovy.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        Flow flow = (Flow)result.getSequence().getActivity(1);
        Step step = (Step)flow.getActivity(0);
        assertStepError(step);
        assertWorkflowError(result);
        
        // verify parallel script was executed.
        Script sc = (Script)flow.getActivity(1);
        assertScriptCompletedWithMessage(sc,"2");        
        
        // however, final script won't get executed - as flow finishes with an error
        sc = (Script)result.getSequence().getActivity(2);
        assertScriptNotRun(sc);      
    }

}


/* 
$Log: FlowErrorFeatureTest.java,v $
Revision 1.3  2005/07/27 15:35:08  clq2
jes_nww_review_unit_tests

Revision 1.2.150.1  2005/07/19 15:38:06  nw
fixed unit tests -100% pass rate now.

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/30 14:00:10  nw
first working draft
 
*/