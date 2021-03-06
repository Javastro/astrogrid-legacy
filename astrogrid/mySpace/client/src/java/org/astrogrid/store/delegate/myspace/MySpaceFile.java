/*
 * $Id: MySpaceFile.java,v 1.3 2004/08/27 22:43:15 dave Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate.myspace;
import java.net.MalformedURLException;
import java.net.URL;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreFile;
import java.util.Date;

/**
 * Represents a file in myspace (it04).  There is also MySpaceFolder to represent
 * a folder.
 * It04 MySpace delegate only has one method for returning the files, and that
 * method returns the whole tree.  So we can assume getParent() getChildren()
 * etc are always fully populated
 *
 * @author mch
 */


public class MySpaceFile implements StoreFile {

	long size ;   
   String name , owner, created, expires, permissions = null;
   URL url = null;
   
   MySpaceFileType type = null;
   MySpaceFolder parentFolder = null;
   
   public MySpaceFile(MySpaceFolder parent, String child) {
      this.name = child;
      this.parentFolder = parent;
   }
   
   public MySpaceFile(MySpaceFolder parent, String aFilename, String anOwner, String aCreatedDate, String anExpiryDate, long aSize, String somePermissions, MySpaceFileType aType) {
      this(parent, aFilename);
      
      this.owner = anOwner;
      this.created = aCreatedDate;
      this.expires = anExpiryDate;
      this.size = aSize;
      this.permissions = somePermissions;
      this.type = aType;
   }

   /**
    * constructor taking URL too - for historical purposes - URLs should be deprecated
    */
   public MySpaceFile(MySpaceFolder parent, String aFilename, String anOwner, String aCreatedDate, String anExpiryDate, long aSize, String somePermissions, MySpaceFileType aType, URL aUrl) {
      this(parent, aFilename);
      
      this.owner = anOwner;
      this.created = aCreatedDate;
      this.expires = anExpiryDate;
      this.size = aSize;
      this.permissions = somePermissions;
      this.type = aType;
      this.url = aUrl;
   }
   
   public String getType() {       return type.toString();   }
   
   public String toString() {      return getName();   }
   
   public String getOwner() {    return owner; }
   
   /** Returns the date the file was created */
   public Date getCreated() { return new Date(created); }
      
   public String getExpires() { return expires; }

   public long getSize() { return size; }

   public String getPermissions() { return permissions; }

   /** Returns the mime type (null if unknown) */
   public String getMimeType() {
      return null;
   }
   
   /** Returns the date the file was last modified (null if unknown) */
   public Date getModified() { return null;  }
   
   /** Returns URL to the file */
   public URL getUrl() { return url; }
   
   /** Returns the path to this file on the server */
   public String getPath() {
      return getParent().getPath()+getName();
   }
   
   public String getName() {           return name; }
   
   /** Returns the parent folder */
   public StoreFile getParent() {
      return parentFolder;
   }

   
   /** Lists children files if this is a container - returns null otherwise */
   public StoreFile[] listFiles() {    return null;   }
   
   /** Returns true if this is a container that can hold other files/folders */
   public boolean isFolder()     {     return false;   }
   
   /** Returns true if this is a self-contained file.  For example, a database
    * table might be represented as a StoreFile but it is not a file */
   public boolean isFile() {           return true;   }
   
   /** Returns true if this represents the same file as the given one */
   public boolean equals(StoreFile anotherFile) {
      if (anotherFile instanceof MySpaceFile) {
         return name.equals( ((MySpaceFile) anotherFile).name) &&
               parentFolder.equals(((MySpaceFile) anotherFile).parentFolder);
      }
      return false;
   }
}

/*
 $Log: MySpaceFile.java,v $
 Revision 1.3  2004/08/27 22:43:15  dave
 Updated filestore and myspace to report file size correctly.

 Revision 1.2.66.1  2004/08/27 16:16:15  dave
 Added temp debug ...

 Revision 1.2  2004/06/14 23:08:52  jdt
 Merge from branches
 ClientServerSplit_JDT
 and
 MySpaceClientServerSplit_JDT
 
 MySpace now split into a client/delegate jar
 astrogrid-myspace-<version>.jar
 and a server/manager war
 astrogrid-myspace-server-<version>.war

 Revision 1.1.2.1  2004/06/14 22:33:20  jdt
 Split into delegate jar and server war.  
 Delegate: astrogrid-myspace-SNAPSHOT.jar
 Server/Manager: astrogrid-myspace-server-SNAPSHOT.war
 
 Package names unchanged.
 If you regenerate the axis java/wsdd/wsdl files etc you'll need
 to move some files around to ensure they end up in the client
 or the server as appropriate.
 As of this check-in the tests/errors/failures is 162/1/22 which
 matches that before the split.

 Revision 1.3  2004/05/03 08:55:53  mch
 Fixes to getFiles(), introduced getSize(), getOwner() etc to StoreFile

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

 Revision 1.2  2004/02/19 23:30:30  mch
 Added getXxxxx properties

 Revision 1.1  2004/02/15 23:16:06  mch
 New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

