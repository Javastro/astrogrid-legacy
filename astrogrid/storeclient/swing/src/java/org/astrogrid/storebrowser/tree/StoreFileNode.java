/*
 * $Id: StoreFileNode.java,v 1.3 2005/03/29 20:13:51 mch Exp $
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
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.storeclient.api.StoreFile;

/**
 * StoreFile adaptor for JTree views (or similar).
 *
 * This implements a threaded child loader, so that the UI doesn't freeze while
 * we wait for remote services to respond.  This requires some rather careful
 * threadsafing; I haven't done this formally, but we have to be very careful
 * with refreshing.  For the moment I've assumed that any reload has to wait
 * for the previous one to finish, otherwise it can get very missing ensuring the
 * loading thread we have a handle to is the same as the one we had a moment ago
 */

public class StoreFileNode extends DefaultMutableTreeNode implements TreeNode {

   Log log = LogFactory.getLog(StoreFileNode.class);
   
   StoreFile file = null;
   Principal user = null;
   
   Throwable lastError = null;
   
   ChildrenLoader loading = null;
   
   DefaultTreeModel model = null; //used to update when children are loaded
   
   public StoreFileNode(DefaultTreeModel givenModel, MutableTreeNode givenParent, StoreFile aFile, Principal aUser) throws IOException {
      this.parent = givenParent;
      this.file = aFile;
      this.user = aUser;
      this.children = null; //means not loaded yet
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
      return loading != null;
   }
   
   /**
    * Returns the number of children <code>TreeNode</code>s the receiver
    * contains.
    */
   public int getChildCount() {
      if (children == null) {
         loadChildren();
      }
      return children.size();
   }

   /** Refresh = clears the children and reloads them */
   public void refresh() {
      loadChildren();
   }
   
   /** Refresh - clears the children and file's children. Synchronised as this
    * should be the only public method that modifies the 'loading' flag*/
   public synchronized void loadChildren() {

      clearError();
      children = new Vector();
      
      if (loading != null) {
         loading.abort(); //so we can stop it and start it again
      }
      else {
         if ((file != null) && (file.isFolder())) {
            loading = new ChildrenLoader(this);
            Thread loadingThread = new Thread(loading);
            loadingThread.start();
         }
      }
   }

   /** Loads single child from given StoreFile.  This is called when updating
    * this TreeNode wrapper with new data from the remote server, so it must also
    * update any suitable model listeners.
    * At the moment just adds to the children
    *
   protected void loadChild(StoreFile newFile) throws IOException {
      log.debug("Adding "+newFile+" to "+this);
      children.add(new StoreFileNode(model, this, newFile, user));
      if (model != null) model.reload(this);
      
   }
   
   /** Runnable that loads the children from the remote service into this instance */
   public class ChildrenLoader implements Runnable {
      
      StoreFileNode node = null;
      boolean aborted = false;
      
      public ChildrenLoader(StoreFileNode givenNode) {
         this.node = givenNode;
      }
      
      public void run() {
         try {
            if (node.file != null) {
               log.debug("Reading file list for "+node.file.getPath()+"...");
               //this is the bit that might take some time
               node.getFile().refresh();
               StoreFile[] childFiles = node.getFile().listFiles(node.getUser());
               if (childFiles != null) {
                  log.debug("...found "+childFiles.length+" files for "+node.file.getPath());
                  //so we have the results, now add them in to the model/node
                  if (!aborted) {
                     SwingUtilities.invokeLater(new NodeFileAdder(node, childFiles, node.model, user));
                  }
               }
               else {
                  log.debug("...found no files for "+node.file.getPath());
               }
            }
         }
         catch (Throwable e) {
            node.setError(e+" loading children of "+this, e);
         }
         node.loading = null; //disengage from node
         if (node.model != null) {
            SwingUtilities.invokeLater(new NodeChanger(node, node.model));
         }
      }
      
      /** Call to abort the update */
      public void abort() {
         aborted = true;
         log.debug("Aborting load for "+node);
      }
   }


   /** Simple class that makes the appropriate Swing component calls to update the
    * display as files come in.  Use SwingUtilities.invokeLater() to call it, so
    * that it's run from the GUI thread, as Swing components are not threadsafe */
   public class NodeChanger implements Runnable {

      TreeNode node;
      DefaultTreeModel model;
      
      public NodeChanger(TreeNode givenNode, DefaultTreeModel givenModel) {
         this.node = givenNode;
         this.model = givenModel;
         
         assert (node != null) : "Node is null";
         assert (model != null) : "Model is null";
      }
      
      public void run() {
         model.nodeChanged(node);
      }
      
   }
   
   
   
   
   /** When we close down this object, we also want to close down any
    * updating that might be going on.  We could make this more explicit...
    */
   public void finalize() {
      try {
         loading.abort();
      }
      catch (NullPointerException npe) {
         //not loading - never mind eh? We could test for loading == null but it might be made null between the check and the call
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
   public void setError(String message, Throwable th) {
      LogFactory.getLog(StoreFileNode.class).error(message,th);
      lastError = th;
   }

   protected void clearError() {
      lastError = null;
   }
   
   public Throwable getError() {
      return lastError;
   }
}
