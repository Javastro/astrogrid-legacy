package org.astrogrid.slinger.myspace.it05;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.xml.rpc.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.slinger.StoreException;
import org.astrogrid.slinger.myspace.MSRL;


/**
 * This is a copy, but with some fixes, of the storeclient MySpace Delegate.
 * By putting it in here, it can be cut down (hopefully to vanishing point)
 * without affecting the storeclient and any applications dependent on it.
 *
 * @author MCH
 */

//
// Note: Throughout this class the acronym `MSS' denotes a MySpace
// Service and AGSL denotes an AstroGrid Store-point Locator.

public class MySpaceIt05Delegate  {
   
    /**
     * Commons logger
     */
    private static final Log log = LogFactory.getLog(MySpaceIt05Delegate.class);

   /** Axis-generated binding = inner delegate */
   private Manager     innerDelegate = null; // Inner delegate.
   /** Location of Manager */
   private MSRL managerMSRL = null; // Location of the Manager.
   /** Account that is operating this delegate - eg user that is browsing myspace */
   private User operator = null;    // User of the delegate

   private boolean allowOverWrite = true;

//
// ======================================================================

//
// Constructors.

/**
 * Constructs delegate that is operated by the given User, to connect to the
 * MySpace Manager at the given endPoint.
 */

   public MySpaceIt05Delegate(User givenOperator, String givenEndPoint) throws IOException
   {
      this.operator = givenOperator;
//      System.out.println("entered MyspaceIt05Delegate: operator = "
//        + operator.toString() + " endpoint = " + endPoint);

      if (!givenEndPoint.startsWith(MSRL.SCHEME)) {
         givenEndPoint = MSRL.SCHEME+":"+givenEndPoint;
      }
      
      managerMSRL = new MSRL(givenEndPoint);
      
      log.debug("the endpoint in myspaceitn05delegate = " + managerMSRL.getDelegateEndpoint());
      try
      {  ManagerService service = new ManagerServiceLocator();
         innerDelegate = service.getAstrogridMyspace(managerMSRL.getDelegateEndpoint());
      }
      catch (ServiceException e)
      {  throw new IOException
           ("Failed to connect to: " + givenEndPoint );
      }
   }
   




// ----------------------------------------------------------------------

   /**
    * Helper function for converting EntryResults to MySpaceFile/Folder
    * returns the name (ie last path token) of an EntryResults
    */
   public String getNameOf(EntryResults entryResult) {
      String path = entryResult.getEntryName();

      //remove trailing slash if any - this just tells us that it's a directory,
      //but not hainvg one doens't mean it isn't a directory, so ignore
      if (path.endsWith("/")) {
         path = path.substring(0,path.length());
      }

      if (path.lastIndexOf("/") == -1) {
         return path;
      }
      
      return path.substring(path.lastIndexOf("/")+1);
   }
   
   /**
    * Helper function for converting EntryResults to MySpaceFile/Folder
    * returns the path only (ie not including name) of an EntryResults
    */
   public String getParentPathOf(EntryResults entryResult) {
      String fullPath = entryResult.getEntryName();

      //remove trailing slash if any - this just tells us that it's a directory,
      //but not hainvg one doens't mean it isn't a directory, so ignore
      if (fullPath.endsWith("/")) {
         fullPath = fullPath.substring(0,fullPath.length());
      }

      if (fullPath.lastIndexOf("/") == -1) {
         return "";
      }
      
      return fullPath.substring(0,fullPath.lastIndexOf("/"));
   }

