/*
 * $Id: StoreFile.java,v 1.1 2004/03/01 15:15:04 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate;

import org.astrogrid.store.AGSL;


/**
 * Used to represent a file in 'storespace'.  It is supposed to look rather
 * like 'java.io.File' rather than the container/component design pattern.
 *
 * @author mch
 */


public interface StoreFile {

   /** Returns the filename/foldername/tablename/etc */
   public String getName();
   
   /** Returns parent folder of this file/folder */
   public StoreFile getParent();
   
   /** Returns true if this is a container that can hold other files/folders */
   public boolean isFolder();
   
   /** Returns true if this is a self-contained file.  For example, a database
    * table might be represented as a StoreFile but it is not a file */
   public boolean isFile();
   
   /** Lists children files if this is a container - returns null otherwise */
   public StoreFile[] listFiles();
   
   /** Returns the path to this file on the server */
   public String getPath();
   
   /** Returns where to find this file using an AStrogrid Store Locator */
   public AGSL toAgsl();
}

/*

 */

