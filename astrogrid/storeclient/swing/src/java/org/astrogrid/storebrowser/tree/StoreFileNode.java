/*
 * $Id: StoreFileNode.java,v 1.1 2005/03/28 02:06:35 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree;

import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.storeclient.api.StoreFile;

/**
 * StoreFile adaptor for JTree views (or similar).
 *
 */

public class StoreFileNode extends DefaultMutableTreeNode implements TreeNode {

   Log log = LogFactory.getLog(StoreFileNode.class);
   
   StoreFile file = null;
   Principal user = null;
   
   Throwable lastError = null;
   
   boolean loading = false;
   
   DefaultTreeModel model = null; //used to update when children are loaded
   
   public StoreFileNode(DefaultTreeModel givenModel, MutableTreeNode givenParent, StoreFile aFile, Principal aUser) throws IOException {
      this.parent = givenParent;
      this.file = aFile;
      this.user = aUser;
      this.children = new Vector(); //empty
      this.model = givenModel;
   }
   
   public StoreFile getFile()  {
      return file;
   }

   public String getName() {
      return file.getName();
   }

   public Principal getUser() {
      return user;
   }
   
   
   /** Returns the *node* path, which may be longer than the file path as it may
    include servers, etc beyond the root of the file */
   public String getFilePath() {
      if (getParent() == null) {
         return "/";
      }
      else {
         return ((StoreFileNode) getParent()).getPath()+"/"+getName();
      }
   }

   /**
    * Returns true if the file is still being loaded */
   public boolean isLoading() {
      return loading;
   }
   
   /**
    * Returns the number of children <code>TreeNode</code>s the receiver
    * contains.
    */
   public int getChildCount() {
      return children.size();
   }

   /** Refresh - clears the children and file's children */
   public void refresh() {
      if ((!loading) && (file != null) && (file.isFolder())) {
         clearError();
         loadChildren();
      }
   }

   /** Loads the list of child files/folders from the remote service into this
    * instance.  At the moment this is a blocking call, but really
    * it ought to spawn a thread that retrieves the children one by one and
    * adds them in so that the UI doesn't halt for slow services
    */
   protected void loadChildren()  {
      children = new Vector();
      Thread loadingThread = new Thread(new ChildrenLoader(this));
      loading = true;
      loadingThread.start();
   }
   
   /** Loads single child from given StoreFile.  This is called when updating
    * this TreeNode wrapper with new data from the remote server, so it must also
    * update any suitable model listeners.
    * At the moment just adds to the children
    */
   protected void loadChild(StoreFile newFile) throws IOException {
      log.debug("Adding "+newFile+" to "+this);
      children.add(new StoreFileNode(model, this, newFile, user));
      if (model != null) model.reload(this);
      
   }
/** Runnable that loads the children from the remote service into this instance */

public class ChildrenLoader implements Runnable {
      
      StoreFileNode node = null;
      
      public ChildrenLoader(StoreFileNode givenNode) {
         this.node = givenNode;
      }
            public void run() {
               try {
                  node.getFile().refresh();
                  StoreFile[] childFiles = node.getFile().listFiles(node.getUser());
                  if (childFiles != null) {
                     for (int i = 0; i < childFiles.length; i++) {
                        try {
                           node.loadChild(childFiles[i]);
                        }
                        catch (IOException e) {
                           System.out.println(e+" loading "+childFiles[i]); //temporary, should log
                           e.printStackTrace(System.out);
                        }
                     }
                  }
               }
               catch (IOException e) {
                  System.out.println(e+" loading children of "+this); //temporary, should log
                  e.printStackTrace(System.out);
               }
               node.loading = false;
               if (model != null) model.nodeChanged(node);
            }
      
   }
   
   
   /**
    * Returns the child <code>TreeNode</code> at index
    * <code>childIndex</code>.
    */
   public TreeNode getChildAt(int childIndex) {
      return (TreeNode) children.get(childIndex);
   }
   
   /**
    * Returns the parent <code>TreeNode</code> of the receiver.
    */
   public TreeNode getParent() {
      return parent;
   }
   
   /**
    * Returns true if the receiver is a leaf.
    */
   public boolean isLeaf() {
      return !file.isFolder();
   }
   
   /**
    * Returns the children of the receiver as an <code>Enumeration</code>.
    */
   public Enumeration children() {
      return children.elements();
   }
   
   /**
    * Returns the index of <code>node</code> in the receivers children.
    * If the receiver does not contain <code>node</code>, -1 will be
    * returned.
    */
   public int getIndex(TreeNode node) {
      return children.indexOf(node);
   }
   
   /**
    * Returns true if the receiver allows children.
    */
   public boolean getAllowsChildren() {
      return true;
   }
   
   /** Stores last error so we can display it;  Recording errors rather than
    * throwing exceptions allows the display to keep functioning even if
    one of the servers goes down (or the interface changes and everything fails horribly) */
   public void setError(Throwable th) {
      LogFactory.getLog(StoreFileNode.class).error("",th);
      lastError = th;
   }

   protected void clearError() {
      lastError = null;
   }
   
   public Throwable getError() {
      return lastError;
   }
}