   /**
    * Helper function for converting EntryResults to MySpaceFile/Folder
    * Creates a MySpaceFile/Folder from the given entry result
    */
   public MySpaceFile makeStoreFile(MySpaceFolder parent, EntryResults result) {
System.out.println("FROG : makeStoreFile()");
System.out.println("  Name : " + result.getEntryName());
System.out.println("  Size : " + result.getSize());

      if (result.getType() == EntryCodes.CON) {
         if (parent == null) {
            return new MySpaceFolder();
         }
         else {
            return new MySpaceFolder(parent, result);
         }
      }
      else {
         if (parent == null) {
            //make up a folder that has the right path
            parent = new MySpaceFolder(getParentPathOf(result));
            
            return new MySpaceFile(parent, result);
         }
         else {
            return new MySpaceFile(parent, result);
         }
      }
   }

   /**
 * Return a tree representation of the files that match the expression
 */

   public MySpaceFile getFiles(String filter) throws IOException
   {
      //returns a list of the files that match the expression
      KernelResults results = innerDelegate.getEntriesList(filter, false);

      this.checkStatusMessages(results, "getting file tree from "+filter);

      if (results == null) {
         return null;
      }
      
      //turn list into a tree of StoreFiles
      
      Object[] fileList =  results.getEntries();

      if (fileList == null) {
         return null;
      }
      
      MySpaceFolder root = new MySpaceFolder(filter.substring(0,filter.lastIndexOf("/")));
      
      //represent entries
      for (int r=0;r<fileList.length;r++) {
         
         EntryResults result = (EntryResults) fileList[r];
         
         //Work out parent. Assumes parent folders appear in list before the children do.
         MySpaceFolder parentFolder;
         //Start with finding parent part of path
         String path = result.getEntryName();
         //remove trailing slash if any - this just tells us that it's a directory,
         //but not hainvg one doens't mean it isn't a directory, so ignore
         if (path.endsWith("/")) {
            path = path.substring(0,path.length());
         }
         //remove starting slash as we don't care if it has one of those or not
         if (path.startsWith("/")) {
            path = path.substring(1);
         }
         if (path.indexOf("/")==-1) {
            //no other slashes - must be a root directory file
            parentFolder = root;
         }
         else {
            String parentPath = path.substring(0, path.lastIndexOf("/")+1);
            try {
               parentFolder = (MySpaceFolder) root.findFile(parentPath);
            }
            catch (FileNotFoundException fnfe) {
               //ignore - might not be working from the root directory, eg frog's homespace
               parentFolder = root;
            }
         }


         //work out type & create right instance
         MySpaceFile file = makeStoreFile(parentFolder, result);
         parentFolder.add(file);
      }
      
      return root;
   }



// ----------------------------------------------------------------------

/**
 * Returns the StoreFile representation of the file at the given path
 */

   public MySpaceFile getFile(String path) throws IOException
   {
System.out.println("FROG : getFile()");
System.out.println("  Path : " + path);

      if (!path.startsWith("/")) path = "/"+path;
      
      KernelResults results = innerDelegate.getEntriesList(path, false);

//
//   Append and check any status messages.

      this.checkStatusMessages(results, "getting file details from "+path);

//
//   Obtain the file which matched the query.  This file is taken
//   to be the first entry in the return array.

      if (results.getEntries() == null) {
         return null;
      }
      else  {
         return makeStoreFile(null, (EntryResults) results.getEntries()[0]);
      }
   }


// ----------------------------------------------------------------------

/**
 * Put the given byte buffer from offset of length bytes, to the given target
 */
   public void putBytes(byte[] bytes, int offset, int length,
     String targetPath, boolean append) throws IOException
   {

      if (!targetPath.startsWith("/")) targetPath = "/"+targetPath;
//
//   Determine how a pre-existing file with the specified name is
//   to be dispatched.

      int dispatchExisting = ManagerCodes.LEAVE;
      if (append)
      {  dispatchExisting = ManagerCodes.APPEND;
      }
      else
      {  if (this.allowOverWrite)
         {  dispatchExisting = ManagerCodes.OVERWRITE;
         }
         else
         {  dispatchExisting = ManagerCodes.LEAVE;
         }
      }

//
//   Extract the subset of the array which is to be sent.

      int numBytes = length - offset;
      byte[] subsetToSend = new byte[length];

      for (int loop=0; loop<numBytes; loop++)
      {  subsetToSend[loop] = bytes[loop + offset];
      }

//
//   Attempt to save the array of bytes as a file.
//m
//   [TODO] The current StoreClient interface has no mechanism for
//   passing the category of file (VOTable etc.), so here it is set
//   to UNKNOWN.

      KernelResults results = innerDelegate.putBytes(targetPath,
        subsetToSend, EntryCodes.UNKNOWN, dispatchExisting, false);

//
//   Append and check any status messages.

      this.checkStatusMessages(results, "putting bytes to "+targetPath);

   }





// ----------------------------------------------------------------------

/**
 * Streaming output - returns a stream that can be used to output to the
 * given location.
 */

