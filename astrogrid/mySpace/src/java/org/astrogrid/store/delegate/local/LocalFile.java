/*
 * $Id: LocalFile.java,v 1.5 2004/05/03 08:55:53 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate.local;
import org.astrogrid.store.delegate.*;

import java.io.File;
import java.net.MalformedURLException;
import org.astrogrid.store.Agsl;
import java.util.Date;

/**
 * Represents a local file (ie a File class) as a StoreFile
 * <p>
 * Note that because this uses the File class to navigate around,
 * it is not necessarily true that child[0].getParent() == child[1].getParent()
 *
 * @author M Hill
 */


public class LocalFile implements StoreFile {
   
   
   File file = null;
   LocalFileStore store = null;
   
   public LocalFile(LocalFileStore theStore, File aFile) {
      assert aFile != null : "Must set file";
      assert theStore != null : "Must set store";
      
      this.file = aFile;
      this.store = theStore;
   }
   
   /** Lists children files if this is a container - returns null otherwise */
   public StoreFile[] listFiles() {
      if (!isFolder()) {
         return null;
      }

      File[] localFiles = file.listFiles();
      StoreFile[] storeFiles = new StoreFile[localFiles.length];
      for (int i=0;i<localFiles.length;i++) {
         storeFiles[i] = new LocalFile(store, localFiles[i]);
      }
      return storeFiles;
   }
   
   /** Returns parent folder of this file/folder */
   public StoreFile getParent() {
      //check to see if we're at the root
      String serverPath = getPath();
      if (serverPath == null) return null;

      return new LocalFile(store, file.getParentFile());//but specify full File
   }
   
   /** Returns true if this is a self-contained file.  For example, a database
    * table might be represented as a StoreFile but it is not a file */
   public boolean isFile() {
      return file.isFile();
   }
   
   /** Returns the filename/foldername/tablename/etc */
   public String getName() {
      return file.getName();
   }
   
   /** Returns the path to this file on the server */
   public String getPath() {
      try {
         return store.getServerPath(file);
      }
      catch (StoreException se) {
         //should never happen
         throw new RuntimeException("Program Error: ", se);
      }
   }

   /** Returns the owner of the file */
   public String getOwner() {
      return null;
   }
   
   /** Returns the mime type (null if unknown) */
   public String getMimeType() {
      return null;
   }
   
   /** Returns the date the file was last modified (null if unknown) */
   public Date getModified() {
      return new Date(file.lastModified());
   }
   
   /** Returns the creation date  (null if unknown) */
   public Date getCreated() {
      return null;
   }
   
   /** Returns the size of the file in bytes (-1 if unknown) */
   public long getSize() {
      return file.length();
   }
   
   
   /** Returns true if this is a container that can hold other files/folders */
   public boolean isFolder() {
      return file.isDirectory();
   }
   
   /** Returns true if this represents the same file as the given one */
   public boolean equals(StoreFile anotherFile) {
      if (anotherFile instanceof LocalFile) {
         return file.equals( ((LocalFile) anotherFile).file);
      }
      return false;
   }
   
   /** Returns user representation - server path */
   public String toString() {
      return getPath();
   }
   
}

/*
$Log: LocalFile.java,v $
Revision 1.5  2004/05/03 08:55:53  mch
Fixes to getFiles(), introduced getSize(), getOwner() etc to StoreFile

Revision 1.4  2004/04/06 13:56:00  mch
Fix to getParent null

Revision 1.3  2004/04/06 11:01:33  mch
Fixed null pointer when LocalFile is root

Revision 1.2  2004/04/06 10:16:23  mch
Fixed isFolder() bug

Revision 1.1  2004/03/04 12:51:31  mch
Moved delegate implementations into subpackages

Revision 1.1  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

 */

