/*$Id: JoinPolicy.java,v 1.1 2004/03/18 10:55:38 nw Exp $
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
         registerFunctions(job);
         // special case for empty workflow
         if (job.findXPathValue("//*[jes:isStep()]") == null) {
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
                 errorFound = true;
             }
             if (s.equals(ExecutionPhase.UNKNOWN)) { // bomb out right now.
                 return s;
             }
             if (s.equals(ExecutionPhase.RUNNING)) {
                 runningFound = true;
             }
             if (s.equals(ExecutionPhase.PENDING)) {
                 pendingFound = true;
             }
         }

         if (runningFound) {
             return ExecutionPhase.RUNNING;
         }
         if (errorFound) {//and no running
             // see if there's a candidate step with the join condition to recover from this error.
             if (nextExecutableStep(job) != null) { // not sure if this is quite right, but will do for now.
                 return ExecutionPhase.RUNNING;
             } else {
                 return ExecutionPhase.ERROR;
             }
         }
         if ( pendingFound) { // and no running, or error...
             return ExecutionPhase.PENDING;
         } 
         // everything must be completed then..
         return ExecutionPhase.COMPLETED;
    }      

     /** Returns a step that either has no execution record, or an execution record that has pending status.
      * @see org.astrogrid.jes.jobscheduler.Policy#nextExecutableStep(org.astrogrid.workflow.beans.v1.Workflow)
      */
     public Step nextExecutableStep(Workflow job) {
         registerFunctions(job);

         Iterator i = job.findXPathIterator("//*[jes:isStep()]"); // returns flattened list of all steps - we are relying on the order these are returned in really.
         boolean justSeenComplete = true; // necessary, special case for first step.
         boolean justSeenError = false;
         while (i.hasNext()) {
             Step s = (Step)i.next();
             ExecutionPhase status = getLatestExecutionPhase(s,ExecutionPhase.PENDING);
             if (status.equals(ExecutionPhase.PENDING)) {//its a candidate             
                 if (justSeenComplete && ! s.getJoinCondition().equals(JoinType.FALSE)) { // think this is right.
                     return s;
                 } 
                 if (justSeenError && ! s.getJoinCondition().equals(JoinType.TRUE)) {
                     return s;
                 }
             } else if (status.equals(ExecutionPhase.COMPLETED)) {
                 justSeenComplete = true;
                 justSeenError = false;
             } else if (status.equals(ExecutionPhase.ERROR)) {
                 justSeenComplete = false;
                 justSeenError = true;
             } else {
                 justSeenComplete = false;
                 justSeenError = true;
             }                 
         }
         return null;
     }
    

}


/* 
$Log: JoinPolicy.java,v $
Revision 1.1  2004/03/18 10:55:38  nw
implemented sequential policy that obeys join conditions
 
*/