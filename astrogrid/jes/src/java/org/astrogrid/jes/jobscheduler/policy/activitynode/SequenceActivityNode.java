/*$Id: SequenceActivityNode.java,v 1.2 2004/07/01 21:15:00 nw Exp $
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

import org.astrogrid.workflow.beans.v1.Sequence;

/** Implementation of an activity node for a sequence.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public class SequenceActivityNode extends AbstractActivityNode implements ActivityNode {
    /** Construct a new SequenceActivityNode
     * 
     */
    public SequenceActivityNode(ActivityNode parent,Sequence seq) {
        super(parent,seq);
        
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.policy.activitynode.ActivityNode#getNodeType()
     */
    public int getNodeType() {
        return SEQUENCE;
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
    
    public Sequence getSequence() {
        return (Sequence)getWrappedWorkflowObject();
    }
}


/* 
$Log: SequenceActivityNode.java,v $
Revision 1.2  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.1  2004/04/21 16:38:48  nw
object model to make interrogating a workflow structure easier
 
*/