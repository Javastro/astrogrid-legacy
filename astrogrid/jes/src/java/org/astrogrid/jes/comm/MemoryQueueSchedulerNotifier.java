/*$Id: MemoryQueueSchedulerNotifier.java,v 1.3 2004/03/03 01:13:42 nw Exp $
 * Created on 18-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.comm;

import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;

import java.rmi.RemoteException;

/** Notifier that maintains a queue, adds notifications to it, where they are consumed by a scheduler running in a different thread.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class MemoryQueueSchedulerNotifier implements SchedulerNotifier {
    /**
     *  Construct a new MemoryQueueSchedulerNotifier
     * @param exec executor to use to service each of the tasks
     * @param scheduler scheduler to use to service tasks.
     */
    public MemoryQueueSchedulerNotifier(Executor exec,JobScheduler scheduler) {
        this.executor = exec;
        this.factory = new TaskFactory(scheduler);
    }
    /** 
     *  Construct a new MemoryQueueSchedulerNotifier, that will use a Queued executor to service each of the tasks in turn.
     * @param scheduler previously-constructed scheduler to pass notifications to.
     */
    public MemoryQueueSchedulerNotifier(JobScheduler scheduler) {
        this(new QueuedExecutor(),scheduler);
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
                        org.astrogrid.jes.types.v1.JobURN convertedURN = new org.astrogrid.jes.types.v1.JobURN(urn.toString());
                    js.scheduleNewJob(convertedURN);
                    } catch (RemoteException e) {
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
                    } catch (RemoteException e) {
                        logger.warn("resume job",e);
                    }
                }
            };
        }
    }

    }



/* 
$Log: MemoryQueueSchedulerNotifier.java,v $
Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/19 13:33:17  nw
removed rough scheduler notifier, replaced with one
using SOAP delegate.

added in-memory concurrent notifier
 
*/