/*$Id: RoughPolicy.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 18-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.policy;

import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobscheduler.*;
import org.astrogrid.jes.types.v1.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** Scheduling policy based on code cut from JobMonitor and JobScheduler.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2004
 *
 */
public class RoughPolicy implements Policy {
    /** Construct a new RoughPolicy
     * 
     */
    public RoughPolicy() {
        super();
    }
    
    /** 
         * 
         *  */
        public Status calculateJobStatus( Job job ) {
            // JBL Note: Does a JobStep in error mean a Job is in error?
            // i.e. Does a JobStep in error mean a Job has stopped (finished)?
            boolean   isJobFinished = true ;
            boolean  bStepStatus = true ; // what does this signify?
            Map guardSteps = new HashMap() ;
        
              for(Iterator i = job.getJobSteps();i.hasNext();  ) {
                
                    JobStep jobStep = (JobStep)i.next() ;
                    guardSteps.put( new Integer(jobStep.getSequenceNumber()), jobStep ) ;
                    
                    // If anything is running, then job is obviously not finished...
                    if( jobStep.getStatus().equals( Status.RUNNING ) ) { 
                        isJobFinished = false ;                
                        break ;              
                    }
                    else if( jobStep.getStatus().equals( Status.COMPLETED ) ) {
                        continue ;
                    }
                    else if( jobStep.getStatus().equals( Status.ERROR ) ) {
                        bStepStatus = false ;
                        continue ;
                    }                 
                    // If step status is initialized, the guardstep must be checked
                    // for its status against the join condition for this step...
                    else if( jobStep.getStatus().equals( Status.INITIALIZED ) ) {
                    
                        String joinCondition = jobStep.getJoinCondition() ;
                        JobStep guardStep = (JobStep)guardSteps.get( new Integer( jobStep.getSequenceNumber() - 1 ) ) ;
                    
                        // If there is no guard step, assume this step should execute...
                        if( guardStep == null )  {
                            isJobFinished = false ;
                            break ;   //JL questionable
                        }
                        
                        // Eliminate those that should execute in any case... 
                        if( joinCondition.equals( JobStep.JOINCONDITION_ANY ) ) {
                            isJobFinished = false ;
                            break ;   
                        }
                       
                        // Eliminate those that should execute provided the previous
                        // guardstep completed successfully... 
                        if( guardStep.getStatus().equals( Status.COMPLETED ) 
                            &&
                            joinCondition.equals( JobStep.JOINCONDITION_TRUE )
                          ) {
                            isJobFinished = false ;
                            break ;   
                        }
                    
                        // Eliminate those that should execute only when the previous
                        // guardstep completed with an error...
                        if( guardStep.getStatus().equals( Status.ERROR ) 
                            &&
                            joinCondition.equals( JobStep.JOINCONDITION_FALSE )
                          ) {
                            isJobFinished = false ;
                            break ;   
                        }
                    
                    } // end if
                
                    // If we get here, this step is no longer a candidate
                    // for execution...loop to examine the next step...
                
                } // end loop
            
            
            if( isJobFinished && bStepStatus ){
                return Status.COMPLETED ;
            }
            else if( isJobFinished && ! bStepStatus  ){
                return Status.ERROR;
            }
            else {
                return Status.RUNNING ;
            }
                            
            
        } // end of interrogateAndSetJobFinished()
        
// from job scheduler
    
    /** 
     * @return an iterator of job steps
     */
   public Iterator calculateDispatchableCandidates( Job job ) {
        

       String
          joinCondition = null ;
       Map  guardSteps = new HashMap() ;
       List candidates = new ArrayList() ;

          for(Iterator iterator = job.getJobSteps(); iterator.hasNext(); ) {
                
             JobStep jobStep = (JobStep)iterator.next() ;
             guardSteps.put( new Integer(jobStep.getSequenceNumber()), jobStep ) ;
                  
             // If step status is initialized, the guardstep must be checked
             // for its status against the join condition for this step...
             if( jobStep.getStatus().equals( Status.INITIALIZED ) ) {
                    
                joinCondition = jobStep.getJoinCondition() ;
                JobStep guardStep = (JobStep)guardSteps.get( new Integer( jobStep.getSequenceNumber() - 1 ) ) ;
                    
                // If there is no guard step (the first step in a job?), 
                // assume this step should execute...
                if( guardStep == null ) {
                    this.maintainCandidateList( candidates, jobStep ) ; 
                    continue ;   
                }  
                      
                // If a guardstep has finished (either OK or in error)
                // and the join condition is "any", the step should execute... 
                if( 
                    ( guardStep.getStatus().equals( Status.COMPLETED )
                      ||
                      guardStep.getStatus().equals( Status.ERROR ) )
                    && 
                    joinCondition.equals( JobStep.JOINCONDITION_ANY ) 
                  ) {
                         
                    this.maintainCandidateList( candidates, jobStep ) ;
                    continue ;   
                }
                       
                // Those that should execute provided the previous
                // guardstep completed successfully are candidates... 
                if( guardStep.getStatus().equals( Status.COMPLETED ) 
                    &&
                    joinCondition.equals( JobStep.JOINCONDITION_TRUE )
                ) {
                    this.maintainCandidateList( candidates, jobStep ) ;
                    continue ;   
                }
                    
                // Those that should execute only when the previous
                // guardstep completed with an error are candidates...
                if( guardStep.getStatus().equals(Status.ERROR ) 
                    &&
                    joinCondition.equals( JobStep.JOINCONDITION_FALSE )
                ) {
                    this.maintainCandidateList( candidates, jobStep ) ;
                    continue ;   
                }
                    
             } // end if
                
                   // If we get here, this step is no longer a candidate
                   // for execution...loop to examine the next step...
                
          } // end while 
           
        
       return candidates.iterator() ;
        
   } // end of identifyDispatchableCandidates()
    
    
   protected void maintainCandidateList( List list, JobStep candidate ) {
           // If the list is empty, no problems in adding this candidate...
           if( list.isEmpty() ) {
               list.add( candidate ) ;
               return ;
           }
            
           // Get the sequence number of any member of the candidate collection...
           int sequenceNumber = ((JobStep)list.get(0)).getSequenceNumber() ;
               
           // If the sequence numbers match, add the candidate...               
           if( sequenceNumber == candidate.getSequenceNumber()) {
               list.add( candidate ) ;  
           } 
           // If the sequence number is less than the collection, we need to clear
           // the collection and start again. We should only schedule jobsteps that
           // are of the same sequence number (i.e. execute those concurrently)
           else if( sequenceNumber > candidate.getSequenceNumber() ) {
               list.clear() ;
               list.add( candidate ) ;
           }
            
           // Ignore any candidates with a higher sequence number than the collection
                
 
   } // end of maintainCandidateList()
    
            
    

}


/* 
$Log: RoughPolicy.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:26:52  nw
refactored - moved below scheduler package

Revision 1.1.2.1  2004/02/19 13:37:10  nw
abstracted the scheduling rules part of the job scheduler
this component can be swapped with others.
determines which steps get scheduled next.
 
*/