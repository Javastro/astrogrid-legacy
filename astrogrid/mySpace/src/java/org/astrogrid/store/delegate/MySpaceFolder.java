/*
 * $Id: MySpaceFolder.java,v 1.1 2004/02/24 15:59:56 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate;

import java.io.File;
import java.util.Hashtable;

/**
 * Represents a folder in myspace. Not threadsafe.
 * See also comments on @link MySpaceEntry
 *
 * @author mch
 */


public class MySpaceFolder extends File {
   Hashtable files = new Hashtable();
   File[] cachedArray = null;
   
   MySpaceFolder parentFolder = null; //getParentFile() does some path analysis
   
   public MySpaceFolder(MySpaceFolder parent, String child) {
      super(parent, child);
      parentFolder = (MySpaceFolder) parent;
   }
   
   public void add(File child) {
      files.put(child.getName(), child);
      cachedArray = null;
   }
   
   public File getChild(String filename) {
      return (File) files.get(filename);
   }
   
   public File[] listFiles() {
      if (cachedArray == null) {
         cachedArray = (File[]) (files.values().toArray(new File[] {}));
      }
         
      return cachedArray;
   }
   
   public int getChildCount() {
      return files.size();
   }
   
   /** Returns full path apart from root */
   public String toString()
   {
      //if this is root return then name
      if (isRoot()) {
         return super.getPath();
      } else {
         //if the parent of this is root, don't use that
         if ( parentFolder.isRoot()) {
            return "/"+getName();
         } else {
            return parentFolder+"/"+getName();
         }
      }
   }
   
   /** Returns the path up to and including the individual@account */
   public String getPath()
   {
      if (parentFolder == null) {
         return "/";
      }
      else if (parentFolder.isRoot()) {
         return "/"+getName();
      }
      else {
         return parentFolder.getPath()+"/"+getName();
      }
   }
   
   /** Naughty method - returns true if root
    * if a server name is given (ie one with slashes) it seems File will
    * make up a bunch of File parents
    */
   
   private boolean isRoot()
   {
      return (parentFolder == null);
   }
   
}

/*
 $Log: MySpaceFolder.java,v $
 Revision 1.1  2004/02/24 15:59:56  mch
 Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

 Revision 1.2  2004/02/17 15:15:27  mch
 Removed unused imports

 Revision 1.1  2004/02/15 23:16:06  mch
 New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

