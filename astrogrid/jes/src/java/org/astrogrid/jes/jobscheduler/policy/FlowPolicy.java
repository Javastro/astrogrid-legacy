/*$Id: FlowPolicy.java,v 1.4 2004/04/21 17:09:18 nw Exp $
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
import org.astrogrid.jes.jobscheduler.policy.activitynode.FlowActivityNode;
import org.astrogrid.jes.jobscheduler.policy.activitynode.SequenceActivityNode;
import org.astrogrid.jes.jobscheduler.policy.activitynode.StepActivityNode;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.types.JoinType;

import java.util.Iterator;

/** Policy implementation that executes flows in parallel, and respects join conditions.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2004
 *
 */
public class FlowPolicy extends LinearPolicy {
    /** Construct a new FlowPolicy
     * 
     */
    public FlowPolicy() {
        super();
           logger.info("Creating Flow Policy");
           this.name =  "FlowPolicy";
           this.description = "Executes job steps in a parallel manner where possible. ignores join conditions.";       
 
    }
    /** 
    * @see org.astrogrid.jes.jobscheduler.Policy#currentJobStatus(org.astrogrid.workflow.beans.v1.Workflow)
    */
   public ExecutionPhase currentJobStatus(Workflow job) {
       // same rules as for the linear policy - if there's something that's not been run yet, run it.
       return super.currentJobStatus(job);
  }      

  public Step nextExecutableStep(Workflow job) {
         logger.debug("LinearPolicy::nextExecutableStep");
         ActivityNode node = builder.buildTree(job);
         FindNextExecutableStepVisitor visitor = new FindNextExecutableStepVisitor();
         node.accept(visitor);
         return visitor.result;

     }
    
     protected static class FindNextExecutableStepVisitor extends LinearPolicy.FindNextExecutableStepVisitor {

         public void visit(StepActivityNode node) {
             if (result != null) { // found one already. leave it.
                 return;
             }
             Step s = node.getStep();
             ExecutionPhase phase = getLatestExecutionPhase(s,ExecutionPhase.PENDING);

                if (justSeenComplete && phase.equals(ExecutionPhase.PENDING)) {
                             logger.debug("found candidate");
                             result = s;                
                }
             if (node.getParent().getNodeType() == ActivityNode.SEQUENCE) { // don't do this in a flow.
                if (phase.equals(ExecutionPhase.COMPLETED)) {
                             justSeenComplete = true;
                } else {
                             justSeenComplete = false;
                }
             }               
        }


     }
}


/* 
$Log: FlowPolicy.java,v $
Revision 1.4  2004/04/21 17:09:18  nw
provided implementation of flow policy

Revision 1.3  2004/04/21 16:39:53  nw
rewrote policy implementations to use object models

Revision 1.2  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.1  2004/03/18 10:55:55  nw
code stub - needs to be implemented
 
*/