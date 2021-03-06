package org.astrogrid.store.delegate.myspaceItn05;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.xml.rpc.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.StoreAdminClient;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreException;
import org.astrogrid.store.delegate.StoreFile;


/**
 * <code>MySpaceIt05Delegate</code> is the delegate class which
 * applications invoke in order to access a MySpace service.
 *
 * @author A C Davenhall (Edinburgh)
 * @author C L Qin (Leicester)
 * @since Iteration 5.  There has been a MySpace delegate since
 *   Iteration 2.  The present delegate dates from Iteration 5.
 * @version Iteration 5.
 */

//
// Note: Throughout this class the acronym `MSS' denotes a MySpace
// Service and AGSL denotes an AstroGrid Store-point Locator.

public class MySpaceIt05Delegate implements StoreClient, StoreAdminClient {
   
    /**
     * Commons logger
     */
    private static final Log logger = LogFactory.getLog(MySpaceIt05Delegate.class);

   /** Axis-generated binding = inner delegate */
   private Manager     innerDelegate = null; // Inner delegate.
   /** Location of Manager */
   private Msrl managerMsrl = null; // Location of the Manager.
   /** Account that is operating this delegate - eg user that is browsing myspace */
   private User operator = null;    // User of the delegate [TODO] Account?.

   private boolean isTest = false;
   private boolean throwExceptions = true;
   private boolean allowOverWrite = true;

   private ArrayList statusList = new ArrayList(); // List of Status messages.

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

      if (!givenEndPoint.startsWith(Msrl.SCHEME)) {
         givenEndPoint = Msrl.SCHEME+":"+givenEndPoint;
      }
      
      managerMsrl = new Msrl(givenEndPoint);
      
      logger.debug("the endpoint in myspaceitn05delegate = " + managerMsrl.getDelegateEndpoint());
      try
      {  ManagerService service = new ManagerServiceLocator();
         innerDelegate = service.getAstrogridMyspace(managerMsrl.getDelegateEndpoint());
      }
      catch (ServiceException e)
      {  throw new IOException
           ("Failed to connect to: " + givenEndPoint );
      }
   }
   

//
// ======================================================================
//
// Get, set and related methods.
//
// These methods are mostly concerned with configuring the internal
// state of the delegate and with retrieving information from it.


/**
 * Specify whether the Manager is being used in test or genuine mode.
 *
 * In test mode the Manager will return standard responses, irrespective
 * of whether it contains any entries.  In genuine mode the Manager
 * genuinely attempts to manipulate its data holdings.
 *
 * @param isTest Flag indicating whether in test or genuine mode: true for
 *    test mode and false otherwise.
 */

   public void setTest(boolean isTest)
   {  this.isTest = isTest;
   }


/**
 * Specify whether a bad status being returned by the Manager will
 * cause the delegate to throw an exception.
 *
 * @param thowExceptions Flag indicating whether or not bad status
 *    returns cause an exception to be throw: true to throw exceptions
 *    and false otherwise.
 */

   public void setThrow(boolean throwExceptions)
   {  this.throwExceptions = throwExceptions;
   }


/**
 * Specify whether the Manager is allowed to overwrite an existing
 * file when importing a new one.
 *
 * param allowOverWrite Flag indicating whether or not overwriting is
 *   allowed.  True indicates that it is.
 */

   public void setOverWrite(boolean allowOverWrite)
   {  this.allowOverWrite = allowOverWrite;
   }


/**
 * Return a list of error codes accummulated by successive
 * invocations of action methods.
 *
 * @return List of error codes, each of type <code>StatusMessage</code>.
 */

   public ArrayList getStatusList()
   {  return statusList;
   }

/**
 * Convenience method to list the error codes.  The codes accummulated
 * by successive invocations of action methods to standard output.  Note
 * that this method does not reset the list of error codes.
 */

   public void outputStatusList()
   {  int numMessages = statusList.size();

      if (numMessages > 0)
      {  for(int loop=0; loop<numMessages; loop++)
         {  StatusMessage message =
             (StatusMessage)statusList.get(loop);
            logger.debug(message.toString() );
         }
      }
      else
      {  logger.debug("No messages returned.");
      }
   }

/**
 * Reset the list of error codes to contain zero entries.
 */

   public void resetStatusList()
   {  statusList.clear();
   }


//
// ======================================================================
//
// StoreClient methods.
//
// The following Action methods implement the StoreClient interface.

/**
 * Return the User of this delegate - ie the account it is being used by
 *
 * @return The User of this delegate.
 */
   public User getOperator()
   {  return operator;
   }
   
// ----------------------------------------------------------------------

