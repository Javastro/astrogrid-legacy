package org.astrogrid.store.delegate.myspaceItn05;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.astrogrid.community.User;

import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;

import org.astrogrid.store.delegate.StoreAdminClient;
import org.astrogrid.store.delegate.StoreClient;
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

public class MySpaceIt05Delegate implements StoreClient, StoreAdminClient
{
//   private StoreClient itn05Delegate = null; // Itn. 05 delegate.
   private Manager     innerDelegate = null; // Inner delegate.
   private Msrl managerMsrl = null; // Location of the Manager.
   private String endPoint;         // End point of the Manager.
   private User operator = null;    // User of the delegate [TODO] Account?.

   private boolean isTest = false;
   private boolean throwExceptions = true;
   private boolean allowOverWrite = true;

   private ArrayList statusList = new ArrayList(); // List of Status messages.

   private static boolean firstChunk = true;

//
// ======================================================================

//
// Constructors.

/**
 * Constructor with no arguments.
 */

   public MySpaceIt05Delegate() throws IOException
   {
   }

/**
 * Constructor with specified User and endPoint.
 */

   public MySpaceIt05Delegate(User operator, String endPoint) 
     throws IOException
   {
      this.operator = operator;
      this.endPoint = endPoint;
//      System.out.println("entered MyspaceIt05Delegate: operator = " 
//        + operator.toString() + " endpoint = " + endPoint);

      //managerMsrl = new Msrl(new URL(endPoint));

      if (endPoint.startsWith(Msrl.SCHEME)) {
         endPoint = endPoint.substring(Msrl.SCHEME.length()+1);
      }
      System.out.println("the endpoint in myspaceitn05delegate = " + endPoint);
      try
      {  ManagerService service = new ManagerServiceLocator();
         innerDelegate = service.getAstrogridMyspace(
            new java.net.URL(endPoint) );
      }
      catch (Exception e)
      {  throw new IOException 
           ("Failed to create delegate for service: " + endPoint );
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
            System.out.println(message.toString() );
         }
      }
      else
      {  System.out.println("No messages returned.");
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
      try {
         Agsl agsl = new Agsl( Msrl.SCHEME + ":" + endPoint );      
         return agsl;
      }catch(MalformedURLException mue) {
         mue.printStackTrace();
      }
      return null;
   }


// ----------------------------------------------------------------------

/**
 * Return a tree representation of the files that match the expression
 */

   public StoreFile getFiles(String filter) throws IOException
   {  EntryNode fileRoot = new EntryNode();

      KernelResults results = innerDelegate.getEntriesList(filter,
        isTest);

//
//   Assemble an array of any files which matched the query.
//   Each file is returned as an EntryResults object, which is
//   converted to the corresponding EntryRecord.

      Object[] fileResults = results.getEntries();
      int numFiles = Array.getLength(fileResults);

      if (numFiles > 0)
      {  ArrayList fileList = new ArrayList();

         for(int loop=0; loop<numFiles; loop++)
         {  EntryResults file = 
              (EntryResults)fileResults[loop];
            fileList.add(file);
         }

         if (filter.equals("*") )
         {  filter = "/*";
         }

         fileRoot = new EntryNode(filter, fileList);

      }

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

      return fileRoot;
   }


// ----------------------------------------------------------------------

/**
 * Return a list of all the files that match the expression
 *
 * @param filter Filter (or query) which the files must match.  Queries 
 *   take the form of entry names which may optionally include a 
 *   wild-card character.  This wild-card character is an asterisk and
 *   it must occur at the end of the name.
 * @return Array of <code>EntryRecord</code>s which satisfy the query.
 *   If no entries satisfy the query a null value is returned.
 */

   public StoreFile[] listFiles(String filter) throws IOException
   {  StoreFile[] files = new StoreFile[1];

      KernelResults results = innerDelegate.getEntriesList(filter,
        isTest);

//
//   Assemble an array of any files which matched the query.
//   Each file is returned as an EntryResults object, which is
//   converted to the corresponding EntryRecord.

      Object[] fileResults = results.getEntries();
      int numFiles = Array.getLength(fileResults);

      if (numFiles > 0)
      {  files = new StoreFile[numFiles];

         for(int loop=0; loop<numFiles; loop++)
         {  EntryRecord file = new EntryRecord(
              (EntryResults)fileResults[loop] );
            files[loop] = file;
         }
      }
      else
      {  files = null;
      }

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

      return files;
   }


// ----------------------------------------------------------------------

/**
 * Returns the StoreFile representation of the file at the given AGSL
 */

