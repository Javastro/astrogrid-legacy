/* AbstractTreeTransferHandler.java
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

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.workflowBuilder.models.WorkflowTreeModelSupport;
import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Script;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Then;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;

/**
 * @author pjn3
 *
 */
public abstract class AbstractTreeTransferHandler extends WorkflowTreeModelSupport implements
		DragGestureListener, DragSourceListener, DropTargetListener {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(AbstractTreeTransferHandler.class);
	
	private WorkflowDnDTree tree;
	private DragSource dragSource; // dragsource
	private DropTarget dropTarget; // droptarget
	private static DefaultMutableTreeNode draggedNode;
	private DefaultMutableTreeNode draggedNodeParent;
	private static JLabel draggedLabel;
	private static JLayeredPane dragPane;
	private boolean drawImage;
	private boolean allowDnD; // Enable DnD to be turned off
	
	protected AbstractTreeTransferHandler(WorkflowDnDTree tree, int action, boolean drawIcon) {
		this.tree = tree;
		drawImage = drawIcon;
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(tree, action, this);
		dropTarget = new DropTarget(tree, action, this);
		allowDnD = true;
	}
	
	/**
	 * Allow drag and drop to be turned on or off
	 * @param b
	 */
	public void allowDnD(boolean b) {
		allowDnD = b;
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {
		if (!allowDnD)
			return;
		TreePath path = tree.getSelectionPath();		
		if (path != null) {
			draggedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
			Vector expandedStates = new Vector();
			for (Enumeration enumeration = draggedNode.depthFirstEnumeration(); enumeration.hasMoreElements(); ) {
				DefaultMutableTreeNode element = (DefaultMutableTreeNode)enumeration.nextElement();
				TreePath treePath = new TreePath(element.getPath());
				expandedStates.add(new Boolean(tree.isExpanded(treePath)));
			}
			draggedNodeParent = (DefaultMutableTreeNode)draggedNode.getParent();
			BufferedImage image = null;
			if (drawImage) {
				Rectangle pathBounds = tree.getPathBounds(path); // get path bounds of selection path
				JComponent lbl = (JComponent)tree.getCellRenderer().getTreeCellRendererComponent(tree, draggedNode, false, tree.isExpanded(path),((DefaultTreeModel)tree.getModel()).isLeaf(path.getLastPathComponent()), 0, false); //returning the label
				lbl.setBounds(pathBounds); // set bounds to lbl
				image = new BufferedImage(lbl.getWidth(), lbl.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE); // buffered image reference passing the labels height and width
				Graphics2D graphics = image.createGraphics(); // creating the graphics for buffered image
				graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // Sets the Composite for the Graphics2D context
				lbl.setOpaque(false);
				lbl.paint(graphics);
				graphics.dispose();
				draggedLabel = new JLabel(new ImageIcon(image));
				draggedLabel.setOpaque(false);
				draggedLabel.setBounds(pathBounds);
				Container container = tree.getTopLevelAncestor();
				if (container == null) {
					drawImage = false;
				}
				else {
					if (container instanceof JWindow) {
						dragPane = ((JWindow)tree.getTopLevelAncestor()).getLayeredPane();
						dragPane.add(draggedLabel, JLayeredPane.DRAG_LAYER);
					}
					else if (container instanceof JFrame){
						dragPane = ((JFrame)tree.getTopLevelAncestor()).getLayeredPane();
						dragPane.add(draggedLabel, JLayeredPane.DRAG_LAYER);
					}
					else {
						drawImage = false;
					}
				}
			}
			dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop, image, new Point(0,0), new TransferableNode(draggedNode, expandedStates, null), this);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent dsde) {
		int action = dsde.getDropAction();
		if (action == DnDConstants.ACTION_COPY) {
			dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
		}
		else {
			if (action == DnDConstants.ACTION_MOVE) {
				dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
			} 
			else {
				dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
			}			
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragOver(DragSourceDragEvent dsde) {
		int action = dsde.getDropAction();
		if (action == DnDConstants.ACTION_COPY) {
			dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
		}
		else {
			if (action == DnDConstants.ACTION_MOVE) {
				dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
			}
			else {
				dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
			}
		}

	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dropActionChanged(DragSourceDragEvent dsde) {
		int action = dsde.getDropAction();
		if (action == DnDConstants.ACTION_COPY) {
			dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
		}
		else {
			if (action == DnDConstants.ACTION_MOVE) {
				dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
			}
			else {
				dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	public void dragDropEnd(DragSourceDropEvent dsde) {
		if (drawImage) {
			dragPane.remove(draggedLabel);
			dragPane.repaint(draggedLabel.getBounds());
		}
		if (dsde.getDropSuccess() && dsde.getDropAction() == DnDConstants.ACTION_MOVE && draggedNodeParent != null) {
			((DefaultTreeModel)tree.getModel()).nodeStructureChanged(draggedNodeParent);
			tree.expandPath(new TreePath(draggedNodeParent.getPath()));
			if (draggedNode != null) {
				tree.expandPath(new TreePath(draggedNode.getPath()));
			}
		}

	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	public void dragExit(DragSourceEvent dse) {
		dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);

	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
		Point pt = dtde.getLocation();
		int action = dtde.getDropAction();
		if (drawImage) {
			paintImage(pt, ((DropTarget)dtde.getSource()).getComponent());
		}
		if (draggedNode == null) {
			DropTargetDropEvent tempDTDropEvent = new  
			DropTargetDropEvent(dtde.getDropTargetContext(),
					dtde.getLocation(), 0, 0);
			Transferable transferable = tempDTDropEvent.getTransferable();
			draggedNode = getActivityNode(transferable, null);
			action = DnDConstants.ACTION_COPY;			
		}
		if (canPerformAction(tree, draggedNode, action, pt)) {
			dtde.acceptDrag(action);
		}
		else {
			if (action == DnDConstants.ACTION_COPY)
				draggedNode = null;
			dtde.rejectDrag();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		Point pt = dtde.getLocation();
		int action = dtde.getDropAction();
		tree.autoscroll(pt);
		if (drawImage && draggedLabel != null) {
			paintImage(pt, ((DropTarget)dtde.getSource()).getComponent());
		}
		if (draggedNode == null) {
			DropTargetDropEvent tempDTDropEvent = new  
			DropTargetDropEvent(dtde.getDropTargetContext(),
					dtde.getLocation(), 0, 0);
			Transferable transferable = tempDTDropEvent.getTransferable();
			draggedNode = getActivityNode(transferable, null);
			action = DnDConstants.ACTION_COPY;
		}
		if (canPerformAction(tree, draggedNode, action, pt)) {
			dtde.acceptDrag(action);
		}
		else {
			if (action == DnDConstants.ACTION_COPY)
				draggedNode = null;
			dtde.rejectDrag();
		}		
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {
		Point pt = dtde.getLocation();
		int action = dtde.getDropAction();
		if (drawImage) {
			paintImage(pt, ((DropTarget)dtde.getSource()).getComponent());
		}
		if (draggedNode == null) {
			DropTargetDropEvent tempDTDropEvent = new  
			DropTargetDropEvent(dtde.getDropTargetContext(),
					dtde.getLocation(), 0, 0);
			Transferable transferable = tempDTDropEvent.getTransferable();
			draggedNode = getActivityNode(transferable, null);
			action = DnDConstants.ACTION_COPY;
		}
		if (canPerformAction(tree, draggedNode, action, pt)) {
			dtde.acceptDrag(action);
		}
		else {
			if (action == DnDConstants.ACTION_COPY)
				draggedNode = null;
			dtde.rejectDrag();
		}
	} 

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde) {
		try {	
			int action = dtde.getDropAction();
			// transferable = Transferable object associated with drop
			Transferable transferable = dtde.getTransferable();
			// Location of drop - use to determine pathTarget
			Point pt = dtde.getLocation();
			// Are we dragging a node - this will need to change when JList objects eg. activities can be dropped.
			// canPerformAction - checks if drop allowed at chosen location
			
			// What is transferable?
			DefaultMutableTreeNode node = null;
			try {
				 node = (DefaultMutableTreeNode)transferable.getTransferData(TransferableNode.NODE_FLAVOR);
			}
			catch (UnsupportedFlavorException ex) {
				node = null;
			}
						
			// if node is not null we are dragging from within tree itself
			if (node!= null) {
				DefaultMutableTreeNode n = new DefaultMutableTreeNode();
				n = node;
				node = null;
				if (canPerformAction(tree, n, action, pt)) {			
					TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
					// newParentNode = drop target
					DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();
					// expandedStates = nodes containing nodes
					Vector expandedStates = (Vector)transferable.getTransferData(TransferableNode.EXPANDED_STATE_FLAVOR);
					int index = -1;
					// Will drop be OK?
					if (newParentNode.getAllowsChildren() && 
						!(newParentNode.getUserObject() instanceof Workflow ||
						  newParentNode.getUserObject() instanceof Step ||
						  newParentNode.getUserObject() instanceof While ||
						  newParentNode.getUserObject() instanceof Parfor ||
						  newParentNode.getUserObject() instanceof For ||
						  newParentNode.getUserObject() instanceof If ||
						  newParentNode.getUserObject() instanceof Then ||
						  newParentNode.getUserObject() instanceof Else ||
						  newParentNode.getUserObject() instanceof Script ||
						  newParentNode.getUserObject() instanceof Scope)) {
						if (executeDrop(tree, n, newParentNode, 0, null, action)) {
							dtde.acceptDrop(action);
							dtde.dropComplete(true);
							return;
						}
					}
					else if ((!newParentNode.getAllowsChildren() && 
							 !newParentNode.isRoot()) || 
							 (newParentNode.getUserObject() instanceof Step ||
							  newParentNode.getUserObject() instanceof Script  ||
							  newParentNode.getUserObject() instanceof For || 
							  newParentNode.getUserObject() instanceof If || 
							  newParentNode.getUserObject() instanceof While ||
							  newParentNode.getUserObject() instanceof Parfor || 
							  newParentNode.getUserObject() instanceof Scope ))  {
						DefaultMutableTreeNode tempNode = newParentNode;
						newParentNode = (DefaultMutableTreeNode)tempNode.getParent();
						index = newParentNode.getIndex(tempNode);
						if (index == -1) {
							dtde.rejectDrop();						
							return;
						}
						if (executeDrop(tree, n, newParentNode, index, expandedStates, action)) {
							dtde.acceptDrop(action);
							dtde.dropComplete(true);
							return;
						}
					}					
				}
				dtde.rejectDrop();
				dtde.dropComplete(false);
				//draggedNode = null;
			}
			else { // we are looking at a dragged in activity
				TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
				DefaultMutableTreeNode activityNode = getActivityNode(transferable, null);								
				DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();
				int index = -1;
				// Will drop be OK?

				// If parent allows children drop dragged node as 1st child, 
				// unless parent node allows children as it needs a sequence 
				// as it's 1 and only child i.e. loops, if, then/else
				if  (newParentNode.getAllowsChildren() &&
					 !(newParentNode.getUserObject() instanceof Workflow ||
					 newParentNode.getUserObject() instanceof Step ||
					 newParentNode.getUserObject() instanceof While ||
					 newParentNode.getUserObject() instanceof Parfor ||
					 newParentNode.getUserObject() instanceof For ||
					 newParentNode.getUserObject() instanceof If ||
					 newParentNode.getUserObject() instanceof Then ||
					 newParentNode.getUserObject() instanceof Else ||
					 newParentNode.getUserObject() instanceof Script ||
					 newParentNode.getUserObject() instanceof Scope ||					 
					 activityNode.getUserObject() instanceof Else )) {
					if (executeDrop(tree, activityNode, newParentNode, 0, null, action)) {
						dtde.acceptDrop(action);
						dtde.dropComplete(true);
						draggedNode = null;
						return;
					}
				}
				
				// If parent is IF and activityNode is Else drop as child at index 1 - Then 
				// activity will be at index 0. Only allow 1 else per If
				else if (newParentNode.getUserObject() instanceof If && 
						 activityNode.getUserObject() instanceof Else && 
						 newParentNode.getChildCount() <= 1 ) {
					if (executeDrop(tree, activityNode, newParentNode, 1, null, action)) {
						dtde.acceptDrop(action);
						dtde.dropComplete(true);
						draggedNode = null;
						return;
					}
				}
				
				// If parent is Then and activityNode is Else drop as child at index 1 of If node
				// only allow 1 else per If
				else if (newParentNode.getUserObject() instanceof Then && 
						 activityNode.getUserObject() instanceof Else &&
						 ((DefaultMutableTreeNode)newParentNode.getParent()).getChildCount() <= 1) {
					DefaultMutableTreeNode ifNode = (DefaultMutableTreeNode)newParentNode.getParent();

					if (executeDrop(tree, activityNode, ifNode, 1, null, action)) {
						dtde.acceptDrop(action);
						dtde.dropComplete(true);
						draggedNode = null;
						return;
					}
				}				
				
				// If parentNode is body of script insert above script itself
				else if (newParentNode.getUserObject() instanceof String) {
					DefaultMutableTreeNode scriptNode = (DefaultMutableTreeNode)newParentNode.getParent();
					DefaultMutableTreeNode scriptParentNode = (DefaultMutableTreeNode)scriptNode.getParent();
					index = scriptParentNode.getIndex(scriptNode);
					if (executeDrop(tree, activityNode, scriptParentNode, index, null, action)) {
						dtde.acceptDrop(action);
						dtde.dropComplete(true);
						draggedNode = null;
						return;
					}
				}
				
				// If parent does not allow children insert dragged node above target node, 
				// or allows children so it can have a Sequence as it's only activity
				else if ((!newParentNode.getAllowsChildren() ||
						  newParentNode.getUserObject() instanceof Step ||
						  newParentNode.getUserObject() instanceof While ||
						  newParentNode.getUserObject() instanceof Parfor ||
						  newParentNode.getUserObject() instanceof For ||
						  newParentNode.getUserObject() instanceof Script ||
						  newParentNode.getUserObject() instanceof Scope ||
						  !(activityNode.getUserObject() instanceof Else)) 
						  && (!(newParentNode.getUserObject() instanceof Then ||
						      newParentNode.getUserObject() instanceof Else))) {
					DefaultMutableTreeNode tempNode = newParentNode;
					newParentNode = (DefaultMutableTreeNode)tempNode.getParent();
					index = newParentNode.getIndex(tempNode);
					if (index == -1) {
						dtde.rejectDrop();
						draggedNode = null;
						return;
					}
					if (executeDrop(tree, activityNode, newParentNode, index, null, action)) {
						dtde.acceptDrop(action);
						dtde.dropComplete(true);
						draggedNode = null;
						return;
					}
				}
				
				// If parent is Then or Else drop activityNode prior to If node
				else if (!(draggedNode.getUserObject() instanceof Else) &&
						 (newParentNode.getUserObject() instanceof Then || 
						 newParentNode.getUserObject() instanceof Else)) {
					DefaultMutableTreeNode scriptNode = (DefaultMutableTreeNode)newParentNode.getParent();
					DefaultMutableTreeNode scriptParentNode = (DefaultMutableTreeNode)scriptNode.getParent();
					index = scriptParentNode.getIndex(scriptNode);
					if (executeDrop(tree, activityNode, scriptParentNode, index, null, action)) {
						dtde.acceptDrop(action);
						dtde.dropComplete(true);
						draggedNode = null;
						return;
					}
				}
				
				else {
					logger.error("Not able to drop " + activityNode + " into " + newParentNode);
				}
				dtde.rejectDrop();
				dtde.dropComplete(false);
				draggedNode = null;
			}
		}
		catch (Exception ex) {
			logger.error("Drop failed: " + ex.getMessage());
			dtde.rejectDrop();
			dtde.dropComplete(false);
			draggedNode = null;
		}		
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte) {
	}
	
    private final synchronized void paintImage(Point pt, Component source) {
        pt = SwingUtilities.convertPoint(source, pt, dragPane);
        if (draggedLabel != null)
        	draggedLabel.setLocation(pt);
    }
    


    /**
     * Test to see if drop can be performed:
     * If no node present at drop location within WorkflowDnDTree return false
     * Otherwise checks action and node relationships to decide if drop can or cannot occur
     * 
     * @param target WorkflowDnDTree
     * @param draggedNode DefaultMutableTreeNode
     * @param action int copy or move
     * @param location Point - drop location
     * @return boolean
     */
	public abstract boolean canPerformAction(WorkflowDnDTree target, DefaultMutableTreeNode draggedNode, int action, Point location);
	
	public abstract boolean executeDrop(WorkflowDnDTree tree, DefaultMutableTreeNode draggedNode, DefaultMutableTreeNode newParentNode, int index, Vector expandedStates, int action);
	
    /*
     * Return DefaultMutableTreeNode with user object set to relevant activity taken from transferable
     * @param transferable
     * @return DefaultMutableTreeNode
     */
    public abstract DefaultMutableTreeNode getActivityNode(Transferable transferable, String s);

}
