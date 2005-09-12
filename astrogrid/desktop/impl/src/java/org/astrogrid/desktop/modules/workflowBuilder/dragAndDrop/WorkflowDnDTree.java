/* WorkflowDnDTree.java
 * Created on 19-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop;

import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.workflowBuilder.renderers.WorkflowTreeCellRenderer;
import org.astrogrid.workflow.beans.v1.AbstractActivity;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Set;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Unset;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkflowDnDTree extends JTree {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(WorkflowDnDTree.class);
	
	private Insets autoscrollInsets = new Insets(20,20,20,20); // insets

	/**
	 * 
	 */
	public WorkflowDnDTree(ApplicationsInternal apps) {
		super();
		setAutoscrolls(true);        
		setRootVisible(true);
		setShowsRootHandles(false); 
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setEditable(false);
		setBorder(new EmptyBorder(10,10,10,10));
		setCellRenderer(new WorkflowTreeCellRenderer(apps));
		new DefaultTreeTransferHandler(this, DnDConstants.ACTION_COPY_OR_MOVE, true);
	}

    /** if expand is true, expand all nodes in the tree. otherwise collapse all nodes */
    public void expandAll(boolean expand) {
        TreeNode root = (TreeNode)getModel().getRoot();
        expandAll(new TreePath(root),expand);
    }
    
    private void expandAll(TreePath parent,boolean expand) {
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >=0) {
            for (Enumeration e= node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(path,expand);
            }
        }
        if (expand) {
            expandPath(parent);
        } else {
            collapsePath(parent);
        }
    }
	
	
	public void autoscroll(Point cursorLocation) {
		Insets insets = getAutoscrollInsets();
		Rectangle outer = getVisibleRect();
		Rectangle inner = new Rectangle(outer.x+insets.left, 
				                        outer.y+insets.top,
										outer.width-(insets.left+insets.right),
										outer.height-(insets.top+insets.bottom));
		if (!inner.contains(cursorLocation)) {
			Rectangle scrollRect = new Rectangle(cursorLocation.x-insets.left,
					                             cursorLocation.y-insets.top,
												 insets.left+insets.right,
												 insets.top+insets.bottom);
			scrollRectToVisible(scrollRect);
		}
	}
	
	public Insets getAutoscrollInsets() {
		return autoscrollInsets;
	}
	
	public static DefaultMutableTreeNode makeDeepCopy(DefaultMutableTreeNode node) {
		DefaultMutableTreeNode copy = new DefaultMutableTreeNode(node.getUserObject());
		for(Enumeration e = node.children();e.hasMoreElements();) {
			copy.add(makeDeepCopy((DefaultMutableTreeNode)e.nextElement()));
		}
		return copy;
	}
    
    


}       	


