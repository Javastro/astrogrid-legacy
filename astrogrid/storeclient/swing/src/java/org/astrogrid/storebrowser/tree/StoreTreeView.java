/*
 * $Id: StoreTreeView.java,v 1.2 2005/03/28 03:28:49 mch Exp $
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
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.storeclient.api.StoreFile;

/**
 * Shows a store file tree.
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
   public StoreFile getSelectedFile()
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

   /** 'Reloads' - actually reorganises the tree after the node has been modified- called when eg files are added/deleted */
   public void reload(TreeNode node) {
      ((DefaultTreeModel) getModel()).reload(node);
   }
   
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
   
   /** Not the right way to do this, but we want to check to see if a node has
    * been expanded and if so refresh all items in it */
   public void fireTreeExpanded(TreePath path) {
      super.fireTreeExpanded(path);
      StoreFileNode node = (StoreFileNode) path.getLastPathComponent();
      for (int i = 0; i < node.getChildCount(); i++) {
         StoreFileNode child = (StoreFileNode) node.getChildAt(i);
         if (!child.isLeaf()) child.refresh();
      }
   }
      
   /*
   public String convertValueToText(Object value, boolean selected,
                                    boolean expanded, boolean leaf, int row,
                                    boolean hasFocus) {
      
      //remember that StoreNode subclases StoreFileNode so we must check it first...
      if (value instanceof StoreNode) {
         return ((StoreNode) value).getName();
      }
      else if (value instanceof StoreFileNode) {
//         try {
            return ((StoreFileNode) value).getName();
//         }
//         catch (URISyntaxException use) {
//            ((StoreFileNode) value).setError(use);
//            return use.getMessage();
//         }
//         catch (IOException ioe) {
//            ((StoreFileNode) value).setError(ioe);
//            return ioe.getMessage();
//         }
      }
      else {
         return value.toString();
      }
   }
    */
}

/*
 $Log: StoreTreeView.java,v $
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

