/*$Id: SubmitStoreFailureTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
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
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.MockJobFactoryImpl;
import org.astrogrid.jes.types.v1.SubmissionResponse;

/** Test behaviour of job controller when store breaks down.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2004
 *
 */
public class SubmitStoreFailureTest extends AbstractTestForJobController {
    /** Construct a new SubmitStoreFailureTest
     * @param s
     */
    public SubmitStoreFailureTest(String s) {
        super(s);
    }
    /** failed to store item.
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(SubmissionResponse result) throws Exception {
        assertNotNull(result);
        assertFalse(result.isSubmissionSuccessful());
        assertNotNull(result.getMessage());        
        assertNull(result.getJobURN());
        // don't expect notifier to have been called.
        assertEquals(0,((MockSchedulerNotifier)nudger).getCallCount());
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#createJobFactory()
     */
    protected AbstractJobFactoryImpl createJobFactory() {
        return new MockJobFactoryImpl(false);
    }

}


/* 
$Log: SubmitStoreFailureTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/17 16:51:02  nw
thorough unit testing for job controller
 
*/