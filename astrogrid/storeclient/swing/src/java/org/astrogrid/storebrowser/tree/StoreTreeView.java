/*
 * $Id: StoreTreeView.java,v 1.5 2005/03/31 19:25:39 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree;

import java.io.IOException;
import java.security.Principal;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.file.FileNode;

/**
 * A Tree view compnonent for displaying and exploring store file trees
 *
 */

public class StoreTreeView extends JTree {

   //the operator of this view, not (necessarily) the owner of the files
   Principal operator = null;
   
   Log log = LogFactory.getLog(StoreTreeView.class);
   
   public StoreTreeView(Principal aUser) throws IOException {
      super(new DirectoryTreeModel(new StoresList(aUser)));
      
      ((StoresList) getModel().getRoot()).setModel( (DefaultTreeModel) getModel());
      
      this.operator = aUser;
      setShowsRootHandles(true);
      setRootVisible(false);
      
      setCellRenderer(new StoreNodeTreeCellRenderer());
   }
   
   /** Convenience routine for returning the selected StoreFile rather than tree node */
   public FileNode getSelectedFile()
   {
      if (getSelectionPath() == null)  {
         return null;
      } else {
         Object node = getSelectionPath().getLastPathComponent();
         if (node instanceof StoreFileNode) {
            return ((StoreFileNode) node).getFile();
         }
         return null;
      }
   }

   /** Redisplays the given node after the node has been modified- called when eg files are added/deleted */
   public void review(TreeNode node) {
      ((DefaultTreeModel) getModel()).reload(node);
   }

   /** Convenience routine - returns the currently selected node as a StoreFileNode */
   public StoreFileNode getSelectedNode() {
      if (getSelectionPath() == null)  {
         return null;
      } else {
         Object node = getSelectionPath().getLastPathComponent();
         if (node instanceof StoreFileNode) {
            return ((StoreFileNode) node);
         }
         return null;
      }
   }
   
}

/*
 $Log: StoreTreeView.java,v $
 Revision 1.5  2005/03/31 19:25:39  mch
 semi fixed a few threading things, introduced sort order to tree

 Revision 1.4  2005/03/30 11:01:43  mch
 Minor UI changes

 Revision 1.3  2005/03/29 20:13:51  mch
 Got threading working safely at last

 Revision 1.2  2005/03/28 03:28:49  mch
 Some fixes for threadsafety

 Revision 1.1  2005/03/28 02:06:35  mch
 Major lump: split picker and browser and added threading to seperate UI interations from server interactions

 Revision 1.1.1.1  2005/02/16 19:57:09  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 15:02:46  mch
 Initial Checkin

 Revision 1.1.2.1  2005/01/26 14:48:06  mch
 Separating slinger and scapi

 Revision 1.1.2.2  2004/11/25 01:28:59  mch
 Added mime type to outputchild

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package



 */

