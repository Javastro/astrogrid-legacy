/*$Id: JoinPolicy.java,v 1.4 2004/07/01 21:15:00 nw Exp $
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
import org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNode;
import org.astrogrid.jes.jobscheduler.policy.activitynode.StepActivityNode;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.types.JoinType;

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
         ActivityNode node = builder.buildTree(job);
         ActivityNodeInfo visitor = new ActivityNodeInfo();
         node.accept(visitor);
                  
         registerFunctions(job);
         // special case for empty workflow
    if (visitor.stepCount == 0) {
        logger.debug("empty workflow - so automatically completed");
        return ExecutionPhase.COMPLETED;
    }
    if (visitor.unknownFound) {
        logger.debug("returning UNKNOWN");
        return ExecutionPhase.UNKNOWN;
    }       
    if (visitor.runningFound) {
        logger.debug("returning 'RUNNING'");            
        return ExecutionPhase.RUNNING;
    }
    if (visitor.errorFound) {//and no running
        // see if there's a candidate step with the join condition to recover from this error.
        if (nextExecutableStep(job) != null) { // not sure if this is quite right, but will do for now.
            logger.debug("seen error, but possiblility of recovery, returning 'RUNNING'");
            return ExecutionPhase.RUNNING;
        } else {
            logger.debug("seen error, no possibility of recovery, returning 'ERROR'");
            return ExecutionPhase.ERROR;
        }
    }

         if ( visitor.pendingFound) { // and no running, or error...
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
        logger.debug("LinearPolicy::nextExecutableStep");
        ActivityNode node = builder.buildTree(job);
        FindNextExecutableStepVisitor visitor = new FindNextExecutableStepVisitor();
        node.accept(visitor);
        return visitor.result;

    }

    
    private static class FindNextExecutableStepVisitor extends BaseActivityNodeVisitor {

        private boolean justSeenComplete = true;
        private boolean justSeenError = false;
        Step result = null;
        
        public void visit(StepActivityNode node) {
            if (result != null) { // found one already. leave it.
                return;
            }
            Step s = node.getStep();

            ExecutionPhase status = getLatestExecutionPhase(s,ExecutionPhase.PENDING);
            if (status.equals(ExecutionPhase.PENDING)) {//its a candidate             
                if (justSeenComplete && ! s.getJoinCondition().equals(JoinType.FALSE)) { // think this is right.
                    logger.debug("found candidate after success");
                    result = s;
                } 
                if (justSeenError && ! s.getJoinCondition().equals(JoinType.TRUE)) {
                    logger.debug("found candidate after error ");
                    result = s;
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
    }     
    

}


/* 
$Log: JoinPolicy.java,v $
Revision 1.4  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.3  2004/04/21 16:39:53  nw
rewrote policy implementations to use object models

Revision 1.2  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.1  2004/03/18 10:55:38  nw
implemented sequential policy that obeys join conditions
 
*/