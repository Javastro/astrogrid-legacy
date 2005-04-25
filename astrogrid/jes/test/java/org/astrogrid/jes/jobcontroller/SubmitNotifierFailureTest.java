/*$Id: SubmitNotifierFailureTest.java,v 1.6 2005/04/25 12:13:54 clq2 Exp $
 * Created on 17-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobcontroller;

import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.impl.MockSchedulerImpl;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** Test what happens when notifier fails.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2004
 *
 */
public class SubmitNotifierFailureTest extends AbstractTestForJobController {
    /** Construct a new SubmitNotifierFailureTest
     * @param s
     */
    public SubmitNotifierFailureTest(String s) {
        super(s);
    }
    
    
    /** expect the submit to fail - so there should be a message, and jobURN should be null.
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(JobURN urn) throws Exception {
        assertNotNull(seenException);
        // check notifier was called - double check we failed in the right place.
        assertTrue(((MockSchedulerImpl)nudger).getCallCount() > 0);

        // look into store - shoudn't be an entry in the store for it. (i.e. should have been cleaned out when failed to submit).
        // in this case, means the store should be empty.
        //assertEquals(0,((InMemoryJobFactoryImpl)fac).getInternalStore().size());
    
        
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#createNotifier()
     */
    protected JobScheduler createNotifier() {
        return new MockSchedulerImpl(false);
    }

}


/* 
$Log: SubmitNotifierFailureTest.java,v $
Revision 1.6  2005/04/25 12:13:54  clq2
jes-nww-776-again

Revision 1.5.170.1  2005/04/11 13:57:56  nw
altered to use fileJobFactory instead of InMemoryJobFactory - more realistic

Revision 1.5  2004/03/15 00:32:01  nw
merged contents of comm package into jobscheduler package.

Revision 1.4  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.3  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/17 16:51:02  nw
thorough unit testing for job controller
 
*/