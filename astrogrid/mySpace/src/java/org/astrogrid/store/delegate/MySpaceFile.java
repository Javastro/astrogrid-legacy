/*
 * $Id: MySpaceFile.java,v 1.1 2004/02/24 15:59:56 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate;

import java.io.File;

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


public class MySpaceFile extends File {
   String owner, created, expires, size, permissions = null;
   
   MySpaceFileType type = null;
   MySpaceFolder parentFolder = null; //the ordinary parent is not quite right
   
   public MySpaceFile(MySpaceFolder parent, String child) {
      super(parent, child);
      parentFolder = parent;
   }
   
   public MySpaceFile(MySpaceFolder parent, String aFilename, String anOwner, String aCreatedDate, String anExpiryDate, String aSize, String somePermissions, MySpaceFileType aType) {
      super(parent, aFilename);
      this.parentFolder = parent;
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

   public String getCanonicalPath()   {
      return super.getPath();
   }
   
   public String getPath()
   {
      return parentFolder.getPath()+"/"+getName();
   }
   
   /** Returns the parent folder - differs from getParent() with makes
    * a guess at the path from the initial filename... */
   public MySpaceFolder getParentFolder() { return parentFolder; }
   
   /**
    * Original File.getParent() returns a string based partly ont he filename
    */
   public String getParent() { return parentFolder.getPath() +"/"; }
}

/*
 $Log: MySpaceFile.java,v $
 Revision 1.1  2004/02/24 15:59:56  mch
 Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

 Revision 1.2  2004/02/19 23:30:30  mch
 Added getXxxxx properties

 Revision 1.1  2004/02/15 23:16:06  mch
 New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

