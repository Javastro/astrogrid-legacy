/*
 * $Id: StoreFile.java,v 1.2 2004/06/14 23:08:52 jdt Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate;

import java.util.Date;
import org.astrogrid.store.Agsl;


/**
 * Used to represent a file in 'storespace'.  It is supposed to look rather
 * like 'java.io.File' rather than the container/component design pattern.
 *
 * Notay very very benay - do not assume that child[0].getParent() == child[1].getParent().
 * Use equals instead
 * @author mch
 */


public interface StoreFile {

   /** Returns the file/folder/table name without path */
   public String getName();
   
   /** Returns parent folder of this file/folder */
   public StoreFile getParent();

   /** Returns the owner of the file
    * @todo - proper ownership stuff */
   public String getOwner();

   /** Returns the creation date  (null if unknown) */
   public Date getCreated();
   
   /** Returns the date the file was last modified (null if unknown) */
   public Date getModified();
   
   /** Returns the size of the file in bytes (-1 if unknown) */
   public long getSize();

   /** Returns the mime type (null if unknown) */
   public String getMimeType();
   
   /** Returns true if this is a container that can hold other files/folders */
   public boolean isFolder();
   
   /** Returns true if this is a self-contained file.  For example, a database
    * table might be represented as a StoreFile but it is not a file */
   public boolean isFile();
   
   /** Lists children files if this is a container - returns null otherwise */
   public StoreFile[] listFiles();
   
   /** Returns the path to this file on the server, including the filename */
   public String getPath();
   
   /** Returns true if this represents the same file as the given one, within
    * this server.  This
    * won't check for references from different stores to the same file */
   public boolean equals(StoreFile anotherFile);
   
}

/*

 */

