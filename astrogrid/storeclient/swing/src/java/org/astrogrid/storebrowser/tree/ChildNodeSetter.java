/*
 * $Id: ChildNodeSetter.java,v 1.1 2005/04/01 01:54:56 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree;

import java.io.IOException;
import java.security.Principal;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.file.FileNode;
import org.astrogrid.storebrowser.swing.ChildLoadCompleter;

/** Swing components are not threadsafe, so adding children/making other changes
 have to be done through the EventQueue.  This class adds the given StoreFile children
 * as StoreFileNodes to the
 * given parent StoreFileNode and updates the model */

public class ChildNodeSetter extends ChildLoadCompleter {
   
   StoreFileNode parent;
   DefaultTreeModel model;
   Principal user;
   
   public ChildNodeSetter(StoreFileNode givenParent, DefaultTreeModel givenModel, Principal givenUser) {
      this.parent = givenParent;
      this.model = givenModel;
      this.user = givenUser;
      
      assert (parent != null) : "Parent is null";
   }
   
   /** Modifies Swing components, so only run from SwingUtilities.invokeLater() */
   public void run() {
      
      parent.removeAllChildren();
      
      if (childFiles != null) {

         parent.setChildren(childFiles);
      }
      if (model != null) model.reload(parent);
   }
   
}


