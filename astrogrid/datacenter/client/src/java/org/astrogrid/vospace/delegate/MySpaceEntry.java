/*
 * $Id: MySpaceEntry.java,v 1.1 2004/02/15 23:16:06 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.vospace.delegate;

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


public class MySpaceEntry extends File {
   String owner, created, expires, size, permissions = null;
   
   MySpaceFileType type = null;
   MySpaceFolder parentFolder = null; //the ordinary parent is not quite right
   
   public MySpaceEntry(MySpaceFolder parent, String child) {
      super(parent, child);
      parentFolder = parent;
   }
   
   public MySpaceEntry(MySpaceFolder parent, String aFilename, String anOwner, String aCreatedDate, String anExpiryDate, String aSize, String somePermissions, MySpaceFileType aType) {
      super(parent, aFilename);
      this.parentFolder = parent;
      this.owner = anOwner;
      this.created = aCreatedDate;
      this.expires = anExpiryDate;
      this.size = aSize;
      this.permissions = somePermissions;
      this.type = aType;
   }
   
   public String getType() {
      return type.toString();
   }
   
   public String toString() {
      return getName();
   }
   
   public String getCanonicalPath()
   {
      return super.getPath();
   }
   
   public String getPath()
   {
      return parentFolder.getPath()+"/"+getName();
   }
}

/*
 $Log: MySpaceEntry.java,v $
 Revision 1.1  2004/02/15 23:16:06  mch
 New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

