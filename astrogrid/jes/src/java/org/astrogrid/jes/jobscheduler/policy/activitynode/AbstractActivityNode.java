/*$Id: AbstractActivityNode.java,v 1.1 2004/04/21 16:38:48 nw Exp $
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
abstract class AbstractActivityNode implements ActivityNode {
    private ActivityNode parent;
    private AbstractActivity activity;
    private List childList = new ArrayList();
    /** Construct a new AbstractActivityNode
     * 
     */
    public AbstractActivityNode(ActivityNode parent,AbstractActivity activity) {
        this.parent = parent;
        this.activity = activity;
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.policy.ActivityNode#getParent()
     */
    public final ActivityNode getParent() {
        return parent;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.ActivityNode#getChildren()
     */
    public final ActivityNode[] getChildren() {
        return (ActivityNode[])childList.toArray(new ActivityNode[]{}); 
    }

    /**
     * @see org.astrogrid.jes.jobscheduler.policy.ActivityNode#getWrappedWorkflowObject()
     */
    public final AbstractActivity getWrappedWorkflowObject() {
        return activity;
    }
    
    public void addChild(ActivityNode child) {
        childList.add(child);
    }
}


/* 
$Log: AbstractActivityNode.java,v $
Revision 1.1  2004/04/21 16:38:48  nw
object model to make interrogating a workflow structure easier
 
*/