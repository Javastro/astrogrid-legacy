/* WorkflowTreeModelSupport.java
 * Created on 17-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.workflowBuilder.models;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * @author pjn3
 *
 * Handles the registration of tree model listeners and event notifications
 */
public class WorkflowTreeModelSupport {
	   private Vector vector = new Vector();

	   public void addTreeModelListener( TreeModelListener listener ) {
	      if ( listener != null && !vector.contains( listener ) ) {
	         vector.addElement( listener );
	      }
	   }

	   public void removeTreeModelListener( TreeModelListener listener ) {
	      if ( listener != null ) {
	         vector.removeElement( listener );
	      }
	   }

	   public void fireTreeNodesChanged( TreeModelEvent e ) {
	      Enumeration listeners = vector.elements();
	      while ( listeners.hasMoreElements() ) {
	         TreeModelListener listener = (TreeModelListener)listeners.nextElement();
	         listener.treeNodesChanged( e );
	      }
	   }

	   public void fireTreeNodesInserted( TreeModelEvent e ) {
	      Enumeration listeners = vector.elements();
	      while ( listeners.hasMoreElements() ) {
	         TreeModelListener listener = (TreeModelListener)listeners.nextElement();
	         listener.treeNodesInserted( e );
	      }
	   }

	   public void fireTreeNodesRemoved( TreeModelEvent e ) {
	      Enumeration listeners = vector.elements();
	      while ( listeners.hasMoreElements() ) {
	         TreeModelListener listener = (TreeModelListener)listeners.nextElement();
	         listener.treeNodesRemoved( e );
	      }
	   }

	   public void fireTreeStructureChanged( TreeModelEvent e ) {
	      Enumeration listeners = vector.elements();
	      while ( listeners.hasMoreElements() ) {
	         TreeModelListener listener = (TreeModelListener)listeners.nextElement();
	         listener.treeStructureChanged( e );
	      }
	   }

}
