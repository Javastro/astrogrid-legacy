/*$Id: StepErrorFeatureTest.java,v 1.3 2004/08/18 21:50:59 nw Exp $
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
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** test behaviour when step throws an error. 
 * also tests behaviour of sequence under error .
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Jul-2004
 *
 */
public class StepErrorFeatureTest extends StepFeatureTest {

    /** Construct a new ErrorStepFeatureTest
     * @param name
     */
    public StepErrorFeatureTest(String name) {
        super(name);
    }

    protected void furtherProcessing(JobURN urn) throws Exception {
        //check call has been made.
        Workflow wf = jobFactory.findJob(urn);
        Step step  = (Step)wf.getSequence().getActivity(1);
        assertStepRunning(step);
        
        // send an error back..
        JobIdentifierType jid = JesUtil.createJobId(urn,step.getId());
        MessageType msg= new MessageType();
        msg.setContent("return");
        msg.setPhase(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.ERROR);
        sched.resumeJob(jid,msg);

    }
    protected void verifyWorkflow(Workflow result) {
        // check the step and workflow completed.
        Step step =(Step)result.getSequence().getActivity(1);
        assertStepError(step);
        
        assertWorkflowError(result);
    }
}


/* 
$Log: StepErrorFeatureTest.java,v $
Revision 1.3  2004/08/18 21:50:59  nw
worked on tests

Revision 1.2  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.1.2.1  2004/07/30 14:00:10  nw
first working draft
 
*/