/*
 * $Id: NodeFileAdder.java,v 1.1 2005/03/29 20:13:51 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree;

import java.io.IOException;
import java.security.Principal;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.storeclient.api.StoreFile;

/** Swing components are not threadsafe, so adding children/making other changes
 have to be done through the EventQueue.  This class adds the given StoreFile children
 * as StoreFileNodes to the
 * given parent StoreFileNode and updates the model */

public class NodeFileAdder implements Runnable {
   
   StoreFileNode parent;
   StoreFile[] children;
   DefaultTreeModel model;
   Principal user;
   
   public NodeFileAdder(StoreFileNode givenParent, StoreFile[] givenChildren, DefaultTreeModel givenModel, Principal givenUser) {
      this.parent = givenParent;
      this.children = givenChildren;
      this.model = givenModel;
      this.user = givenUser;
      
      assert (parent != null) : "Parent is null";
   }
   
   /** Modifies Swing components, so only run from SwingUtilities.invokeLater() */
   public void run() {
      
      if (children != null) {
         
         for (int i = 0; i < children.length; i++) {
            //LogFactory.getLog(NodeFileAdder.class).debug("Adding "+children[i]+" to "+parent);
            
            try {
               StoreFileNode childNode = new StoreFileNode(model, parent, children[i], user);
               
               //bit of a botch - make sure child doesn't refer to parent before inserting
               childNode.setParent(null);
               
               //insert
               parent.add(childNode);
            }
            catch (IOException e) {
               LogFactory.getLog(NodeFileAdder.class).error("Creating child node for "+children[i],e);
            }
            
         }
      }
      if (model != null) model.reload(parent);
   }
   
}


