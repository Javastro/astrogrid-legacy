/*
 * $Id: StoreRootNode.java,v 1.5 2005/04/01 01:54:56 mch Exp $
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
import org.astrogrid.storebrowser.swing.ChildrenLoader;
import org.astrogrid.storeclient.StoreFileResolver;

/**
 * Represents the root of a store service (eg homespace, myspace, ftp, disk, etc) which
 * also corresponds to some concept of 'the store' and so may be displayed slightly
 * differently.
 *
 */

public class StoreRootNode extends StoreFileNode {
   
   String name = null;
   String uri = null;
   
   boolean connected = false;
   
   public StoreRootNode(DefaultTreeModel model, StoresList root, String storeName, String storeUri, Principal aUser) throws IOException {
      super(model, root, null, aUser);
      this.name = storeName;
      this.uri = storeUri;
   }

   /** Spawns a thread so that the display can work while we connect.  This is not
    * properly threadsafe - it's OK as the treview is written 'just now', ie make
    * sure you can't call refresh while calling this... */
   public synchronized void connect() {

      if (!connected) {
         connected = true;
         InitialConnector loading = new InitialConnector(this);
         Thread loadingThread = new Thread(loading);
         loadingThread.start();
      }
      
   }
   
   /** Thread that connects and loads first children */
   public class InitialConnector implements Runnable {
      
      StoreFileNode node = null;
      
      public InitialConnector(StoreRootNode givenNode) {
         node = givenNode;
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
