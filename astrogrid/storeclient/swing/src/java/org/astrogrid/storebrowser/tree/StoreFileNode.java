/*
 * $Id: StoreFileNode.java,v 1.5 2005/04/01 01:54:56 mch Exp $
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
import javax.swing.tree.TreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.file.FileNode;
import org.astrogrid.storebrowser.swing.ChildrenLoader;

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
   
   FileNode file = null;
   Principal user = null;
   
   Throwable lastError = null;
   
//   ChildrenLoader loading = null;
   
   DefaultTreeModel model = null; //used to update when children are loaded

   /* -1 = no children loaded, 0 = loading, 1 = loaded.  By
   using just the one flag and having it changed in one place only we can stop
   threading problems.  Note that the loading thread changes it, but the loading
    thread cna only be created in one place */
   private int completeness = -1;
   
   public StoreFileNode(DefaultTreeModel givenModel, MutableTreeNode givenParent, FileNode aFile, Principal aUser) throws IOException {
      this.parent = givenParent;
      this.file = aFile;
      this.user = aUser;
      this.model = givenModel;
      children = new Vector();
   }
   
   public FileNode getFile()  {
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
      return (completeness == 0);
   }
   
   /**
    * Returns the number of children <code>TreeNode</code>s the receiver
    * contains.
    */
   public synchronized int getChildCount() {
      if (completeness == -1) {
         startLoadChildren();
      }
      return children.size();
   }

   /** Refresh = clears the children and reloads them */
   public void refresh() {
      startLoadChildren();
   }

   /** Override so we can order it properly */
   public void add(MutableTreeNode newChild) {

      if (children == null) {
         insert(newChild,0);
      }
      else {
         //find insert position - don't like this, doubt it's threadsafe
         Enumeration e = children.elements();
         int i = 0;
         while (e.hasMoreElements() &&  ((StoreFileNode) e.nextElement()).compareTo( (StoreFileNode) newChild) < 0 ) {
            i++;
         }
         insert(newChild, i);
      }
   }

   /** Returns < 0 if this file should be placed before the given file in
    * directory lists, 0 if they are equal and > 0 if it should be placed after */
   public int compareTo(StoreFileNode node) {
      if (file.isFolder() && (!node.file.isFolder())) {
         return -1;
      }
      if (!file.isFolder() && (node.file.isFolder())) {
         return 1;
      }
      //otherwise by name comparison
//    return (file.getName().compareTo(node.file.getName()));
      //otherwise by caseless name comparison
      return (file.getName().toLowerCase().compareTo(node.file.getName().toLowerCase()));
   }
   
   /** Refresh - clears the children and file's children. Synchronised as this
    * should be the only public method that modifies the completeness flag  */
   public synchronized void startLoadChildren() {

      if ( completeness != 0)  { //only if not already loading
         completeness = 0;
         clearError();
      
         if ((file != null) && (file.isFolder())) {
            ChildrenLoader loading = new ChildrenLoader(file, new ChildNodeSetter(this, model, user));
            Thread loadingThread = new Thread(loading);
            loadingThread.start();
         }
      }
   }

   public void setChildren(FileNode[] childFiles) {
         for (int i = 0; i < childFiles.length; i++) {
            //LogFactory.getLog(NodeFileAdder.class).debug("Adding "+children[i]+" to "+parent);
            
            try {
               StoreFileNode childNode = new StoreFileNode(model, parent, childFiles[i], user);
               
               //bit of a botch - make sure child doesn't refer to parent before inserting
               childNode.setParent(null);
               
               //insert
               add(childNode);
            }
            catch (IOException e) {
               LogFactory.getLog(ChildNodeSetter.class).error("Creating child node for "+childFiles[i],e);
            }
            
         }
         completeness = 1;
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
    *
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