   public OutputStream putStream(String targetPath, boolean append) throws IOException
   {
      if (targetPath == null) {
         throw new IllegalArgumentException("No path given to put stream to");
      }

      if (!targetPath.startsWith("/")) targetPath = "/"+targetPath;
      
      return new MySpaceOutputStream(targetPath, append);
   }


// ----------------------------------------------------------------------

/**
 * Get a file's contents as a stream
 */
   public InputStream getStream(String sourcePath) throws IOException
   {

     if (!sourcePath.startsWith("/")) sourcePath = "/"+sourcePath;

      /*
//
//  [TODO]: This implementation is really a placeholder.  The
//   entire file contents are returned from the Manager in one call
//   and then merely streamed from a String in the delegate.
//   Returning the contents from the Manager to the delegate should
//   be streamed.

      byte[] contents = this.getBytes(sourcePath);

      ByteArrayInputStream inStream =
        new ByteArrayInputStream(contents);

      return inStream;
       */

      //streamed implementation, but getUrl is deprecated...  Need some
      //sort of getBytes() a bit at a time.
      URL url = getUrl(sourcePath);

      if (url == null)
      {  throw new FileNotFoundException(
           "Failed to find URL for path: " + sourcePath+" on "+managerMSRL);
      }

      return url.openStream();
   }


// ----------------------------------------------------------------------
   
/**
 * Get the URL to the given file.  This is a short term method; urls are not
    * appropriate to access private files
 */

   public URL getUrl(String path) throws IOException
   {
System.out.println("") ;
System.out.println("MySpaceDelegate.getUrl") ;
System.out.println("  Path : " + path) ;

      if (!path.startsWith("/")) path = "/"+path;
      
      KernelResults results = innerDelegate.getEntriesList(path, false);

      this.checkStatusMessages(results, "getting URL of "+path);

      if ((results == null)  || (results.getEntries() == null)) {
         return null;
      }

      Object[] entries = results.getEntries();
      EntryResults entry = (EntryResults) entries[0];
      
      //this seems to return a localhost
      URL url = new URL(entry.getEntryUri());
System.out.println("  URL  : " + url.toString()) ;
      
      // >>> HACK ALERT!
      // >>> @author peter.shillan
      // This "hack" gets around the problem of being returned localhost URLs.
      // The preferred way is to configure MySpace with the full server name.
      // The problem only arises if the servers are co-located and the solution works
      // in these circumstances.
      String host = url.getHost();
System.out.println("  Host : " + host) ;


      // If localhost is returned, try to work out the real host name.
      if(host.equalsIgnoreCase("localhost")) {
        String protocol = url.getProtocol();
        int port = url.getPort();
        String urlPath = url.getPath();
        
        try {
          InetAddress addr = InetAddress.getLocalHost();
          String fullHostName = addr.getCanonicalHostName();
          url = new URL(protocol, fullHostName, port, urlPath);
        }
        catch(UnknownHostException e) {
          // Default url will be used.
        }
      }
      // <<< HACK ALERT!
System.out.println("  URL  : " + url.toString()) ;
      return url;
   }




/**
 * Given a results object returned by the inner delegate, the array
 * of status results are extracted, appended to the delegate's internal
 * list and checked to see whether any correspond to an error.
 *
 * @param results Results object returned by the inner delegate.
 */

