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
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.jobscheduler.Dispatcher;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import junit.framework.Test;

/** Default implementation of a Job Scheduler
 * 
 */
public class SchedulerImpl implements org.astrogrid.jes.jobscheduler.JobScheduler, ComponentDescriptor {
	
    /** Construct a scheduler
     *  Construct a new JobScheduler
     * @param facade interface to persistent store, and object model
     * @param dispatcher interface to something that dispatches individual job steps
     * @param policy interface to something that determines which job steps to execute next
     */
    public SchedulerImpl(BeanFacade facade,Dispatcher dispatcher,Policy policy) {
        assert facade != null;
        assert dispatcher != null;
        assert policy != null;
        this.policy = policy;
        this.facade = facade;
        this.dispatcher = dispatcher;
    }
    /** the policy used to determine which steps to run next */
    protected final Policy policy;
    /** facade to a job factory */
    protected final BeanFacade facade;
    /** dispatcher for new jobs */
    protected final Dispatcher dispatcher;
    

	private static Log
		logger = LogFactory.getLog( SchedulerImpl.class ) ;
		
    /** register a new job with the scheduler
     * <p>
     * initializes execution records for the job, then starts the job running.
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#scheduleNewJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void scheduleNewJob( JobURN jobURN ) {
        logger.debug("Scheduling new Job: " + jobURN.toString());
        try {
	        JobFactory factory = facade.getJobFactory() ;
	        factory.begin() ;
	        Workflow job;
            try {
                job = factory.findJob( Axis2Castor.convert(jobURN)) ;
            } catch (NotFoundException e) {
                logger.error("Could not find job for urn:" + jobURN.getValue());
                return;
            } 
            
            job.getJobExecutionRecord().setStartTime(new Date());
            job.getJobExecutionRecord().setStatus(ExecutionPhase.INITIALIZING);
            factory.updateJob(job);            
            // Schedule one or more job steps....
            scheduleSteps( job ) ;
            updateJobStatus(job);
			factory.updateJob( job ) ;              // Update any changed details to the database                           		
			factory.end ( true ) ;   // Commit and cleanup
        } catch (JesException e) {
            logger.error(e);
        }

         	 
    } // end of scheduleJob()

    /**
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#abortJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void abortJob(JobURN jobURN)  {
        logger.debug("Aborting job: " + jobURN.toString());
        try {
            JobFactory factory = facade.getJobFactory();
            Workflow job = factory.findJob(Axis2Castor.convert(jobURN));
            //check current phase
            ExecutionPhase currentPhase = job.getJobExecutionRecord().getStatus();
            if (currentPhase.getType() < ExecutionPhase.COMPLETED_TYPE) { // i.e. still running, or not running yet..
                logger.debug("marking job as in error");
                job.getJobExecutionRecord().setStatus(ExecutionPhase.ERROR);
                MessageType msg = new MessageType();
                msg.setContent("Aborted by user");
                msg.setSource("JES");
                msg.setTimestamp(new Date());
                msg.setPhase(currentPhase);
                msg.setLevel(LogLevel.INFO);
                job.getJobExecutionRecord().addMessage(msg);
                factory.updateJob(job);
            } else {
                logger.debug("job has already finished");
            }
        } catch (NotFoundException e) {
            logger.warn("Attempted to abort job that doesn't exist:" + jobURN.getValue());
        } catch (JesException e) {
           logger.error("AbortJob",e);
        }
        
    }


    /**
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#deleteJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void deleteJob(JobURN jobURN) {
        logger.debug("Deleting job" + jobURN.toString());
        try {
            JobFactory factory = facade.getJobFactory();
            Workflow job = factory.findJob(Axis2Castor.convert(jobURN));
            factory.deleteJob(job);
        } catch (NotFoundException e) {
            logger.warn("Attempted to delete job that doesn't exist: " + jobURN.getValue());
        } catch (JesException e) {
            logger.error("DeleteJob",e);
        }
    }


    /** resume executioin of a job
     * <p>
     * records information returned by tool execution in the workflow document, and then attempts to execute further steps in the workflow.
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#resumeJob(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType)
     */
    public void resumeJob(JobIdentifierType id,org.astrogrid.jes.types.v1.cea.axis.MessageType info) {
        logger.debug("Resuming executioin of " + id.toString());
        Workflow job = null;  
        JobFactory factory = null;  
        try {
             factory = facade.getJobFactory() ;
             factory.begin() ;
            org.astrogrid.workflow.beans.v1.execution.JobURN urn = null;          
             try {
                  urn = JesUtil.extractURN(id);
                job = factory.findJob(urn ) ;
             } catch (NotFoundException e) {
                 logger.error("Could not find job for urn" + urn.getContent());
                 return;
             }        
                  
             String xpath = JesUtil.extractXPath(id);
             Step jobStep = (Step)job.findXPathValue(xpath);
             if (jobStep == null) {
                 logger.error("Culd not find step " + xpath + " for urn " + urn.getContent());
                 return;
             }              
             //add message into execution record for step.      
             StepExecutionRecord er =JesUtil.getLatestOrNewRecord(jobStep);             
             er.addMessage(Axis2Castor.convert(info));
             ExecutionPhase status = Axis2Castor.convert(info.getPhase());
             // only update status if executioin record hasn't already passed this status.
             if (status.getType() > er.getStatus().getType()) {
                er.setStatus(status);
                if (status.getType() >= ExecutionPhase.COMPLETED_TYPE) { // finished or error
                     er.setFinishTime(new Date()); 
                }
              }
              factory.updateJob(job);
                            
             // now go try run some more steps.
             scheduleSteps(job);  
             updateJobStatus(job);
             factory.updateJob( job ) ;             // Update any changed details to the database
             factory.end( true ) ;   // Commit and cleanup
        } catch (JesException e) {
            // basically, somethings gone wrong with the job store, rather than with steps. its a fatal.
            logger.fatal("System error",e);
            // could try saving the error in the job itself.
            if (job != null) {
                MessageType mt = new MessageType();
                mt.setPhase(ExecutionPhase.ERROR);
                mt.setLevel(LogLevel.ERROR);
                mt.setSource("Jes System");
                mt.setTimestamp(new Date());               
                mt.setContent("System Error " + e.getClass().getName() + " " + e.getMessage());
                job.getJobExecutionRecord().addMessage(mt);
                job.getJobExecutionRecord().setStatus(ExecutionPhase.ERROR);
                try {
                    factory.updateJob(job);
                } catch (JesException jex) {
                    logger.fatal("can't save error report into workflow",jex);
                }
            } 
        }

    }     
    
    /** create a new execution record, pre-populated */
    protected StepExecutionRecord newStepExecutionRecord() {
        StepExecutionRecord result = new StepExecutionRecord();
        result.setStartTime(new Date());
        result.setStatus(ExecutionPhase.INITIALIZING);
        return result;
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
                        logger.info("Step execution failed:",t);                 
                         MessageType message = new MessageType();
                         StringBuffer buff = new StringBuffer();
                        buff.append("Failed: ")
                            .append( t.getClass().getName())
                            .append('\n')
                            .append( t.getMessage())
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
                    }               
                    step = policy.nextExecutableStep(job);        
                } // end while
                // no more runnable jobs at the moment.
    } // end of scheduleSteps()

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