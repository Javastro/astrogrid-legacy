/*$Id: ResumeJobSuccessTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
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

import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.types.v1.JobInfo;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.Status;
import org.astrogrid.jes.types.v1.SubmissionResponse;

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
    protected void performTest(SubmissionResponse result) throws Exception {
        // check all is ok.
        assertNotNull(result);
        JobURN urn = result.getJobURN();
        assertNotNull(urn);
        // find job, get first jobstep out of it.
        Job j = fac.findJob(urn);
        assertNotNull(j);
        JobStep step = (JobStep)j.getJobSteps().next(); // got to have at least one job step
        //use this job step to build an info object
        JobInfo info = new JobInfo();
        info.setJobURN(urn);
        info.setComment("TEST COMMENT");
        info.setStatus(Status.COMPLETED);
        info.setStepNumber(step.getStepNumber());
        // could take copy of job here from store (need to clone, as using in-memory store). then compare it to result after scheduling.
        js.resumeJob(info);
        //now check behaviour is as expected.
        //as we're using in-memory job store, changes happen to objects directly.
        assertEquals("TEST COMMENT",step.getComment());
        assertEquals(Status.COMPLETED,step.getStatus());

        assertEquals(Status.RUNNING,j.getStatus());
    }
}


/* 
$Log: ResumeJobSuccessTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/