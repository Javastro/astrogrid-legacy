/*
 * @(#)JobScheduler.java   1.0
 *  
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobscheduler.impl;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import junit.framework.Test;

/** Default implementation of a Job Scheduler
 * 
 */
public class SchedulerImpl extends AbstractJobSchedulerImpl implements org.astrogrid.jes.jobscheduler.JobScheduler, ComponentDescriptor {
	
    /** Construct a scheduler
     *  Construct a new JobScheduler
     * @param facade interface to persistent store, and object model
     * @param dispatcher interface to something that dispatches individual job steps
     * @param policy interface to something that determines which job steps to execute next
     */
    public SchedulerImpl(JobFactory factory,Dispatcher dispatcher,Policy policy) {
        super(factory);
        assert dispatcher != null;
        assert policy != null;
        this.policy = policy;
        this.dispatcher = dispatcher;
    }
    /** the policy used to determine which steps to run next */
    protected final Policy policy;
    /** dispatcher for new jobs */
    protected final Dispatcher dispatcher;
    
    /** does nothing */
    protected Workflow initializeJob(Workflow job) {
        return job;
    }    		
    /** fragment is an xpath into the workflow document */
    protected Step getStepForFragment(Workflow job, String fragment) {
        return (Step)job.findXPathValue(fragment);
    }
    
    /** 
    use policy to determine one or more steps that can be executed.
    <p>
    update status (and record any errors) for each step execution
    when no more steps can be executed, update status of job.
    */
    protected void scheduleSteps( Workflow job ) {
                Step step = policy.nextExecutableStep(job);
                while( step != null   ) {
                   StepExecutionRecord er = newStepExecutionRecord();
                   step.addStepExecutionRecord(er);
                    try{
                        dispatcher.dispatchStep( job, step ) ;
                        er.setStatus(ExecutionPhase.RUNNING);
                    } catch (Throwable t) { // absolutely anything
                        Throwable root = findRootCause(t);
                        logger.info("Step: '" + step.getName() + "' (Tool: '" + step.getTool().getName() + "'): execution failed. Root Cause:",root);
                        logger.debug("Full stacktrace of failure",t);              
                         MessageType message = new MessageType();
                         StringBuffer buff = new StringBuffer();
                        buff.append("Failed: ")
                            .append( root.getClass().getName())
                            .append('\n')
                            .append( root.getMessage())
                            .append('\n');
                         StringWriter writer = new StringWriter();                            
                            t.printStackTrace(new PrintWriter(writer));
                          buff.append(writer.toString());
                          
                         message.setContent(buff.toString());
                         message.setLevel(LogLevel.ERROR);
                         message.setSource("Dispatcher");
                         message.setPhase(ExecutionPhase.ERROR);
                         message.setTimestamp(new Date());
                         er.addMessage(message);
                         er.setStatus(ExecutionPhase.ERROR);
                         er.setFinishTime(new Date());
                    }               
                    step = policy.nextExecutableStep(job);        
                } // end while
                // no more runnable jobs at the moment.
    } // end of scheduleSteps()
    
    private Throwable findRootCause(final Throwable t) {
        Throwable result = t;
        while (result.getCause() != null) {
            result = result.getCause();
        }
        return result;

    }

    /** use policy to calculate status of job. if policy decides job is finished, record finish date, notify that job is finished. */
    protected void updateJobStatus(Workflow job) {
        ExecutionPhase status = policy.currentJobStatus(job);
        job.getJobExecutionRecord().setStatus(status);
        if (status.getType() >= ExecutionPhase.COMPLETED_TYPE) { // finished or in error
            job.getJobExecutionRecord().setFinishTime(new Date());
            this.notifyJobFinished(job);
        }      
    }

          
    /**
     * Called when a job is completed, to notify some external observer
     * <p>
     * empty for now - extend as needed.
     */
    public void notifyJobFinished(Workflow job) {
        JobExecutionRecord er = job.getJobExecutionRecord();
        logger.info("Job " + er.getJobId().getContent() + " finished with status " + er.getStatus());
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Standard Job Scheduler";
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Standard Implementation";
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }

} // end of class JobScheduler