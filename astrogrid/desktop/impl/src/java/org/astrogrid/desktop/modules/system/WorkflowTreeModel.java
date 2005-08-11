/* WorkflowTreeModel.java
 * Created on 01-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.workflow.beans.v1.Else;
import org.astrogrid.workflow.beans.v1.Flow;
import org.astrogrid.workflow.beans.v1.For;
import org.astrogrid.workflow.beans.v1.If;
import org.astrogrid.workflow.beans.v1.Parfor;
import org.astrogrid.workflow.beans.v1.Scope;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Then;
import org.astrogrid.workflow.beans.v1.While;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * tree model for Workflow.
 * @todo move this out of the system module.
 * @author Phil Nicolson pjn3@star.le.ac.uk 2/7/05
 *  
 */
public class WorkflowTreeModel implements TreeModel {
	
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(WorkflowTreeModel.class);
    
    private Workflow rootWorkflow;	

	/**
	 * 
	 */
	public WorkflowTreeModel(Workflow root) {
		rootWorkflow = root;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot() {
		return rootWorkflow;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent) {
		int i = 0;
		if(parent instanceof Workflow) {
			i = 1;
		}
		else if(parent instanceof Sequence) {
			Sequence s = (Sequence)parent;
			i = s.getActivityCount();
		}
		else if(parent instanceof Flow) {
			Flow f = (Flow)parent;
			i = f.getActivityCount();
		}
		else if(parent instanceof For) {
			For f = (For)parent;
			Sequence s = (Sequence)f.getActivity();
			i = s.getActivityCount();
		}
		else if (parent instanceof If) {
			If a = (If)parent;
			if (a.getElse() != null && a.getElse() instanceof Else) 
				i = i + 1;
			if (a.getThen() != null && a.getThen() instanceof Then)
				i = i + 1;
		}
		else if (parent instanceof Else || parent instanceof Then) {
	        i = 1;
		}
		else if (parent instanceof For || parent instanceof Parfor || parent instanceof While) {
			i = 1;
		}
		else if (parent instanceof Scope) {
			i = 1;
		}
		return i;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node) {
		boolean b = true;
		if (node instanceof Sequence || node instanceof Flow) {
			b = false;
		}
		else if (node instanceof If || node instanceof Else || node instanceof Then) {
			b = false;
		}
		else if (node instanceof For || node instanceof Parfor || node instanceof While) {
			b = false;
		}
		else if (node instanceof Scope) {
			b = false;
		}
		else if (node instanceof Workflow) {
			b = false;
		}
		return b;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void addTreeModelListener(TreeModelListener l) {

	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void removeTreeModelListener(TreeModelListener l) {

	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index) {
		Object ob = null;
		if (parent instanceof Workflow){
			Workflow w = (Workflow)parent;
			ob = w.getSequence();
		}
		else if(parent instanceof Sequence) {
			Sequence s = (Sequence)parent;
			ob = s.getActivity(index);
		}
		else if(parent instanceof Flow) {
			Flow f = (Flow)parent;
			ob = f.getActivity(index);
		}
		else if (parent instanceof For) {
			For f = (For)parent;
			Sequence s = (Sequence)f.getActivity();
			ob = s.getActivity(index);
		}
		else if (parent instanceof Parfor) {
			Parfor p = (Parfor)parent;
			ob = p.getActivity();
		}
		else if (parent instanceof Scope) {
			Scope s = (Scope)parent;
			ob = s.getActivity();
		}
		else if (parent instanceof While) {
			While w = (While)parent;
			ob = w.getActivity();
		}
		else if (parent instanceof If) {
			If i = (If)parent;
			if (index == 0) {
				if ((i.getThen() != null) && (i.getThen() instanceof Then)){
					ob = i.getThen();
				}
				else if ((i.getElse() != null) && (i.getElse() instanceof Else)){
					ob = i.getElse();
				}
			}
			if (index == 1) {
				if ((i.getElse() != null) && (i.getElse() instanceof Else)){
					ob = i.getElse();
				}
				else if ((i.getThen() != null) && (i.getThen() instanceof Then)){
					ob = i.getThen();
				}
			}
		}		
		else if (parent instanceof Else) {
			Else e = (Else)parent;			
			ob = e.getActivity();			
		}
		else if (parent instanceof Then) {
			Then t = (Then)parent;			
			ob = t.getActivity();			
		}				
		return ob;
	}


	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child) {
		logger.debug("getIndexOfChild");
		// TODO Auto-generated method stub
		return -1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}
}
