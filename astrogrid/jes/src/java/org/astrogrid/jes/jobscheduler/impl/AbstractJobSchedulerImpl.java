/*$Id: AbstractJobSchedulerImpl.java,v 1.3 2004/07/09 15:49:08 nw Exp $
 * Created on 10-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl;

import org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-May-2004
 *
 */
public abstract class AbstractJobSchedulerImpl implements JobScheduler {
    /** Construct a new AbstractJobSchedulerImpl
     * 
     */
    public AbstractJobSchedulerImpl(JobFactory factory) {
        assert factory != null;        
        this.factory = factory;
    }

    /** facade to a job factory */
    protected final JobFactory factory;
    protected static final Log logger = LogFactory.getLog( JobScheduler.class );


    /** register a new job with the scheduler
     * <p>
     * initializes execution records for the job, then starts the job running.
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#scheduleNewJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public final void scheduleNewJob( JobURN jobURN ) {
        logger.debug("Scheduling new Job: " + jobURN.toString());
        try {
            Workflow job;
            try {
                job = factory.findJob( Axis2Castor.convert(jobURN)) ;
            } catch (NotFoundException e) {
                logger.error("Could not find job for urn:" + jobURN.getValue());
                return;
            } 
            try {
                job = initializeJob(job);   
                logger.debug("initialized job " + job.getJobExecutionRecord().getJobId().getContent());
            } catch (Exception e) {
                logger.error("Could not initialize job for urn:" + jobURN.getValue(),e);
                return;
            }
            job.getJobExecutionRecord().setStartTime(new Date());
            job.getJobExecutionRecord().setStatus(ExecutionPhase.INITIALIZING);
            factory.updateJob(job);
                                  
            // Schedule one or more job steps....
            scheduleSteps( job ) ;
         
            updateJobStatus(job);             
            factory.updateJob( job ) ;              // Update any changed details to the database                                                
        } catch (JesException e) {
            logger.error(e);
        }

             
    }

    /** initialize the job / workflow document, ready for execution */
    protected abstract Workflow initializeJob(Workflow job) throws Exception;

    /** resume executioin of a job
     * <p>
     * records information returned by tool execution in the workflow document, and then attempts to execute further steps in the workflow.
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#resumeJob(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType)
     */
    public final void resumeJob(JobIdentifierType id,org.astrogrid.jes.types.v1.cea.axis.MessageType info) {
        logger.debug("Resuming executioin of " + id.toString());
        Workflow job = null;  
        try {
            org.astrogrid.workflow.beans.v1.execution.JobURN urn = null;          
             try {
                  urn = JesUtil.extractURN(id);
                job = factory.findJob(urn ) ;
             } catch (NotFoundException e) {
                 logger.error("Could not find job for urn" + urn.getContent());
                 return;
             }        
                  
             String fragment = JesUtil.extractXPath(id);
             Step jobStep = getStepForFragment(job, fragment);
             if (jobStep == null) {
                 logger.error("Culd not find step " + fragment + " for urn " + urn.getContent());
                 return;
             }              
             //add message into execution record for step.      
             StepExecutionRecord er =JesUtil.getLatestOrNewRecord(jobStep);             
             er.addMessage(Axis2Castor.convert(info));
             ExecutionPhase status = Axis2Castor.convert(info.getPhase());
            updateStepStatus(job,jobStep, status);
              factory.updateJob(job);
                            
             // now go try run some more steps.
             scheduleSteps(job);  
             updateJobStatus(job);
             factory.updateJob( job ) ;             // Update any changed details to the database
        } catch (JesException e) {
            // basically, somethings gone wrong with the job store, rather than with steps. its a fatal.
            logger.fatal("System error",e);
            // could try saving the error in the job itself.
            if (job != null) {
                recordFatalError(job, e);
                try {
                    factory.updateJob(job);
                } catch (JesException jex) {
                    logger.fatal("can't save error report into workflow",jex);
                }
            } 
        }

    }

    /** record a thrown exception in the workflow document,
     * and set status of entrire workflow to error
     * @param job
     * @param e
     */
    protected void recordFatalError(Workflow job, JesException e) {
        MessageType mt = new MessageType();
        mt.setPhase(ExecutionPhase.ERROR);
        mt.setLevel(LogLevel.ERROR);
        mt.setSource("Jes System");
        mt.setTimestamp(new Date());     
        StringWriter content = new StringWriter();
        PrintWriter pw = new PrintWriter(content);
        pw.println("System Error " + e.getClass().getName());
        e.printStackTrace(pw);
        mt.setContent(content.toString());
        job.getJobExecutionRecord().addMessage(mt);
        job.getJobExecutionRecord().setStatus(ExecutionPhase.ERROR);
    }

    protected void updateStepStatus(Workflow wf,Step step, ExecutionPhase status) {
           // only update status if executioin record hasn't already passed this status.        
           StepExecutionRecord er = JesUtil.getLatestOrNewRecord(step); 
            if (status.getType() > er.getStatus().getType()) {
               er.setStatus(status);
               if (status.getType() >= ExecutionPhase.COMPLETED_TYPE) { // finished or error
                    er.setFinishTime(new Date()); 
               }
             }           
    }   
    /**
     * update the status of the entire job - involves rescanning tree in someway.
     */
    protected abstract void updateJobStatus(Workflow job);
    

