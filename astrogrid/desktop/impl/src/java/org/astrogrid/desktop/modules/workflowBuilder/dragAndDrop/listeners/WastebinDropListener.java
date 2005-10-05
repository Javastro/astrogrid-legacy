/* WastebinDropListener.java
 * Created on 28-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.listeners;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.TransferableNode;
import org.astrogrid.desktop.modules.workflowBuilder.dragAndDrop.WorkflowDnDTree;
import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Then;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WastebinDropListener implements DropTargetListener {
    /**
     * Commons Logger for this class
     */
    private final Log logger = LogFactory.getLog(WastebinDropListener.class);
    
    private WorkflowDnDTree tree;
	/**
	 * 
	 */
	public WastebinDropListener() {
		super();
	}
	/**
	 * 
	 */
	public WastebinDropListener(WorkflowDnDTree tree) {
		super();
		this.tree = tree;
	}	

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
		int action = dtde.getDropAction();
//		Transferable transferable = dtde.getTransferable();
//		 following is a 1.4 fix
		DropTargetDropEvent tempDTDropEvent = new  
		DropTargetDropEvent(dtde.getDropTargetContext(),
				dtde.getLocation(), 0, 0);
		Transferable transferable = tempDTDropEvent.getTransferable();
		DefaultMutableTreeNode draggedNode = null;
		try {
			draggedNode = (DefaultMutableTreeNode)transferable.getTransferData(TransferableNode.NODE_FLAVOR);
		}
		catch (Exception ex) {
			;
		}
		if (action == DnDConstants.ACTION_MOVE) {
			if (canPerformDrop(draggedNode)) {
				dtde.acceptDrag(action);
			} else {
				dtde.rejectDrag();
			}
		} else {
			dtde.rejectDrag();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		int action = dtde.getDropAction();
		// Transferable transferable = dtde.getTransferable(); java 1.5 method
		// following is a 1.4 fix
		DropTargetDropEvent tempDTDropEvent = new  
		DropTargetDropEvent(dtde.getDropTargetContext(),
				dtde.getLocation(), 0, 0);
		Transferable transferable = tempDTDropEvent.getTransferable();
		DefaultMutableTreeNode draggedNode = null;
		try {
			draggedNode = (DefaultMutableTreeNode)transferable.getTransferData(TransferableNode.NODE_FLAVOR);
		}
		catch (Exception ex) {
			;
		}
		if (action == DnDConstants.ACTION_MOVE) {
			if (canPerformDrop(draggedNode)) {
				dtde.acceptDrag(action);
			} else {
				dtde.rejectDrag();
			}
		} else {
			dtde.rejectDrag();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte) {

	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde) {
		int action = dtde.getDropAction();
		if (action == DnDConstants.ACTION_MOVE) {
			Transferable transferable = dtde.getTransferable();
			DefaultMutableTreeNode draggedNode = null;
			try {
				draggedNode = (DefaultMutableTreeNode)transferable.getTransferData(TransferableNode.NODE_FLAVOR);
				
				((DefaultTreeModel)tree.getModel()).removeNodeFromParent(draggedNode);
				draggedNode.removeFromParent();
				dtde.acceptDrop(action);
				dtde.dropComplete(true);
				draggedNode = null;
			}
			catch (Exception ex) {
				logger.error("Exception dropping activity into wastebin: " + ex.getMessage());
				draggedNode = null;
			}			
		} else {
			dtde.rejectDrop();
			dtde.dropComplete(false);
		}
	}
	
	private boolean canPerformDrop(DefaultMutableTreeNode draggedNode) {
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)draggedNode.getParent();
		if (draggedNode.isRoot() ||
		    parentNode.getUserObject() instanceof Workflow ||
			draggedNode.getUserObject() instanceof Workflow ||
			draggedNode.getUserObject() instanceof Tool ||
			draggedNode.getUserObject() instanceof String ||
			draggedNode.getUserObject() instanceof Then) {
			return false;
		} else if (draggedNode.getUserObject() instanceof Sequence &&
				   (parentNode.getUserObject() instanceof Else ||
				   	parentNode.getUserObject() instanceof Then ||
					parentNode.getUserObject() instanceof Scope ||
					parentNode.getUserObject() instanceof For ||
					parentNode.getUserObject() instanceof While ||
					parentNode.getUserObject() instanceof Parfor)) {
			return false;
		} else {
			return true;
		}		
	}
}
