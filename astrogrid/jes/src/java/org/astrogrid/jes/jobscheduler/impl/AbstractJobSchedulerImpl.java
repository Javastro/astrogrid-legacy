/*$Id: AbstractJobSchedulerImpl.java,v 1.8 2004/12/03 14:47:41 jdt Exp $
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

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.jobscheduler.JobScheduler;
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

/**Abstract implementation of a job scheduler. used as as a base class for the groovy job scheduler, and maybe future others.
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

    /** initialize the job / workflow document, ready for execution 
     * @see #scheduleNewJob(JobURN)*/
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
             // hook into updating status of job.
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
     * @todo want to able to send a debuf-level message here too
     */
    protected void recordFatalError(Workflow job, JesException e) {
        MessageType errorMessage = new MessageType();
        errorMessage.setPhase(ExecutionPhase.ERROR);
        errorMessage.setLevel(LogLevel.ERROR);
        errorMessage.setSource("Jes System");
        errorMessage.setTimestamp(new Date());
        errorMessage.setContent(JesUtil.getMessageChain(e));
        
        MessageType debugMessage = new MessageType();
        debugMessage.setPhase(errorMessage.getPhase());
        debugMessage.setLevel(LogLevel.INFO);
        debugMessage.setSource(errorMessage.getSource());
        debugMessage.setTimestamp(errorMessage.getTimestamp());
        StringWriter content = new StringWriter();
        PrintWriter pw = new PrintWriter(content);
        pw.println("System Error " + e.getClass().getName());
        e.printStackTrace(pw);
        debugMessage.setContent(content.toString());
        
        job.getJobExecutionRecord().addMessage(errorMessage);
        job.getJobExecutionRecord().addMessage(debugMessage);        
        job.getJobExecutionRecord().setStatus(ExecutionPhase.ERROR);
    }
    
    /** @see #resumeJob(JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType) */
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
     * update the status of the entire job - maybe by rescanning tree in someway.
     * @see #resumeJob(JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType)
     */
    protected abstract void updateJobStatus(Workflow job);
    

    /** project the step correspondinig to this fragment from the workflow 
     * @return the step, or null if not found.
     * @see #resumeJob(JobIdentifierType, org.astrogrid.jes.types.v1.cea.axis.MessageType) where this method is used.
     * @see #reportResults(JobIdentifierType, ResultListType) where this method is used.*/
    protected abstract Step getStepForFragment(Workflow job, String fragment); 
    
    /**
     * select steps that can be executed, and fire them off.
     * used within {@link #resumeJob}, {@link #reportResults}, {@link scheduleNewJob} 
     */
    protected abstract void scheduleSteps(Workflow job) ;
    


    /** helper method to create a new execution record, pre-populated */
    public static StepExecutionRecord newStepExecutionRecord() {
        StepExecutionRecord result = new StepExecutionRecord();
        result.setStartTime(new Date());
        result.setStatus(ExecutionPhase.INITIALIZING);
        return result;
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
                        
            // and then call the hook for extension classes to do something with the results too.            
            org.astrogrid.applications.beans.v1.cea.castor.ResultListType castorResults =  Axis2Castor.convert(results);            
            storeResults(job,jobStep,castorResults);          
             factory.updateJob(job); // save our changes.
             
             // see if anything else can be run now.
             // now go try run some more steps.
             scheduleSteps(job);  
             updateJobStatus(job);
             factory.updateJob( job ) ;             // Update any changed details to the database
        } catch (JesException e) {
            // somthing badly wrong.
            logger.fatal("report results - jes exception",e);
        }
    }
    
    /** store / do something with the results of a step
     *  
     * @param wf
     * @param step
     * @param results
     * @see #reportResults(JobIdentifierType, ResultListType)
     */
    protected abstract void storeResults(Workflow wf,Step step,org.astrogrid.applications.beans.v1.cea.castor.ResultListType results);

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
    
    /**called when a workfllow completes.
     *  do nothing-implementaiton - may be overridden.
     * */
    public void notifyJobFinished(Workflow job) {   
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
    

    
}


/* 
$Log: AbstractJobSchedulerImpl.java,v $
Revision 1.8  2004/12/03 14:47:41  jdt
Merges from workflow-nww-776

Revision 1.7.2.1  2004/12/01 21:48:20  nw
adjusted to work with new summary object,
and changed package of JobURN

Revision 1.7  2004/11/29 20:00:24  clq2
jes-nww-714

Revision 1.6.68.2  2004/11/25 23:34:34  nw
improved error messages reported from jes

Revision 1.6.68.1  2004/11/24 18:48:29  nw
attempt to get nicer error messages

Revision 1.6  2004/08/13 09:07:58  nw
tidied imports

Revision 1.5  2004/08/04 16:51:46  nw
added parameter propagation out of cea step call.

Revision 1.4  2004/07/30 15:42:34  nw
merged in branch nww-itn06-bz#441 (groovy scripting)

Revision 1.3.20.1  2004/07/30 15:10:04  nw
removed policy-based implementation,
adjusted tests, etc to use groovy implementation

Revision 1.3  2004/07/09 15:49:08  nw
fixed NPE in processing results.
should fix regression on SimpleJavaWorkflowEndToEndTest

Revision 1.2  2004/07/09 09:30:28  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions

Revision 1.1.2.1  2004/05/21 11:25:19  nw
first checkin of prototype scrpting workflow interpreter
 
*/