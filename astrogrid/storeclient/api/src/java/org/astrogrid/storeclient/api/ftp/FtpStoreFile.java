/*
 $Id: FtpStoreFile.java,v 1.1 2005/03/28 02:06:35 mch Exp $

 (c) Copyright...
 */

package org.astrogrid.storeclient.api.ftp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.Principal;
import java.util.Date;
import java.util.Hashtable;
import org.apache.commons.net.ftp.FTPFile;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.storeclient.api.StoreFile;

/**
 * Represents a file on a ftp server.  Extends UrlTarget to get some freebies from it to
 * do with getting streams and stuff
 * <P>
 * Adapted from the ACE project
 * <p>
 */

public class FtpStoreFile implements StoreFile {
   
   private SlingerFtpClient ftpClient = null;

   /** commons net representation */
   private FTPFile file = null;
   
   String path = null;

   /** Cache to parents/children */
   FtpStoreFile parent = null;
   FtpStoreFile[] children = null;

   Hashtable properties = null;
   
   Principal user = null;
   
   /**
    * Construct a FtpFile that represents a file at the given URL
    */
   public FtpStoreFile(URL url, Principal givenUser) throws IOException
   {
      user = givenUser;
      if (!url.getProtocol().equals("ftp")) {
         throw new IllegalArgumentException(url+" is not an FTP target");
      }
      
      //need to now load up appropriate login for this server, for the given user
      LoginAccount loginAccount = LoginAccount.ANONYMOUS;
      
      try {
         ftpClient = new SlingerFtpClient(url, loginAccount);
         path = url.getPath();
         file = ftpClient.getFile(url.getPath());
      }
      catch (IOException ioe)
      {
         //catch & rethrow with more info about target
         IOException nioe = new IOException(ioe+" connecting to "+url);
         nioe.setStackTrace(ioe.getStackTrace()); //preserve same stack trace
         throw nioe;
      }
   }

   /**
    * Construct a FtpFile with an existing ftp client.  Normally used by
    * parent/child creaters
    */
   private FtpStoreFile(SlingerFtpClient client, String aPath, Principal givenUser) throws IOException
   {
      ftpClient = client;
      path = aPath;
      file = ftpClient.getFile(path);
      user = givenUser;
   }

   /**
    * Construct a FtpFile with an existing ftp client and file.  Normally used by
    * parent/child creaters
    */
   private FtpStoreFile(FtpStoreFile aParent, FTPFile aFile, String aPath, Principal user) throws IOException
   {
      parent = aParent;
      ftpClient = parent.ftpClient;
      path = aPath;
      file = aFile;
   }


   /** Should be used for *all* access to ftp client; checks that it's connected,
    and (re)connects if need be
    */
   public SlingerFtpClient getFtp() {
      
      if (!ftpClient.isConnected()) {
      }
      
      return ftpClient;
   }
   
   /** For debug, returns server & port */
   public String toString()
   {
      return "FtpFile "+getFtp()+"#"+path+" (size="+getSize()+", owner="+getOwner()+", changed="+getModified()+")";
   }

   /** Clears the cached children and regets the details on this instance. */
   public void refresh()  throws IOException {
      file = getFtp().getFile(path);
      children = null;
      listFiles(user); //bit of a botch
   }
   
   /**
    * Returns the parent of the given file
    */
   public StoreFile getParent(Principal user) throws IOException {
   
      //already at root
      if (path.equals("/"))  {
         return null;
      }

      if (parent == null) {
         //get path - remove last token
         String parentPath = path;
         if (parentPath.endsWith("/")) {
            parentPath = parentPath.substring(0,parentPath.length()-1);
         }
         
         //remove last path token, not including slash
         parentPath = parentPath.substring(0, parentPath.lastIndexOf("/")+1);
         
         parent = new FtpStoreFile(getFtp(), parentPath, user);
      }
      return parent;
   }

   /**
    * Delete this file
    */
   public void delete(Principal user) throws IOException {
      getFtp().deleteFile(path);
   }
   
   /** Returns true if this is a container that can hold other files/folders */
   public boolean isFolder() {
      return file.isDirectory();
   }
   
   /** Returns true if it exists - eg it may be a reference to a file about to be
    * created. I think these all exist... */
   public boolean exists() {
      return true;
   }

