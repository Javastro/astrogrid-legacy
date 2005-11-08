/* DefaultTreeTransferHandler.java
 * Created on 17-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop;

import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Then;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author pjn3
 *
 */
public class DefaultTreeTransferHandler extends AbstractTreeTransferHandler {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DefaultTreeTransferHandler.class);
    private WorkflowDnDTree tree;
	/**
	 * @param tree
	 * @param action
	 * @param drawIcon
	 */
	public DefaultTreeTransferHandler(WorkflowDnDTree tree, int action) {
		super(tree, action, true);
		this.tree = tree;
	}	
	
	/**
	 * @param tree
	 * @param action
	 * @param drawIcon
	 */
	public DefaultTreeTransferHandler(WorkflowDnDTree tree, int action, boolean drawIcon) {
		super(tree, action, drawIcon);
		this.tree = tree;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.AbstractTreeTransferHandler#canPerformAction(javax.swing.JTree, javax.swing.tree.DefaultMutableTreeNode, int, java.awt.Point)
	 */
	public boolean canPerformAction(WorkflowDnDTree target,
			                        DefaultMutableTreeNode draggedNode, 
									int action, 
									Point location) {
		TreePath pathTarget = target.getPathForLocation(location.x, location.y);
		if (pathTarget == null) {
			target.setSelectionPath(null);
			return false;
		}
		
		if (draggedNode == null) {
			return false;
		}
		
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();
				
		// The following prevents dropping nodes onto parents that do not accept children
		// makes sense, but need to consider how to drop children above nodes that do not accept children 		 
		// DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();
		// if (!newParentNode.getAllowsChildren()) {
		//   return false;
		// }
		
				
		target.setSelectionPath(pathTarget);
		if (action == DnDConstants.ACTION_COPY) { 
			// includes dragging activity into tree  
			// Move rules are simple here as AbstractTreeTransferHandler takes care of all complex drops
			
			if (parentNode.getUserObject() instanceof Workflow ||
				(draggedNode.getUserObject() instanceof Else &&
				!(
					(parentNode.getUserObject() instanceof If && parentNode.getChildCount() <= 1) || 
					(parentNode.getUserObject() instanceof Then && ((DefaultMutableTreeNode)parentNode.getParent()).getChildCount() <= 1)
				)
				)) 
			{
				return false;					
			}
			else {
				return true;
			}
		}		
		else {			
			if (action == DnDConstants.ACTION_MOVE) {
				// Dragging nodes already in the tree
				// Prevent sequence being dragged away from activities that require Sequence as 1st and only child
				DefaultMutableTreeNode draggedNodeParent = (DefaultMutableTreeNode)draggedNode.getParent();
				if (draggedNode.getUserObject() instanceof Sequence && ( 
					draggedNodeParent.getUserObject() instanceof Workflow || 
					draggedNodeParent.getUserObject() instanceof While ||
					draggedNodeParent.getUserObject() instanceof Parfor ||
					draggedNodeParent.getUserObject() instanceof For ||
					draggedNodeParent.getUserObject() instanceof Else ||
					draggedNodeParent.getUserObject() instanceof Then ||
					draggedNodeParent.getUserObject() instanceof Scope ||
					draggedNodeParent.getUserObject() instanceof String)) {
					return false;
				}
				// dragging nodes already in the tree			
				// Move rules:
				else if (draggedNode.isRoot() || 
					parentNode.getUserObject() instanceof Workflow  ||
					parentNode == draggedNode.getParent() ||
					parentNode.getUserObject() instanceof String ||
					draggedNode.getUserObject() instanceof String ||
					parentNode.getUserObject() instanceof Tool ||
					draggedNode.getUserObject() instanceof Tool ||
					draggedNode.getUserObject() instanceof Else ||
					parentNode.getUserObject() instanceof Else ||
					draggedNode.getUserObject() instanceof Then ||
					parentNode.getUserObject() instanceof Then
					) {
					return false;
				}
				else {
					return true;
				}
			}
			else {
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.AbstractTreeTransferHandler#executeDrop(javax.swing.JTree, javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.DefaultMutableTreeNode, int, java.util.Vector, int)
	 */
	public boolean executeDrop(WorkflowDnDTree target, 
			                   DefaultMutableTreeNode draggedNode,
			                   DefaultMutableTreeNode newParentNode, 
							   int index,
			                   Vector expandedStates, 
							   int action) {
		if (action == DnDConstants.ACTION_COPY) {
			// COPY: We are dragging in from the activity list
			target.expandPath(new TreePath(newParentNode.getPath()));
			((DefaultTreeModel)target.getModel()).insertNodeInto(draggedNode,
                       											 newParentNode,
																 index);

			TreePath treePath = new TreePath(draggedNode.getFirstLeaf().getPath());
			target.scrollPathToVisible(treePath);
			target.setSelectionPath(treePath);
			 if (tree.getWorkflowBuilderImpl().autoPopUp && (action == DnDConstants.ACTION_COPY))
			 	tree.getWorkflowBuilderImpl().editNode(draggedNode, true);
			return true;
		}
		if (action == DnDConstants.ACTION_MOVE) {
			// MOVE: We are dragging within the tree
			TreePath oldParentPath = new TreePath(((DefaultMutableTreeNode)draggedNode.getParent()).getPath());
			//target.collapsePath(oldParentPath); 
			((DefaultTreeModel)target.getModel()).removeNodeFromParent(draggedNode); 
			draggedNode.removeFromParent();
			target.expandPath(new TreePath(newParentNode.getFirstLeaf().getPath()));
			((DefaultTreeModel)target.getModel()).insertNodeInto(draggedNode, newParentNode, index);
			TreePath treePath = new TreePath(draggedNode.getPath());
			int i = 0;		
			for (Enumeration enumeration = draggedNode.depthFirstEnumeration(); enumeration.hasMoreElements(); i++ ) {
				DefaultMutableTreeNode element = (DefaultMutableTreeNode)enumeration.nextElement();
			    TreePath path = new TreePath(element.getPath());
			    if (((Boolean)expandedStates.get(i)).booleanValue()) {
			    	target.expandPath(path);
			    }
			}		
			target.scrollPathToVisible(treePath);
			target.setSelectionPath(treePath);
			return true;            
		}
		return false;
	}
	
}
