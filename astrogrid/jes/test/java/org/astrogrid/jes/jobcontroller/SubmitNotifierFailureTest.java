/*$Id: SubmitNotifierFailureTest.java,v 1.3 2004/03/09 14:23:54 nw Exp $
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

import org.astrogrid.jes.comm.MockSchedulerNotifier;
import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.impl.workflow.InMemoryJobFactoryImpl;
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
        assertTrue(((MockSchedulerNotifier)nudger).getCallCount() > 0);

        // look into store - shoudn't be an entry in the store for it. (i.e. should have been cleaned out when failed to submit).
        // in this case, means the store should be empty.
        assertEquals(0,((InMemoryJobFactoryImpl)fac).getInternalStore().size());
        
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#createNotifier()
     */
    protected SchedulerNotifier createNotifier() {
        return new MockSchedulerNotifier(false);
    }

}


/* 
$Log: SubmitNotifierFailureTest.java,v $
Revision 1.3  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/17 16:51:02  nw
thorough unit testing for job controller
 
*/