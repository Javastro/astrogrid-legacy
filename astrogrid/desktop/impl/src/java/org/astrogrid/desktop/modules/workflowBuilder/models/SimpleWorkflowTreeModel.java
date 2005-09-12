/*$Id: SimpleWorkflowTreeModel.java,v 1.1 2005/09/12 18:53:45 nw Exp $
 * Created on 12-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.workflowBuilder.models;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * simple model, based on default tree model
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Sep-2005
 *
 */
public class SimpleWorkflowTreeModel extends DefaultTreeModel {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SimpleWorkflowTreeModel.class);

/** Construct a new WorkflowTreeModel
     * @param root
     */
    public SimpleWorkflowTreeModel() {
        super(null);
    }

/** set the tree to display a new workflow */
public void setWorkflow(Workflow wf) {
    this.wf = wf;
    this.setRoot(createTree(wf)); // assume this fires notifications of.
}

private Workflow wf;

public Workflow getWorkflow() {
    return wf;
}

private DefaultMutableTreeNode createTree(Workflow workflow) {
	DefaultMutableTreeNode root = new DefaultMutableTreeNode();
	root.setUserObject(workflow);
	root.add(activityTree(workflow.getSequence()));
	root.setAllowsChildren(true);
    return(root);
}

private DefaultMutableTreeNode activityTree(Tool t) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode();
    node.setUserObject(t);
    node.setAllowsChildren(false);
    return node;
}
private DefaultMutableTreeNode activityTree( AbstractActivity activity ) { 
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
        node.setAllowsChildren(true);
        Tool t= ((Step)activity).getTool();
        node.add(activityTree(t));
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

/* 
$Log: SimpleWorkflowTreeModel.java,v $
Revision 1.1  2005/09/12 18:53:45  nw
finished shaping workflow builder.
 
*/