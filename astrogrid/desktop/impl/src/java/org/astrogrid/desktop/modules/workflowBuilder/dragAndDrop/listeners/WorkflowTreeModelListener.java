/* WorkflowTreeListener.java
 * Created on 22-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.listeners;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.ui.WorkflowBuilderImpl;
import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Then;

/**
 * @author pjn3
 *
 * Detetects when data displayed by workflow tree changes
 */
public class WorkflowTreeModelListener implements TreeModelListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(WorkflowTreeModelListener.class);
    private WorkflowBuilderImpl workflowBuilder;

	/**
	 * 
	 */
	public WorkflowTreeModelListener(WorkflowBuilderImpl w) {
		super();
		workflowBuilder = w;		
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesChanged(TreeModelEvent e) {
		workflowBuilder.validateWorkflow();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesInserted(TreeModelEvent e) {		
				
		 DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
		 int index = e.getChildIndices()[0];
		 DefaultMutableTreeNode childNode = (DefaultMutableTreeNode)(parentNode.getChildAt(index));

		 if (parentNode.getUserObject() instanceof Sequence) {
		 	Sequence s = (Sequence)parentNode.getUserObject();		 	
		 	s.addActivity(index,(AbstractActivity)childNode.getUserObject());
		 }
		 else if (parentNode.getUserObject() instanceof Flow) {
		 	Flow f = (Flow)parentNode.getUserObject();
		 	f.addActivity((AbstractActivity)childNode.getUserObject());
		 }
		 else if (parentNode.getUserObject() instanceof If) {
		 	 If i = (If)parentNode.getUserObject();
		 	 i.setElse((Else)childNode.getUserObject());
		 }
		 else if (parentNode.getUserObject() instanceof Then) {
		 	DefaultMutableTreeNode ifNode = (DefaultMutableTreeNode)parentNode.getParent();
		 	 If i = (If)ifNode.getUserObject();
		 	 i.setElse((Else)childNode.getUserObject());
		 }
		
		 workflowBuilder.validateWorkflow();		 
	}


	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
	 */
	public void treeNodesRemoved(TreeModelEvent e) {

		 DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());
		 int index = e.getChildIndices()[0];
		 if (parentNode.getUserObject() instanceof Sequence) {
		 	Sequence s = (Sequence)parentNode.getUserObject();
		 	int activityCount = s.getActivityCount();
		 	if (index <= activityCount) {
		 		s.removeActivity(s.getActivity(index));
		 	} else {
		 		logger.error("trying to remove activity which does not exist");
		 	}
		 } else if (parentNode.getUserObject() instanceof Flow) {
			 Flow f = (Flow)parentNode.getUserObject();
			 int activityCount = f.getActivityCount();
			 if (index <= activityCount) {
			 	f.removeActivity(f.getActivity(index));
			 } else {
			 	logger.error("trying to remove activity which does not exist");
			 }
		 } else if (parentNode.getUserObject() instanceof If) {
		 	 If i = (If)parentNode.getUserObject();
		 	 i.setElse(null);
		 } else {
		 	logger.error("Trying to remove node that should not be removed");
		 }
		 
		 workflowBuilder.validateWorkflow();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
	 */
	public void treeStructureChanged(TreeModelEvent e) {
		workflowBuilder.validateWorkflow();
	}	
}
