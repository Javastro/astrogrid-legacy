package org.astrogrid.store.delegate.myspaceItn05;

/**
 * ManagerGenuine.java
 *
 * <p>
 * Generate genuine responses from a MySpace Manager.
 * </p>
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

import java.util.*;

import org.astrogrid.Configurator;
import org.astrogrid.i18n.*;

import org.astrogrid.mySpace.mySpaceManager.MMC;
import org.astrogrid.mySpace.mySpaceManager.Configuration;
import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;
import org.astrogrid.mySpace.mySpaceManager.MySpaceActions;
import org.astrogrid.mySpace.mySpaceStatus.*;

public class ManagerGenuine implements Manager
{  private MySpaceActions actions = new MySpaceActions();
   private static MySpaceStatus status = new MySpaceStatus();
   private static Logger logger = new Logger();

   private String registryName = "";

// ----------------------------------------------------------------------

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
   {  KernelResults results = new KernelResults();

      setUp();

      try
      {
//
//      Write a message to the log file.

         logger.setActionName("getEntriesList");
         logger.appendMessage("Invoked getEntriesList...");

//
//      [TODO]: The following statement is a place-holder.  In due
//      course obtain the account details from the SOAP header.  Also
//      the account class will probably not be a String.

         String account = null;
         logger.setAccount(account);

//
//      Invoke the MySpaceActions object perform the appropriate tasks.

         actions.setRegistryName(registryName);
         Vector dataItems = actions.getEntriesList(account, query);

         if (dataItems != null)
         {
//
//         Copy the results to the return object.

            int numEntries = dataItems.size();

            if (numEntries > 0)
            {  ArrayList entries = new ArrayList();

               for(int loop=00; loop<numEntries; loop++)
               {  DataItemRecord itemRecord = 
                    (DataItemRecord)dataItems.elementAt(loop);
                  EntryResults entry =
                    itemRecord.getEntryResults();
                  entries.add(entry);
               }

               results.setEntries(entries.toArray() );
            }

//
//         Generate a success message.

            status.addCode(MySpaceStatusCode.AGMMCI00001,
              MySpaceStatusCode.INFO, MySpaceStatusCode.LOG,
              this.getClassName() );
         }

//
//      Copy any status messages to the results object.

         ArrayList statusList = status.getStatusResults();
         results.setStatusList(statusList.toArray() );
         status.reset();
      }
      catch (Exception e)
      {  logger.appendMessage("Exception in getEntriesList: " +
           e.toString() );
      }

      logger.close();
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
   {  KernelResults results = new KernelResults();

      setUp();

      try
      {
//
//      Write a message to the log file.

         logger.setActionName("putString");
         logger.appendMessage("Invoked putString...");

//
//      [TODO]: The following statement is a place-holder.  In due
//      course obtain the account details from the SOAP header.  Also
//      the account class will probably not be a String.

         String account = null;
         logger.setAccount(account);

//
//      Invoke the MySpaceActions object perform the appropriate tasks.

         actions.setRegistryName(registryName);
         boolean success = actions.putString(account, newFile, contents, 
           category, dispatchExisting);

//
//      Generate a success method if the method succeeded.

         if (success)
         {  status.addCode(MySpaceStatusCode.AGMMCI00001,
              MySpaceStatusCode.INFO, MySpaceStatusCode.LOG,
              this.getClassName() );
         }

//
//      Copy any status messages to the results object.

         ArrayList statusList = status.getStatusResults();
         results.setStatusList(statusList.toArray() );
         status.reset();
      }
      catch (Exception e)
      {  logger.appendMessage("Exception in putString: " +
           e.toString() );
      }

      logger.close();
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
   {  KernelResults results = new KernelResults();

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
   {  KernelResults results = new KernelResults();


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
   {  KernelResults results = new KernelResults();

      setUp();

      try
      {
//
//      Write a message to the log file.

         logger.setActionName("getString");
         logger.appendMessage("Invoked getString...");

//
//      [TODO]: The following statement is a place-holder.  In due
//      course obtain the account details from the SOAP header.  Also
//      the account class will probably not be a String.

         String account = null;
         logger.setAccount(account);

//
//      Invoke the MySpaceActions object perform the appropriate tasks.

         actions.setRegistryName(registryName);

         String contents = "";
         Vector dataItems = actions.getEntriesList(account, fileName);
         if (dataItems != null)
         {  if (dataItems.size() > 0)
            {  DataItemRecord itemRecord = 
                 (DataItemRecord)dataItems.elementAt(0);

               int dataItemId = itemRecord.getDataItemID();
               contents = actions.getString(account, dataItemId);
            }
            else
            {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                 MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                 this.getClassName() );
            }
         }
         else
         {  status.addCode(MySpaceStatusCode.AGMMCE00201,
             MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
             this.getClassName() );
         }

//
//      Add the contents to the results objects.

         results.setContentsString(contents);

//
//      Generate a success method if the method succeeded.

         if (status.getSuccessStatus() )
         {  status.addCode(MySpaceStatusCode.AGMMCI00001,
              MySpaceStatusCode.INFO, MySpaceStatusCode.LOG,
              this.getClassName() );
         }

//
//      Copy any status messages to the results object.

         ArrayList statusList = status.getStatusResults();
         results.setStatusList(statusList.toArray() );
         status.reset();
      }
      catch (Exception e)
      {  logger.appendMessage("Exception in getString: " +
           e.toString() );
      }

      logger.close();
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
   {  KernelResults results = new KernelResults();



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
   {  KernelResults results = new KernelResults();

      setUp();

      try
      {
//
//      Write a message to the log file.

         logger.setActionName("createContainer");
         logger.appendMessage("Invoked createContainer...");

//
//      [TODO]: The following statement is a place-holder.  In due
//      course obtain the account details from the SOAP header.  Also
//      the account class will probably not be a String.

         String account = null;
         logger.setAccount(account);

//
//      Invoke the MySpaceActions object perform the appropriate tasks.

         actions.setRegistryName(registryName);
         boolean createOk = actions.createContainer(account,
           newContainerName);

//
//      Generate a success method if the method succeeded.

         if (status.getSuccessStatus() )
         {  status.addCode(MySpaceStatusCode.AGMMCI00001,
              MySpaceStatusCode.INFO, MySpaceStatusCode.LOG,
              this.getClassName() );
         }

//
//      Copy any status messages to the results object.

         ArrayList statusList = status.getStatusResults();
         results.setStatusList(statusList.toArray() );
         status.reset();
      }
      catch (Exception e)
      {  logger.appendMessage("Exception in createContaine: " +
           e.toString() );
      }

      logger.close();
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
   {  KernelResults results = new KernelResults();

      setUp();

      try
      {
//
//      Write a message to the log file.

         logger.setActionName("copyFile");
         logger.appendMessage("Invoked copyFile...");

//
//      [TODO]: The following statement is a place-holder.  In due
//      course obtain the account details from the SOAP header.  Also
//      the account class will probably not be a String.

         String account = null;
         logger.setAccount(account);

//
//      Invoke the MySpaceActions object perform the appropriate tasks.

         actions.setRegistryName(registryName);

         Vector dataItems = actions.getEntriesList(account, oldFileName);
         if (dataItems != null)
         {  if (dataItems.size() > 0)
            {  DataItemRecord itemRecord = 
                 (DataItemRecord)dataItems.elementAt(0);

               int dataItemId = itemRecord.getDataItemID();
               boolean deleteOk = actions.copyFile(account, dataItemId,
                 newFileName);
            }
            else
            {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                 MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                 this.getClassName() );
            }
         }
         else
         {  status.addCode(MySpaceStatusCode.AGMMCE00201,
             MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
             this.getClassName() );
         }

//
//      Generate a success method if the method succeeded.

         if (status.getSuccessStatus() )
         {  status.addCode(MySpaceStatusCode.AGMMCI00001,
              MySpaceStatusCode.INFO, MySpaceStatusCode.LOG,
              this.getClassName() );
         }

//
//      Copy any status messages to the results object.

         ArrayList statusList = status.getStatusResults();
         results.setStatusList(statusList.toArray() );
         status.reset();
      }
      catch (Exception e)
      {  logger.appendMessage("Exception in copyFile: " +
           e.toString() );
      }

      logger.close();
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
   {  KernelResults results = new KernelResults();

      setUp();

      try
      {
//
//      Write a message to the log file.

         logger.setActionName("moveFile");
         logger.appendMessage("Invoked moveFile...");

//
//      [TODO]: The following statement is a place-holder.  In due
//      course obtain the account details from the SOAP header.  Also
//      the account class will probably not be a String.

         String account = null;
         logger.setAccount(account);

//
//      Invoke the MySpaceActions object perform the appropriate tasks.

         actions.setRegistryName(registryName);

         Vector dataItems = actions.getEntriesList(account, oldFileName);
         if (dataItems != null)
         {  if (dataItems.size() > 0)
            {  DataItemRecord itemRecord = 
                 (DataItemRecord)dataItems.elementAt(0);

               int dataItemId = itemRecord.getDataItemID();
               boolean deleteOk = actions.moveFile(account, dataItemId,
                 newFileName);
            }
            else
            {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                 MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                 this.getClassName() );
            }
         }
         else
         {  status.addCode(MySpaceStatusCode.AGMMCE00201,
             MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
             this.getClassName() );
         }

//
//      Generate a success method if the method succeeded.

         if (status.getSuccessStatus() )
         {  status.addCode(MySpaceStatusCode.AGMMCI00001,
              MySpaceStatusCode.INFO, MySpaceStatusCode.LOG,
              this.getClassName() );
         }

//
//      Copy any status messages to the results object.

         ArrayList statusList = status.getStatusResults();
         results.setStatusList(statusList.toArray() );
         status.reset();
      }
      catch (Exception e)
      {  logger.appendMessage("Exception in moveFile: " +
           e.toString() );
      }

      logger.close();
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
   {  KernelResults results = new KernelResults();

      setUp();

      try
      {
//
//      Write a message to the log file.

         logger.setActionName("deleteFile");
         logger.appendMessage("Invoked deleteFile...");

//
//      [TODO]: The following statement is a place-holder.  In due
//      course obtain the account details from the SOAP header.  Also
//      the account class will probably not be a String.

         String account = null;
         logger.setAccount(account);

//
//      Invoke the MySpaceActions object perform the appropriate tasks.

         actions.setRegistryName(registryName);

         Vector dataItems = actions.getEntriesList(account, fileName);
         if (dataItems != null)
         {  if (dataItems.size() > 0)
            {  DataItemRecord itemRecord = 
                 (DataItemRecord)dataItems.elementAt(0);

               int dataItemId = itemRecord.getDataItemID();
               boolean deleteOk = actions.deleteFile(account, dataItemId);
            }
            else
            {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                 MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                 this.getClassName() );
            }
         }
         else
         {  status.addCode(MySpaceStatusCode.AGMMCE00201,
             MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
             this.getClassName() );
         }

//
//      Generate a success method if the method succeeded.

         if (status.getSuccessStatus() )
         {  status.addCode(MySpaceStatusCode.AGMMCI00001,
              MySpaceStatusCode.INFO, MySpaceStatusCode.LOG,
              this.getClassName() );
         }

//
//      Copy any status messages to the results object.

         ArrayList statusList = status.getStatusResults();
         results.setStatusList(statusList.toArray() );
         status.reset();
      }
      catch (Exception e)
      {  logger.appendMessage("Exception in deleteFile: " +
           e.toString() );
      }

      logger.close();
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
   {  KernelResults results = new KernelResults();



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
   {  KernelResults results = new KernelResults();



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
   {  KernelResults results = new KernelResults();

      setUp();

      try
      {
//
//      Write a message to the log file.

         logger.setActionName("createAccount");
         logger.appendMessage("Invoked createAccount...");

//
//      [TODO]: The following statement is a place-holder.  In due
//      course obtain the account details from the SOAP header.  Also
//      the account class will probably not be a String.

         String account = null;
         logger.setAccount(account);

//
//      Invoke the MySpaceActions object perform the appropriate tasks.

         actions.setRegistryName(registryName);
         boolean success = actions.createAccount(account, newAccount);

//
//      Generate a success method if the method succeeded.

         if (success)
         {  status.addCode(MySpaceStatusCode.AGMMCI00001,
              MySpaceStatusCode.INFO, MySpaceStatusCode.LOG,
              this.getClassName() );
         }

//
//      Copy any status messages to the results object.

         ArrayList statusList = status.getStatusResults();
         results.setStatusList(statusList.toArray() );
         status.reset();
      }
      catch (Exception e)
      {  logger.appendMessage("Exception in createAccount: " +
           e.toString() );
      }

      logger.close();
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

   public KernelResults deleteAccount(String deadAccount, boolean test) 
     throws java.rmi.RemoteException
   {  KernelResults results = new KernelResults();

      setUp();

      try
      {
//
//      Write a message to the log file.

         logger.setActionName("deleteAccount");
         logger.appendMessage("Invoked deleteAccount...");

//
//      [TODO]: The following statement is a place-holder.  In due
//      course obtain the account details from the SOAP header.  Also
//      the account class will probably not be a String.

         String account = null;
         logger.setAccount(account);

//
//      Invoke the MySpaceActions object perform the appropriate tasks.

         actions.setRegistryName(registryName);
         boolean success = actions.deleteAccount(account, deadAccount);

//
//      Generate a success method if the method succeeded.

         if (success)
         {  status.addCode(MySpaceStatusCode.AGMMCI00001,
              MySpaceStatusCode.INFO, MySpaceStatusCode.LOG,
              this.getClassName() );
         }

//
//      Copy any status messages to the results object.

         ArrayList statusList = status.getStatusResults();
         results.setStatusList(statusList.toArray() );
         status.reset();
      }
      catch (Exception e)
      {  logger.appendMessage("Exception in deleteAccount: " +
           e.toString() );
      }

      logger.close();
      return results;
   }

// ----------------------------------------------------------------------

// Internal methods.

/**
 * Set up for running a Manager action.  Values are either read from
 * the properties file or hard-coded.  [TODO]: ideally they should
 * all be read from the properties file.
 *
 */

