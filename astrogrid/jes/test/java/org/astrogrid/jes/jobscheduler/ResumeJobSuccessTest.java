/*$Id: ResumeJobSuccessTest.java,v 1.6 2004/03/09 14:24:09 nw Exp $
 * Created on 19-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

/** test behaviour of 'resumeTest' method.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
 *
 */ 
public class ResumeJobSuccessTest extends AbstractTestForJobScheduler {
    /** Construct a new ResumeJobSuccessTest
     * @param arg0
     */
    public ResumeJobSuccessTest(String arg0) {
        super(arg0);
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(JobURN urn) throws Exception {  
        assertNotNull(urn);
        // find job, get first jobstep out of it.
        Workflow j = fac.findJob(urn);
        assertNotNull(j);
        Step step = (Step)JesUtil.getJobSteps(j).next(); // got to have at least one job step
        // shouldn't have been executed yet.
        assertEquals(0,step.getStepExecutionRecordCount());
        //use this job step to build an info object
        MessageType info = new MessageType();   
  
        String xpath = j.getXPathFor(step);
        assertNotNull(xpath);
        assertEquals(step,j.findXPathValue(xpath));
        JobIdentifierType id = JesUtil.createJobId(urn,xpath);       
        info.setContent("TEST COMMENT");
        info.setPhase(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.COMPLETED);
        
        // could take copy of job here from store (need to clone, as using in-memory store). then compare it to result after scheduling.
        js.resumeJob(id,info);
        //now check behaviour is as expected.
        //as we're using in-memory job store, changes happen to objects directly.
        // should now have an execution record.
        assertEquals(1,step.getStepExecutionRecordCount());
        StepExecutionRecord exRec = JesUtil.getLatestOrNewRecord(step);
        int count = exRec.getMessageCount();
        assertEquals(1,count);
        
        assertEquals("TEST COMMENT",exRec.getMessage(0).getContent());
        assertEquals(ExecutionPhase.COMPLETED,exRec.getStatus());

        assertEquals(ExecutionPhase.RUNNING,j.getJobExecutionRecord().getStatus());
    }
}


/* 
$Log: ResumeJobSuccessTest.java,v $
Revision 1.6  2004/03/09 14:24:09  nw
upgraded to new job controller wsdl

Revision 1.5  2004/03/05 16:16:55  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/