/*$Id: JoinPolicy.java,v 1.2 2004/04/08 14:43:26 nw Exp $
 * Created on 18-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.policy;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.jes.jobscheduler.Policy;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;
import org.astrogrid.workflow.beans.v1.types.JoinType;

import java.util.Iterator;

/** Sequential Policy that takes join conditions into account
 * @see LinearPolicy - based on this.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2004
 *@todo done 'next element' method. now need to do 'current status'
 */
public class JoinPolicy extends AbstractPolicy {
    /** Construct a new JoinPolicy
     * 
     */
    public JoinPolicy() {
        super();
           logger.info("Creating Join Policy");
           this.name =  "JoinPolicy";
           this.description = "Executes job steps in a top-to-bottom, purely sequential manner. treats flows as sequences. respects join conditions.";       
         
    }
    /** 
      * @see org.astrogrid.jes.jobscheduler.Policy#currentJobStatus(org.astrogrid.workflow.beans.v1.Workflow)
      */
     public ExecutionPhase currentJobStatus(Workflow job) {
         logger.debug("JoinPolicy::currentJobStatus()");
         registerFunctions(job);
         // special case for empty workflow
         if (job.findXPathValue("//*[jes:isStep()]") == null) {
             logger.debug("Found no steps - so is already completed");
             return ExecutionPhase.COMPLETED;
         }  
         Iterator i = job.findXPathIterator("//*[jes:isStep()]");
        
         boolean runningFound = false;       
         boolean pendingFound = false;
         boolean errorFound = false;
         while (i.hasNext()) {
             Step step = (Step)i.next();
             ExecutionPhase s = getLatestExecutionPhase(step,ExecutionPhase.PENDING);
             if (s.equals(ExecutionPhase.ERROR)) {
                 logger.debug("found error");
                 errorFound = true;
             }
             if (s.equals(ExecutionPhase.UNKNOWN)) { // bomb out right now.
                 logger.debug("found unknown - returning immeadiately");
                 return s;
             }
             if (s.equals(ExecutionPhase.RUNNING)) {
                 logger.debug("found running");
                 runningFound = true;
             }
             if (s.equals(ExecutionPhase.PENDING)) {
                 logger.debug("found pending");
                 pendingFound = true;
             }
         }

         if (runningFound) {
             logger.debug("returning 'RUNNING'");
             return ExecutionPhase.RUNNING;
         }
         if (errorFound) {//and no running
             // see if there's a candidate step with the join condition to recover from this error.
             if (nextExecutableStep(job) != null) { // not sure if this is quite right, but will do for now.
                 logger.debug("seen error, but possiblility of recovery, returning 'RUNNING'");
                 return ExecutionPhase.RUNNING;
             } else {
                 logger.debug("seen error, no possibility of recovery, returning 'ERROR'");
                 return ExecutionPhase.ERROR;
             }
         }
         if ( pendingFound) { // and no running, or error...
             logger.debug("returning 'PENDING'");
             return ExecutionPhase.PENDING;
         } 
         // everything must be completed then..
         logger.debug("returning 'COMPLETED'");
         return ExecutionPhase.COMPLETED;
    }      

     /** Returns a step that either has no execution record, or an execution record that has pending status.
      * @see org.astrogrid.jes.jobscheduler.Policy#nextExecutableStep(org.astrogrid.workflow.beans.v1.Workflow)
      */
     public Step nextExecutableStep(Workflow job) {
         logger.debug("JoinPolicy::nextExecutableStep()");
         registerFunctions(job);

         Iterator i = job.findXPathIterator("//*[jes:isStep()]"); // returns flattened list of all steps - we are relying on the order these are returned in really.
         boolean justSeenComplete = true; // necessary, special case for first step.
         boolean justSeenError = false;
         while (i.hasNext()) {
             Step s = (Step)i.next();
             ExecutionPhase status = getLatestExecutionPhase(s,ExecutionPhase.PENDING);
             if (status.equals(ExecutionPhase.PENDING)) {//its a candidate             
                 if (justSeenComplete && ! s.getJoinCondition().equals(JoinType.FALSE)) { // think this is right.
                     logger.debug("found candidate after success");
                     return s;
                 } 
                 if (justSeenError && ! s.getJoinCondition().equals(JoinType.TRUE)) {
                     logger.debug("found candidate after error ");
                     return s;
                 }
             } else if (status.equals(ExecutionPhase.COMPLETED)) {
                 logger.debug("found completed");
                 justSeenComplete = true;
                 justSeenError = false;
             } else if (status.equals(ExecutionPhase.ERROR)) {
                 logger.debug("found error");
                 justSeenComplete = false;
                 justSeenError = true;
             } else {
                 justSeenComplete = false;
                 justSeenError = true;
             }                 
         }
         logger.debug("no candidate steps found");
         return null;
     }
    

}


/* 
$Log: JoinPolicy.java,v $
Revision 1.2  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.1  2004/03/18 10:55:38  nw
implemented sequential policy that obeys join conditions
 
*/