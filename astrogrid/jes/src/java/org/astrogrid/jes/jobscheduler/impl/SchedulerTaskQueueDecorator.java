/*$Id: SchedulerTaskQueueDecorator.java,v 1.1 2004/03/15 00:30:54 nw Exp $
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

import org.astrogrid.jes.component.ComponentDescriptor;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
import junit.framework.Test;

/** Notifier that maintains a queue, adds notifications to it, where they are consumed by a scheduler running in a different thread.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
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
    protected final Executor executor;
    protected final TaskFactory factory;
    /**
     * @see org.astrogrid.jes.comm.SchedulerNotifier#notify(org.astrogrid.jes.job.Job)
     */
    public void scheduleNewJob(JobURN urn) throws Exception {        
        executor.execute(factory.createTask(urn));
    }
    /**
     * @see org.astrogrid.jes.comm.SchedulerNotifier#notify(org.astrogrid.jes.types.v1.JobInfo)
     */
    public void resumeJob(JobIdentifierType ji,MessageType i) throws Exception {
        executor.execute(factory.createTask(ji,i));
    }    
    
    /** hidden method - add another runnable to the queue. useful for testing - i.e. can insert a 'end of test' runnable 
     * after all other tasks.
     * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
     *
     */
    public void addTask(Runnable r)  throws Exception {
        executor.execute(r);
    }
    /** helper class to build runnable tasks to be passed to the executor. Ensuring that only this class has a reference to the
     * job scheduler itself ensures that we don't accidentaly call it directly;
     * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
     *
     */
    static class TaskFactory {
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