/*$Id: StepActivityNode.java,v 1.1 2004/04/21 16:38:48 nw Exp $
 * Created on 21-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.policy.activitynode;

import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.Step;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public class StepActivityNode extends AbstractActivityNode implements ActivityNode {
    /** Construct a new StepActivityNode
     * @param parent
     * @param activity
     */
    public StepActivityNode(ActivityNode parent, Step activity) {
        super(parent, activity);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNode#getNodeType()
     */
    public int getNodeType() {
        return STEP;
    }

    
    public Step getStep() {
        return (Step)getWrappedWorkflowObject();
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNode#accept(org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNodeVisitor)
     */
    public void accept(ActivityNodeVisitor visitor) {
        visitor.visit(this);
    }
}


/* 
$Log: StepActivityNode.java,v $
Revision 1.1  2004/04/21 16:38:48  nw
object model to make interrogating a workflow structure easier
 
*/