   public static void checkStatusMessages(KernelResults results, String message) throws StoreException
   {
      Object[] statusResults = results.getStatusList();

      for(int loop=0; loop<statusResults.length; loop++)
      {
         StatusMessage status = new StatusMessage( (StatusResults)statusResults[loop] );

         if (status.getSeverity() == StatusCodes.ERROR)
         {
            throw new StoreException(status.getMessage()+", "+message);
         }
      }
   }


// ----------------------------------------------------------------------

/**
 * Special OutputStream which writes to the myspace in lumps of bytes.
 */

   private class MySpaceOutputStream extends OutputStream
   {  private String targetPath = null;

      private byte[] buffer = new byte[32000];
      private int cursor = 0;  //insert point


      public MySpaceOutputStream(String aTargetPath, boolean append)
        throws IOException
      {  this.targetPath = aTargetPath;

//
//      If the new stream is not being appended to an existing file
//      then attempt to overwrite any existing file.

         if (!append)
         {
            KernelResults results = innerDelegate.putString(targetPath,"", EntryCodes.UNKNOWN, ManagerCodes.OVERWRITE, false);
            checkStatusMessages(results, "appending to "+aTargetPath);
         }
      }

      /** OutputStream method implementation.  All writes go through this method -
       * it batches up the low byte of each character 'i' into a buffer, and
       * when the buffer is full 'flushes' it to the server using writeString()
       * with append on.
       */
      public void write(int i) throws IOException
      {
//
//      Get low byte - streams do not do words.

         buffer[cursor] = (byte) (i & 0xFF);
         
         cursor++;
         if (cursor >= buffer.length)
         {  flush();
         }
      }


      public void flush() throws IOException
      {
//
//      Append string to file - cursor = length

         putBytes(buffer, 0, cursor, targetPath, true);
         
         cursor=0;
      }

      /** For human readable debugging */
      public String toString() {
         return "MySpaceOutputStream ["+managerMSRL+"#"+targetPath+"]";
      }

      public void close() throws IOException
      {  flush();
         super.close();
      }
   }

   
   /**
    * Inner class that implements StoreFile.  It's useful to have it as an
    * inner class so that it can access private methods of the delegate to
    * getParent, children, etc
    */
   public class MySpaceFile  {
      
      String name = null; //just file or folder name, not path
      String owner = null;
      Date created = null;
      Date expires = null;
      long size = -1;
      String permissions = null;
      URL url = null;

      String mime = null ;
      MySpaceFileType type = null;
      MySpaceFolder parentFolder = null;

      /** Constructs an empty file - suitable eg for root */
      private MySpaceFile() {
      }
      
      /**
       * Constructs a file from the given parent folder and the axis-generated
       * class returned by the binding
       */
      public MySpaceFile(MySpaceFolder parent, EntryResults bindingEntry)  {
   
         this.parentFolder = parent;
         this.name = getNameOf(bindingEntry);
         
         this.owner = bindingEntry.getOwnerId();
         this.created = new Date(bindingEntry.getCreationDate());
         this.expires = new Date(bindingEntry.getExpiryDate());
         this.size = bindingEntry.getSize();
         this.permissions = bindingEntry.getPermissionsMask();
         this.mime = bindingEntry.getMime() ;
         this.type = MySpaceFileType.getForManagerRef(bindingEntry.getType());
         try {
            this.url = new URL(bindingEntry.getEntryUri());
         }
         catch (MalformedURLException mue) {
            //log but don't crash
            log.error("Server returned invalid URL '"+bindingEntry.getEntryUri()+"' for entry "+bindingEntry.getEntryName());
         }
      }
      
      public String getType() {        return type.toString();   }
      
      public String toString() {       return getName();   }
      
      public String getOwner() {       return owner; }
      
