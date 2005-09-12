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
	private ApplicationsInternal apps;

	/**
	 * 
	 */
	public WorkflowDnDTree(ApplicationsInternal apps) {
		super();
		setAutoscrolls(true);
		setModel(treeModel);
		setRootVisible(true);
		setShowsRootHandles(false); 
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setEditable(false);
		setBorder(new EmptyBorder(10,10,10,10));
		setCellRenderer(new WorkflowTreeCellRenderer(apps));
		new DefaultTreeTransferHandler(this, DnDConstants.ACTION_COPY_OR_MOVE, true);
	}

	/**
	 * @param value
	 */
	public WorkflowDnDTree(DefaultMutableTreeNode root, ApplicationsInternal apps) {
		setAutoscrolls(true);
		DefaultTreeModel treemodel = new DefaultTreeModel(root);
		setModel(treeModel);
		setRootVisible(true);
		setShowsRootHandles(false); 
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setEditable(false);
		setBorder(new EmptyBorder(10,10,10,10));
		setCellRenderer(new WorkflowTreeCellRenderer(apps));
		new DefaultTreeTransferHandler(this, DnDConstants.ACTION_COPY_OR_MOVE, true);
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
	
	public DefaultMutableTreeNode createTree(Workflow workflow) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		root.setUserObject(workflow);
		root.add(activityTree(workflow.getSequence()));
		root.setAllowsChildren(true);
        return(root);
	}
	
    public DefaultMutableTreeNode activityTree( AbstractActivity activity ) { 
    	DefaultMutableTreeNode node = new DefaultMutableTreeNode();
          if( activity instanceof Sequence ) {
          	AbstractActivity[] activityArray = ((Sequence)activity).getActivity() ;
          	node.setUserObject((Sequence)activity);
          	node.setAllowsChildren(true);
            for( int i=0; i < activityArray.length; i++ ){
                node.add(activityTree( activityArray[i] ));
            }
          }
       	  else if( activity instanceof Flow ) {
       	   	node.setUserObject((Flow)activity);
       	   	node.setAllowsChildren(true);
        	AbstractActivity[] activityArray = ((Flow)activity).getActivity() ;
            for( int i=0; i < activityArray.length; i++ ){
                node.add(activityTree( activityArray[i] ));
            }
          }
          else if( activity instanceof Step ) {
            node.setUserObject((Step)activity);
            node.setAllowsChildren(false);
          }
          else if( activity instanceof Script ) {
            node.setUserObject((Script)activity);            
            DefaultMutableTreeNode body = new DefaultMutableTreeNode();
            body.setUserObject(((Script)activity).getBody());
            body.setAllowsChildren(false);
            node.add(body);
          }               
          else if( activity instanceof For ) {
             node.setUserObject((For)activity);
             node.setAllowsChildren(true);
             node.add(activityTree(((For)activity).getActivity()));
          }
          else if( activity instanceof If ) {
          	If ifObj = (If)activity;
          	node.setUserObject(ifObj);
          	node.setAllowsChildren(true);
        	if (ifObj.getThen() != null)
        	{
        		DefaultMutableTreeNode n = new DefaultMutableTreeNode();
        		n.setUserObject(ifObj.getThen());
        		n.add(activityTree(ifObj.getThen().getActivity()));
        		n.setAllowsChildren(true);
        		node.add(n);        		
        	}
        	if (ifObj.getElse() != null) {
        		DefaultMutableTreeNode n = new DefaultMutableTreeNode();
        		n.setUserObject(ifObj.getElse());
        		n.add(activityTree(ifObj.getElse().getActivity()));
        		n.setAllowsChildren(true);
        		node.add(n);        		
        	}
          } 
          else if( activity instanceof Parfor ) {
             node.setUserObject((Parfor)activity);
             node.setAllowsChildren(true);
             node.add(activityTree(((Parfor)activity).getActivity()));
          }
          else if( activity instanceof Scope ) {
             node.setUserObject((Scope)activity);
             node.setAllowsChildren(true);
          }
          else if( activity instanceof Set ) {
          	  node.setAllowsChildren(false);
              node.setUserObject((Set)activity);
          }                    
          else if( activity instanceof Unset ) {
          	  node.setAllowsChildren(false);
              node.setUserObject((Unset)activity);
          }                        
          else if( activity instanceof While ) {
              node.setUserObject((While)activity);
              node.setAllowsChildren(true);
              node.add(activityTree(((While)activity).getActivity()));
          }                                                            
          else {
              logger.error( "unsupported Activity" ) ;
          } 
          return node;
      }
    


}       	


