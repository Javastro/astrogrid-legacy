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

import org.astrogrid.community.common.util.CommunityMessage;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.types.v1.JobInfo;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	        Job job = factory.findJob( jobURN) ; 
            
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
     * @modified NWW I've changed type of this - does not throw exceptions now. instead will record any problems as comments in the job step */
    protected void scheduleSteps( Job job ) {

          String communitySnippet = CommunityMessage.getMessage( job.getToken()
                                                          , job.getUserId() + "@" + job.getCommunity()
                                                          , job.getGroup() ) ;
            
            Iterator i  = policy.calculateDispatchableCandidates(job);
            JobStep step = null;
            try {
                while( i.hasNext() ) {
                        step = (JobStep)i.next() ;
                        dispatcher.dispatchStep( communitySnippet, step ) ;
                        step.setStatus(Status.RUNNING);
                }            
                job.setStatus( Status.RUNNING ) ;   
            } catch (Throwable t) { //catch everything
                logger.info("Step execution failed:",t);
                if (step != null) {
                    String message = "Failed: " + t.getClass().getName()
                        + "\n " + t.getMessage();
                    step.setComment(message);
                    step.setStatus(Status.ERROR);
                    job.setStatus(Status.ERROR);
                    notifyJobFinished(step.getParent());
                }
            }             
        
    } // end of scheduleSteps()

//  stuff copied from job monitor
	public void resumeJob(JobInfo info) {
        Job job = null;      
        try { 
             // Create the necessary Job structures.
             // This involves persistence, so we bracket the transaction 
             // before finding and updating the JobStep status and comment...
    
             JobFactory factory = facade.getJobFactory() ;
             factory.begin() ;
             job = factory.findJob(info.getJobURN() ) ; // todo - match types.
             JobStep jobStep = findJobStep( job,info ) ;
                
             jobStep.setStatus( info.getStatus() ) ;
             jobStep.setComment(info.getComment());
                
             Status status = policy.calculateJobStatus(job);
             job.setStatus(status);
                
             factory.updateJob( job ) ;             // Update any changed details to the database
             if( status.equals(Status.RUNNING) ) {
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
    public void notifyJobFinished(Job job) {
    }


    /** @todo refactor away into object model 
     * @todo make more efficient*/
     protected JobStep findJobStep( Job job, JobInfo info )  throws NotFoundException{
             for(Iterator iterator = job.getJobSteps(); iterator.hasNext(); ) {
                  JobStep jobStep = (JobStep)iterator.next() ;
                 if( jobStep.getStepNumber() == info.getStepNumber() ) {
                     return jobStep;
                 }
             }
             throw new NotFoundException("Could not find job step " + info.getStepNumber() + " in job " + job.getId());   
     } // end of updateJobStepStatus()


} // end of class JobScheduler