      /** Returns the date the file was created */
      public Date getCreated() {       return created; }
         
      public Date getExpires() {       return expires; }
   
      public long getSize() {          return size; }
   
      public String getPermissions() { return permissions; }
   
      /** Returns the mime type (null if unknown) */
      public String getMimeType() {    return mime ; }
      
      /** Returns the date the file was last modified (null if unknown) */
      public Date getModified() {      return null;  }
      
      /** Returns URL to the file */
      public URL getUrl() {            return url; }
      
      /** Returns the path to this file on the server including filename */
      public String getPath() {
         return getParent().getPath()+name;
      }
      
      /** Returns true if this is a container that can hold other files/folders */
      public boolean isFolder()     {     return false;   }
      
      /** Returns true if this is a self-contained file.  For example, a database
       * table might be represented as a StoreFile but it is not a file */
      public boolean isFile() {           return true;   }
      
      /** Returns file or folder name */
      public String getName() {
         return name;
      }
      
      /** Returns the parent folder */
      public MySpaceFile getParent() {
         return parentFolder;
      }
   
      
      /** Lists children files if this is a container - returns null otherwise */
      public MySpaceFile[] listFiles() {    return null;   }
      
      /** Returns true if this represents the same file as the given one */
      public boolean equals(MySpaceFile anotherFile) {
         if (anotherFile instanceof MySpaceFile) {
            return name.equals( ((MySpaceFile) anotherFile).name) &&
                  parentFolder.equals(((MySpaceFile) anotherFile).parentFolder);
         }
         return false;
      }
   }

   /**
    * Represents a folder in myspace.
    * See also comments on @link MySpaceFile
    *
    * @author mch
    */
   private class MySpaceFolder extends MySpaceFile  {
      
      Hashtable children = new Hashtable();
   
      boolean isRoot = false;
      
      /** Creates a folder that is a child of another one. */
      public MySpaceFolder(MySpaceFolder parent, EntryResults bindingEntry)  {
         super(parent, bindingEntry);
      }
   
      /** Creates the root folder */
      public MySpaceFolder() {
         super();
         isRoot = true;
      }
   
      /** Creates a folder from a path */
      public MySpaceFolder(String givenPath) {
         super();
         if (givenPath.startsWith("/")) {
            givenPath = givenPath.substring(1); //chop off leading slash
         }
         this.name = givenPath;
      }

      /** Adds the given StoreFile as a child that exists in this folder */
      public void add(MySpaceFile child) {
         children.put(child.getName(), child);
      }
   
      /** Returns the StoreFile representation of the child with the given filename */
      public MySpaceFile getChild(String filename) {
         return (MySpaceFile) children.get(filename);
      }
      
      /** Returns an array of the files in this container */
      public MySpaceFile[] listFiles() {
         return (MySpaceFile[]) (children.values().toArray(new MySpaceFile[] {}));
      }
   
      /** Returns path on server */
      public String getPath() {
         if (isRoot) {
            return "";
         }
         else {
            if (getParent() == null) {
               //temporary allowance for folders that describe a path
               return getName()+"/";
            }
            else {
               return getParent().getPath()+getName()+"/";
            }
         }
      }
      
      /** Returns true - this is a container */
      public boolean isFolder() {      return true;   }
      
      /** Returns false - this is a container */
      public boolean isFile() {        return false;  }
      
