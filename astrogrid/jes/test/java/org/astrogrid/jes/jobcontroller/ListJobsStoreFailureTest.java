/*$Id: ListJobsStoreFailureTest.java,v 1.5 2004/03/15 00:06:57 nw Exp $
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

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.axis.Identifier;
import org.astrogrid.community.beans.v1.axis._Account;
import org.astrogrid.jes.delegate.v1.jobcontroller.JesFault;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.MockJobFactoryImpl;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.types.v1.WorkflowSummary;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

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
        this.acc = new _Account();
        acc.setCommunity(new Identifier(ListJobsSuccessTest.COMMUNITY));
        acc.setName(new Identifier(ListJobsSuccessTest.USERID));
    }
    protected _Account acc;
    /**
     * @see org.astrogrid.jes.jobcontroller.AbstractTest#performTest(org.astrogrid.jes.types.v1.SubmissionResponse)
     */
    protected void performTest(JobURN urn) throws Exception {
        try {
        WorkflowSummary[] wl = jc.readJobList(acc);
        fail("expected to throw exception");
        } catch (JesFault e) {
        }
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
        public Iterator findUserJobs(Account acc) throws JobException {
            throw new JobException("You wanted me to fail");
        }

}

}


/* 
$Log: ListJobsStoreFailureTest.java,v $
Revision 1.5  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.4  2004/03/09 14:23:54  nw
tests that exercise the job contorller service implememntiton

Revision 1.3  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.1  2004/02/17 16:51:02  nw
thorough unit testing for job controller
 
*/