/*$Id: ActivityNodeInfo.java,v 1.1 2004/04/21 16:39:53 nw Exp $
 * Created on 21-Apr-2004
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
import org.astrogrid.jes.jobscheduler.policy.activitynode.StepActivityNode;
import org.astrogrid.workflow.beans.v1.Step;

/** Visitor that analyses statuses seen.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
class ActivityNodeInfo extends BaseActivityNodeVisitor {
    /** Construct a new ActivityNodeInfo
     * 
     */
    public ActivityNodeInfo() {
        super();
    }
    /** true if a running step encountered */
    boolean runningFound = false;       
    /** true if a pending step encountered */
    boolean pendingFound = false;
    /** true if an error step found */
    boolean errorFound = false;
    /** true if an unknown step found */
    boolean unknownFound = false;
    /** true if no execution statuses found */
    boolean noStatusFound = true;
    /** number of steps seen */
    int stepCount = 0;    
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNodeVisitor#visit(org.astrogrid.jes.jobscheduler.policy.activitynode.StepActivityNode)
     */
    public void visit(StepActivityNode node) {
        stepCount++;
            noStatusFound = false;
            Step step = node.getStep();
            ExecutionPhase s = getLatestExecutionPhase(step,ExecutionPhase.PENDING);
  
            if (s.equals(ExecutionPhase.ERROR)) {
                logger.debug("saw error");
                errorFound = true;
            } else  if (s.equals(ExecutionPhase.UNKNOWN)) {
                logger.warn("saw unknown");
                unknownFound = true;
                
            } else if (s.equals(ExecutionPhase.RUNNING)) {
                logger.debug("saw running");
                runningFound = true;
            } else if (s.equals(ExecutionPhase.PENDING)) {
                logger.debug("saw pending");
                pendingFound = true;
            }        

    }

}


/* 
$Log: ActivityNodeInfo.java,v $
Revision 1.1  2004/04/21 16:39:53  nw
rewrote policy implementations to use object models
 
*/