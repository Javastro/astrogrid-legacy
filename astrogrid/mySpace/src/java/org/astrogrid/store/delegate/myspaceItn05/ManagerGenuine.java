package org.astrogrid.store.delegate.myspaceItn05;

/**
 * ManagerGenuine.java
 *
 * <p>
 * Generate standard test responses from a MySpace Manager.
 * </p>
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

import java.util.*;

public class ManagerGenuine implements Manager
{

/**
 * `Heart-beat' method to check whether the Manager is up and running.
 *
 * @return The standard string `Adsum.' (Latin for `I am here',
 *    apparently) is always returned.
 */

   public String heartBeat() throws java.rmi.RemoteException
   {  return "Adsum.";
   }


// ----------------------------------------------------------------------

/**
 * Search the MySpace Manager and return a list of all the files
 * which match the query.
 *
 * @param query Query which the entry must match.  Queries take the form
 *   of entry names which may optionally include a wild-card character.
 *   This wild-card character is an asterisk and it must occur at the
 *   end of the name.
 * @param test Flag indicating whether a real or standard test Manager
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager, including a list of
 *   any <code>EntryRecord</code>s which satisfy the query.
 */

   public KernelResults getEntriesList(String query, boolean test)
     throws java.rmi.RemoteException
   {
//
//   Generate the name of the file which matches the query.
//
//   If the does not end in a wild-card it is returned unaltered
//   as the matching file name.  If it does then the wild card
//   is removed and a fake ending for the file name substituted.

      String entryName = "";
      int queryLen = query.length();
      int astPos = query.lastIndexOf("*");

      if ((queryLen - 1) == astPos)
      {  entryName = query.substring(0, queryLen - 1) + "xyz";
      }
      else
      {  entryName = query;
      }

//
//   Generate an EntryResults for this file.

      int    entryId = 0;
      String entryUri = "http://blue.nowhere.org/f1";
      String owner = "owner";
      Date   creationDate = new Date(0);
      Date   expiryDate = new Date(0);
      long   creation = creationDate.getTime();
      long   expiry = expiryDate.getTime();
      int    size = 1;
      int    type = EntryCodes.VOT;
      String permissions = "permissions";

      EntryResults entry = new EntryResults();

      entry.setEntryName(entryName);
      entry.setEntryId(entryId);
      entry.setEntryUri(entryUri);
      entry.setOwnerId(owner);
      entry.setCreationDate(creation);
      entry.setExpiryDate(expiry);
      entry.setSize(size);
      entry.setType(type);
      entry.setPermissionsMask(permissions);

//
//   Add this EntryResults to a list of EntryRecords.

      ArrayList entries = new ArrayList();
      entries.add(entry);

//
//   Add the list of EntryResults to the returned KernelResults object.

      KernelResults results = new KernelResults();
      results.setEntries(entries.toArray() );

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "getEntriesList executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Save the contents of a local String as a new file in the Service.
 *
 * @param newFile Name of the new file.
 * @param contents Contents of the new file.
 * @param category Category code for the file.  One of
 *   <code>EntryCodes.UNKNOWN</code>, <code>EntryCodes.CON</code>,
 *   <code>EntryCodes.VOT</code>,  <code>EntryCodes.QUERY</code>,
 *   <code>EntryCodes.WORKFLOW</code> or <code>EntryCodes.XML</code>.
 * @param dispatchExisting If the target MSS already contains a file
 *   called <code>entryName</code> then this flag specifies how this
 *   existing file is to be dispatched.  The permitted values are:
 *   <code>ManagerCodes.LEAVE</code>, 
 *   <code>ManagerCodes.OVERWRITE</code> and
 *   <code>ManagerCodes.APPEND</code>.
 * @param test Flag indicating whether a real or standard test Manager
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults putString(String newFile, String contents, 
     int category, int dispatchExisting, boolean test) 
     throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "putString executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Save the contents of an array of bytes as a new file in the Service.
 *
 * @param newFile Name of the new file.
 * @param contents Contents of the new file.
 * @param category Category code for the file.  One of
 *   <code>EntryCodes.UNKNOWN</code>, <code>EntryCodes.CON</code>,
 *   <code>EntryCodes.VOT</code>,  <code>EntryCodes.QUERY</code>,
 *   <code>EntryCodes.WORKFLOW</code> or <code>EntryCodes.XML</code>.
 * @param dispatchExisting If the target MSS already contains a file
 *   called <code>entryName</code> then this flag specifies how this
 *   existing file is to be dispatched.  The permitted values are:
 *   <code>ManagerCodes.LEAVE</code>, 
 *   <code>ManagerCodes.OVERWRITE</code> and
 *   <code>ManagerCodes.APPEND</code>.
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults putBytes(String newFile, byte[] contents, 
     int category, int dispatchExisting, boolean test) 
     throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "putBytes executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Save the contents of a URI as a new entry in the Service.
 *
 * @param newFile Name of the new file.
 * @param uri URI whose contents are to saved.
 * @param category Category code for the file.  One of
 *   <code>EntryCodes.UNKNOWN</code>, <code>EntryCodes.CON</code>,
 *   <code>EntryCodes.VOT</code>,  <code>EntryCodes.QUERY</code>,
 *   <code>EntryCodes.WORKFLOW</code> or <code>EntryCodes.XML</code>.
 * @param dispatchExisting If the target MSS already contains a file
 *   called <code>entryName</code> then this flag specifies how this
 *   existing file is to be dispatched.  The permitted values are:
 *   <code>ManagerCodes.LEAVE</code>, 
 *   <code>ManagerCodes.OVERWRITE</code> and
 *   <code>ManagerCodes.APPEND</code>.
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults putUri(String newFile, String uri, int category, 
     int dispatchExisting, boolean test) 
     throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "putUri executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Retrieve the contents of a specified file in the Service as a String.
 *
 * @param fileName Name of the required file.
 * @param test Flag indicating whether a real or standard test Manager is to
 *   be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults getString (String fileName, boolean test) 
     throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate the contents for the String.
//
//   TODO: may want to make the fake contents an XML fragment.

      String contentsString = "The Snark was a Boojum you see.";

//
//   Add the contents to the results objects.

      results.setContentsString(contentsString);

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "getString executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Retrieve the contents of a specified file in the Service as an array of
 * bytes.
 *
 * @param fileName AGSL of the required file.
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults getBytes (String fileName, boolean test) 
     throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate the byte array to be returned.

      byte[] contentsBytes = new byte[] {1, 2, 3, 4, 5};

//
//   Add the contents to the results objects.

      results.setContentsBytes(contentsBytes);


//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "getBytes executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
    }


