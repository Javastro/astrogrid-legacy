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

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.workflowBuilder.models.WorkflowTreeModelSupport;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
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

/**
 * @author pjn3
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
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
	
	protected AbstractTreeTransferHandler(WorkflowDnDTree tree, int action, boolean drawIcon) {
		this.tree = tree;
		drawImage = drawIcon;
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(tree, action, this);
		dropTarget = new DropTarget(tree, action, this);
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {
		logger.error("dragGestureRecognized: dge");
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
			dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop, image, new Point(0,0), new TransferableNode(draggedNode, expandedStates, null, null), this);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent dsde) {
		logger.error("dragEnter: dsde");
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
		logger.error("dragOver: dsde");		
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
		logger.error("dropActionChanged: dsde");
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
		logger.error("dragDropEnd: dsde");
		if (drawImage) {
			dragPane.remove(draggedLabel);
			dragPane.repaint(draggedLabel.getBounds());
		}
		if (dsde.getDropSuccess() && dsde.getDropAction() == DnDConstants.ACTION_MOVE && draggedNodeParent != null) {
			((DefaultTreeModel)tree.getModel()).nodeStructureChanged(draggedNodeParent);
			tree.expandPath(new TreePath(draggedNodeParent.getPath()));
			tree.expandPath(new TreePath(draggedNode.getPath()));
		}

	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	public void dragExit(DragSourceEvent dse) {
		logger.error("dragExit: dse");
		dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);

	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
		logger.error("dragEnter: dtde");
		Point pt = dtde.getLocation();
		int action = dtde.getDropAction();
		if (drawImage) {
			paintImage(pt, ((DropTarget)dtde.getSource()).getComponent());
		}
		if (canPerformAction(tree, draggedNode, action, pt)) {
			dtde.acceptDrag(action);
		}
		else {
			dtde.rejectDrag();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		logger.error("dragOver: dtde");
		Point pt = dtde.getLocation();
		int action = dtde.getDropAction();
		tree.autoscroll(pt);
		if (drawImage && draggedLabel != null) {
			paintImage(pt, ((DropTarget)dtde.getSource()).getComponent());
		}		
		if (canPerformAction(tree, draggedNode, action, pt)) {
			dtde.acceptDrag(action);
		}
		else {
			dtde.rejectDrag();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {
		logger.error("dropActionCHanged: dtde");
		Point pt = dtde.getLocation();
		int action = dtde.getDropAction();
		if (drawImage) {
			paintImage(pt, ((DropTarget)dtde.getSource()).getComponent());
		}
		if (canPerformAction(tree, draggedNode, action, pt)) {
			dtde.acceptDrag(action);
		}
		else {
			dtde.rejectDrag();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde) {
		try {
			logger.error("drop: dtde");
			int action = dtde.getDropAction();
			// transferable = Transferable object associated with drop
			Transferable transferable = dtde.getTransferable();
			// Location of drop - use to determine pathTarget
			Point pt = dtde.getLocation();
			// Are we dragging a node - this will need to change when JList objects eg. activities can be dropped.
			// canPerformAction - checks if drop allowed at chosen location
			
			// What is transferable?
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)transferable.getTransferData(TransferableNode.NODE_FLAVOR);
			String activityString = (String)transferable.getTransferData(TransferableNode.STRING_FLAVOR);
			ApplicationInformation td = (ApplicationInformation)transferable.getTransferData(TransferableNode.TASK_DETAILS_FLAVOR);
			
			if (node != null && canPerformAction(tree, draggedNode, action, pt)) {
				TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
				// newParentNode = drop target
				DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();
				// expandedStates = nodes containing nodes
				Vector expandedStates = (Vector)transferable.getTransferData(TransferableNode.EXPANDED_STATE_FLAVOR);
				int index = -1;
				// Will drop be OK?
				if ((newParentNode.isLeaf() && !newParentNode.getAllowsChildren()) ||
						(node.getAllowsChildren() && newParentNode.getAllowsChildren() && !newParentNode.isRoot())) {
					DefaultMutableTreeNode tempNode = newParentNode;
					newParentNode = (DefaultMutableTreeNode)tempNode.getParent();
					index = newParentNode.getIndex(tempNode);
					if (index == -1) {
						dtde.rejectDrop();
						return;
					}
					else {
						// index = newParentNode.getChildCount();
					}
					if (executeDrop(tree, node, newParentNode, index, expandedStates, action)) {
						dtde.acceptDrop(action);
						dtde.dropComplete(true);
						return;
					}
				}
				dtde.rejectDrop();
				dtde.dropComplete(false);
			}
			else if (activityString != null && canPerformAction(tree, draggedNode, action, pt)) {
				TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
				DefaultMutableTreeNode activityNode = new DefaultMutableTreeNode();
				
				if (activityString.equalsIgnoreCase("SEQUENCE")) {
					activityNode.setUserObject(new Sequence());
					activityNode.setAllowsChildren(true);		          	
				}
				else if (activityString.equalsIgnoreCase("FLOW")) {
					activityNode.setUserObject(new Flow());
					activityNode.setAllowsChildren(true);
				}
				else if (activityString.equalsIgnoreCase("STEP")) {
					activityNode.setUserObject(new Step());
					activityNode.setAllowsChildren(false);
				}
		        else if(activityString.equalsIgnoreCase("SCRIPT")) {
		        	activityNode.setUserObject(new Script());
		        	activityNode.setAllowsChildren(false);
		          }
				else if (activityString.equalsIgnoreCase("SET")) {					
					activityNode.setUserObject(new Set());
					activityNode.setAllowsChildren(false);
				}
				else if (activityString.equalsIgnoreCase("UNSET")) {					
					activityNode.setUserObject(new Unset());
					activityNode.setAllowsChildren(false);
				}
				else if (activityString.equalsIgnoreCase("FOR")) {
					activityNode.setUserObject(new For());
					activityNode.setAllowsChildren(true);
				}
				else if (activityString.equalsIgnoreCase("IF")) {					
					activityNode.setUserObject(new If());
					activityNode.setAllowsChildren(true);
				}
				else if (activityString.equalsIgnoreCase("PARFOR")) {
					activityNode.setUserObject(new Parfor());
					activityNode.setAllowsChildren(true);
				}
				else if (activityString.equalsIgnoreCase("SCOPE")) {
					activityNode.setUserObject(new Scope());
					activityNode.setAllowsChildren(true);
				}
				else if (activityString.equalsIgnoreCase("WHILE")) {
					activityNode.setUserObject(new While());
					activityNode.setAllowsChildren(true);
				}
				
				
				DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();				
				int index = -1;
				// Will drop be OK?
				if (true) { //(newParentNode.isLeaf() && !newParentNode.getAllowsChildren()) {
					DefaultMutableTreeNode tempNode = newParentNode;
					newParentNode = (DefaultMutableTreeNode)tempNode.getParent();
					index = newParentNode.getIndex(tempNode);
					if (index == -1) {
						dtde.rejectDrop();
						return;
					}
					else {
						// index = newParentNode.getChildCount();
					}
					if (executeDrop(tree, activityNode, newParentNode, index, null, action)) {
						dtde.acceptDrop(action);
						dtde.dropComplete(true);
						return;
					}
				}
			}
			else if (td != null && canPerformAction(tree, draggedNode, action, pt)) {
				TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
				DefaultMutableTreeNode taskNode = new DefaultMutableTreeNode();

				ApplicationsInternal apps = tree.getApplicationsInternal();
                int selectedInterface = 0;
                if (td.getInterfaces().length > 1) {
                    // @todo prompt user  in a little dialog to select which interface to use..                   
                } 
                /*
				ApplicationDescription applicationDescription = apps.getApplicationDescription(td.getId());
				InterfacesType interfaces = applicationDescription.getInterfaces();
				Interface iface = null;
				for (int i = 0; i <= interfaces.get_interfaceCount(); i++) {
					if (interfaces.get_interface(i).getName().equalsIgnoreCase(td.getInterfaceName())) {
						iface = interfaces.get_interface(i);
						break;
					}
				}			
				Tool tool = applicationDescription.createToolFromInterface(iface);
                */
                Tool tool = apps.createTemplateTool(td.getInterfaces()[selectedInterface].getName(),td);
				Step step = new Step();
				step.setTool(tool);
				step.setName("Dragged step");
				taskNode.setUserObject(step);
				
				DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode)pathTarget.getLastPathComponent();				
				int index = -1;
				// Will drop be OK?
				if (true) { //(newParentNode.isLeaf() && !newParentNode.getAllowsChildren()) {
					DefaultMutableTreeNode tempNode = newParentNode;
					newParentNode = (DefaultMutableTreeNode)tempNode.getParent();
					index = newParentNode.getIndex(tempNode);
					if (index == -1) {
						dtde.rejectDrop();
						return;
					}
					else {
						// index = newParentNode.getChildCount();
					}
					if (executeDrop(tree, taskNode, newParentNode, index, null, action)) {
						dtde.acceptDrop(action);
						dtde.dropComplete(true);
						return;
					}
				}
				
			}
		}
		catch (Exception ex) {
			logger.error("Drop failed: " + ex.getMessage());
			dtde.rejectDrop();
			dtde.dropComplete(false);
		}		
	}

	/* (non-Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte) {
		logger.error("dragExit: dte");
		// TODO Auto-generated method stub

	}
	
    private final synchronized void paintImage(Point pt, Component source) {
        pt = SwingUtilities.convertPoint(source, pt, dragPane);
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

}
