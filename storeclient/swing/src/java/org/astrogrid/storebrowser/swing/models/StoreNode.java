/*
 * $Id: StoreNode.java,v 1.1.1.1 2005/02/16 15:02:46 mch Exp $
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
import org.astrogrid.storeclient.api.StoreFile;
import org.astrogrid.storeclient.api.StoreFileResolver;

/**
 * Adaptor for a store (eg homespace, myspace, ftp, disk, etc) rather than a file
 * in a store, to a tree view.
 * Because the root storefile corresponds to this store, this also acts as the
 * wrapper around that storefile
 *
 */

public class StoreNode extends StoreFileNode {
   
   String name = null;
   String uri = null;
   
   public StoreNode(StoreFileNode root, String storeName, String storeUri, Principal aUser) {
      super(root, null, aUser);
      this.name = storeName;
      this.uri = storeUri;
   }
   
   public String getUri() {
      return uri;
   }
   
   public String getName() {
      return name;
   }

   /** lazy load of file so we don't have to load it until it's expanded */
   public StoreFile getFile() throws IOException, URISyntaxException {
      if (file == null) {
        file = StoreFileResolver.resolveStoreFile(uri, user);
      }
      return file;
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
