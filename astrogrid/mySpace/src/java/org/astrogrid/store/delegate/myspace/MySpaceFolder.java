/*
 * $Id: MySpaceFolder.java,v 1.4 2004/05/03 08:55:53 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate.myspace;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.StoreFile;

/**
 * Represents a folder in myspace. Not threadsafe.
 * See also comments on @link MySpaceFile
 *
 * @author mch
 */


public class MySpaceFolder extends MySpaceFile  {
   
   Hashtable children = new Hashtable();

   boolean isRoot = false;
   
   /** Create folder from a myspace reference.  Use to create root folders - to
    * create entries that are children, use the constructor that tkaes a parent
   public MySpaceFolder(Msrl givenMsrl, String givenName) {
      this.agsl = givenMsrl.toAgsl();
      name = givenName;
   }
 
   /** Creates a folder that is a child of another one. */
   public MySpaceFolder(MySpaceFolder parent, String childName) {
      super(parent, childName);
   }

   /** Creates the root folder */
   public MySpaceFolder(String childName) {
      super(null, childName);
      isRoot = true;
   }

   /** Adds the given StoreFile as a child that exists in this folder */
   public void add(StoreFile child) {
      children.put(child.getName(), child);
   }

   /** Returns the StoreFile representation of the child with the given filename */
   public StoreFile getChild(String filename) {
      return (StoreFile) children.get(filename);
   }
   
   /** Returns an array of the files in this container */
   public StoreFile[] listFiles() {
      return (StoreFile[]) (children.values().toArray(new StoreFile[] {}));
   }

   /** Returns path on server */
   public String getPath() {
      if (isRoot) {
         return "";
      }
      else
      {
         return getParent().getPath()+getName()+"/";
      }
   }
   
   /** Returns the file path */
   public String toString()    {    return getPath();   }
   
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

   /**
    * Returns the folder or file matching the given path in the *children* of
    * this folder.  So if the path is '/famous/stuff/main/' the returned StoreFile
    * will be the MySpaceFolder instance representing the 'main' directory
    * @todo test
    */
   public StoreFile findFile(String path) throws FileNotFoundException {
      
      //locate file
      StringTokenizer dirTokens = new StringTokenizer(path, "/");
      MySpaceFolder folder = this;
      StoreFile child = null;
      while (dirTokens.hasMoreTokens())
      {
         String token = dirTokens.nextToken();
         child = folder.getChild(token);
         if (child == null) {
            throw new FileNotFoundException("No such token '"+token+"' in path "+path+" from "+this);
         }
         else {
            if (child.isFolder()) {
               folder = (MySpaceFolder) child;
            }
         }
      }
      
      if (dirTokens.hasMoreTokens()) {
         throw new FileNotFoundException("path "+path+" only partly found from "+this);
      }
      
      return child;
      
   }
   
   
}

/*
 $Log: MySpaceFolder.java,v $
 Revision 1.4  2004/05/03 08:55:53  mch
 Fixes to getFiles(), introduced getSize(), getOwner() etc to StoreFile

 Revision 1.3  2004/04/26 16:40:54  mch
 More fixes to It05 delegate

 Revision 1.2  2004/04/23 11:38:19  mch
 Fixes to return correct AGSL plus change to File model for It05 delegate

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

