/*$Id: GroovyResumeJobSuccesssTest.java,v 1.3 2004/08/18 21:50:59 nw Exp $
 * Created on 13-May-2004
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
import org.astrogrid.jes.jobcontroller.AbstractTestForJobController;
import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;
import org.astrogrid.jes.jobscheduler.impl.AbstractTestForSchedulerImpl;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/** test resume job works in scripted scheduler.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-May-2004
 *
 */
public class GroovyResumeJobSuccesssTest extends AbstractTestForSchedulerImpl {
    /** Construct a new ScriptedResumerJobSuccesssTest
     * @param arg0
     */
    public GroovyResumeJobSuccesssTest(String arg0) {
        super(arg0);
    }
/**
     * @see org.astrogrid.jes.jobscheduler.impl.AbstractTestForResume#getKeyFor(org.astrogrid.workflow.beans.v1.Workflow, org.astrogrid.workflow.beans.v1.Step)
     */
    protected String getKeyFor(Workflow j, Step step) {
        String id = step.getId();
        assertNotNull(id);
        return id;
    }
/**
 * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
 */
protected void performTest(JobURN urn) throws Exception {  
    assertNotNull(urn);
    scheduler.scheduleNewJob(JesUtil.castor2axis(urn));    
    // find job, get first jobstep out of it.
    Workflow j = fac.findJob(urn);
    assertNotNull(j);
    if (getInputFileNumber() == EMPTY_WORKFLOW) {
        assertFalse(JesUtil.getJobSteps(j).hasNext());
        return;
    }
    Step step = (Step)JesUtil.getJobSteps(j).next(); // got to have at least one job step
    // should  have been executed
    assertEquals(1,step.getStepExecutionRecordCount()); 

    String stepId = step.getId();
    assertNotNull(stepId);
    JobIdentifierType id = JesUtil.createJobId(urn,stepId);   
    //use this job step to build an info object
    MessageType info = new MessageType();      
    info.setContent("initializing");
    info.setPhase(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.INITIALIZING);       
    scheduler.resumeJob(id,info);
    
    // now a running message
    info = new MessageType();
    info.setContent("running ok");
    info.setPhase(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.RUNNING);
    scheduler.resumeJob(id,info);
    
    // now a completed message
    info = new MessageType();
    info.setContent("completed");
    info.setPhase(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.COMPLETED);
    scheduler.resumeJob(id,info);                    
    
    // now, say the appcon we're talking to is buggy and doen't follow protocol - what happens
    info = new MessageType();
    info.setContent("running again");
    info.setPhase(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.RUNNING);          
    scheduler.resumeJob(id,info);
    
    //now check behaviour is as expected.
    
     // should now have an execution record.
    assertEquals(1,step.getStepExecutionRecordCount());
    StepExecutionRecord exRec = JesUtil.getLatestOrNewRecord(step);
    int count = exRec.getMessageCount();
    assertEquals(4,count);
    
    assertEquals(ExecutionPhase.COMPLETED,exRec.getStatus());
    assertFalse("workflow should not be in error",j.getJobExecutionRecord().getStatus().equals(ExecutionPhase.ERROR));
    assertTrue("workflw should either be running or completed",j.getJobExecutionRecord().getStatus().getType() >= ExecutionPhase.RUNNING_TYPE);
}

}
/* 
$Log: GroovyResumeJobSuccesssTest.java,v $
Revision 1.3  2004/08/18 21:50:59  nw
worked on tests

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

Revision 1.1.2.1  2004/05/21 11:25:42  nw
first checkin of prototype scrpting workflow interpreter
 
*/