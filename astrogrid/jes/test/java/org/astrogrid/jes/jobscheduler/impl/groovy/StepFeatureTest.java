/*$Id: StepFeatureTest.java,v 1.3 2004/08/03 16:32:26 nw Exp $
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

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Set;
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
        Set set = new Set();
        set.setVar("x");
        set.setValue("${42}");
        wf.getSequence().addActivity(set);
        
        Step s = new Step();
        s.setName("test step");
        Tool t = new Tool();
        t.setInterface("unknown");
        t.setName("test tool");
        t.setInput(new Input());
        t.setOutput(new Output());
        ParameterValue in = new ParameterValue();
        in.setName("in");
        in.setValue("${x}");
        t.getInput().addParameter(in);
        
        ParameterValue out = new ParameterValue();
        out.setName("out");
        out.setIndirect(true);
        out.setValue("resuts-${new java.util.Date()}.xml");
        t.getOutput().addParameter(out);
        
        s.setTool(t);
        wf.getSequence().addActivity(s);
        return wf;
    }
    
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForFeature#verifyWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    protected void verifyWorkflow(Workflow result) {
        Step step =(Step)result.getSequence().getActivity(1);
        assertStepCompleted(step);
        super.assertWorkflowCompleted(result);
        
        assertEquals(1,super.disp.getCallCount());
        Tool t = super.disp.getLatestTool();
        assertNotNull(t);
        assertEquals("42", t.findXPathValue("input/parameter[name='in']/value"));
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.impl.scripting.AbstractTestForFeature#furtherProcessing()
     */
    protected void furtherProcessing(JobURN urn) throws Exception{
            //check call has been made.
        Workflow wf = jobFactory.findJob(urn);
        Step step  = (Step)wf.getSequence().getActivity(1);
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
Revision 1.3  2004/08/03 16:32:26  nw
remove unnecessary envId attrib from rules
implemented variable propagation into parameter values.

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