/*$Id: FlowActivityNode.java,v 1.1 2004/04/21 16:38:48 nw Exp $
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
import org.astrogrid.workflow.beans.v1.Flow;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public class FlowActivityNode extends AbstractActivityNode implements ActivityNode {
    /** Construct a new FlowActivityNode
     * @param parent
     * @param activity
     */
    public FlowActivityNode(ActivityNode parent, Flow activity) {
        super(parent, activity);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNode#getNodeType()
     */
    public int getNodeType() {
        return FLOW;
    }

    public Flow getFlow() {
        return (Flow)getWrappedWorkflowObject();
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNode#accept(org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNodeVisitor)
     */
    public void accept(ActivityNodeVisitor visitor) {
        visitor.visit(this);
        ActivityNode[] children = getChildren();
        for (int i = 0; i < children.length; i++) {
            children[i].accept(visitor);
        }
    }
}


/* 
$Log: FlowActivityNode.java,v $
Revision 1.1  2004/04/21 16:38:48  nw
object model to make interrogating a workflow structure easier
 
*/