   public StoreFile getFile(String path) throws IOException
   {  EntryRecord requestedFile = new EntryRecord();

      KernelResults results = innerDelegate.getEntriesList(path,
        isTest);

//
//   Obtain the file which matched the query.  This file is taken
//   to be the first entry in the return array.

      Object[] fileResults = results.getEntries();
      int numFiles = Array.getLength(fileResults);

      if (numFiles > 0)
      {  requestedFile = new EntryRecord(
           (EntryResults)fileResults[0] );
      }
      else
      { requestedFile  = null;
      }

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

      return requestedFile;
   }


// ----------------------------------------------------------------------

/**
 * Put the given byte buffer from offset of length bytes, to the given target
 */
   public void putBytes(byte[] bytes, int offset, int length, 
     String targetPath, boolean append) throws IOException
   {
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
//
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
   public void putString(String contents, String targetPath,
     boolean append) throws IOException
   {
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
   {  return new MySpaceOutputStream(targetPath, append);
   }


// ----------------------------------------------------------------------

/**
 * Get a file's contents as a stream
 */
   public InputStream getStream(String sourcePath) throws IOException
   {
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


//      URL url = getUrl(sourcePath);

//      if (url == null)
//      {  throw new FileNotFoundException(
//           "Failed to find URL for path: " + sourcePath);
//      }

//      return url.openStream();
   }


// ----------------------------------------------------------------------
   
/**
 * Get the URL to stream.
 */

   public URL getUrl(String sourcePath) throws IOException
   {  URL url = null;
      boolean conformingUrl = true;
      String uri = "";

      try
      {  EntryRecord file = (EntryRecord)this.getFile(sourcePath);
         uri = file.getEntryUri();

         try
         {  url = new URL(uri);
         }
         catch (Exception e)
         {  conformingUrl = false;
            throw new IOException ();
         }
      }
      catch (Exception e)
      {  url = null;

         if (!conformingUrl)
         {  throw new IOException ("File has invalid URL: " + uri);
         }
         else
         {  throw new IOException (
              "Failed to obtain URL for file on service: " + 
              this.endPoint);
         }
      }

      return url;
   }


// ----------------------------------------------------------------------

/**
 * Delete a file.
 */

   public void delete(String deletePath) throws IOException
   {  KernelResults results = innerDelegate.deleteFile(deletePath,
        isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------

/**
 * Copy a file to a target Agsl.
 */

   public void copy(String sourcePath, Agsl targetPath) throws IOException
   {  String targetFile = targetPath.getPath();
      KernelResults results = innerDelegate.copyFile(sourcePath,
        targetFile, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------

/**
 * Copy a file from a source Agsl.
 */

   public void copy(Agsl source, String targetPath) throws IOException
   {  String sourcePath = source.getPath();
      KernelResults results = innerDelegate.copyFile(sourcePath,
        targetPath, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }

// ----------------------------------------------------------------------
   
/**
 * Move (or rename) a file to a target Agsl.
 */

   public void move(String sourcePath, Agsl targetPath) throws IOException
   {  String targetFile = targetPath.getPath();
      KernelResults results = innerDelegate.moveFile(sourcePath,
        targetFile, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------

/**
 * Moves (or rename) a file from a source Agsl.
 */

   public void move(Agsl source, String targetPath) throws IOException
   {  String sourcePath = source.getPath();
      KernelResults results = innerDelegate.moveFile(sourcePath,
        targetPath, isTest);

//
//   Append and check any status messages.

      this.appendAndCheckStatusMessages(results);

   }


// ----------------------------------------------------------------------
   
/**
 * Create a container.
 */

   public void newFolder(String targetPath) throws IOException
   {  KernelResults results = innerDelegate.createContainer(
        targetPath, isTest);

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
 */
   public String getString(String targetPath) throws IOException
   {  String contents = "";

//
//   Attempt to retrieve the contents of the file as a String.

      KernelResults results = innerDelegate.getString(targetPath,
        isTest);

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
 */
   public byte[] getBytes(String targetPath) throws IOException
   {  byte[] contents = null;

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
 * @param fileName Name of the file to be modified.
 * @param newOwner Account of the new owner.
 */

   public void changeOwner(String fileName, User newOwner) 
     throws IOException
   {  String owner = newOwner.getAccount();
      KernelResults results = innerDelegate.changeOwner(
        fileName, owner, isTest);

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
 * @return A flag indicating whether any of the given messages
 *   corresponds to an error.  A value of true is returned if an error
 *   was found; otherwise false.
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

      if (errorRaised)
      {  throw new StoreException(messageFromManager);
      }
   }


// ----------------------------------------------------------------------

/**
 * Special OutputStream which writes to a String, then sends it when the 
 * stream is closed.
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
         {  putString("", targetPath, false);
         }
      }


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

         if (firstChunk)
         {  putBytes(buffer, 0, cursor, targetPath, false);
         }
         else
         {  putBytes(buffer, 0, cursor, targetPath, true);
         }

         firstChunk = false;
         cursor=0;
      }


      public void close() throws IOException
      {  flush();
         super.close();
      }
   }
}
