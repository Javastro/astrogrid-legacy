/*$Id: ActivityNodeBuilder.java,v 1.1 2004/04/21 16:38:48 nw Exp $
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

import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Workflow;

/** Builds an activity node tree from a workflow document
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public class ActivityNodeBuilder {
    /** Construct a new ActivityNodeBuilder
     * 
     */
    public ActivityNodeBuilder() {
        super();
    }
    /** build an activity node tree from a workflow */
    public ActivityNode buildTree(Workflow wf) {
        Sequence base = wf.getSequence();
        
        return buildFromSequence(null,base);
    }

    private void processChildren(AbstractActivityNode parentNode, AbstractActivity[] children) {
        for (int i = 0; i < children.length; i++) {
            AbstractActivity child = children[i];
            if (child instanceof Step) {
                buildFromStep(parentNode,(Step)child);
            } else if (child instanceof Flow) {
                buildFromFlow(parentNode,(Flow)child);
            } else if (child instanceof Sequence) {
                buildFromSequence(parentNode,(Sequence)child);
            } else {
                throw new IllegalArgumentException("Encountered activity that was neither step, sequence or flow!: " + child.getClass().getName());
            }                 
        }
    }
    
    private ActivityNode buildFromSequence(AbstractActivityNode parent,Sequence seq) {
        AbstractActivityNode node = new SequenceActivityNode(parent,seq);
        if (parent != null) {
            // parent may be null, as sequence is the root.
            parent.addChild(node);
        }
        AbstractActivity[] children = seq.getActivity();
        processChildren(node,children);
        return node;
    }
    

    
    private void buildFromFlow(AbstractActivityNode parent, Flow flow) {
        AbstractActivityNode node = new FlowActivityNode(parent,flow);      
        parent.addChild(node);  
        AbstractActivity[] children = flow.getActivity();
        processChildren(node,children);
    }
    
    private void buildFromStep(AbstractActivityNode parent, Step step) {
        AbstractActivityNode node = new StepActivityNode(parent,step);
        parent.addChild(node);
    }
    
    
}


/* 
$Log: ActivityNodeBuilder.java,v $
Revision 1.1  2004/04/21 16:38:48  nw
object model to make interrogating a workflow structure easier
 
*/