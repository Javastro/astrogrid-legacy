/*$Id: GroovySubmitNewJobSuccessTest.java,v 1.3 2004/08/18 21:50:59 nw Exp $
 * Created on 12-May-2004
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
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.jobscheduler.impl.AbstractJobSchedulerImpl;
import org.astrogrid.jes.jobscheduler.impl.AbstractTestForSchedulerImpl;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** adaptation of existing test, using scripted job scheduler. should have same external behaviour.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-May-2004
 *
 */
public class GroovySubmitNewJobSuccessTest extends AbstractTestForSchedulerImpl{
    /** Construct a new ScriptedSubmitNewJobSuccessTest
     * @param arg0
     */
    public GroovySubmitNewJobSuccessTest(String arg0) {
        super(arg0);
    }
    /** At this point, a job has been entered into the store, and the duff-nudger has been called.
     * this method take the place of the real scheduler notifier - it calls the scheduler.
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(JobURN urn) throws Exception {
    
        assertNotNull(urn);
        // could take copy of job here from store (need to clone, as using in-memory store). then compare it to result after scheduling.
        scheduler.scheduleNewJob(JesUtil.castor2axis(urn));
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
        assertFalse("job shouldb't be in error",job.getJobExecutionRecord().getStatus().equals(ExecutionPhase.ERROR));
        assertTrue("expected job to be running or completed",job.getJobExecutionRecord().getStatus().getType() >= ExecutionPhase.RUNNING_TYPE);
    }

   
}


/* 
$Log: GroovySubmitNewJobSuccessTest.java,v $
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