/*$Id: SchedulerTaskQueueDecorator.java,v 1.3 2004/03/15 23:45:07 nw Exp $
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

import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.Executor;
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
 * @todo extend to handle delete and cancel requests.
 *
 */
public class SchedulerTaskQueueDecorator implements JobScheduler , ComponentDescriptor{

    /** 
     *  Construct a new MemoryQueueSchedulerNotifier, that will use a Queued executor to service each of the tasks in turn.
     * @param scheduler previously-constructed scheduler to pass notifications to.
     */
    public SchedulerTaskQueueDecorator(JobScheduler scheduler) {
        this.executor = new QueuedExecutor();
        this.factory = new TaskFactory(scheduler);
    }
    /** concurrency framework component that provides a task queue */
    protected final Executor executor;
    /** factory component that creates tasks to add to the queue*/
    protected final TaskFactory factory;
    /**adds a task to the queue that will call 'scheduleNewJob' with the current parameters on the wrapped job scheduler
     * @see org.astrogrid.jes.comm.SchedulerNotifier#notify(org.astrogrid.jes.job.Job)
     */
    public void scheduleNewJob(JobURN urn) throws Exception {        
        executor.execute(factory.createTask(urn));
    }
    /**adds a task to te queue that will call 'resumeJob' with the current parameters on the wrapped job scheduler
     * @see org.astrogrid.jes.comm.SchedulerNotifier#notify(org.astrogrid.jes.types.v1.JobInfo)
     */
    public void resumeJob(JobIdentifierType ji,MessageType i) throws Exception {
        executor.execute(factory.createTask(ji,i));
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
        
        private static final Log logger = LogFactory.getLog("TaskQueue");
        /** create a task to schedule new job */
        public Runnable createTask(final JobURN urn) {
            return new Runnable() {
                public void run() {
                    try {
                    js.scheduleNewJob(urn);
                    } catch (Exception e) {
                        logger.warn("schedule new job",e);
                    }
                }                
            };
        } 
        /** create a task to resume a new job */
        public Runnable createTask(final JobIdentifierType ji, final MessageType msg) {
            return new Runnable() {
                public void run() {
                    try {
                    js.resumeJob(ji,msg);
                    } catch (Exception e) {
                        logger.warn("resume job",e);
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

    }



/* 
$Log: SchedulerTaskQueueDecorator.java,v $
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