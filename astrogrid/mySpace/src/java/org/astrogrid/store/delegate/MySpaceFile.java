/*
 * $Id: MySpaceFile.java,v 1.3 2004/03/01 16:38:58 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate;

import java.net.MalformedURLException;
import org.astrogrid.store.Agsl;

/**
 * Represents a file in vospace.  There is also MySpaceFolder to represent
 * a folder.
 *
 * Currently extends File, which was part of an effort to make myspace/vospace
 * 'look' like any other file system.  I've abandoned that so not sure if
 * this is really a good idea - eg some File methods are not overridden
 * (esp Stream operations...)
 *
 * @author mch
 */


public class MySpaceFile implements StoreFile {
   String name, owner, created, expires, size, permissions = null;
   
   MySpaceFileType type = null;
   MySpaceFolder parentFolder = null; //the ordinary parent is not quite right
   
   public MySpaceFile(MySpaceFolder parent, String child) {
      this.name = child;
      this.parentFolder = parent;
   }
   
   public MySpaceFile(MySpaceFolder parent, String aFilename, String anOwner, String aCreatedDate, String anExpiryDate, String aSize, String somePermissions, MySpaceFileType aType) {
      this(parent, aFilename);
      
      this.owner = anOwner;
      this.created = aCreatedDate;
      this.expires = anExpiryDate;
      this.size = aSize;
      this.permissions = somePermissions;
      this.type = aType;
   }
   
   public String getType() {       return type.toString();   }
   
   public String toString() {      return getName();   }
   
   public String getOwner() {    return owner; }
   
   /** Returns the date the file was created */
   public String getCreated() { return created; }
      
   public String getExpires() { return expires; }

   public String getSize() { return size; }

   public String getPermissions() { return permissions; }

   /** Returns the location of this file as an Astrogrid Storepoint Location */
   public Agsl toAgsl()
   {
      try {
         return new Agsl(parentFolder.toAgsl()+"/"+getName());
      }
      catch (MalformedURLException mue) {
         throw new RuntimeException("Program error: Should not be possible to construct myspacefiles with illegal AGSLs...");
      }
   }
   
   /** Returns the path to this file on the server */
   public String getPath() {
      if (parentFolder != null) {
         return parentFolder.getPath()+getName();
      } else {
         return getName();
      }
   }
   
   public String getName() {           return name; }
   
   /** Returns the parent folder - differs from getParent() with makes
    * a guess at the path from the initial filename... */
   public StoreFile getParent() {      return parentFolder; }

   
   /** Lists children files if this is a container - returns null otherwise */
   public StoreFile[] listFiles() {    return null;   }
   
   /** Returns true if this is a container that can hold other files/folders */
   public boolean isFolder()     {     return false;   }
   
   /** Returns true if this is a self-contained file.  For example, a database
    * table might be represented as a StoreFile but it is not a file */
   public boolean isFile() {           return true;   }
   
}

/*
 $Log: MySpaceFile.java,v $
 Revision 1.3  2004/03/01 16:38:58  mch
 Merged in from datacenter 4.1 and odd cvs/case problems

 Revision 1.2  2004/03/01 15:15:04  mch
 Updates to Store delegates after myspace meeting

 Revision 1.1  2004/02/24 15:59:56  mch
 Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

 Revision 1.2  2004/02/19 23:30:30  mch
 Added getXxxxx properties

 Revision 1.1  2004/02/15 23:16:06  mch
 New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

