/*
 * $Id: StoreFileTreeModel.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.slinger.ui.swing.singlestore;

import java.io.IOException;
import java.security.Principal;
import javax.swing.JOptionPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.slinger.StoreFile;
import org.astrogrid.ui.TreeModelSupport;

/**
 * A TreeModel adaptor to a StoreFile tree, suitable for use by JTree
 *
 */

public class StoreFileTreeModel extends TreeModelSupport implements TreeModel {
   
   StoreFile root;
   Principal user;
   
   Log log = LogFactory.getLog(StoreFileTreeModel.class);
   
   public StoreFileTreeModel(StoreFile givenRoot, Principal givenUser) {
      this.root = givenRoot;
      this.user = givenUser;
   }
   
   public void setRoot(StoreFile newRoot) {
      root = newRoot;
      fireTreeStructureChanged(new TreeModelEvent(this, new TreePath("/")));
   }
   
   public void refresh(Principal user) throws IOException {
      root.refresh();
      fireTreeStructureChanged(new TreeModelEvent(this, new TreePath("/")));
   }
   /**
    * Returns the number of children of <code>parent</code>.
    * Returns 0 if the node
    * is a leaf or if it has no children.  <code>parent</code> must be a node
    * previously obtained from this data source.
    *
    * @param   parent  a node in the tree, obtained from this data source
    * @return  the number of children of the node <code>parent</code>
    */
   public int getChildCount(Object parent)  {
      try {
         StoreFile[] files = ((StoreFile) parent).listFiles(user);
         if (files != null) {
            return files.length;
         }
         else {
            return 0;
         }
      }
      catch (IOException ioe) {
         log.error(ioe+" reading children of "+parent, ioe);
         JOptionPane.showMessageDialog(null, ioe, "Error reading children of "+parent, JOptionPane.ERROR);
         return 0;
      }
      
   }
   
   /**
    * Returns the index of child in parent.  If <code>parent</code>
    * is <code>null</code> or <code>child</code> is <code>null</code>,
    * returns -1.
    *
    * @param parent a note in the tree, obtained from this data source
    * @param child the node we are interested in
    * @return the index of the child in the parent, or -1 if either
    *    <code>child</code> or <code>parent</code> are <code>null</code>
    */
   public int getIndexOfChild(Object parent, Object child) {
      try {
         if ((parent == null) || (child == null)) {
            return -1;
         } else {
            StoreFile[] files = ((StoreFile) parent).listFiles(user);
            
            for (int f=0;f<files.length;f++) {
               if (files[f].equals(child)) {
                  return f;
               }
            }
            return -1;
         }
      }
      catch (IOException ioe) {
         log.error(ioe+" finding child "+child+" of "+parent,ioe);
         JOptionPane.showMessageDialog(null, ioe, "Error finding child of "+parent, JOptionPane.ERROR);
         return -1;
      }
   }
   
   /**
    * Returns true if the storefile given is not a folder
    */
   public boolean isLeaf(Object storefile) {
      return !((StoreFile) storefile).isFolder();
   }
   
   /**
    * Returns the child of <code>parent</code> at index <code>index</code>
    * in the parent's
    * child array.  <code>parent</code> must be a node previously obtained
    * from this data source. This should not return <code>null</code>
    * if <code>index</code>
    * is a valid index for <code>parent</code> (that is <code>index >= 0 &&
    * index < getChildCount(parent</code>)).
    *
    * @param   parent  a node in the tree, obtained from this data source
    * @return  the child of <code>parent</code> at index <code>index</code>
    */
   public Object getChild(Object parent, int index) {
      try {
         return ((StoreFile) parent).listFiles(user)[index];
      }
      catch (IOException ioe) {
         log.error(ioe+" finding child "+index+" of "+parent,ioe);
         JOptionPane.showMessageDialog(null, ioe, "Error finding child of "+parent, JOptionPane.ERROR);
         return null;
      }
   }
   
   /**
    * Returns the root of the tree.  Returns <code>null</code>
    * only if the tree has no nodes.
    */
   public Object getRoot() {
      return root;
   }
   
   public void valueForPathChanged( TreePath path, Object newValue) {
      //doesn't happen (I don't think) - nodes are static
   }
   
}


/*
 $Log: StoreFileTreeModel.java,v $
 Revision 1.2  2004/12/07 01:33:36  jdt
 Merge from PAL_Itn07

 Revision 1.1.2.2  2004/11/29 21:49:25  mch
 fixed some log.error() calls

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */

