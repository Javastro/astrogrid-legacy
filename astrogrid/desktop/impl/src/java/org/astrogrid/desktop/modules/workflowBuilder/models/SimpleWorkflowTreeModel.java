/*$Id: SimpleWorkflowTreeModel.java,v 1.6 2005/12/13 15:09:06 pjn3 Exp $
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

import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
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
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.StepExecutionRecord;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Unmarshaller;

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
	if (workflow.getJobExecutionRecord() != null)
		root.add(getJobExecutionRecord(workflow.getJobExecutionRecord()));
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
        if (((Step)activity).getStepExecutionRecordCount() > 0) {        	
        	node.add(getExecutionRecord(((Step)activity).getStepExecutionRecord()));
        }
      }
      else if( activity instanceof Script ) {
        node.setUserObject((Script)activity);            
        DefaultMutableTreeNode body = new DefaultMutableTreeNode();
        body.setUserObject(((Script)activity).getBody());
        body.setAllowsChildren(false);        
        node.add(body);
        if (((Script)activity).getStepExecutionRecordCount() > 0) {
        	body.setAllowsChildren(true);
        	node.add(getExecutionRecord(((Script)activity).getStepExecutionRecord()));
        }
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
         node.add(activityTree(((Scope)activity).getActivity()));
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

	public DefaultMutableTreeNode copyTree(DefaultMutableTreeNode orig) throws CastorException{
		//return (DefaultMutableTreeNode)orig.clone()
		// ok. first take a copy of the user objects. by marshalong and unmarshalling.
		AbstractActivity a = (AbstractActivity) orig.getUserObject();    
		StringWriter sw = new StringWriter();
		a.marshal(sw); 
		StringReader sr = new StringReader(sw.toString());
		AbstractActivity aCopy = (AbstractActivity)Unmarshaller.unmarshal(a.getClass(),sr); // this should work whatever the subclass of AbstractActivity that a is.
		//return deepClone(orig, aCopy);
		// now use the cloned activity tree to create a new tree of mutable treenodes - handily we've already got code for this..
		return activityTree(aCopy);
	}

	private DefaultMutableTreeNode getJobExecutionRecord(JobExecutionRecord jer) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();		
    	node.setUserObject(jer);
    	for (int i = 0; i < jer.getMessageCount(); i ++) {
    		DefaultMutableTreeNode message = new DefaultMutableTreeNode();
    		message.setUserObject(jer.getMessage(i));
        	node.add(message);	
    	}    			
		return node;
	}
	
	private DefaultMutableTreeNode getExecutionRecord(StepExecutionRecord[] records) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		for (int i = 0; i < records.length; i++ ) {			
    		node.setUserObject(records[i]);
    		for (int j = 0; j < records[i].getMessageCount(); j ++) {
    			DefaultMutableTreeNode message = new DefaultMutableTreeNode();
    			message.setUserObject(records[i].getMessage(j));
        		node.add(message);	
    		}    			
		}
		return node;
	}
	
	private DefaultMutableTreeNode getMessages(StepExecutionRecord record) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode();
		node.setUserObject(record);
		for (int i = 0; i < record.getMessageCount(); i++ ) {
			DefaultMutableTreeNode message = new DefaultMutableTreeNode();
			message.setUserObject(record.getMessage(i));
    		node.add(message);	
		}
		return node;
	}
	
}

/* 
$Log: SimpleWorkflowTreeModel.java,v $
Revision 1.6  2005/12/13 15:09:06  pjn3
Merge of pjn_workbench_8_12_05

Revision 1.5.8.1  2005/12/09 16:12:52  pjn3
Changed to handle workflow transcripts

Revision 1.5  2005/11/17 17:48:49  nw
different approach to making a tree copy.

Revision 1.4  2005/11/08 09:56:04  pjn3
branch pjn_workbench_2_11_05

Revision 1.3.6.1  2005/11/07 15:38:45  pjn3
initial approach to copying nodes

Revision 1.3  2005/10/12 13:30:10  nw
merged in fixes for 1_2_4_beta_1

Revision 1.1.6.1  2005/10/10 16:24:29  nw
reviewed phils workflow builder
skeletal javahelp

Revision 1.2  2005/09/29 17:16:40  pjn3
Drag and drop work complete 1322

Revision 1.1.2.1  2005/09/23 09:41:21  pjn3
First commit after CVS return - initial work

Revision 1.1  2005/09/12 18:53:45  nw
finished shaping workflow builder.
 
*/