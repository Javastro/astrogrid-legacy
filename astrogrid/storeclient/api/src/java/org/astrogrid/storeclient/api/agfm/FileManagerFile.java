/*
 * $Id: FileManagerFile.java,v 1.1 2005/03/25 16:19:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.storeclient.api.agfm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Date;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.slinger.StoreException;
import org.astrogrid.slinger.vospace.IVOSRN;
import org.astrogrid.storeclient.api.StoreFile;

/**
 * Wrapper around the FileManagerNode that is used to access AstroGrid's FileManager
 * services (MySpace)
 *
 * @author M Hill
 */


public class FileManagerFile implements StoreFile {
   
   FileManagerClientFactory factory = new FileManagerClientFactory();
   FileManagerClient client = null;
   FileManagerNode node = null;;
   
   IVOSRN id = null;

   public FileManagerFile(IVOSRN givenId, Principal aUser) throws IOException, RegistryException, CommunityException, URISyntaxException  {
      FileManagerClient client = factory.login(/*aUser*/);
      node = client.node(givenId.toOldIvorn());
      this.id = givenId;
   }

   public FileManagerFile(FileManagerClient givenClient, FileManagerNode givenNode) throws IOException {
      this.client = givenClient;
      this.node = givenNode;
      String s = node.getIvorn().toString();
      try {
         this.id = new IVOSRN(s);
      }
      catch (URISyntaxException e) {
         throw new StoreException(e+" from "+s,e);
      }
   }
   
   /** Refreshes from the server */
   public void refresh() throws IOException {
      try {
         node = client.node(node.getIvorn());
      }
      catch (RegistryException e) { throw new StoreException(e+" refreshing "+node.getIvorn(), e); }
      catch (CommunityException e) {throw new StoreException(e+" refreshing "+node.getIvorn(), e); }
      catch (URISyntaxException e) {throw new StoreException(e+" refreshing "+node.getIvorn(), e); }
   }
   
   /** Returns the file/folder/table name without path */
   public String getName()       {  return node.getName();    }
   
   /** Returns the mime type (null if unknown) */
   public String getMimeType()   {  return null;  }
   
   /** Returns the size of the file in bytes (-1 if unknown) */
   public long getSize()         {  return node.getMetadata().getSize().longValue();   }
   
   /** Returns true if this is a container that can hold other files/folders */
   public boolean isFolder()     {  return node.isFolder();  }
   
   /** Returns true if this is a self-contained file.  For example, a database
    * table might be represented as a StoreFile but it is not a file */
   public boolean isFile()       {  return node.isFile();   }
   
   /** Returns the owner of the file */
   public String getOwner()      {  return null; }

   /** Returns the creation date  (null if unknown) */
   public Date getCreated()      {  return node.getMetadata().getCreateDate().getTime(); }
   
   /** Returns the date the file was last modified (null if unknown) */
   public Date getModified()     {  return node.getMetadata().getModifyDate().getTime(); }

   /** Returns the path to this file on the server, including the filename */
   public String getPath()       {  return id.getFragment();      }

   /** Returns an appropriate RL (eg url, msrl) for this file */
   public String getUri()        {      return node.getMetadata().getNodeIvorn().toString();   }
   
   /** Returns true if it exists - eg it may be a reference to a file about to be
    * created.  In this case returns true as 'I don't know'... which probably is not good... */
   public boolean exists()       {
      return true;
   }
   
   /** Renames the file to the given filename. Affects only the name, not the
    * path */
   public void renameTo(String newFilename, Principal user) throws IOException {
//    node.copy();
//    node.delete();
      throw new UnsupportedOperationException("Todo");
   }
   
   /** If this is a folder, creates an output stream to a child file */
   public OutputStream outputChild(String filename, Principal user, String mimeType) throws IOException {
      FileManagerNode newChild = node.addFile(filename);
      return newChild.writeContent();
   }
   
   /** Used to set the mime type of the data about to be sent to the target. . */
   public void setMimeType(String mimeType, Principal user) throws IOException {
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream openOutputStream(Principal user, String mimeType, boolean append) throws IOException {
      if (isFolder()) {
         throw new UnsupportedOperationException("Cannot open streams to folders. (Path "+getPath()+")");
      }
      if (append) {
         return node.appendContent();
      }
      return node.writeContent();
   }
   
   /** Returns parent folder of this file/folder, if permission granted */
   public StoreFile getParent(Principal user) throws IOException {
      FileManagerNode parentNode = node.getParentNode();
      if (parentNode == null) {
         return null;
      }
      return new FileManagerFile(client, parentNode);
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public InputStream openInputStream(Principal user) throws IOException {
      if (isFolder()) {
         throw new UnsupportedOperationException("Cannot open streams to folders. (Path "+getPath()+")");
      }
      return node.readContent();
   }
   
   /** Deletes this file */
   public void delete(Principal user) throws IOException {
      node.delete();
   }
   
   /** Lists children files if this is a container - returns null otherwise */
   public StoreFile[] listFiles(Principal user) throws IOException {
      if (isFolder()) {
         return null;
      }
      StoreFile[] sf = new StoreFile[node.getChildCount()];
      for (int i = 0; i < sf.length; i++) {
         sf[i]= new FileManagerFile(client, (FileManagerNode) node.getChildAt(i));
      }
      return sf;
   }
   
   /** IF this is a folder, creats a subfolder, returning link to it */
   public StoreFile makeFolder(String newFolderName, Principal user) throws IOException {
      if (!isFolder()) {
         throw new UnsupportedOperationException("Cannot make a folder in a file "+getPath());
      }
      FileManagerNode subFolder = node.addFolder(newFolderName);
      return new FileManagerFile(client, subFolder);
   }

   /** Returns true if this represents the same file as the given one, within
    * this server.  This
    * won't check for references from different stores to the same file */
   public boolean equals(StoreFile anotherFile) {
      return getPath().equals(anotherFile.getPath());
   }
   
   
}

/*
$Log: FileManagerFile.java,v $
Revision 1.1  2005/03/25 16:19:57  mch
Added FIleManger suport

 */

