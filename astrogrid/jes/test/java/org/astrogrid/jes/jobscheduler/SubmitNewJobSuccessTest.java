/*$Id: SubmitNewJobSuccessTest.java,v 1.7 2004/03/10 14:37:35 nw Exp $
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
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

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
    protected void performTest(JobURN urn) throws Exception {

        assertNotNull(urn);
        // could take copy of job here from store (need to clone, as using in-memory store). then compare it to result after scheduling.
        js.scheduleNewJob(JesUtil.castor2axis(urn));
        //now check behaviour is as expected.
        // should have dispatched something - should be all steps.
        //
        if (getInputFileNumber() != EMPTY_WORKFLOW) {
            assertTrue(((MockDispatcher)dispatcher).getCallCount() > 0);
        } else {
            assertEquals(0,((MockDispatcher)dispatcher).getCallCount());
        }
        
        Workflow job = fac.findJob(urn);
        assertNotNull(job);
        assertEquals(ExecutionPhase.RUNNING,job.getJobExecutionRecord().getStatus());
    }
}


/* 
$Log: SubmitNewJobSuccessTest.java,v $
Revision 1.7  2004/03/10 14:37:35  nw
adjusted tests to handle an empty workflow

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

Revision 1.1.2.2  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/