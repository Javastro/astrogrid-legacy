/*$Id: SubmitNewJobSuccessTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
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
import org.astrogrid.jes.jobscheduler.dispatcher.*;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.Status;
import org.astrogrid.jes.types.v1.SubmissionResponse;

/** Test default vanilla behaviour
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
 *
 */
public class SubmitNewJobSuccessTest extends AbstractTestForJobScheduler {
    /** Construct a new SubmitNewJobSuccessTest
     * @param arg0
     */
    public SubmitNewJobSuccessTest(String arg0) {
        super(arg0);
    }
    /** At this point, a job has been entered into the store, and the duff-nudger has been called.
     * this method take the place of the real scheduler notifier - it calls the scheduler.
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(SubmissionResponse result) throws Exception {
        // check all is ok.
        assertNotNull(result);
        JobURN urn = result.getJobURN();
        assertNotNull(urn);
        // could take copy of job here from store (need to clone, as using in-memory store). then compare it to result after scheduling.
        js.scheduleNewJob(urn);
        //now check behaviour is as expected.
        // should have dispatched something - should be all steps.
        assertTrue(((MockDispatcher)dispatcher).getCallCount() > 0);
        //
        Job job = fac.findJob(urn);
        assertNotNull(job);
        assertEquals(Status.RUNNING,job.getStatus());
    }
}


/* 
$Log: SubmitNewJobSuccessTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/