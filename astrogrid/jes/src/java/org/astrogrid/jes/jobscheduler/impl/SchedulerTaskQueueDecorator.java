/*$Id: SchedulerTaskQueueDecorator.java,v 1.7 2004/11/05 16:52:42 jdt Exp $
 * Created on 18-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl;

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.Startable;

import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
import junit.framework.Test;

/** A scheduler implementation that decorates another scheduler with a task queue.
 * <p>
 * the decorated scheduler executes in a separate, single thread. Any method calls are encapsulated as task objects, and added to the task queue.
 * <p>
 * This ensures that the decorated scheduler executes in a single-threaded manner - as all requests to it are serialized by the task queue. Because of this,
 * there is no need for the underlying implementation to worry about synchronization, concurrency, etc. In particular, underlying Job stores need provide no locking 
 * ability - as there will never by multiple concurrent updates to a job execution record.
 * <p>
 * Although the single-threaded nature may appear to be a bottle neck, we can expect this design to scale quite well - the scheduler is bound by IO 
 * due to   communication with external web services, so serial processing of tasks is not a problem - each task is quick to process and short-lived.
 * There could be further gains due to removal of overhead in calling synchronized methods and aquiring locks in the job store. 
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class SchedulerTaskQueueDecorator implements JobScheduler , ComponentDescriptor, Startable{

    /** 
     *  Construct a new MemoryQueueSchedulerNotifier, that will use a Queued executor to service each of the tasks in turn.
     * @param scheduler previously-constructed scheduler to pass notifications to.
     */
    public SchedulerTaskQueueDecorator(JobScheduler scheduler) {
        this.executor = new QueuedExecutor();
        this.factory = new TaskFactory(scheduler);
    }
    /** concurrency framework component that provides a task queue */
    protected final QueuedExecutor executor;
    /** factory component that creates tasks to add to the queue*/
    protected final TaskFactory factory;
    /**adds a task to the queue that will call 'scheduleNewJob' with the current parameters on the wrapped job scheduler
     * @see org.astrogrid.jes.comm.SchedulerNotifier#notify(org.astrogrid.jes.job.Job)
     */
    public void scheduleNewJob(JobURN urn) throws Exception {        
        executor.execute(factory.createSubmitJobTask(urn));
    }
    /**adds a task to te queue that will call 'resumeJob' with the current parameters on the wrapped job scheduler
     * @see org.astrogrid.jes.comm.SchedulerNotifier#notify(org.astrogrid.jes.types.v1.JobInfo)
     */
    public void resumeJob(JobIdentifierType ji,MessageType i) throws Exception {
        executor.execute(factory.createResumeTask(ji,i));
    }    
    
    /**
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#abortJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void abortJob(JobURN jobURN) throws Exception {
        executor.execute(factory.createAbortJobTask(jobURN));
       
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#deleteJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void deleteJob(JobURN jobURN) throws Exception {
        executor.execute(factory.createDeleteJobTask(jobURN));
    }    

    /**
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#reportResults(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.ResultListType)
     */
    public void reportResults(JobIdentifierType id, ResultListType results) throws Exception {
        executor.execute(factory.createReportResultsJobTask(id,results));
    }
    
    /** general method to add another runnable to the queue. useful for testing - i.e. can insert a 'end of test' runnable 
     * after all other tasks.
     * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
     *
     */
    public void addTask(Runnable r)  throws Exception {
        executor.execute(r);
    }
    /** helper class to build runnable tasks to be passed to the executor. Ensuring that only this class has a reference to the
     * wrapped job scheduler itself ensures that we don't accidentaly call it directly;
     * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
     *
     */
    private static class TaskFactory {
        public TaskFactory(JobScheduler js) {
            this.js = js;
        }
        final JobScheduler js;
                
        /**
         * @param id
         * @param results
         * @return
         */
        public Runnable createReportResultsJobTask(final JobIdentifierType id, final ResultListType results) {
            return new Runnable() {
                public void run() {
                    try {
                        js.reportResults(id,results);
                    } catch (Exception e) {
                        logger.error("report results",e);
                    }
                }
            };
        }

        
        private static final Log logger = LogFactory.getLog("TaskQueue");
        /** create a task to schedule new job */
        public Runnable createSubmitJobTask(final JobURN urn) {
            return new Runnable() {
                public void run() {
                    try {
                    js.scheduleNewJob(urn);
                    } catch (Exception e) {
                        logger.error("schedule new job",e);
                    }
                }                
            };
        } 
        /** create a task to resume a new job */
        public Runnable createResumeTask(final JobIdentifierType ji, final MessageType msg) {
            return new Runnable() {
                public void run() {
                    try {
                    js.resumeJob(ji,msg);
                    } catch (Exception e) {
                        logger.error("resume job",e);
                    }
                }
            };
        }
        public Runnable createAbortJobTask(final JobURN urn) {
            return new Runnable() {
                public void run() {
                    try {
                        js.abortJob(urn);
                    } catch (Exception e) {
                        logger.error("abort job",e);
                    }
                }
            };
        }
        public Runnable createDeleteJobTask(final JobURN urn) {
            return new Runnable() {
                public void run() {
                    try {
                        js.deleteJob(urn);
                    } catch (Exception e) {
                        logger.error("delete job",e);
                    }
                }
            };
        }
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "MemoryQueueSchedulerNotifier";
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Notifier that interacts with in-process scheduler,running in a separate thread." +            "Notifications passed onto task queue for this thread";  
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
    }
    /**shuts down the scheduler thread.
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        executor.shutdownNow();
    }



    }



/* 
$Log: SchedulerTaskQueueDecorator.java,v $
Revision 1.7  2004/11/05 16:52:42  jdt
Merges from branch nww-itn07-scratchspace

Revision 1.6.18.1  2004/11/05 15:43:49  nw
tidied imports

Revision 1.6  2004/09/16 21:43:10  nw
enabled worker thread to be shut down.

Revision 1.5  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.4  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.3  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.2  2004/03/15 01:30:45  nw
factored component descriptor out into separate package

Revision 1.1  2004/03/15 00:30:54  nw
factored implemetation of scheduler out of parent package.

Revision 1.6  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.5  2004/03/07 21:04:39  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.4.4.1  2004/03/07 20:38:52  nw
added componet descriptor interface impl,
refactored any primitive types passed into constructor

Revision 1.4  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/19 13:33:17  nw
removed rough scheduler notifier, replaced with one
using SOAP delegate.

added in-memory concurrent notifier
 
*/