/*
 * $Id: StoreFileNode.java,v 1.1 2005/02/16 15:02:46 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.swing.models;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.TreeNode;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.storeclient.api.StoreFile;

/**
 * StoreFile adaptor for JTree views (or similar).
 *
 */

public class StoreFileNode implements TreeNode {
   
   StoreFile file = null;
   Principal user = null;
   
   Throwable lastError = null;
   
   StoreFileNode parentNode = null;
   Vector childNodes = null; //null=children need to be loaded
   
   public StoreFileNode(StoreFile aFile, Principal aUser) {
      this.file = aFile;
      this.user = aUser;
   }

   public StoreFileNode(StoreFileNode givenParent, StoreFile aFile, Principal aUser) {
      this.parentNode = givenParent;
      this.file = aFile;
      this.user = aUser;
   }
   
   public StoreFile getFile() throws IOException, URISyntaxException {
      return file;
   }

   public String getName() {
      return file.getName();
   }

   /** Returns the *node* path, which may be longer than the file path as it may
    include servers, etc beyond the root of the file */
   public String getPath() {
      if (getParent() == null) {
         return "/";
      }
      else {
         return ((StoreFileNode) getParent()).getPath()+"/"+getName();
      }
   }
   /**
    * Returns the number of children <code>TreeNode</code>s the receiver
    * contains.
    */
   public int getChildCount() {
      clearError();
      try {
         return getChildNodes().length;
      }
      catch (Throwable th) {
         setError(th);
         return 0;
      }
   }

   /** Refresh - clears the children and file's children */
   public void refresh() throws IOException, URISyntaxException {
      getFile().refresh();
      childNodes = null;
   }
   
   public StoreFileNode[] getChildNodes() throws IOException, URISyntaxException {
      if (childNodes == null) {
         if (getFile() == null) {
            throw new RuntimeException("Node '"+getPath()+"' has no node children and no file");
         }
         StoreFile[] childFiles = getFile().listFiles(user);
         if (childFiles == null) {
            childNodes = new Vector();
         }
         else {
            childNodes = new Vector();
            for (int i = 0; i < childFiles.length; i++) {
               childNodes.add( new StoreFileNode(this, childFiles[i], user));
            }
         }
      }
      return (StoreFileNode[]) childNodes.toArray( new StoreFileNode[] {});
         
   }
   
   
   /**
    * Returns the child <code>TreeNode</code> at index
    * <code>childIndex</code>.
    */
   public TreeNode getChildAt(int childIndex) {
      clearError();
      try {
         return getChildNodes()[childIndex];
      }
      catch (Throwable th) {
         setError(th);
         return null;
      }
   }
   
   /**
    * Returns the parent <code>TreeNode</code> of the receiver.
    */
   public TreeNode getParent() {
      try {
         if ((parentNode == null) && (getFile().getParent(user) != null)) {
            clearError();
            parentNode = new StoreFileNode(getFile().getParent(user), user);
         }
         return parentNode;
      }
      catch (Throwable th) {
         setError(th);
         return null;
      }
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
      return childNodes.elements();
   }
   
   /**
    * Returns the index of <code>node</code> in the receivers children.
    * If the receiver does not contain <code>node</code>, -1 will be
    * returned.
    */
   public int getIndex(TreeNode node) {
      clearError();
      try {
         getChildNodes();
         return childNodes.indexOf(node);
      }
      catch (Throwable th) {
         setError(th);
         return -1;
      }
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
