/*$Id: ListJobsStoreFailureTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
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

import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.MockJobFactoryImpl;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.types.v1.ListCriteria;
import org.astrogrid.jes.types.v1.SubmissionResponse;
import org.astrogrid.jes.types.v1.WorkflowList;

import java.util.Iterator;

/** test behaviour of job controller listing tests when store fails.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Feb-2004
 *
 */
public class ListJobsStoreFailureTest extends AbstractTestForJobController {
    /** Construct a new ListJobsStoreFailureTest
     * @param s
     */
    public ListJobsStoreFailureTest(String s) {
        super(s);
    }
    protected void setUp() throws Exception {
        super.setUp();
        this.criteria = new ListCriteria();
        criteria.setCommunity(ListJobsSuccessTest.COMMUNITY);
        criteria.setUserId(ListJobsSuccessTest.USERID);
    }
    protected ListCriteria criteria;    
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(SubmissionResponse result) throws Exception {
        assertNotNull(result);   
        assertTrue(result.isSubmissionSuccessful());

        WorkflowList wl = jc.readJobList(criteria);
        assertNotNull(wl);
        assertNotNull(wl.getMessage()); //i.e. no errors
        assertEquals(wl.getUserId(), ListJobsSuccessTest.USERID);
        assertEquals(wl.getCommunity(), ListJobsSuccessTest.COMMUNITY);
        String[] rawArr = wl.getWorkflow();
        assertNotNull(rawArr);
        assertEquals(0,rawArr.length);
    }
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#createJobFactory()
     */
    protected AbstractJobFactoryImpl createJobFactory() {
        return new MyMockJobFactoryImpl();
        
    }
    
    /** mock that lets submits succeed (although they don't do anything), but searched fail */
    private static class MyMockJobFactoryImpl extends MockJobFactoryImpl {
        
            /**
         * @see org.astrogrid.jes.job.JobFactory#findUserJobs(java.lang.String, java.lang.String, java.lang.String)
         */
        public Iterator findUserJobs(String userid, String community, String jobListXML) throws JobException {
            throw new JobException("You wanted me to fail");
        }

}

}


/* 
$Log: ListJobsStoreFailureTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 16:51:02  nw
thorough unit testing for job controller
 
*/