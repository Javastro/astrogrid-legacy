/*$Id: InMemorySystemTest.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 19-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes;

import org.astrogrid.jes.comm.MemoryQueueSchedulerNotifier;
import org.astrogrid.jes.comm.SchedulerNotifier;
import org.astrogrid.jes.delegate.v1.jobcontroller.JobController;
import org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor;
import org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler;
import org.astrogrid.jes.impl.workflow.AbstractJobFactoryImpl;
import org.astrogrid.jes.impl.workflow.CastorBeanFacade;
import org.astrogrid.jes.impl.workflow.InMemoryJobFactoryImpl;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.jobscheduler.dispatcher.MockDispatcher;
import org.astrogrid.jes.jobscheduler.policy.RoughPolicy;
import org.astrogrid.jes.testutils.io.FileResourceLoader;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.Status;
import org.astrogrid.jes.types.v1.SubmissionResponse;

import EDU.oswego.cs.dl.util.concurrent.Mutex;
import EDU.oswego.cs.dl.util.concurrent.Sync;

import java.io.InputStream;
import java.util.Iterator;

/** Test that builds entire system (out of in-memory components), and feeds workflow documents into it.
 * <p>
 * tests behaviour. communication between components via direct method call - so no SOAP being tested here.
 * <p/>
 * tests that workflows fed into the system to get processed, and checks that all jobs in a workflow are executed.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
 *
 */
public class InMemorySystemTest extends AbstractTestWorkflowInputs {
    protected JobController jc;

    protected AbstractJobFactoryImpl factory;
    protected Sync barrier;

    /** Construct a new InMemorySystemTest
     * @param arg
     */
    public InMemorySystemTest(String arg) {
        super(arg);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        // create store
        factory = new InMemoryJobFactoryImpl();
        BeanFacade facade = new CastorBeanFacade(factory);
        // other things scheduler needs
        MockDispatcher dispatcher = new MockDispatcher();
        Policy policy = new RoughPolicy();
        barrier = new Mutex();
        JobScheduler scheduler = new ObservableJobScheduler(facade,dispatcher,policy,barrier);
        // create way of talking to scheduler
        SchedulerNotifier notifier = new MemoryQueueSchedulerNotifier(scheduler);
        // other components
        JobMonitor jm = new org.astrogrid.jes.jobmonitor.JobMonitor(notifier);
        jc = new org.astrogrid.jes.jobcontroller.JobController(facade,notifier);
        // close the loop, pass the monitor to the dispatcher
        dispatcher.setMonitor(jm);
        
        //
    }
    
    /**
     * @see org.astrogrid.jes.AbstractTestWorkflowInputs#testIt(java.io.InputStream, int)
     */
    protected void testIt(InputStream is, int resourceNum) throws Exception {
        // parse the document, feed it into the system.
        String docString = FileResourceLoader.streamToString(is);
        assertNotNull(docString);
        SubmissionResponse resp = jc.submitJob(docString);
        assertNotNull(resp);
        assertTrue(resp.isSubmissionSuccessful());
        // now wait for notification that the system has finished processing.
        barrier.attempt(Sync.ONE_SECOND * 3); // will block for up to three seconds waiting for notification.
        // need to wait until message queue is empty - i.e. need some sort of call back from scheduler.
        // anyhoo, get finished document from store, output it.
        Job job = factory.findJob(resp.getJobURN());
        assertNotNull(job);
        System.out.println(job.getDocumentXML());
        assertEquals(Status.COMPLETED,job.getStatus());
        // verify that all steps have been run
        for (Iterator i = job.getJobSteps(); i.hasNext(); ) {
            JobStep step = (JobStep)i.next();
            assertNotNull(step.getComment()); // as mock notifier always returns a comment
            assertEquals(Status.COMPLETED,step.getStatus());
        }
         
    }
    /** extended job scheduler that will notify us when tasks are complete. 
     * does this by holding on to a synch object, only releasing when job is done.*/
    public static class ObservableJobScheduler extends org.astrogrid.jes.jobscheduler.JobScheduler {

        /** Construct a new ObservableJobScheduler
         * @param facade
         * @param dispatcher
         * @param policy
         */
        public ObservableJobScheduler(BeanFacade facade, Dispatcher dispatcher, Policy policy,Sync barrier){
            super(facade, dispatcher, policy);
            this.barrier = barrier;
            try {
            barrier.acquire();
            } catch (InterruptedException e) {
                // oh well. everythings broken then.
                throw new RuntimeException("Can't acquire lock",e);
            }
        }
        protected Sync barrier;
        /**
         * @see org.astrogrid.jes.jobscheduler.JobScheduler#notifyJobFinished(org.astrogrid.jes.job.Job)
         */
        public void notifyJobFinished(Job job) {
            barrier.release();
        }

        /**
         * @see org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler#scheduleNewJob(org.astrogrid.jes.types.v1.JobURN)
         */
        public void scheduleNewJob(JobURN jobURN) {

            super.scheduleNewJob(jobURN);
        }

    }
}


/* 
$Log: InMemorySystemTest.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.2  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/