/*$Id: StepFeatureTest.java,v 1.2 2004/07/30 15:42:34 nw Exp $
 * Created on 09-Jul-2004
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
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** Feature test to test execution of the 'Step' construct. 
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Jul-2004
 *
 */
public class StepFeatureTest extends AbstractTestForFeature {
    /** Construct a new StepFeatureTest
     * @param name
     */
    public StepFeatureTest(String name) {
        super(name);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForFeature#buildWorkflow()
     */
    protected Workflow buildWorkflow() {
        Workflow wf = super.createMinimalWorkflow();
        Step s = new Step();
        s.setName("test step");
        Tool t = new Tool();
        t.setInterface("unknown");
        t.setName("test tool");
        t.setInput(new Input());
        t.setOutput(new Output());
        //@todo add parameters in here later.
        s.setTool(t);
        wf.getSequence().addActivity(s);
        return wf;
    }
    
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        Step step =(Step)result.getSequence().getActivity(0);
        assertStepCompleted(step);
        super.assertWorkflowCompleted(result);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForFeature#furtherProcessing()
     */
    protected void furtherProcessing(JobURN urn) throws Exception{
            //check call has been made.
        Workflow wf = jobFactory.findJob(urn);
        Step step  = (Step)wf.getSequence().getActivity(0);
        assertStepRunning(step);
        // ok, looks good. now we simulate the 'return' from the application.
        //@todo later need to work with return results, etc.
        JobIdentifierType jid = JesUtil.createJobId(urn,step.getId());
        MessageType msg= new MessageType();
        msg.setContent("return");
        msg.setPhase(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.COMPLETED);
        sched.resumeJob(jid,msg);
    }

}


/* 
$Log: StepFeatureTest.java,v $
Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.3  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.1.2.2  2004/07/30 14:00:10  nw
first working draft

Revision 1.1.2.1  2004/07/28 16:24:23  nw
finished groovy beans.
moved useful tests from old python package.
removed python implemntation

Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/