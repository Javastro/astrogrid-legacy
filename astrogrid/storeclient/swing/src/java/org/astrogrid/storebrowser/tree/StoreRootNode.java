/*
 * $Id: StoreRootNode.java,v 1.1 2005/03/28 02:06:35 mch Exp $
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
import javax.swing.tree.DefaultTreeModel;
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

   /** Spawns a thread so that the display can work while we connect */
   public void connect() {
      
      Thread loadingThread = new Thread(new InitialConnector(this));
      loading = true;
      loadingThread.start();
   }
   
   /** Thread that connects and loads first children */
   public class InitialConnector implements Runnable {
      
      StoreRootNode node = null;
      
      public InitialConnector(StoreRootNode givenNode) {
         this.node = givenNode;
      }
      
      public void run() {
         try {
            node.file = StoreFileResolver.resolveStoreFile(uri, user);
            node.model.nodeChanged(node);
            node.loadChildren();
         }
         catch (URISyntaxException e) {
            setError(e);
         }
         catch (IOException e) {
            setError(e);
         }
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
