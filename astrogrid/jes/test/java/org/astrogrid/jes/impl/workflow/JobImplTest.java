/*$Id: JobImplTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 11-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.impl.workflow;

import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobStep;

import java.io.InputStream;
import java.util.Iterator;

/** unit test for job impl. just concerned about whether iterator of job steps is correct or not really.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class JobImplTest extends AbstractTestWorkflowInputs {
    /**
     * Constructor for JobImplTest.
     * @param arg0
     */
    public JobImplTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    /**
     * @see org.astrogrid.jes.impl.workflow.WorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
        SubmitJobRequestImpl req = new SubmitJobRequestImpl(is);
        Job job = new JobImpl(req);
        assertNotNull(job);
        Iterator i = job.getJobSteps();
        assertNotNull(i);
        assertTrue(i.hasNext());
        int count;
        for (count = 0; i.hasNext(); count++) {
            Object o = i.next();
            assertNotNull(o);
            assertTrue(o instanceof JobStep);
            JobStep js = (JobStep)o;
            // maybe could add some more checking in here.
        }
        try {
            assertEquals("Didn't see expected number of jobs", expectedSeenJobs[resourceNum],count);
        } catch (IndexOutOfBoundsException e) {
            fail("need to update expectedSeenJobs after altering workflowInputs");
        }
    }
    /** number of jobs we expect to see in each workflow */
    public final static int[] expectedSeenJobs = {1,4,3,2,1,9};

}


/* 
$Log: JobImplTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.3  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.2  2004/02/17 12:57:11  nw
improved documentation

Revision 1.1.2.1  2004/02/12 01:12:54  nw
skeleton unit tests for the castor object model
 
*/