      /**
       * Returns the folder or file matching the given path in the *children* of
       * this folder.  So if the path is '/famous/stuff/main/' the returned StoreFile
       * will be the MySpaceFolder instance representing the 'main' directory
       */
      public MySpaceFile findFile(String path) throws FileNotFoundException {

         //remove part of path that matches this folder
         if (path.startsWith(getPath())) {
            path = path.substring(getPath().length());
         }
         
         if (path.trim().length()==0) {
            return this;
         }
         
         //locate file
         StringTokenizer dirTokens = new StringTokenizer(path, "/");
         MySpaceFolder folder = this;
         MySpaceFile child = null;
         while (dirTokens.hasMoreTokens())
         {
            String token = dirTokens.nextToken();
            child = folder.getChild(token);
            if (child == null) {
               throw new FileNotFoundException("No such token '"+token+"' in path '"+path+"' from "+this);
            }
            else {
               if (child.isFolder()) {
                  folder = (MySpaceFolder) child;
               }
            }
         }
         
         if (dirTokens.hasMoreTokens()) {
            throw new FileNotFoundException("path "+path+" only partly found from "+this);
         }
         
         return child;
         
      }
      
   }
}

/*
$Log: MySpaceIt05Delegate.java,v $
Revision 1.2  2005/01/26 17:31:57  mch
Split slinger out to scapi, swib, etc.

Revision 1.1.2.1  2005/01/26 14:35:23  mch
Separating slinger and scapi

Revision 1.1.2.4  2004/12/06 21:03:16  mch
Fixes to resolving homespace

Revision 1.1.2.3  2004/11/24 21:15:18  mch
Lots of nice UI changes

Revision 1.1.2.2  2004/11/23 17:46:52  mch
Fixes etc

Revision 1.1.2.1  2004/11/22 14:20:01  mch
Copied in MySpaceIt05Delegate

Revision 1.11  2004/11/07 22:19:57  mch
added endpoint to exception

Revision 1.10  2004/11/05 17:39:59  mch
Added outputstream.toString with useful info

Revision 1.9  2004/09/02 10:25:41  dave
Updated FileStore and MySpace to handle mime type and file size.
Updated Community deployment script.

Revision 1.8.2.1  2004/09/02 00:01:45  dave
Extended EntryResults and delegate MySpaceFile to handle mime type.

Revision 1.8  2004/08/27 22:43:15  dave
Updated filestore and myspace to report file size correctly.

Revision 1.7.12.2  2004/08/27 16:17:48  dave
....

Revision 1.7.12.1  2004/08/27 16:16:15  dave
Added temp debug ...

Revision 1.7  2004/08/18 19:00:01  dave
Myspace manager modified to use remote filestore.
Tested before checkin - integration tests at 91%.

Revision 1.6  2004/08/05 15:37:25  mch
Fix for empty myspaces

Revision 1.5.26.2  2004/08/09 12:37:58  dave
Added debug to delegate ...

Revision 1.5.26.1  2004/08/06 22:25:03  dave
Refactored bits and broke a few tests ...

Revision 1.5  2004/06/29 14:20:01  gps
- making clear note of the "localhost hack"

Revision 1.4  2004/06/24 11:45:53  gps
- removed extraneous code from getUrl()

Revision 1.3  2004/06/24 11:43:08  gps
- fixed 'localhost' return problem in getUrl()

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

Revision 1.29  2004/05/19 16:24:33  mch
Properly typed Agsl creation, some fixes to tests

Revision 1.28  2004/05/14 12:53:06  mch
Added assertion to getAgsl() for folders

Revision 1.27  2004/05/14 09:39:55  mch
Fixed prepended myspace to endpoint MSRL

Revision 1.26  2004/05/12 14:48:01  mch
Fixed endpoint AGSL error

Revision 1.25  2004/05/12 09:01:02  mch
Prepended myspace: to endpoints on constructor

Revision 1.24  2004/05/04 14:36:29  jdt
Javadoc comments were interfering with each other.

Revision 1.23  2004/05/03 14:46:22  mch
Fixes for int tests

Revision 1.22  2004/05/03 13:39:40  mch
Removed dependencies on EntryRecord and EntryNode

Revision 1.21  2004/05/03 08:55:53  mch
Fixes to getFiles(), introduced getSize(), getOwner() etc to StoreFile

 Added inner classes to represent files.
 Bug fixes: firstChunk not threadsafe
            constructor setting wrong endpoint
 */