   /** Returns the date the file was last modified (null if unknown) */
   public Date getModified() {
      return file.getTimestamp().getTime();
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public InputStream openInputStream(Principal user) throws IOException {
      getFtp().setFileTransferMode(SlingerFtpClient.BINARY_FILE_TYPE);
      return getFtp().retrieveFileStream(path);
   }

   /** Returns the creation date  (null if unknown) */
   public Date getCreated() {
//    return file.getTimestamp();
      return null;
   }
   
   /** Used to set the mime type of the file. Does nothing */
   public void setMimeType(String mimeType, Principal user) throws IOException {
   }
   
   /** Returns the size of the file in bytes (-1 if unknown) */
   public long getSize() {
      return file.getSize();
   }
   
   /** Returns the mime type (null if unknown) */
   public String getMimeType() {
      return null;
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream openOutputStream(Principal user, String mimeType, boolean append) throws IOException {
      getFtp().setFileTransferMode(SlingerFtpClient.BINARY_FILE_TYPE);
      if (append) {
         return getFtp().appendFileStream(path);
      }
      else {
         return getFtp().storeFileStream(path);
      }
   }
   
   /** Returns true if this is a self-contained file.  For example, a database
    * table might be represented as a StoreFile but it is not a file */
   public boolean isFile() {
      return !isFolder();
   }
   
   /** Returns the file/folder/table name without path */
   public String getName() {
      return new File(path).getName();  //use File parser
   }
   
   /** Returns the path to this file on the server, including the filename */
   public String getPath() {
      return path;
   }
   
   /** Returns the owner of the file */
   public String getOwner() {
      return file.getUser();
   }
   
   /** Returns true if this represents the same file as the given one, within
    * this server.  This
    * won't check for references from different stores to the same file */
   public boolean equals(StoreFile anotherFile) {
      return (anotherFile.getPath().equals(path));
   }
   
   /** Lists children files if this is a container - returns null otherwise */
   public StoreFile[] listFiles(Principal user) throws IOException {
      if ( (children == null) && isFolder()) {

         if (!path.endsWith("/")) {
            path = path +"/";
         }
         System.out.println("Getting FTP files at "+path+"...");
         FTPFile[] c = getFtp().listFiles(path);
         System.out.println("..done");
         if (c!=null) {
            children = new FtpStoreFile[c.length];
            for (int i = 0; i < c.length; i++) {
               children[i] = new FtpStoreFile(this, c[i], path+c[i].getName(), user);
            }
         }
      }
      return children;
   }

   /** Returns an appropriate RL (eg url, msrl) for this file */
   public String getUri() {
      return getFtp().getUrl()+path;
   }
   
   /** Renames the file to the given filename. Affects only the name, not the
    * path */
   public void renameTo(String newFilename, Principal user) throws IOException {
      getFtp().rename(path, newFilename);
   }
   
   /** IF this is a folder, creats a subfolder */
   public StoreFile makeFolder(String newFolderName, Principal user) throws IOException {
      if (isFolder()) {
         String newPath = path+newFolderName;
         boolean success = getFtp().createFolder(newPath);
         if (!success) {
            throw new IOException("Could not create folder "+newPath+" on "+getFtp().getUrl());
         }
         return new FtpStoreFile(ftpClient, newPath, user);
      }
      throw new IllegalArgumentException(this+" is not a folder; cannot make subfolder "+newFolderName);
   }

   /** If this is a folder, creates an node to a child file */
   public StoreFile makeFile(String filename, Principal user) throws IOException {
      if (!isFolder()) {
         throw new IllegalArgumentException(path+" is not a folder; cannot create child "+filename);
      }
      return new FtpStoreFile(this, ftpClient.getFile(path+filename), path+filename, user);
   }
   
   
   /**
    *
    */
   public static void main(String[] args) throws IOException {
      
      StoreFile file = new FtpStoreFile(new URL("ftp://ftp.roe.ac.uk/pub/"), LoginAccount.ANONYMOUS);
      System.out.println(file);
      StoreFile parent = file.getParent(LoginAccount.ANONYMOUS);
      System.out.println("Parent: "+parent);
      StoreFile[] children = file.listFiles(LoginAccount.ANONYMOUS);
      for (int i = 0; i < children.length; i++) {
         System.out.println("Child: "+children[i]);
      }
   }
   
}
