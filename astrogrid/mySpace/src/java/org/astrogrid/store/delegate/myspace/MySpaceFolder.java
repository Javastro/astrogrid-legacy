/*
 * $Id: MySpaceFolder.java,v 1.1 2004/03/04 12:51:31 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate.myspace;
import org.astrogrid.store.delegate.*;

import java.net.MalformedURLException;
import java.util.Hashtable;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;

/**
 * Represents a folder in myspace. Not threadsafe.
 * See also comments on @link MySpaceEntry
 *
 * @author mch
 */


public class MySpaceFolder implements StoreFile {
   
   Hashtable files = new Hashtable();
   
   MySpaceFolder parentFolder = null; //getParentFile() does some path analysis
   
   String name = null;
   
   Agsl agsl = null;
   
   /** Create folder from a myspace reference.  Use to create root folders - to
    * create entries that are children, use the constructor that tkaes a parent */
   public MySpaceFolder(Msrl givenMsrl, String givenName) {
      this.agsl = givenMsrl.toAgsl();
      name = givenName;
   }
 
   /** Creates a folder that is a child of another one.  Parent cannot be null - use
    * the constructor that takes an Msrl to define the root folder*/
   public MySpaceFolder(MySpaceFolder parent, String childName) {
      
      assert parent != null : "Parent must not be null - use MySpaceFolder(Msrl) for root folders";
      
      this.parentFolder = parent;
      this.name = childName;
      
      try {
         //build AGSL from parents one and child
         agsl = new Agsl(parent.toAgsl()+"/"+childName);
      }
      catch (MalformedURLException mue) {
         throw new RuntimeException("Program Error: should not be possible to build illegal AGSLs here", mue);
      }
      
   }

   /** Adds the given StoreFile as a file that exists in this folder */
   public void add(StoreFile child) {
      files.put(child.getName(), child);
   }

   /** Returns the StoreFile representation of the child with the given filename */
   public StoreFile getChild(String filename) {
      return (StoreFile) files.get(filename);
   }
   
   /** Returns an array of the files in this container */
   public StoreFile[] listFiles() {
      return (StoreFile[]) (files.values().toArray(new StoreFile[] {}));
   }

   /** Returns path on server */
   public String getPath() {
      if (parentFolder != null) {
         return parentFolder.getPath()+getName()+"/";
      }
      else {
         return "";
      }
   }
   
   /** Returns full agsl path */
   public String toString()    {    return getName();   }
   

   /** Returns where to find this file using an AStrogrid Store Locator */
   public Agsl toAgsl() {           return agsl; }
   
   /** Returns parent folder of this file/folder */
   public StoreFile getParent() {   return this.parentFolder;  }
   
   /** Returns true - this is a container */
   public boolean isFolder() {      return true;   }
   
   /** Returns false - this is a container */
   public boolean isFile() {        return false;  }
   
   /** Returns the filename/foldername/tablename/etc */
   public String getName() {        return this.name; }
   
   /** Returns true if this represents the same file as the given one */
   public boolean equals(StoreFile anotherFile) {
      if (anotherFile instanceof MySpaceFolder) {
         return name.equals( ((MySpaceFolder) anotherFile).name) &&
               parentFolder.equals(((MySpaceFolder) anotherFile).parentFolder);
      }
      return false;
   }
   
}

/*
 $Log: MySpaceFolder.java,v $
 Revision 1.1  2004/03/04 12:51:31  mch
 Moved delegate implementations into subpackages

 Revision 1.4  2004/03/01 22:38:46  mch
 Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

 Revision 1.3  2004/03/01 16:38:58  mch
 Merged in from datacenter 4.1 and odd cvs/case problems

 Revision 1.2  2004/03/01 15:15:04  mch
 Updates to Store delegates after myspace meeting

 Revision 1.1  2004/02/24 15:59:56  mch
 Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

 Revision 1.2  2004/02/17 15:15:27  mch
 Removed unused imports

 Revision 1.1  2004/02/15 23:16:06  mch
 New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

