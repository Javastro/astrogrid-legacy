/*$Id: BaseActivityNodeVisitor.java,v 1.1 2004/04/21 16:39:53 nw Exp $
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
import org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNodeVisitor;
import org.astrogrid.jes.jobscheduler.policy.activitynode.FlowActivityNode;
import org.astrogrid.jes.jobscheduler.policy.activitynode.SequenceActivityNode;
import org.astrogrid.jes.jobscheduler.policy.activitynode.StepActivityNode;
import org.astrogrid.workflow.beans.v1.Step;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** base, null visitor.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public abstract class BaseActivityNodeVisitor implements ActivityNodeVisitor {
    protected static final Log logger = LogFactory.getLog("ActivityNodeVisitor");
    /** Construct a new BaseActivityNodeVisitor
     * 
     */
    public BaseActivityNodeVisitor() {
        super();
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNodeVisitor#visit(org.astrogrid.jes.jobscheduler.policy.activitynode.FlowActivityNode)
     */
    public void visit(FlowActivityNode node) {
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNodeVisitor#visit(org.astrogrid.jes.jobscheduler.policy.activitynode.SequenceActivityNode)
     */
    public void visit(SequenceActivityNode node) {
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNodeVisitor#visit(org.astrogrid.jes.jobscheduler.policy.activitynode.StepActivityNode)
     */
    public void visit(StepActivityNode node) {
    }
    
    // helper methods
    /** access the phase value for the latest execution record in the step
     * <p>
     * if no execution record present, return defaultValue
     * @param s step to examine
     * @param defaultValue status to return when no execution records found
     * @return execution phase.
     */
    protected ExecutionPhase getLatestExecutionPhase(Step s, ExecutionPhase defaultValue) {
        int count = s.getStepExecutionRecordCount();
        if (count == 0) {
            return defaultValue;
        } else {
            return s.getStepExecutionRecord(count-1).getStatus();
        }
    }    
}


/* 
$Log: BaseActivityNodeVisitor.java,v $
Revision 1.1  2004/04/21 16:39:53  nw
rewrote policy implementations to use object models
 
*/