/**
 * Return the Agsl of the service to which this client is connected.
 *
 * @return The Agsl of the service to which this client is connected.
 */
   public Agsl getEndpoint()
   {
      Agsl agsl = new Agsl( managerMsrl );
      return agsl;
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
   public StoreFile makeStoreFile(MySpaceFolder parent, EntryResults result) {
logger
        .info("makeStoreFile(MySpaceFolder, EntryResults) - FROG : makeStoreFile()");
logger.info("makeStoreFile(MySpaceFolder, EntryResults) -   Name"
        + result.getEntryName());
logger.info("makeStoreFile(MySpaceFolder, EntryResults) -   Size"
        + result.getSize());

      StoreFile file;
      if (result.getType() == EntryCodes.CON) {
         if (parent == null) {
            file = new MySpaceFolder();
         }
         else {
            file = new MySpaceFolder(parent, result);
         }
      }
      else {
         if (parent == null) {
            //make up a folder that has the right path
            parent = new MySpaceFolder(getParentPathOf(result));
            
            file = new MySpaceFile(parent, result);
         }
         else {
            file = new MySpaceFile(parent, result);
         }
      }
logger.info("makeStoreFile(MySpaceFolder, EntryResults) -   Size"
        + file.getSize());
      return file;
   }

   /**
 * Return a tree representation of the files that match the expression
 */

   public StoreFile getFiles(String filter) throws IOException
   {
      //returns a list of the files that match the expression
      KernelResults results = innerDelegate.getEntriesList(filter, isTest);

      this.appendAndCheckStatusMessages(results);

      if (results == null) {
         return null;
      }
      
      //turn list into a tree of StoreFiles
      
      Object[] fileList =  results.getEntries();

      if (fileList == null) {
         return null;
      }
      
      MySpaceFolder root = new MySpaceFolder();
      
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
            String parentPath = path.substring(0, path.lastIndexOf("/"));
            parentFolder = (MySpaceFolder) root.findFile(parentPath);
         }


         //work out type & create right instance
         StoreFile file = makeStoreFile(parentFolder, result);
         parentFolder.add(file);
      }
      
      return root;
   }


// ----------------------------------------------------------------------

/*
 * Return a list of all the files that match the expression
 *
 * @param filter Filter (or query) which the files must match.  Queries
 *   take the form of entry names which may optionally include a
 *   wild-card character.  This wild-card character is an asterisk and
 *   it must occur at the end of the name.
 * @return Array of <code>EntryRecord</code>s which satisfy the query.
 *   If no entries satisfy the query a null value is returned.
 *

   public StoreFile[] listFiles(String filter) throws IOException
   {
      KernelResults results = innerDelegate.getEntriesList(filter, isTest);

      this.appendAndCheckStatusMessages(results);

      if (results == null) {
         return null;
      }
//
//   Assemble an array of any files which matched the query.
//   Each file is returned as an EntryResults object, which is
//   converted to the corresponding EntryRecord.

      //copy list into a list of StoreFiles
      Object[] resultList = results.getEntries();
      StoreFile[] fileList = new StoreFile[resultList.length];
      
      for(int loop=0; loop<resultList.length; loop++)
      {
         fileList[loop] = makeStoreFile(null, (EntryResults) resultList[loop]);
      }

      return fileList;
   }
*/

// ----------------------------------------------------------------------

