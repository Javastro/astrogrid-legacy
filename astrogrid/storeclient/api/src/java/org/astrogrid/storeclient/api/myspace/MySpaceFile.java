/*
 * $Id: MySpaceFile.java,v 1.1 2005/02/16 19:57:06 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.storeclient.api.myspace;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Date;
import javax.xml.rpc.ServiceException;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.community.User;
import org.astrogrid.storeclient.api.StoreFile;
import org.astrogrid.storeclient.api.StoreFileResolver;
import org.astrogrid.slinger.myspace.MSRL;
import org.astrogrid.slinger.myspace.it05.AstrogridMyspaceSoapBindingStub;
import org.astrogrid.slinger.myspace.it05.KernelResults;
import org.astrogrid.slinger.myspace.it05.ManagerServiceLocator;
import org.astrogrid.slinger.myspace.it05.MySpaceIt05Delegate;
//import org.astrogrid.store.delegate.myspaceItn05.MySpaceIt05Delegate;

/**
 * Represents a file in myspace as a StoreFile. Not very nice, wrapping old storefile
 *
 * @author M Hill
 */


public class MySpaceFile implements StoreFile {
   
   AstrogridMyspaceSoapBindingStub soapClient = null;

   Principal user = null;
   
   String path = null;
   String owner = null;
   Date modified = null;
   Date created = null;
   Date expires = null;
   long size = -1;
   String mimeType = null;
//   URL streamUrl = null;
   boolean exists = true;
   
   MySpaceFile parent = null;
   MySpaceFile[] children = null;

   MSRL myspaceServer = null;
   
   public MySpaceFile(AstrogridMyspaceSoapBindingStub myspaceClient, MSRL myspace, String aPath, Principal aUser) throws IOException {
      this.soapClient = myspaceClient;
      this.myspaceServer = myspace.getManagerMsrl(); //get just the manager, without any path
      this.user = aUser;
      this.path = aPath;
      
      loadFileDetails(aPath);
   }

   public MySpaceFile(MySpaceFile aParent, String aPath, Principal aUser) throws IOException {
      this.parent = aParent;
      this.soapClient = parent.soapClient;
      this.myspaceServer = parent.myspaceServer;
      this.user = aUser;
      this.path = aPath;
      
      loadFileDetails(aPath);
   }
   
   public MySpaceFile(MSRL myspace, Principal aUser) throws IOException, ServiceException {
      this((AstrogridMyspaceSoapBindingStub) new ManagerServiceLocator().getAstrogridMyspace(myspace.getDelegateEndpoint()),
           myspace,
           myspace.getPath(),
           aUser);
   }
   
   /** used by loadDetails */
   private MySpaceFile(MySpaceFile aParent)  {
      this.parent = aParent;
      this.myspaceServer = parent.myspaceServer;
      this.soapClient = parent.soapClient;
   }

   /**
    * Loads the details of this file (not its children) from the given path
    */
   private void loadFileDetails(String aPath) throws IOException {
      this.path = aPath;

      if ((path == null) || (path.trim().length()==0)) { path = "/"; }//root

      if (path.equals("/")) { return; }//server doesn't return anything for root

      String searchPath = path;
//don't do this as we want the folder details      if (searchPath.endsWith("/")) { searchPath = searchPath +"*"; } //folder
      if (searchPath.endsWith("/")) {
         searchPath = searchPath.substring(0, searchPath.length()-1);
      }
      
      System.out.println("Getting entry at "+path+" from "+soapClient+"...");
//      KernelResults kresults = soapClient.getEntriesList(path+"*", false);
      MySpaceIt05Delegate delegate = new MySpaceIt05Delegate(User.ANONYMOUS, myspaceServer.getDelegateEndpoint().toString());
      org.astrogrid.slinger.myspace.it05.MySpaceIt05Delegate.MySpaceFile oldTree = delegate.getFile(searchPath);
      System.out.println("...got results");

      if (oldTree == null) {
         //nothing found at this spot - ie there's nothing there, this points to a new file
         exists = false;
         //throw new IOException("No entries found at "+searchPath+" on "+soapClient);
      }
      else {
         loadFileDetails(this, oldTree);
      }
   }
   
   /** Loads just the file details from the old storefile into this instance - not the children */
   private static void loadFileDetails(MySpaceFile file, org.astrogrid.slinger.myspace.it05.MySpaceIt05Delegate.MySpaceFile oldTree) throws IOException {
      if (oldTree.isFolder() && !file.path.endsWith("/")) {
         file.path = file.path+"/";
      }
      file.owner = oldTree.getOwner();
      file.created = oldTree.getCreated();
      file.size = oldTree.getSize();
      file.mimeType = oldTree.getMimeType();
      if ((file.mimeType != null) && (file.mimeType.equals("null"))) {
         file.mimeType = null;
      }
      file.modified = oldTree.getModified();
   }