// ----------------------------------------------------------------------

/**
 * Create a new container in the Service.
 *
 * @param newContainerName Name of the new container.
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults createContainer(String newContainerName, 
     boolean test) throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "createContainer executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Create a copy of a file within the same Service.  The existing file
 * is unaltered.
 *
 * @param oldFileName Name of the existing file.
 * @param newFileName Nameof the new file.
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults copyFile(String oldFileName, String newFileName,
     boolean test) throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "copyFile executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Move a file to a new location within the same Service.  The existing
 * entry will no longer exist.
 *
 * @param oldFileName Name of the existing file.
 * @param newFileName Name of the new file.
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults moveFile(String oldFileName, String newFileName,
     boolean test) throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "moveFile executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Delete a file from an MSS.
 *
 * @param fileName Name of the file to be deleted.
 */

   public KernelResults deleteFile(String fileName, boolean test) 
     throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "deleteFile executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Extend the lifetime of a file in an MSS.
 *
 * @param fileName Name of the entry to be modified.
 * @param newExpiryDate New expiry date for the file.
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults extendLifetime(String fileName, long newExpiryDate,
     boolean test) throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "extendLifetime executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

/**
 * Change the owner of a MySpace file.

 *
 * @param fileName Name of the file to be modified.
 * @param newOwner Account of the new owner.
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults changeOwner(String fileName, String newOwner, 
     boolean test) throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "changeOwner executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }


// ----------------------------------------------------------------------

// The following methods provide System Administration functions.

/**
 * Create a new account on a Service.
 *
 * <p>
 * `Create a new account' is something of a misnomer.  This method is
 * really just creating the base set of containers which every account
 * needs.
 * </p>
 *
 * @param newAccount Account of the user to be created.
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults createAccount(String newAccount, boolean test)
     throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "createAccount executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
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
 * @param test Flag indicating whether a real or standard test Manager 
 *   is to be run.  A value of `true' will run a test Manager.
 * @return Standard return object from the Manager.
 */

   public KernelResults deleteAccount(String deadUser, boolean test) 
     throws java.rmi.RemoteException
   {
//
//   Generate an object to hold the results returned by this method.

      KernelResults results = new KernelResults();

//
//   Generate a StatusResults for this operation.

      int severity = StatusCodes.INFO;
      String message = "deleteAccount executed successfully.";
      Date timeStampDate = new Date(0);
      long timeStamp = timeStampDate.getTime();

      StatusResults status = new StatusResults();

      status.setSeverity(severity);
      status.setMessage(message);
      status.setTimeStamp(timeStamp);

//
//   Add this StatusResults to a list of StatusRecords.

      ArrayList statusList = new ArrayList();
      statusList.add(status);

//   Add the list of StatusResults to the returned KernelResults object.

      results.setStatusList(statusList.toArray() );


      return results;
   }
}