    /** project the step correspondinig to this fragment from the workflow 
     * @return the step, or null if not found.*/
    protected abstract Step getStepForFragment(Workflow job, String fragment); 
    
    /**
     * select steps that can be executed, and fire them off.
     */
    protected abstract void scheduleSteps(Workflow job) ;
    


    /** helper method to create a new execution record, pre-populated */
    public static StepExecutionRecord newStepExecutionRecord() {
        StepExecutionRecord result = new StepExecutionRecord();
        result.setStartTime(new Date());
        result.setStatus(ExecutionPhase.INITIALIZING);
        return result;
    }
        
    /**
         * @see org.astrogrid.jes.jobscheduler.JobScheduler#abortJob(org.astrogrid.jes.types.v1.JobURN)
         */
    public final void abortJob(JobURN jobURN) {
        logger.debug("Aborting job: " + jobURN.toString());
        try {
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
    public final void deleteJob(JobURN jobURN) {
        logger.debug("Deleting job" + jobURN.toString());
        try {
            Workflow job = factory.findJob(Axis2Castor.convert(jobURN));
            factory.deleteJob(job);
        } catch (NotFoundException e) {
            logger.warn("Attempted to delete job that doesn't exist: " + jobURN.getValue());
        } catch (JesException e) {
            logger.error("DeleteJob",e);
        }
    }
    


    /** Record results of a job step execution
     * @see org.astrogrid.jes.jobscheduler.JobScheduler#reportResults(org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.ResultListType)
     */
    public final void reportResults(JobIdentifierType id, ResultListType results) throws Exception {
        logger.debug("reporting results of " + id.toString());
        Workflow job = null;
        try {
            org.astrogrid.workflow.beans.v1.execution.JobURN urn = null;          
            try {
                 urn = JesUtil.extractURN(id);
               job = factory.findJob(urn ) ;
            } catch (NotFoundException e) {
                logger.error("Could not find job for urn" + urn.getContent());
                return;
            }
            String xpath = JesUtil.extractXPath(id);
            Step jobStep = this.getStepForFragment(job,xpath);
            if (jobStep == null) {
                logger.error("Culd not find step " + xpath + " for urn " + urn.getContent());
                return;
            } 
            // ok, found the job step this results are bound for.
            // going to do two things now - record results as comment in the executionRecord.
            StepExecutionRecord er =JesUtil.getLatestOrNewRecord(jobStep);       
            MessageType resultsMessage = buildResultsMessage(results);
            er.addMessage(resultsMessage);
                        

            // and then set results parameters to values in results message - nice, although some issues for looping, etc - only last set will be recorded.
            // results shold be placed in the execution record somehow - but that's a todo for when we have loops.                        
            org.astrogrid.applications.beans.v1.cea.castor.ResultListType castorResults =  Axis2Castor.convert(results);
            ParameterValue[] resultValues =  results.getResult();
            for (int i = 0; i < resultValues.length; i++) {
                if (!resultValues[i].isIndirect()) { // only if its a direct parameter                    
                    org.astrogrid.applications.beans.v1.parameters.ParameterValue stepParameter 
                        = (org.astrogrid.applications.beans.v1.parameters.ParameterValue) jobStep.findXPathValue("tool/output/parameter[name='" + resultValues[i].getName() + "']");
                    if (stepParameter != null && ! stepParameter.getIndirect()) {// only if we found one.., and we agree its indirect.
                        logger.debug("setting value of " + stepParameter.getName() + " to " + resultValues[i].getValue());
                        stepParameter.setValue(resultValues[i].getValue());
                     }
                } 
            }
            // wonder if we want to update step status too?
             factory.updateJob(job); // save our changes.                                                                         
        } catch (JesException e) {
            // somthing badly wrong.
            logger.fatal("report results - jes exception",e);
        }
    }

    /** build an execution message from the resultslist
     * @param results
     * @return
     */
    private MessageType buildResultsMessage(ResultListType results) {
        MessageType resultsMessage = new MessageType();
        resultsMessage.setLevel(LogLevel.INFO);
        resultsMessage.setPhase(ExecutionPhase.COMPLETED);
        resultsMessage.setSource("CEA");
        resultsMessage.setTimestamp(new Date());   
        StringWriter content = new StringWriter(); 
        try {
            Axis2Castor.convert(results).marshal(content);
        } catch (CastorException e) { 
            e.printStackTrace(new PrintWriter(content));
        }

        resultsMessage.setContent(content.toString());
        return resultsMessage;
    }
    
}


/* 
$Log: AbstractJobSchedulerImpl.java,v $
Revision 1.3  2004/07/09 15:49:08  nw
fixed NPE in processing results.
should fix regression on SimpleJavaWorkflowEndToEndTest

Revision 1.2  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/