   /** As load details but also Loads the children from the server */
   private void loadDetails(String aPath) throws IOException {
      this.path = aPath;

      if ((path == null) || (path.trim().length()==0)) {
         path = "/"; //root
      }
      
      System.out.println("Getting entries at "+path+" from "+soapClient+"...");
//      KernelResults kresults = soapClient.getEntriesList(path+"*", false);
      MySpaceIt05Delegate temp = new MySpaceIt05Delegate(User.ANONYMOUS, myspaceServer.getDelegateEndpoint().toString());
      String searchPath = path;
      if (!searchPath.endsWith("/")) { searchPath = searchPath+"/"; }
      if (!searchPath.startsWith("/")) { searchPath = "/"+searchPath; }
      searchPath = searchPath +"*";
      org.astrogrid.slinger.myspace.it05.MySpaceIt05Delegate.MySpaceFile oldTree = temp.getFiles(searchPath);
      System.out.println("...got results");
      
      if (oldTree == null) {
         //no children
         return;
//         throw new IOException("No entries found at "+searchPath+" on "+soapClient);
      }
      loadDetails(this, oldTree);
   }
   
   private static void loadDetails(MySpaceFile file, org.astrogrid.slinger.myspace.it05.MySpaceIt05Delegate.MySpaceFile oldTree) throws IOException {
      loadFileDetails(file, oldTree);

      if (oldTree.listFiles() != null) {
         file.children = new MySpaceFile[oldTree.listFiles().length];
         for (int i = 0; i < oldTree.listFiles().length; i++) {
            file.children[i] = new MySpaceFile(file);
            file.children[i].path = file.getPath()+oldTree.listFiles()[i].getName(); //don't use oldTree's path as it is relative to search path
            loadDetails(file.children[i], oldTree.listFiles()[i]);
         }
      }
   }


   /** Refreshes from the server; generally just clears any caches so forcing a
    * reload next time the properties are accessed */
   public void refresh() throws IOException {
      children = null;
      loadFileDetails(getPath()); //reload details using existing user
   }
   
   /** Returns the file/folder/table name without path */
   public String getName() {
      return new File(path).getName(); //use file parser
   }
   
   /** Returns the mime type (null if unknown) */
   public String getMimeType()   {  return mimeType;  }
   
   /** Returns the size of the file in bytes (-1 if unknown) */
   public long getSize()         {  return size;   }
   
   /** Returns the owner of the file */
   public String getOwner() {      return owner;   }
   
   /** Returns the path to this file on the server, including the filename */
   public String getPath() {       return path;    }

   /** Returns an appropriate RL (eg url, msrl) for this file */
   public String getUri() {
      return myspaceServer+"#"+getPath();
   }
   
   /** Returns true if this is a container that can hold other files/folders */
   public boolean isFolder() {   return getPath().endsWith("/");  }
   
   /** Returns true if this is a self-contained file.  For example, a database
    * table might be represented as a StoreFile but it is not a file */
   public boolean isFile() {       return !isFolder();   }
   
   /** Returns true if it exists - eg it may be a reference to a file about to be
    * created */
   public boolean exists() {
      return exists;
   }
   
   
   /** Returns the creation date  (null if unknown) */
   public Date getCreated() {  return created; }
   
   /** Returns the date the file was last modified (null if unknown) */
   public Date getModified() { return modified; }

   /** Renames the file to the given filename. Affects only the name, not the
    * path */
   public void renameTo(String newFilename, Principal user) throws IOException {
//    soapClient.move(new Agsl(m
      throw new UnsupportedOperationException("Todo");
   }
   
   /** If this is a folder, creates an output stream to a child file */
   public OutputStream outputChild(String filename, Principal user, String mimeType) throws IOException {
      MySpaceIt05Delegate temp = new MySpaceIt05Delegate(User.ANONYMOUS, soapClient._getProperty(soapClient.ENDPOINT_ADDRESS_PROPERTY).toString());
      return temp.putStream(getPath()+filename, false);
   }
   
