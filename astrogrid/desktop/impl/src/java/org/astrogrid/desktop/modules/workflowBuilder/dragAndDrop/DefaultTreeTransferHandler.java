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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Then;

/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DefaultTreeTransferHandler extends AbstractTreeTransferHandler {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DefaultTreeTransferHandler.class);
	/**
	 * @param tree
	 * @param action
	 * @param drawIcon
	 */
	public DefaultTreeTransferHandler(WorkflowDnDTree tree, int action) {
		super(tree, action, true);
	}	
	
	/**
	 * @param tree
	 * @param action
	 * @param drawIcon
	 */
	public DefaultTreeTransferHandler(WorkflowDnDTree tree, int action, boolean drawIcon) {
		super(tree, action, drawIcon);
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.AbstractTreeTransferHandler#canPerformAction(javax.swing.JTree, javax.swing.tree.DefaultMutableTreeNode, int, java.awt.Point)
	 */
	public boolean canPerformAction(WorkflowDnDTree target,
			                        DefaultMutableTreeNode draggedNode, 
									int action, 
									Point location) {
		logger.error("canPerformAction: ");
		logger.error("target: " + target);
		logger.error("draggedNode: " + draggedNode);
		logger.error("action: " + action);
		logger.error("location: " + location);		
		TreePath pathTarget = target.getPathForLocation(location.x, location.y);
		if (pathTarget == null) {
			target.setSelectionPath(null);
			return false;
		}
		// temp - at the moment all task/activity drag items will return true for the drop
		if (draggedNode == null) {
			return true;
		}
				
		// The following prevents dropping nodes onto parents that do not accept children
		// makes sense, but need to consider how to drop children above nodes that do not accept children 		 
		 DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();
		 if (!newParentNode.getAllowsChildren()) {
		   return false;
		 }
		
				
		target.setSelectionPath(pathTarget);
		if (action == DnDConstants.ACTION_COPY) {
			return true;
		}
		else {
			if (action == DnDConstants.ACTION_MOVE) {
				DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();
				if (draggedNode.getUserObject() instanceof String) {
					if (parentNode.getAllowsChildren()) {
						return true;
					}
					else {
						return false;
					}
				}
				// Move rules:
				// 1) Cannot move root
				// 2) Cannot drop onto root - need to revisit this, not sure if I am right here
				// 3) Cannot drop a node onto itself
				// 4) Cannot drop a node onto a Script body, nor onto a Script node
				// 5) If will only accept drops of then and else
				// 6) Cannot drop onto Then or Else - sequence will be there for that - should be able to improve
				
				else if (draggedNode.isRoot() || 
					parentNode.isRoot()  ||
					parentNode == draggedNode.getParent() ||
					draggedNode.isNodeDescendant(parentNode) ||
					((DefaultMutableTreeNode)parentNode.getParent()).getUserObject() instanceof Script ||
					parentNode.getUserObject() instanceof Script ||
					((parentNode.getUserObject() instanceof If) && !(draggedNode.getUserObject() instanceof Then || draggedNode.getUserObject() instanceof Else)) ||
					((parentNode.getUserObject() instanceof Then) || (parentNode.getUserObject() instanceof Else))) {
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
		logger.error("DefaultTreeTransferHandler: executeDrop");
		logger.error("target: " + target);
		logger.error("draggedNode: " + draggedNode);
		logger.error("newParentNode: "+ newParentNode);
		logger.error("index: " + index);
		logger.error("expandedStates: " + expandedStates);
		logger.error("action: " + action);
		if (action == DnDConstants.ACTION_COPY) {
			DefaultMutableTreeNode newNode = target.makeDeepCopy(draggedNode);
			target.expandPath(new TreePath(newParentNode.getPath()));
			((DefaultTreeModel)target.getModel()).insertNodeInto(newNode,
					                                             newParentNode,
																 index);
			TreePath treePath = new TreePath(newNode.getPath());
			int i = 0;
			for (Enumeration enumeration = newNode.depthFirstEnumeration(); enumeration.hasMoreElements(); i++) {
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
		if (action == DnDConstants.ACTION_MOVE) {
			// if draggedNode has no parent then we can assume it is either an activity or task
            if (draggedNode.getParent() != null) {
			    TreePath oldParentPath = new TreePath(((DefaultMutableTreeNode)draggedNode.getParent()).getPath());
			    draggedNode.removeFromParent();
			    target.expandPath(new TreePath(newParentNode.getPath()));
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
            else {           
            	target.expandPath(new TreePath(newParentNode.getPath()));
            	((DefaultTreeModel)target.getModel()).insertNodeInto(draggedNode, newParentNode, index);
            	TreePath treePath = new TreePath(newParentNode.getPath());			
            	target.scrollPathToVisible(treePath);
            	target.setSelectionPath(treePath);
            	return true;            	
            }
		}
		return false;
	}
	
}