/**
 * Returns the StoreFile representation of the file at the given AGSL
 */

   public StoreFile getFile(String path) throws IOException
   {
logger.info("getFile(String) - FROG : getFile()");
logger.info("getFile(String) -   Path" + path);

      if (!path.startsWith("/")) path = "/"+path;
      
      KernelResults results = innerDelegate.getEntriesList(path, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

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


   /**
    * Returns the Agsl for the given source path.  NB = no such thing for
    * folders at the moment
    */
   public Agsl getAgsl(String sourcePath) throws IOException {
      assert (!sourcePath.endsWith("/") && !sourcePath.endsWith("\\")) : "Cannot create AGSLs for folders (yet)";
      
      return new Agsl(getEndpoint(), sourcePath);
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
        subsetToSend, EntryCodes.UNKNOWN, dispatchExisting, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------

/**
 * Put the given string into the given location
 */
   public void putString(String contents, String targetPath, boolean append)
      throws IOException
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
//   Attempt to save the String as a file.
//
//   [TODO] The current StoreClient interface has no mechanism for
//    passing the category of file (VOTable etc.), so here it is set
//    to UNKNOWN.

      KernelResults results = innerDelegate.putString(targetPath,
        contents, EntryCodes.UNKNOWN, dispatchExisting, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------

/**
 * Copy the contents of the file at the given source url to the given
 * location.
 */
   public void putUrl(URL source, String targetPath, boolean append)
     throws IOException
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
//   Attempt to save the URL as a file.
//
//   [TODO] The current StoreClient interface has no mechanism for
//    passing the category of file (VOTable etc.), so here it is set
//    to UNKNOWN.

      String uri = source.toString();
      KernelResults results = innerDelegate.putUri(targetPath,
        uri, EntryCodes.UNKNOWN, dispatchExisting, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------

/**
 * Streaming output - returns a stream that can be used to output to the
 * given location.
 */

   public OutputStream putStream(String targetPath, boolean append)
     throws IOException
   {
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
           "Failed to find URL for path: " + sourcePath+" on "+managerMsrl);
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
logger.info("getUrl(String)");
logger.info("getUrl(String) - MySPaceDeletage.getUrl");
logger.info("getUrl(String) -   Path" + path);

      if (!path.startsWith("/")) path = "/"+path;
      
      KernelResults results = innerDelegate.getEntriesList(path, isTest);

      this.appendAndCheckStatusMessages(results);

      if ((results == null)  || (results.getEntries() == null)) {
         return null;
      }

      Object[] entries = results.getEntries();
      EntryResults entry = (EntryResults) entries[0];
      
      //this seems to return a localhost
      URL url = new URL(entry.getEntryUri());
logger.info("getUrl(String) -   URL " + url.toString());
      
      // >>> HACK ALERT!
      // >>> @author peter.shillan
      // This "hack" gets around the problem of being returned localhost URLs.
      // The preferred way is to configure MySpace with the full server name.
      // The problem only arises if the servers are co-located and the solution works
      // in these circumstances.
      String host = url.getHost();
logger.info("getUrl(String) -   Host" + host);


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
logger.info("getUrl(String) -   URL " + url.toString());
      
      return url;
   }


// ----------------------------------------------------------------------

/**
 * Delete a file.
 */

   public void delete(String deletePath) throws IOException
   {
      if (!deletePath.startsWith("/")) deletePath = "/"+deletePath;
      
      KernelResults results = innerDelegate.deleteFile(deletePath, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------

   /**
    * Copy a file to a target Agsl.
    * Because myspace can 'pull' better than push, this calls the target Agsl
    * store (if different from this one) with a putUrl to the sourcepath
    */

   public void copy(String sourcePath, Agsl target) throws IOException
   {
/*
 *
 *
      if (target.getEndpoint().equals(getEndpoint())) {
         String targetPath = target.getPath();
         if (!targetPath.startsWith("/")) targetPath = "/"+targetPath;
         if (!sourcePath.startsWith("/")) sourcePath = "/"+sourcePath;
      
         KernelResults results = innerDelegate.copyFile(sourcePath,
               targetPath, isTest);
//
//   Append and check any status messages.

         this.appendAndCheckStatusMessages(results);

      }
      else {
         StoreClient targetStore = StoreDelegateFactory.createDelegate(operator, target);
         targetStore.putUrl(getUrl(sourcePath), target.getPath(), false);

      }
 *
 *
 */
logger.info("copy(String, Agsl) - ----");
logger.info("copy(String, Agsl) - MySpaceIt05Delegate.copy");
logger.info("copy(String, Agsl) -   Source path : '" + sourcePath + "'");
logger.info("copy(String, Agsl) -   Target agsl : '" + target.toString() + "'");

      URL url = getUrl(sourcePath) ;
// BANG !!
logger.info("copy(String, Agsl) -   Source URL  : '" + url.toString() + "'");

logger.info("copy(String, Agsl)");
logger.info("copy(String, Agsl) - Creating target delegate ....");
      StoreClient targetStore =
         StoreDelegateFactory.createDelegate(
            operator,
            target
            );
logger.info("copy(String, Agsl) - Done");

logger.info("copy(String, Agsl)");
logger.info("copy(String, Agsl) - Calling target delegate ....");
      targetStore.putUrl(
         url,
         target.getPath(),
         false
         );
logger.info("copy(String, Agsl) - Done");

   }


// ----------------------------------------------------------------------

/**
 * Copy a file from a source Agsl.
 */

   public void copy(Agsl source, String targetPath) throws IOException
   {
      if (source.getEndpoint().equals(getEndpoint())) {
         String sourcePath = source.getPath();
         if (!targetPath.startsWith("/")) targetPath = "/"+targetPath;
         if (!sourcePath.startsWith("/")) sourcePath = "/"+sourcePath;
      
         KernelResults results = innerDelegate.copyFile(sourcePath, targetPath, isTest);

//
//   Append and check any status messages.

         this.appendAndCheckStatusMessages(results);
      }
      else
      {
         StoreClient sourceStore = StoreDelegateFactory.createDelegate(operator, source);
         
         putUrl(sourceStore.getUrl(source.getPath()), targetPath, false);
      }
   }

// ----------------------------------------------------------------------
   
/**
 * Move (or rename) a file to a target Agsl.
 */

   public void move(String sourcePath, Agsl target) throws IOException
   {
      //could call innerdelegate move if target/source stores are the same
      
      copy(sourcePath, target);
      delete(sourcePath);
   }


// ----------------------------------------------------------------------

/**
 * Moves (or rename) a file from a source Agsl.
 */

   public void move(Agsl source, String targetPath) throws IOException
   {
      //could call innerdelegate move if target/source stores are the same
      
      copy(source, targetPath);
      StoreDelegateFactory.createDelegate(operator, source);
   }


// ----------------------------------------------------------------------
   
/**
 * Create a container.
 */

   public void newFolder(String targetPath) throws IOException
   {
      if (!targetPath.startsWith("/")) targetPath = "/"+targetPath;

      KernelResults results = innerDelegate.createContainer(targetPath, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);
   }


//
// ======================================================================
//
// StoreAdminClient methods.
//
// The following Action methods implement the StoreAdminClient interface.

/**
 * Create a new account on an MSS.
 *
 * <p>
 * `Create a new account' is something of a misnomer.  This method is
 * really just creating the base set of containers which every account
 * needs.
 * </p>
 *
 * @param newAccount Account of the user to be created.
 */

   public void createUser(User newAccount) throws IOException
   {  String account = newAccount.getUserId();
      KernelResults results = innerDelegate.createAccount(
        account, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------

/**
 * Delete an account from an MSS.
 *
 * <p>
 * `Delete an account' is something of a misnomer.  The method merely
 * removes all extant containers belonging to an account.  Any files
 * belonging to the account must have been removed previously.  However,
 * the method will remove any remaining arbitrarily complex tree of
 * containers.  If any files are found the account will be left
 * untouched.
 * </p>
 *
 * @param deadAccount Details of the account to be deleted.
 */

   public void deleteUser(User deadAccount) throws IOException
   {  String account = deadAccount.getUserId();
      KernelResults results = innerDelegate.deleteAccount(
        account, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ======================================================================

//
// Additional Action methods not in the StoreClient or StoreAdminClient
// interfaces.

/**
 * `Heart-beat' method to check whether the Manager is up and running.
 *
 * @return Return true if the Manager is responding; otherwise return
 *  false.
 */

   public boolean heartBeat() throws IOException
   {  boolean response = false;

      try
      {  String result = innerDelegate.heartBeat();
         if (result.equals("Adsum.") )
         {  response = true;
         }
      }
      catch (Exception all)
      {  response = false;
      }

      return response;
   }


// ----------------------------------------------------------------------

/**
 * Return the contents of a file as a String.
 *
 * @param targetPath Path to the file whose contents are to be
 *    retrieved.
 * @return The contents of the file.
 * @deprecated (extremely) - will break on large files.  Use getStream() instead.
 */
 public String getString(String targetPath) throws IOException
   {  String contents = "";

     if (!targetPath.startsWith("/")) targetPath = "/"+targetPath;
//
//   Attempt to retrieve the contents of the file as a String.

      KernelResults results = innerDelegate.getString(targetPath, isTest);

//
//   Obtain the retrieved contents from the results object.

      if (results.getContentsString() != null)
      {  contents = results.getContentsString();
      }

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

      return contents;
   }


// ----------------------------------------------------------------------

/**
 * Return the contents of a file as an array of bytes.
 *
 * @param targetPath Path to the file whose contents are to be
 *    retrieved.
 * @return The contents of the file.
 * @deprecated (extremely) - will break on large files.  Use getStream() instead.
 */
   public byte[] getBytes(String targetPath) throws IOException
   {  byte[] contents = null;

     if (!targetPath.startsWith("/")) targetPath = "/"+targetPath;
//
//   Attempt to retrieve the contents of the file as a String.

      KernelResults results = innerDelegate.getBytes(targetPath,
        isTest);

//
//   Obtain the retrieved contents from the results object.

      if (results.getContentsBytes() != null)
      {  contents = results.getContentsBytes();
      }

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

      return contents;
   }


// ----------------------------------------------------------------------

/**
 * Extend the lifetime of a file in an MSS.
 *
 * <p>
 * This method operates on multiple AGSLs by permitting wild-cards in
 * the AGSL path.
 * </p>
 *
 * @param fileName File name of the entry to be modified.
 * @param newExpiryDate New expiry date for the file.
 */

   public void extendLifetime(String fileName, Date newExpiryDate)
     throws IOException
   {  long expiry = newExpiryDate.getTime();
      KernelResults results = innerDelegate.extendLifetime(
        fileName, expiry, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------

/**
 * Change the owner of a MySpace file.
 *
 * <p>
 * This method operates on multiple AGSLs by permitting wild-cards in
 * the AGSL path.
 * </p>
 *
 * @param path Path (incl wild cards) of fiels to be modified.
 * @param newOwner Account of the new owner.
 */

   public void changeOwner(String path, User newOwner)
     throws IOException
   {  String owner = newOwner.getAccount();
      KernelResults results = innerDelegate.changeOwner(
        path, owner, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ======================================================================

//
// Additional internal methods.

/**
 * Given a results object returned by the inner delegate, the array
 * of status results are extracted, appended to the delegate's internal
 * list and checked to see whether any correspond to an error.
 *
 * @param results Results object returned by the inner delegate.
 */

   private void appendAndCheckStatusMessages(KernelResults results)
     throws StoreException
   {  boolean errorRaised = false;
      String messageFromManager = "none.";

      Object[] statusResults = results.getStatusList();
      int numStatus = Array.getLength(statusResults);

      if (numStatus > 0)
      {  StatusMessage status = new StatusMessage();

         for(int loop=0; loop<numStatus; loop++)
         {
//
//         Convert each statusResults object to a StatusMessage.

            status = new StatusMessage( (StatusResults)statusResults[loop] );

//
//         Add the StatusMessage to the accummulating list.

            this.statusList.add(status);

//
//         Check whether any errors have been raised, and if so preserve
//         the text associated with the first.

            if (status.getSeverity() == StatusCodes.ERROR)
            {  if (!errorRaised)
               {  messageFromManager = status.getMessage();
               }
               errorRaised = true;
            }
         }
      }

      if (errorRaised && throwExceptions)
      {  throw new StoreException(messageFromManager);
      }
   }


// ----------------------------------------------------------------------

/**
 * Special OutputStream which writes to a String, then sends it when the
 * stream is closed.
 */

   private class MySpaceOutputStream extends OutputStream
   {
    /**
     * Commons Logger for this class
     */
    private final Log logger = LogFactory.getLog(MySpaceOutputStream.class);
  private String targetPath = null;

      private byte[] buffer = new byte[32000];
      private int cursor = 0;  //insert point


      public MySpaceOutputStream(String aTargetPath, boolean append)
        throws IOException
      {  this.targetPath = aTargetPath;

//
//      If the new stream is not being appended to an existing file
//      then attempt to overwrite any existing file.

         if (!append)
         {  putString("", targetPath, false);
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
         return "MySpaceOutputStream ["+managerMsrl+"#"+targetPath+"]";
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
   private class MySpaceFile implements StoreFile {
    /**
     * Commons Logger for this class
     */
    private final Log logger = LogFactory.getLog(MySpaceFile.class);
      
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
            logger.error("Server returned invalid URL "+bindingEntry.getEntryUri()+" for entry "+bindingEntry.getEntryName());
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
      public StoreFile getParent() {
         return parentFolder;
      }
   
      
      /** Lists children files if this is a container - returns null otherwise */
      public StoreFile[] listFiles() {    return null;   }
      
      /** Returns true if this represents the same file as the given one */
      public boolean equals(StoreFile anotherFile) {
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
    /**
     * Commons Logger for this class
     */
    private final Log logger = LogFactory.getLog(MySpaceFolder.class);
      
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
      public void add(StoreFile child) {
         children.put(child.getName(), child);
      }
   
      /** Returns the StoreFile representation of the child with the given filename */
      public StoreFile getChild(String filename) {
         return (StoreFile) children.get(filename);
      }
      
      /** Returns an array of the files in this container */
      public StoreFile[] listFiles() {
         return (StoreFile[]) (children.values().toArray(new StoreFile[] {}));
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
      public StoreFile findFile(String path) throws FileNotFoundException {
         
         //locate file
         StringTokenizer dirTokens = new StringTokenizer(path, "/");
         MySpaceFolder folder = this;
         StoreFile child = null;
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
Revision 1.12  2005/01/13 11:27:39  jdt
Merges from myspace-nww-890

Revision 1.11.18.1  2005/01/12 15:43:34  nw
replaced System.out with logging.

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
Fixed prepended myspace to endpoint msrl

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