   /** Used to set the mime type of the data about to be sent to the target. . */
   public void setMimeType(String mimeType, Principal user) throws IOException {
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream openOutputStream(Principal user, String mimeType, boolean append) throws IOException {
      //bit of a botch
      MySpaceIt05Delegate temp = new MySpaceIt05Delegate(User.ANONYMOUS, soapClient._getProperty(soapClient.ENDPOINT_ADDRESS_PROPERTY).toString());
      return temp.putStream(getPath(), false);
   }
   
   /** Returns parent folder of this file/folder, if permission granted */
   public StoreFile getParent(Principal user) throws IOException {
      if (parent == null) {
         String parentPath = "/"+getPath();
         if (parentPath.endsWith("/")) {
            parentPath = parentPath.substring(0, parentPath.length()-1);
         }
         if ((parentPath == null) || (parentPath.length()==0) || (parentPath.equals("/"))) {
            return null;
         }
         parentPath = parentPath.substring(0, parentPath.lastIndexOf("/"));
         parent = new MySpaceFile(soapClient, myspaceServer, parentPath, user);
      }
      return parent;
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public InputStream openInputStream(Principal user) throws IOException {
      if (isFolder()) {
         throw new UnsupportedOperationException("Cannot open streams to folders. (Path "+getPath()+")");
      }
      MySpaceIt05Delegate oldClient = new MySpaceIt05Delegate(User.ANONYMOUS, myspaceServer.getDelegateEndpoint().toString());
      return oldClient.getUrl(path).openStream();
   }
   
   /** Deletes this file */
   public void delete(Principal user) throws IOException {
      String delPath = getPath();
      if (!delPath.startsWith("/")) { delPath = "/"+delPath; }
      if (delPath.endsWith("/")) {
         delPath = delPath.substring(0, delPath.length()-1);
      }
      KernelResults results = soapClient.deleteFile(delPath, false);
      MySpaceIt05Delegate.checkStatusMessages(results, "deleting "+delPath);
   }
   
   /** Lists children files if this is a container - returns null otherwise */
   public StoreFile[] listFiles(Principal user) throws IOException {
      if (!isFolder()) {
         return null;
      }
      
      if (children == null) {
         loadDetails(getPath());
      }
      return children;
   }
   
   /** IF this is a folder, creats a subfolder, returning link to it */
   public StoreFile makeFolder(String newFolderName, Principal user) throws IOException {
      if (!isFolder()) {
         throw new UnsupportedOperationException("Cannot make a folder in a file "+getPath());
      }
      String targetPath = getPath();
      if (!targetPath.startsWith("/")) targetPath = "/"+targetPath;
      KernelResults results = soapClient.createContainer(targetPath+newFolderName, false);
      MySpaceIt05Delegate.checkStatusMessages(results, "making folder "+targetPath+newFolderName);
      return new MySpaceFile(this, getPath()+newFolderName, user);
   }

   /** Returns true if this represents the same file as the given one, within
    * this server.  This
    * won't check for references from different stores to the same file */
   public boolean equals(StoreFile anotherFile) {
      return getPath().equals(anotherFile.getPath());
   }
   
   /** Quick tests */
   /**
    *
    */
   public static void main(String[] args) throws IOException, IOException, URISyntaxException
   {
      Principal user = LoginAccount.ANONYMOUS;
      
      String uri = "myspace:http://zhumulangma.star.le.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager#newt/votable/";
      
      StoreFile file = StoreFileResolver.resolveStoreFile(uri, LoginAccount.ANONYMOUS);

//      file.makeFolder("wibble2", user);
      file.refresh();
      StoreFile[] files = file.listFiles(user);
   }
   
}

/*
$Log: MySpaceFile.java,v $
Revision 1.1  2005/02/16 19:57:06  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 15:02:46  mch
Initial Checkin

Revision 1.1.2.1  2005/01/26 14:42:59  mch
Separating slinger and scapi

Revision 1.1.2.9  2004/12/06 21:03:16  mch
Fixes to resolving homespace

Revision 1.1.2.8  2004/12/03 11:50:19  mch
renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

Revision 1.1.2.7  2004/11/29 19:30:13  mch
Some fixes to myspace source resolving, and moved loginaccount to a new directory

Revision 1.1.2.6  2004/11/25 18:28:21  mch
More tarting up

Revision 1.1.2.5  2004/11/25 01:28:59  mch
Added mime type to outputchild

Revision 1.1.2.4  2004/11/24 21:15:18  mch
Lots of nice UI changes

Revision 1.1.2.3  2004/11/23 17:46:52  mch
Fixes etc

Revision 1.1.2.2  2004/11/22 14:20:01  mch
Copied in MySpaceIt05Delegate

Revision 1.1.2.1  2004/11/22 00:46:28  mch
New Slinger Package


 */

