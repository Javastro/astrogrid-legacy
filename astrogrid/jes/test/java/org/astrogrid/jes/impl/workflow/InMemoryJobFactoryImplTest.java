/*$Id: InMemoryJobFactoryImplTest.java,v 1.4 2004/03/04 01:57:35 nw Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.AbstractTestWorkflowInputs;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.io.InputStream;
import java.util.Iterator;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/** Test persistence funcitonality of the in-memory job factory
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Feb-2004
 *
 */
public class InMemoryJobFactoryImplTest extends AbstractTestWorkflowInputs {
    public static final String GROUP_ID = "jodrell";
    public static final String USER_ID = "nww";
    /**
     * Constructor for InMemoryJobFactoryImplTest.
     * @param arg0
     */
    public InMemoryJobFactoryImplTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    protected static JobFactory jf;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    
    /** construct a test suite explicitly, so that we can setup stuff once at the start of a series of tests
     * <p>
     * want to be able to do this so that only one jobFactoryImpl instance is created, and a batch of tests performed on it
     * @return
     */
    public static Test suite() {

        TestSuite tests = new TestSuite(InMemoryJobFactoryImplTest.class);
        tests.addTest(new InMemoryJobFactoryImplTest("finallyTestListJobs"));        
        tests.addTest(new InMemoryJobFactoryImplTest("finallyTestDeleteLast"));
        tests.addTest(new InMemoryJobFactoryImplTest("finallyTestListUnknownJobs"));
        tests.addTest(new InMemoryJobFactoryImplTest("finallyTestUnknownJobs"));
        return new TestSetup(tests) {
            protected void setUp() throws Exception {
                jf = new InMemoryJobFactoryImpl();
            }
        };
    }


    /**
     * @see org.astrogrid.jes.impl.workflow.WorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
        SubmitJobRequest req = new SubmitJobRequestImpl(is);
        assertNotNull(req);
        // create job
        Workflow j = jf.createJob(req);
        assertNotNull(j);
        JobURN jobURN = j.getJobExecutionRecord().getJobId();
        lastURN = jobURN;
        assertNotNull(jobURN);
        // find job
        Workflow j1 = jf.findJob(jobURN);
        assertNotNull(j1);
        assertEquals(jobURN.getContent(),j1.getJobExecutionRecord().getJobId().getContent());
        //update job - change the status code
        ExecutionPhase newStatus = ExecutionPhase.RUNNING;
        // quick check its not already that..
        if (newStatus.equals(j.getJobExecutionRecord().getStatus())) {
            newStatus = ExecutionPhase.COMPLETED;
        }
        j.getJobExecutionRecord().setStatus(newStatus);
        jf.updateJob(j);
        Workflow j2 = jf.findJob(jobURN);
        assertNotNull(j2);
        assertEquals(newStatus,j2.getJobExecutionRecord().getStatus());
        
    }
    /** used to record an arbitrary urn, for use later */
    protected static JobURN lastURN;
    
    /** another unit test to run at the end */
    public void finallyTestListJobs() throws Exception {
        Account acc = new Account();
        acc.setName(USER_ID);
        acc.setCommunity(GROUP_ID);
        Iterator i = jf.findUserJobs(acc);
        assertNotNull(i);
        assertTrue(i.hasNext());
        while (i.hasNext()) {
            Object o = i.next();
            assertNotNull(o);
            assertTrue(o instanceof Workflow);
            Account j = ((Workflow)o).getCredentials().getAccount();
            
            assertEquals(USER_ID,j.getName());
            assertEquals(GROUP_ID,j.getCommunity());
        }
    }   
    
    public void finallyTestListUnknownJobs() throws Exception {
        // should still return an iterator. just an empty one.
        Account acc = new Account();
        acc.setName("non existent");
        acc.setCommunity("no community");
        Iterator i = jf.findUserJobs(acc);
        assertNotNull(i);
        assertFalse(i.hasNext());                
    }    
    
    public void finallyTestUnknownJobs() throws Exception {
        JobURN unknownURN = new JobURN();
        unknownURN.setContent("unknown:urn:job");
        try {
        Workflow j = jf.findJob(unknownURN);
        fail("should have thrown");
        } catch (NotFoundException ne) {
            // ok expected
        } 
    }
    
    
    /** this is a unit test, but doesn't follow naming convention of test* so that it isn't picked up by reflection - needs to be run last*/
    public void finallyTestDeleteLast() throws Exception {
        Workflow j = jf.findJob(lastURN);
        assertNotNull("lastURN has no corresponding job",j);
        jf.deleteJob(j);
        try {
         jf.findJob(lastURN);
         fail("should have thrown not found exception");
        } catch (NotFoundException ne) {
         // ok, expected
        }
    }
    


    
}


/* 
$Log: InMemoryJobFactoryImplTest.java,v $
Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.3  2004/03/03 01:13:41  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.4  2004/02/19 13:41:25  nw
added tests for everything :)

Revision 1.1.2.3  2004/02/17 15:46:31  nw
updated to throw exceptions when jobs are not found

Revision 1.1.2.2  2004/02/17 12:57:11  nw
improved documentation

Revision 1.1.2.1  2004/02/12 01:12:54  nw
skeleton unit tests for the castor object model
 
*/