/*$Id: GroovySubmitNewJobNotifierFailsTest.java,v 1.5 2004/12/03 14:47:40 jdt Exp $
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
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.jobscheduler.impl.AbstractTestForSchedulerImpl;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.util.Iterator;

/** test what happens when the notifier fails in the scripted scheduler
 * expect errors to propagate upwards, and cause entire workflow to be in error.
 * @author Noel Winstanley nw@jb.man.ac.uk 13-May-2004
 *
 */
public class GroovySubmitNewJobNotifierFailsTest extends AbstractTestForSchedulerImpl {
    /** Construct a new ScriptedSubmitNewJobNotifierFailsTeste
     * @param arg
     */
    public GroovySubmitNewJobNotifierFailsTest(String arg) {
        super(arg);
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTestForJobController#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(JobURN urn) throws Exception {
    
        assertNotNull(urn);
        // could take copy of job here from store (need to clone, as using in-memory store). then compare it to result after scheduling.
        scheduler.scheduleNewJob(Castor2Axis.convert(urn));
        //now check behaviour is as expected.
        // should have dispatched something - should be all steps.
        if (getInputFileNumber() != EMPTY_WORKFLOW) {
            assertTrue(((MockDispatcher)dispatcher).getCallCount() > 0);
        } else {
            assertEquals(0,((MockDispatcher)dispatcher).getCallCount());
            return; // nothing else we want to test about this one.
        }
        
        Workflow job = fac.findJob(urn);
        assertNotNull(job);
        // all steps should either be in an error state, or pending.
        Iterator i = JesUtil.getJobSteps(job);
        boolean errorSeen = false;
        while (i.hasNext()) {
            Step s = (Step)i.next();
            ExecutionPhase e = JesUtil.getLatestOrNewRecord(s).getStatus();
            if (e.equals(ExecutionPhase.ERROR)) {
                errorSeen = true;
            } else if (! e.equals(ExecutionPhase.PENDING)) {
                fail("All steps should either be error or pending, but step " + s.getName() + " is " + e);
            }
            
        }
        assertTrue("expected to see a step in error",errorSeen);
        
        // job itself should be in an error state.
        assertEquals("job should be in error",ExecutionPhase.ERROR,job.getJobExecutionRecord().getStatus());
        
    }
    /**set up dispatcher to fail.
     * @see org.astrogrid.jes.jobscheduler.AbstractTestForJobScheduler#createDispatcher()
     */
    protected Dispatcher createDispatcher() {
        return new MockDispatcher(false);
    }

  
}


/* 
$Log: GroovySubmitNewJobNotifierFailsTest.java,v $
Revision 1.5  2004/12/03 14:47:40  jdt
Merges from workflow-nww-776

Revision 1.4.14.1  2004/12/01 21:46:26  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.4  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.3.46.1  2004/11/05 16:08:26  nw
tidied imports

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