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
package org.astrogrid.jes.jobscheduler;

import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.common.util.CommunityMessage;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.Iterator;

/**Scheduling framework. Composes together a set of different objects - this means that different behaviours can be gained by 
 * composing a scheduler using different objects.
 */
public class JobScheduler implements org.astrogrid.jes.delegate.v1.jobscheduler.JobScheduler {
	
    /** Construct a scheduler
     *  Construct a new JobScheduler
     * @param facade interface to persistent store, and object model
     * @param dispatcher interface to something that dispatches individual job steps
     * @param policy interface to something that determines which job steps to execute next
     */
    public JobScheduler(BeanFacade facade,Dispatcher dispatcher,Policy policy) {
        this.policy = policy;
        this.facade = facade;
        this.dispatcher = dispatcher;
    }
    protected final Policy policy;
    protected final BeanFacade facade;
    protected final Dispatcher dispatcher;
    

	private static Log
		logger = LogFactory.getLog( JobScheduler.class ) ;
		

    public void scheduleNewJob( JobURN jobURN ) {
        try {
	        JobFactory factory = facade.getJobFactory() ;
	        factory.begin() ;
	        Workflow job = factory.findJob( JesUtil.axis2castor(jobURN)) ; 
            
            // Schedule one or more job steps....
            scheduleSteps( job ) ;

			factory.updateJob( job ) ;              // Update any changed details to the database                           		
			factory.end ( true ) ;   // Commit and cleanup
        } catch (JesException e) {
            logger.error(e);
        }

         	 
    } // end of scheduleJob()
    
    /** 
     * Dispatch a series of steps. 
     * @modified NWW I've changed type of this - does not throw exceptions now. instead will record any problems as comments in the job step 
     * @todo add creation of account object.*/
    protected void scheduleSteps( Workflow job ) {

            Iterator i  = policy.calculateDispatchableCandidates(job);
            Step step = null;
            StepExecutionRecord er = new StepExecutionRecord();
            try {
                while( i.hasNext() ) {
                        step = (Step)i.next() ;
                        step.addStepExecutionRecord(er);
                        er.setStartTime(new Date());
                        dispatcher.dispatchStep( job, step ) ;
                        er.setStatus(ExecutionPhase.RUNNING);
                }            
                job.getJobExecutionRecord().setStatus( ExecutionPhase.RUNNING ) ;   
            } catch (Throwable t) { //catch everything
                logger.info("Step execution failed:",t);
                if (step != null) {                    
                    MessageType message = new MessageType();
                    message.setContent( "Failed: " + t.getClass().getName() + "\n " + t.getMessage());
                    message.setLevel(LogLevel.ERROR);
                    message.setSource("Job Controller");
                    message.setPhase(ExecutionPhase.ERROR);
                    message.setTimestamp(new Date());

                    job.getJobExecutionRecord().setStatus(ExecutionPhase.ERROR);
                    notifyJobFinished(job);
                }
            }             
        
    } // end of scheduleSteps()



//  stuff copied from job monitor
    /** @todo implement xpath location part */
	public void resumeJob(JobIdentifierType id,org.astrogrid.jes.types.v1.cea.axis.MessageType info) {
        Workflow job = null;      
        try { 

             JobFactory factory = facade.getJobFactory() ;
             factory.begin() ;             
             job = factory.findJob(JesUtil.extractURN(id) ) ; // todo - match types.
             // compute by applying xpath to workflow. has to wait until we remove job.
             String xpath = JesUtil.extractXPath(id);
             Step jobStep = (Step)job.findXPathValue(xpath);
             // need to handle case of not finding job step here...
                
              // should have an execution record already..
             StepExecutionRecord er =JesUtil.getLatestRecord(jobStep);
             er.addMessage(JesUtil.axis2castor(info));

               
             ExecutionPhase status = policy.calculateJobStatus(job);
             job.getJobExecutionRecord().setStatus(status);
                
             factory.updateJob( job ) ;             // Update any changed details to the database
             if( status.equals(ExecutionPhase.RUNNING) ) {
                scheduleSteps(job);
            } else {
                notifyJobFinished(job);
             }                  
            factory.updateJob(job); // save any further changes.
             factory.end( true ) ;   // Commit and cleanup
    
         } catch( Exception jex ) {
             logger.error(jex);
             logger.info("Encountered error - notifying job has finished");
             this.notifyJobFinished(job);
         }
    }     
          
    /**
     * Called when a job is completed, to notify some external observer
     * <p>
     * empty for now - extend as needed.
     */
    public void notifyJobFinished(Workflow job) {
    }


    /** @todo refactor away into object model 
     * @todo make more efficient*/
    /*
     protected JobStep findJobStep( Job job,  info )  throws NotFoundException{
             for(Iterator iterator = job.getJobSteps(); iterator.hasNext(); ) {
                  JobStep jobStep = (JobStep)iterator.next() ;
                 if( jobStep.getStepNumber() == info.getStepNumber() ) {
                     return jobStep;
                 }
             }
             throw new NotFoundException("Could not find job step " + info.getStepNumber() + " in job " + job.getId());   
     } // end of updateJobStepStatus()
*/

} // end of class JobScheduler