/*
 * $Id: RootStoreNode.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.slinger.ui.models;


import java.io.File;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.TreeNode;
import org.astrogrid.config.SimpleConfig;

/**
 * Special one-off root storenode for tree models, that represents the highest level node, containing
 * homespace, local disk space, etc
 *
 */

public class RootStoreNode extends StoreFileNode {
   
   /** Default constructor includes some standard stores appropriately configured */
   public RootStoreNode(Principal aUser) {
      super(null, aUser);

      if ((user != null) && (!user.getName().toUpperCase().startsWith("ANONYMOUS"))) {
         addStore(new StoreNode(this, "My Space", "homespace:"+aUser.getName(), aUser));
      }
   }
   
   /** Constructs with given stores */
   public RootStoreNode(StoreNode[] stores, Principal aUser) {
      super(null, aUser);
      setStores(stores);
   }

   public void setStores(StoreNode[] newStores) {
      childNodes = new Vector(newStores.length);
      for (int i = 0; i < newStores.length; i++) {
         addStore(newStores[i]);
      }
      
   }

   public void addStore(StoreNode store) {
      if (childNodes == null) {
         childNodes = new Vector();
      }
      if (store.getUri().trim().toLowerCase().startsWith("file:")) {
         //check that it's allowed
         if (!SimpleConfig.getSingleton().getBoolean("slinger.permitfile", false)) {
            throw new IllegalArgumentException("Browsing local file stores is not permitted here");
         }
      }
      
      childNodes.add(store);
   }

   public void removeStore(StoreNode store) {
      childNodes.remove(store);
   }

   public void removeStore(String storeName) {
      for (int i = 0; i < childNodes.size(); i++) {
         if (((StoreNode) childNodes.elementAt(i)).getName().equals(storeName)) {
            childNodes.remove(i);
         }
      }
   }

   /** used for building paths properly, so we know where the root really is */
   public String getName() {
      return "Stores";
   }
   
   public String getPath() {
      return getName();
   }
   
   
   
   /**
    * There is no parent to this one
    */
   public TreeNode getParent() {
      return null;
   }
   
   /**
    * This is not a leaf
    */
   public boolean isLeaf() {
      return false;
   }
   
   /** Adds a set of default stores.  This is mostly used for 'quick hack'
    * GUIs to have an initial set of stores.  Don't include disk by default
    * as some GUIs - eg on servers - should *not* have disk browsers :-)
    */
   public void addDefaultStores(boolean includeLocalDisk) {
      //homespace
      addHomespace();
         if (includeLocalDisk) {
            addStore(new StoreNode(this, "Local Disk", "file://", user));
         }

         addStore(new StoreNode(this, "Twmbarlwm:8080", "myspace:http://twmbarlwm.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager", user));
         addStore(new StoreNode(this, "Twmbarlwm:8888", "myspace:http://twmbarlwm.star.le.ac.uk:8888/astrogrid-mySpace-SNAPSHOT/services/Manager", user));
         addStore(new StoreNode(this, "Zoomalooma", "myspace:http://zhumulangma.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager", user));
         addStore(new StoreNode(this, "Katatjuta", "myspace:http://katatjuta.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager", user));
         addStore(new StoreNode(this, "ROE FTP", "ftp://ftp.roe.ac.uk/pub/", user));
         addStore(new StoreNode(this, "Ed FTP", "ftp://ftp.ed.ac.uk/pub/", user));
         addStore(new StoreNode(this, "Mirror Service", "ftp://ftp.mirrorservice.org/", user));
         //stores.add(new StoreNode(root, "iBiblio", "ftp://ftp.ibiblio.org/", user));
         addStore(new StoreNode(this, "SRB @SDSC", "srb://testuser.sdsc:PASSWORD@srb.sdsc.edu:5555/home/testuser.sdsc/", user));
   }
   
   public void addHomespace() {
         if ((user != null) && (!user.getName().toUpperCase().startsWith("ANONYMOUS"))) {
            String userName = user.getName();
            int atIdx = userName.indexOf("@");
            if (atIdx == -1) {
               throw new IllegalArgumentException("User name "+userName+" must be of the form <name>@<community>, eg mch@roe.ac.uk");
            }
            addStore(new StoreNode(this, "MySpace", "homespace:"+user.getName()+"#"+userName.substring(0,atIdx), user));
         }
         else {
            //default to frogs as an example
            addStore(new StoreNode(this, "Frogs MySpace", "homespace:frog@org.astrogrid.release#frog", user));
            SimpleConfig.getSingleton().setProperty("homespace.frog@org.astrogrid.release","ivo://org.astrogrid.release/myspace");
            SimpleConfig.getSingleton().setProperty("ivorn.org.astrogrid.release/myspace","myspace:http://katatjuta.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager#frog");
         }
      }
      
   
}

