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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * @author pjn3
 *
 * Handles the registration of tree model listeners and event notifications
 * @modified nww - replaced vector with a set - then guaranteed to have no duplicates.
 */
public class WorkflowTreeModelSupport {
	   private Collection vector = new HashSet();

	   public void addTreeModelListener( TreeModelListener listener ) {
	      if ( listener != null ) {
	         vector.add( listener );
	      }
	   }

	   public void removeTreeModelListener( TreeModelListener listener ) {
	      if ( listener != null ) {
	         vector.remove( listener );
	      }
	   }

	   public void fireTreeNodesChanged( TreeModelEvent e ) {
	      Iterator listeners = vector.iterator();
	      while ( listeners.hasNext() ) {
	         TreeModelListener listener = (TreeModelListener)listeners.next();
	         listener.treeNodesChanged( e );
	      }
	   }

	   public void fireTreeNodesInserted( TreeModelEvent e ) {
              Iterator listeners = vector.iterator();
              while ( listeners.hasNext() ) {
	         TreeModelListener listener = (TreeModelListener)listeners.next();
	         listener.treeNodesInserted( e );
	      }
	   }

	   public void fireTreeNodesRemoved( TreeModelEvent e ) {
              Iterator listeners = vector.iterator();
              while ( listeners.hasNext() ) {
	         TreeModelListener listener = (TreeModelListener)listeners.next();
	         listener.treeNodesRemoved( e );
	      }
	   }

	   public void fireTreeStructureChanged( TreeModelEvent e ) {
              Iterator listeners = vector.iterator();
              while ( listeners.hasNext() ) {
	         TreeModelListener listener = (TreeModelListener)listeners.next();
	         listener.treeStructureChanged( e );
	      }
	   }

}
