/*
 * $Id: StoreRootNode.java,v 1.3 2005/03/29 20:13:51 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.storebrowser.tree;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.astrogrid.storeclient.api.StoreFileResolver;

/**
 * Represents the root of a store service (eg homespace, myspace, ftp, disk, etc) which
 * also corresponds to some concept of 'the store' and so may be displayed slightly
 * differently.
 *
 */

public class StoreRootNode extends StoreFileNode {
   
   String name = null;
   String uri = null;
   
   public StoreRootNode(DefaultTreeModel model, StoresList root, String storeName, String storeUri, Principal aUser) throws IOException {
      super(model, root, null, aUser);
      this.name = storeName;
      this.uri = storeUri;
   }

   /** Spawns a thread so that the display can work while we connect.  This is not
    * properly threadsafe - it's OK as the treview is written 'just now', ie make
    * sure you can't call refresh while calling this... */
   public void connect() {

      loading = new InitialConnector(this);
      Thread loadingThread = new Thread(loading);
      loadingThread.start();
   }
   
   /** Thread that connects and loads first children */
   public class InitialConnector extends StoreFileNode.ChildrenLoader {
      
      public InitialConnector(StoreRootNode givenNode) {
         super(givenNode);
      }
      
      public void run() {
         try {
            node.file = StoreFileResolver.resolveStoreFile(uri, user);
         }
         catch (URISyntaxException e) {
            setError(e+" resolving storefile at "+uri, e);
         }
         catch (IOException e) {
            setError(e+" resolving storefile at "+uri, e);
         }
         SwingUtilities.invokeLater(new NodeChanger(node, node.model));
         super.run();
      }
   }
   
   public String getUri() {
      return uri;
   }
   
   public String getName() {
      return name;
   }

   /**
    * Returns true if the receiver is a leaf.
    */
   public boolean isLeaf() {
      return false;
   }
   
   
   /**
    * Returns true if the receiver allows children.
    */
   public boolean getAllowsChildren() {
      return true;
   }
   
   
}