// Note that the method is protected rather than private so that it can
// be tested.

   protected void setUp()
   {  try
      {  
//
//      Get the configuration options from the properties file.

         Configuration config = new Configuration("dummy");

//
//      Get the MySpace registry name.

         MMC.getInstance().checkPropertiesLoaded();
         registryName = MMC.getProperty(MMC.REGISTRYCONF,
             MMC.CATLOG);  

//
//      Set the log file options.
//
//      [TODO]: these too should be read from the properties file.

         boolean astroGridLog = true; // Write AstroGrid log?
         boolean mySpaceLog = true;   // Write MySpace log?
//         boolean echoLog = false;     // Echo log to standard out?
         boolean echoLog = true;

         String mySpaceLogFileName = registryName + ".log";

         Logger logger = new Logger(astroGridLog, mySpaceLog, echoLog,
            mySpaceLogFileName);
 
//         System.out.println("registryName, mySpaceLogFileName: \n"
//           + "  " + registryName + "\n"
//           + "  " + mySpaceLogFileName);
      }
      catch (Exception es)
      {  es.printStackTrace();
      }
   }
// -----------------------------------------------------------------

/**
 * Obtain the name of the current Java class.
 */

   protected String getClassName()
   { Class currentClass = this.getClass();
     String name =  currentClass.getName();
     int dotPos = name.lastIndexOf(".");
     if (dotPos > -1)
     {  name = name.substring(dotPos+1, name.length() );
     }

     return name;
   }
}
