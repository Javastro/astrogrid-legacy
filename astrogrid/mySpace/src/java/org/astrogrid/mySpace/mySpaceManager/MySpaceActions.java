package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;

import org.astrogrid.store.delegate.myspaceItn05.ManagerCodes;
import org.astrogrid.store.delegate.myspaceItn05.EntryCodes;

import org.astrogrid.mySpace.mySpaceStatus.*;


/**
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 2.
 * @version Iteration 5.
 * @TODO update logging statements to use commons-logging
 */

public class MySpaceActions
{  private static Logger logger = new Logger();


//   private static boolean DEBUG = true;

   private static String registryName;

//
// Constructor.

/**
 * Default constructor.
 */

   public MySpaceActions()
   {  super();
   }

//
// Action methods.
//
// Each of the following methods implements one of the actions which
// the MySpaceActions can perform.

// -----------------------------------------------------------------

/**
  * Lookup the details of a single DataHolder.
  */

   public DataItemRecord lookupDataHolderDetails(String account,
     int dataItemID)
   {  DataItemRecord dataItem = new DataItemRecord();
      dataItem = null;

      try
      {  RegistryManager reg = new RegistryManager(registryName);

         dataItem = this.internalLookupDataHolderDetails(account,
           dataItemID, reg);
      }
      catch (Exception all)
      {  MySpaceStatus status  = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00100, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return dataItem;
   }

// -----------------------------------------------------------------

/**
  * Lookup the details of a named set of DataHolders.
  */

   public Vector getEntriesList(String account, String query)
   {  Vector dataItemVector = new Vector();

      try
      {  RegistryManager reg = new RegistryManager(registryName);

         dataItemVector = this.internalLookupDataHoldersDetails(
           account,  query, reg);
      }
      catch (Exception all)
      {  MySpaceStatus status  = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00100, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return dataItemVector;
   }


// -----------------------------------------------------------------

/**
 * Save (or put) the contents of an input String as a specified
 * DataHolder in the MySpace system.
 */

   public boolean putString(String account, String newDataItemName, 
     String contents, int contentsType, int dispatchExisting)
   {  boolean returnStatus = true;

      MySpaceStatus status = new MySpaceStatus();
      logger.appendMessage("entered putString");

      try
      {
//
//      Attempt to open the registry and proceed if ok.

         logger.appendMessage("registryName: " + registryName);
         RegistryManager reg = new RegistryManager(registryName);
         logger.appendMessage("after RegistryManager: " +
           status.getSuccessStatus());
         if (status.getSuccessStatus())
         {

//
//         Assemble the UserAccount from the account.

            UserAccount userAcc = new UserAccount(account, "", "");
            logger.appendMessage("after UserAccount");
//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {
               logger.appendMessage("after checkSystemAuthorisation");
               logger.appendMessage("hello");
               logger.appendMessage("dispatchExisting, ManagerCodes.OVERWRITE"
                 + dispatchExisting + " " + ManagerCodes.OVERWRITE);

//
//            If the ManagerCodes.OVERWRITE option has been specified then 
//            delete any existing dataHolder of the specified name.

               if (dispatchExisting == ManagerCodes.OVERWRITE)
               {  boolean deleteOk = this.deleteExistingDataHolder(
                    account, newDataItemName, reg);
               }
// logger.appendMessage("MYSPACEACTION BUG266."+deleteOk);
// } else if(dispatchExisting == ManagerCodes.APPEND){
// logger.appendMessage("MYSPACEATION.upLoadDataHolder append = true" );
// }

//
//            Check that the specified dataHolder can be created.

               logger.appendMessage("before checkCanBeCreated");
               if(this.checkCanBeCreated(newDataItemName, userAcc,
                 "", reg) == true)
               {
                  logger.appendMessage("after checkCanBeCreated");
//
//               Create a DataItemRecord for the new DataHolder.

                  Date creation = new Date();

                  int newdataItemID = -1;
                  String dataItemFileName = "";

                  int dataItemType = contentsType;
                  int dataItemSize = contents.length();

                  DataItemRecord newDataItem = new DataItemRecord
                    (newDataItemName, newdataItemID,
                    dataItemFileName, account, creation, creation,
                    dataItemSize, dataItemType, "permissions");

//
//               Attempt to add this entry to the registry.

                  logger.appendMessage("before addDataItemRecord");
                  newDataItem = reg.addDataItemRecord(newDataItem);
                  logger.appendMessage("after addDataItemRecord");

                  System.out.println("new file name: " +
                     newDataItem.getDataItemFile() );

                  if (newDataItem != null)
                  {  newdataItemID = newDataItem.getDataItemID();
                     dataItemFileName = newDataItem.getDataItemFile();

//
//                  Attempt to copy the contents of the input string as
//                  a new file on the server.
//
//                  [TODO]: Remove the hard-wired server name.

                     String serverName = "serv1";
                     System.out.println("serverName: " + serverName);
                     String serverDirectory =
                       reg.getServerDirectory(serverName);
                     System.out.println("serverDirectory" +
                       serverDirectory);

                     String copyTo = serverDirectory + dataItemFileName;

                     ServerDriver serverDriver = new ServerDriver();
                     if(!serverDriver.upLoadString(contents, copyTo) )
                     {

//
//                     The actual up-load of the contents failed.
//                     Delete the new entry from the registry, to bring
//                     the registry back into line with reality and
//                     report an error.

                        reg.deleteDataItemRecord(newdataItemID);
                        status.addCode(MySpaceStatusCode.AGMMCE00202,
                          MySpaceStatusCode.ERROR,
                          MySpaceStatusCode.LOG, this.getClassName() );

                         returnStatus = false;
                     }
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00203,
                       MySpaceStatusCode.ERROR,
                       MySpaceStatusCode.LOG, this.getClassName() );

                     returnStatus = false;
                  }
               }
            }
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );

         returnStatus = false;
      }

      return returnStatus;
   }

// -----------------------------------------------------------------

/**
 * Get (read) the contents of a file held on a MySpace Server and
 * return them as a String.
 */

  public String getString(String account, int dataItemID)
  {  String contents = "";

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Attempt to open the registry and proceed if ok.

         RegistryManager reg = new RegistryManager(registryName);

         if (status.getSuccessStatus())
         {

//
//         Assemble the UserAccount from the UserID and CommunityID.

            UserAccount userAcc = new UserAccount(account, "", "");

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Attempt to lookup the details of the DataHolder and proceed
//            if ok.

               DataItemRecord dataItem = 
                 this.internalLookupDataHolderDetails(account,
                   dataItemID, reg);
               if (dataItem != null)
               {

//
//               Obtain the name of the file.
//
//               [TODO]: do not hard-wire the server name.

                   String serverName = "serv1";
                   String serverDirectory =
                          reg.getServerDirectory(serverName);

                   String serverFile = serverDirectory +
                          dataItem.getDataItemFile();

                   System.out.println("serverFile: " + serverFile);
                   ServerDriver serverDriver = new ServerDriver();
                   contents = serverDriver.retrieveString(serverFile);
                   System.out.println("contents" + contents);
                   if (contents == null)
                   {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                        MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                        this.getClassName() );

                      contents = "";
                   }
               }
               else
               {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                     MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                     this.getClassName() );

                  contents = "";
               }
            }
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );

         contents = "";
      }

      return contents;
   }


// -----------------------------------------------------------------

/**
 * Create a new container.  The  operation is implemented by creating
 * a new entry in the MySpace registry.  The MySpace server is not
 * touched.
 */

   public boolean createContainer(String account, String newContainerName)
   {  boolean returnStatus = false;
      DataItemRecord newContainer = new DataItemRecord();
      newContainer = null;

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Attempt to open the registry and proceed if ok.

         RegistryManager reg = new RegistryManager(registryName);
         if (status.getSuccessStatus())
         {

//
//         Assemble the UserAccount from the UserID and CommunityID.

            UserAccount userAcc = new UserAccount(account, "", "");

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Check that the specified container can be created.

               if(this.checkCanBeCreated(newContainerName, userAcc,
                 "", reg) == true)
               {

//
//               Create a DataItemRecord for the container.

                  Date creation = new Date();

                  int newdataItemID = -1;
                  String dataItemFileName = "";
                  int dataItemType = EntryCodes.CON;

                  DataItemRecord newDataItem = new DataItemRecord
                    (newContainerName, newdataItemID, dataItemFileName,
                    account, creation, creation, 0, dataItemType,
                    "permissions");

//
//               Attempt to add this entry to the registry.

                  newDataItem = reg.addDataItemRecord(newDataItem);
                  if (newDataItem != null)
                  {  returnStatus = true;
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00209,
                       MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                       this.getClassName() );
                  }
               }
            }
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );
      }

      return returnStatus;
   }

// -----------------------------------------------------------------

/**
  * Copy a file from one location on a MySpace server to another
  * location on the same server.
  */

   public boolean copyFile(String account, int oldDataItemID,
     String newDataItemName)
   {  boolean returnStatus = false;

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Attempt to open the registry and proceed if ok.

         RegistryManager reg = new RegistryManager(registryName);
         if (status.getSuccessStatus())
         {

//
//         Assemble the UserAccount from the UserID and CommunityID.

            UserAccount userAcc = new UserAccount(account, "", "");

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Attempt to lookup the details of the original DataHolder
//            and proceed if ok.

               DataItemRecord oldDataItem = 
                 this.internalLookupDataHolderDetails(account,
                   oldDataItemID, reg);
               if (status.getSuccessStatus())
               {

//
//               Check that the original item is a DataHolder rather
//               than a container.

                  if (oldDataItem.getType() != DataItemRecord.CON)
                  {

//
//                  Check that the specified output container can be
//                  created.

                     if(this.checkCanBeCreated(newDataItemName, userAcc,
                       "", reg) == true)
                     {

//
//                     Create a DataItemRecord for the new DataHolder.

                        Date creation = new Date();

                        int newdataItemID = -1;
                        String dataItemFileName = "";

                        int dataItemType = oldDataItem.getType();
                        int dataItemSize = oldDataItem.getSize();

                        DataItemRecord newDataItem = new DataItemRecord
                          (newDataItemName, newdataItemID,
                          dataItemFileName, account, creation, creation,
                          dataItemSize, dataItemType, "permissions");

//
//                     Attempt to add this entry to the registry.

                        newDataItem = reg.addDataItemRecord(newDataItem);
                        if (newDataItem != null)
                        {  newdataItemID = newDataItem.getDataItemID();
                           dataItemFileName = newDataItem.getDataItemFile();

//
//                        Attempt to copy the DataHolder.
//
//                        [TODO]: do not hard-wire the server name.

                           String oldServerName = "serv1";
                           String newServerName = "serv1";

                           boolean copyOk = true;
//
//                        Check whether the new and old files are on
//                        the same server and proceed accordingly.


                           if (oldServerName.equals(newServerName) )
                           {
//                              System.out.println(
//                                "Input and output files are on the " +
//                                "same server.");

                              String serverDirectory =
                                reg.getServerDirectory(oldServerName);

                              String copyFrom = serverDirectory +
                                oldDataItem.getDataItemFile();
                              String copyTo = serverDirectory +
                                dataItemFileName;

                              ServerDriver serverDriver = new ServerDriver();
                              if(!serverDriver.copyDataHolder(copyFrom,
                                copyTo) )
                              {  copyOk = false;
                              }
                           }
                           else
                           {
//                              System.out.println(
//                                "Input and output files are on " +
//                                "different servers.");

//
//                           The old and new file are on different
//                           servers.
//
//                           Assemble the URI for the old file from the
//                           server URI and the file name of the file.

                              String oldServerURI =
                                reg.getServerURI(oldServerName);

                              String oldDataHolderURI =
                                oldServerURI + oldDataItem.getDataItemFile();

                              String newServerDirectory =
                                reg.getServerDirectory(newServerName);

                              String copyTo = newServerDirectory +
                                dataItemFileName;

                              ServerDriver serverDriver = new ServerDriver();
                              if(!serverDriver.importDataHolder(
                                oldDataHolderURI, copyTo) )
                              {  copyOk = false;
                              }
                           }

//
//                        Set the return status if all was ok.
//                        Otherwise report an error.

                           if (copyOk)
                           {  returnStatus = true;
                           }
                           else
                           {

//
//                           The actual copy of the DataHolder failed.
//                           Delete its entry from the registry, to bring
//                           the registry back into line with reality and
//                           report an error.

                              reg.deleteDataItemRecord(newdataItemID);
                              status.addCode(MySpaceStatusCode.AGMMCE00202,
                                MySpaceStatusCode.ERROR,
                                MySpaceStatusCode.LOG, this.getClassName() );
                           }
                        }
                        else
                        {  status.addCode(MySpaceStatusCode.AGMMCE00203,
                             MySpaceStatusCode.ERROR,
                             MySpaceStatusCode.LOG, this.getClassName() );
                        }
                     }
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00204,
                       MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                       this.getClassName() );
                  }
               }
            }
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );
      }

      return returnStatus;
   }

// -----------------------------------------------------------------

/**
  * Move a File from one location on a MySpace server to another
  * location on the same server.
  *
  * The move operation is implemented entirely by manipulating the
  * entries in the MySpace registry.  The files on the Server are not
  * touched.
  */

   public boolean moveFile(String account, int oldDataItemID, 
      String newDataItemName)
   {  boolean returnStatus = false;

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Attempt to open the registry and proceed if ok.

         RegistryManager reg = new RegistryManager(registryName);
         if (status.getSuccessStatus())
         {

//
//         Assemble the UserAccount from the account.

            UserAccount userAcc = new UserAccount(account, "", "");

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Attempt to lookup the details of the original DataHolder
//            and proceed if ok.

               DataItemRecord oldDataItem = 
                 this.internalLookupDataHolderDetails(account,
                   oldDataItemID, reg);
               if (status.getSuccessStatus())
               {

//
//               Check that the original item is a DataHolder rather
//               than a container.

                  if (oldDataItem.getType() != DataItemRecord.CON)
                  {

//
//                  Check that the specified output container can be
//                  created.

                     if(this.checkCanBeCreated(newDataItemName, userAcc,
                       "", reg) == true)
                     { 

//
//                     Create a DataItemRecord for the new DataHolder.


                        int expiryPeriod = reg.getServerExpiryPeriod(
                          oldDataItem.getServer() );
                        Date creation = new Date();

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(creation);
                        cal.add(Calendar.DATE, expiryPeriod);
                        Date expiry = cal.getTime();

                        String dataItemFileName = 
                          oldDataItem.getDataItemFile();
                        int dataItemType = oldDataItem.getType();
                        int dataItemSize = oldDataItem.getSize();
                        String permissionsMask =
                          oldDataItem.getPermissionsMask();

                        DataItemRecord newDataItem = new DataItemRecord
                          (newDataItemName, oldDataItemID,
                          dataItemFileName, account, creation, expiry,
                          dataItemSize, dataItemType, permissionsMask);

//
//                     Attempt to update this entry in the registry.

                        if (reg.updateDataItemRecord(newDataItem) )
                        {
//
//                        Set the return argument to the new dataItem.

                           returnStatus = true;
                        }
                        else
                        {  status.addCode(MySpaceStatusCode.AGMMCE00205,
                             MySpaceStatusCode.ERROR,
                             MySpaceStatusCode.NOLOG, this.getClassName());
                        }
                     }
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00206,
                       MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                       this.getClassName());
                  }
               }
            }
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );
      }

      return returnStatus;
   }


// -----------------------------------------------------------------

/**
 * Delete a DataHolder or container from a MySpace server.
 */

   public boolean deleteFile(String account, int dataItemID)
   {  boolean returnStatus = false;

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Attempt to open the registry and proceed if ok.

         RegistryManager reg = new RegistryManager(registryName);

         if (status.getSuccessStatus())
         {

//
//         Assemble the UserAccount from the UserID and CommunityID.

            UserAccount userAcc = new UserAccount(account, "", "");

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Attempt to lookup the details of the DataHolder and proceed
//            if ok.

               DataItemRecord dataItem = 
                 this.internalLookupDataHolderDetails(account,
                   dataItemID, reg);
               if (dataItem != null)
               {

//
//               If the DataHolder is a container then check that it is
//               empty.  Containers which are not empty cannot be deleted.

                  boolean proceedToDelete = true;

                  if (dataItem.getType() == DataItemRecord.CON)
                  {  String query = dataItem.getDataItemName() + "/*";

                     Vector childrenDataItemVector = 
                       this.internalLookupDataHoldersDetails(account,
                         query, reg);
                     if (status.getSuccessStatus())
                     {  status.reset();
                     }

                     if (childrenDataItemVector != null)
                     {  if (childrenDataItemVector.size() > 0)
                        {  proceedToDelete = false;

                           status.addCode(MySpaceStatusCode.AGMMCE00210,
                             MySpaceStatusCode.ERROR, 
                             MySpaceStatusCode.NOLOG,
                             this.getClassName() );
                        }
                     }
                  }

                  if (proceedToDelete)
                  {

//
//                  Check that the user is permitted to delete the DataHolder:

                     String permissions;
                     permissions = dataItem.getPermissionsMask();
                     String ownerID;
                     ownerID = dataItem.getOwnerID();

                     if (userAcc.checkAuthorisation(UserAccount.READ,
                       ownerID, permissions))
                     {  boolean deletedOk = false;

//
//                     Check that the file is not a container.

                        if (dataItem.getType() != EntryCodes.CON)
                        {
//
//                        Attempt to delete the DataHolder.
//
//                        [TODO]: do not hard-wire the server name.

                           String serverName = "serv1";
                           String serverDirectory =
                             reg.getServerDirectory(serverName);

                           String a = serverDirectory +
                             dataItem.getDataItemFile();

                           ServerDriver serverDriver = 
                             new ServerDriver();
                           deletedOk = serverDriver.deleteDataHolder(a);
                        }
                        else
                        {  deletedOk = true;
                        }

                        if (deletedOk)
                        {
//
//                        Delete the entry for the DataHolder in the registry,
//                        to bring the registry into line with reality.

                           reg.deleteDataItemRecord(dataItemID);

//
//                        Set the return status to success.

                          returnStatus = true;
                        }
                        else
                        {  status.addCode(MySpaceStatusCode.AGMMCE00211,
                             MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                             this.getClassName() );
                        }
                     }
                     else
                     {  status.addCode(MySpaceStatusCode.AGMMCE00212,
                          MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                          this.getClassName() );
                     }
                  }
               }
               else
               {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                    MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                    this.getClassName());
               }
            }
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );
      }

      return returnStatus;
   }


// =================================================================

//
// The following `action methods' implement system administration
// functions.  Specifically creating and deleting accounts.

/**
 * Create a new account.  The  operation is implemented by creating
 * a new top-level containers in the MySpace registry corresponding to
 * the new account.  The MySpace server is not touched.
 *
 * @param account The account which is performing the operation
 *   (typically belonging to a System Administrator).
 *
 * @param newAccount The account which is to be created.
 */

   public boolean createAccount(String account, String newAccount)
   {  boolean returnStatus = true;

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Attempt to open the registry and proceed if ok.

         RegistryManager reg = new RegistryManager(registryName);
         if (status.getSuccessStatus())
         {

//
//         Assemble the UserAccount from the account.

            UserAccount userAcc = new UserAccount(account, "", "");

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Check that the user is permitted to add new users.

               if (userAcc.checkCanModifyUsers() )
               {
//
//               Create a UserAccount for the new account.

                  UserAccount newUserAcc = new UserAccount(newAccount,
                    "", "");

                  Vector containerNames = new Vector();

//
//               Create a container name for the top-level container
//               of the new user.

                  String containerName = newUserAcc.getBaseContainer();
                  containerNames.add(containerName);

//
//               Create the standard workflow, query and votable containers.

                  containerName = newUserAcc.getBaseContainer() +  "/query";
                  containerNames.add(containerName);

                  containerName = newUserAcc.getBaseContainer() + "/workflow";
                  containerNames.add(containerName);

                  containerName = newUserAcc.getBaseContainer() +  "/votable";
                  containerNames.add(containerName);

//
//               Add entries corresponding to these container names
//               to the registry.

                  DataItemRecord itemRec = new DataItemRecord();

                  Date creation = new Date();
                  creation = Calendar.getInstance().getTime();

                  for (int loop = 0; loop < containerNames.size(); loop++)
                  {  containerName = (String)containerNames.get(loop);

//
//                  Check that this container does not already exist.

                     Vector vec = reg.lookupDataItemRecords(containerName);
                     if (vec.size() == 0)
                     {

//
//                     Create an entry for the current entry.

                        int dataItemID = -1;

                        itemRec = new DataItemRecord(containerName,
                          dataItemID, "none", "sysadmin", creation,
                          creation, 0, EntryCodes.CON, "permissions");

//
//                     Add the entry to the registry.

                        itemRec = reg.addDataItemRecord(itemRec);
                        if (itemRec == null)
                        {  status.addCode(MySpaceStatusCode.AGMMCE00209,
                             MySpaceStatusCode.ERROR, 
                             MySpaceStatusCode.LOG,
                             this.getClassName() );

                           returnStatus = false;
                        }
                     }
                     else
                     {  status.addCode(MySpaceStatusCode.AGMMCE00215,
                             MySpaceStatusCode.ERROR, 
                             MySpaceStatusCode.LOG,
                             this.getClassName() );

                        returnStatus = false;
                     }
                  }
               }
            }
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );

         logger.appendMessage("Registry name = " + registryName);

         returnStatus = false;
      }

      return returnStatus;
   }


// -----------------------------------------------------------------

/**
 * Delete an account.  The  operation is implemented by deleting the
 * entire tree of containers belonging to the account.  Before attempting
 * to delete any of the containers a check is made that the tree
 * does not contain any dataHolders (ie. that it is solely containers).
 * If any dataHolders are found operation is aborted without touching
 * the tree.
 *
 * @param account The account which is performing the operation
 *   (typically belonging to a System Administrator).
 *
 * @param deadAccount The account which is to be deleted.
 */

   public boolean deleteAccount(String account, String deadAccount)
   {  boolean returnStatus = false;

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Attempt to open the registry and proceed if ok.

         RegistryManager reg = new RegistryManager(registryName);
         if (status.getSuccessStatus())
         {

//
//         Assemble the UserAccount from the account.

            UserAccount userAcc = new UserAccount(account, "", "");

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Check that the user is permitted to delete users.

               if (userAcc.checkCanModifyUsers() )
               {  
//
//               Create a UserAccount for the dead account.

                  UserAccount deadUserAcc = new UserAccount(deadAccount,
                    "", "");

//
//               Lookup all the entries for the dead account in the 
//               registry and proceed if some entries were found.

                  String query = deadUserAcc.getBaseContainer() + "*";

                  Vector entries = reg.lookupDataItemRecords(query);
                  if (entries.size() > 0)
                  {  boolean deleteOk = true;

//
//                  Examine every entry and check that it is a container.

                     DataItemRecord itemRec = new DataItemRecord();

                     for (int currentEntry = 0; currentEntry < entries.size();
                       currentEntry++)
                     {  itemRec =
                          (DataItemRecord)entries.elementAt(currentEntry);

                        if (itemRec.getType() != EntryCodes.CON)
                        {  deleteOk = false;
                        }
                     }

//
//                  Proceed to delete the user if all his entries were
//                  containers; otherwise report an error.

                     if (deleteOk)
                     {  boolean allRemoved = true;

                        for (int currentEntry = 0; 
                          currentEntry < entries.size(); currentEntry++)
                        {  itemRec =
                             (DataItemRecord)entries.elementAt(currentEntry);

                           int itemRecID = itemRec.getDataItemID();
                           boolean deleteStatus =
                             reg.deleteDataItemRecord(itemRecID);

                           if (!deleteStatus)
                           {  allRemoved = false;

                              status.addCode(
                                MySpaceStatusCode.AGMMCE00220,
                                MySpaceStatusCode.ERROR,
                                MySpaceStatusCode.LOG,
                                this.getClassName() );
                           }
                        }

                        if (allRemoved)
                        {  returnStatus = true;
                        }
                     }
                  }
               }
            }
         }

         if (!returnStatus)
         {  status.addCode(MySpaceStatusCode.AGMMCE00221,
              MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
              this.getClassName() );
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );
      }

      return returnStatus;
   }

// =================================================================

//
// The following methods are not `action methods', that is they
// do not correspond to high-level functions of the MySpace system.
// Most of them are private.

/**
 * Set the registry name.
 */

   public void setRegistryName(String registryName)
   {  MySpaceActions.registryName = registryName;
   }

// -----------------------------------------------------------------

/**
 * Internal method to lookup the details of a single DataHolder.
 *
 * Apart from being private, rather than public,
 * internalLookupDataHolderDetails differs from lookupDataHolderDetails
 * in that an existing RegistryManager manager object is passed as
 * an argument.
 */

   private DataItemRecord internalLookupDataHolderDetails(String userID,
     int dataItemID, RegistryManager reg)
   {  DataItemRecord dataItem = new DataItemRecord();
      dataItem = null;

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Assemble the UserAccount from the UserID and CommunityID.

         UserAccount userAcc = new UserAccount(userID, "", "");

//
//      Check the user's system authorisation and proceed if ok.

         if (userAcc.checkSystemAuthorisation(UserAccount.READ) )
         {

//
//         Attempt to lookup up the details of the DataHolder in the
//         mySpace registry.
//
//         Note that in the following clause two conditions are
//         checked for: the item not being in the registry and
//         the user not having the privilege to access it.  The
//         error messages set in these two cases are deliberately
//         identical.

            DataItemRecord dataItemFound =
              reg.lookupDataItemRecord(dataItemID);
            if (dataItemFound != null)
            {

//
//            Check whether the user has the privileges to access the
//            DataHolder.

               String permissions;
               permissions = dataItemFound.getPermissionsMask();
               String ownerID;
               ownerID = dataItemFound.getOwnerID();

               if (userAcc.checkAuthorisation(UserAccount.READ,
                 ownerID, permissions))
               {

//
//               All is ok; copy the DataItemRecord to the return argument.

                  dataItem = dataItemFound;
               }
               else
               {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                    MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                    this.getClassName() );
               }
            }
            else
            {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                 MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                 this.getClassName());
            }
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
            MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
            this.getClassName() );
      }

      return dataItem;
   }

// -----------------------------------------------------------------

/**
 * Internal method to lookup the details of a named set of DataHolders.
 *
 * Apart from being private, rather than public,
 * internalLookupDataHoldersDetails differs from LookupDataHoldersDetails
 * in that an existing RegistryManager manager object is passed as
 * an argument.
 */

   private Vector internalLookupDataHoldersDetails(String account, 
     String query, RegistryManager reg)
   {  Vector dataItemVector = new Vector();

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Assemble the UserAccount from the accounr.

         UserAccount userAcc = new UserAccount(account, "",  "");

//
//      Check the user's system authorisation and proceed if ok.

         if (userAcc.checkSystemAuthorisation(UserAccount.READ) )
         {

//
//         Attempt lookup in the registry the entries for the DataHolders
//         which match the query string.
//
//         Note that in the following clauses items which the user
//         does not have the privilege to access are treated as though
//         they do not exist.

            Vector dataItemFoundVector =
              reg.lookupDataItemRecords(query);
            if (status.getSuccessStatus())
            {

//
//            Check whether any DataHolders were found (it is perfectly
//            possible that no entries will match the query string).

               if (dataItemFoundVector.size() > 0)
               {

//
//               Examine every entry and check that the user has the
//               privilege to access it.  If not remove it from the list.

                  for (int loop = 0; loop < dataItemFoundVector.size();
                    loop++)
                  {  DataItemRecord currentItem = 
                       (DataItemRecord)dataItemFoundVector.get(loop);

                     String permissions;
                     permissions = currentItem.getPermissionsMask();
                     String ownerID;
                     ownerID = currentItem.getOwnerID();

                     if (!userAcc.checkAuthorisation(UserAccount.READ,
                       ownerID, permissions))
                     {  dataItemFoundVector.remove(loop);
                     }
                  }

//
//               If any DataItemRecords remain in the Vector then copy
//               the Vector to the return vector.  If no items are left
//               then issue an informational message.

                  if (dataItemFoundVector.size() > 0)
                  {  dataItemVector = dataItemFoundVector;
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCI00250,
                       MySpaceStatusCode.INFO, MySpaceStatusCode.NOLOG,
                       this.getClassName() );
                  }
               }
               else
               {  status.addCode(MySpaceStatusCode.AGMMCI00250,
                    MySpaceStatusCode.INFO, MySpaceStatusCode.NOLOG,
                    this.getClassName() );
               }
            }
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );
      }

      return dataItemVector;
   }

// -----------------------------------------------------------------

/**
 * <p>
 * An internal convenience method to check whether the user can create
 * a specifed DataHolder or container.  The checks performed include
 * the following:
 * </p>
 * <ul>
 *  <li> that the DataHolder or container does not already exist,
 *  <li> that the item is at least three levels deep in the container
 *    hierarchy; ie. that an attempt is not being made to create a
 *    top-level user container or a second-level server container.
 *  <li> that the parent container exists,
 * </ul>
 * <p>
 * The method returns true if all is ok and the DataHolder or container
 * can be created and false otherwise.
 * </p>
 */

   private boolean checkCanBeCreated(String newDataItemName,
     UserAccount userAcc, String credentials, RegistryManager reg)
   {  boolean canBeCreated = false;
      MySpaceStatus status = new MySpaceStatus();

      logger.appendMessage("entered checkCanBeCreated");
      try
      {

//
//      Check that the output DataHolder does not already exist.

         String account = userAcc.getUserId();
         logger.appendMessage("account, newDataItemName: " + account
           + " " + newDataItemName);

         boolean exists = this.existsDataHolder(account, newDataItemName,
           reg);

         logger.appendMessage("newDataItemName, exists: " +
           newDataItemName + " " + exists);

         if (!exists)
         {

//
//         Check that the container hierarchy is at least two levels
//         deep; ie. that an attempt is not being made to create a
//         top-level user container.

            int containSepPos1 = newDataItemName.indexOf("/");
            int containSepPos2 = newDataItemName.indexOf("/",
              containSepPos1+1);

            if (containSepPos2 > 0)
            {

//
//            Check that the user is permitted to create the output
//            DataHolder:
//              obtain the name of its parent container,
//              check that this container exists,
//              [TODO]: check that the user is allowed to write to this 
//                      container.

               logger.appendMessage("before parentContainer");
               String parentContainer = newDataItemName.substring
                 (0, newDataItemName.lastIndexOf("/") );
               logger.appendMessage("parentContainer: " +
                 parentContainer);

               boolean parentExists = this.existsDataHolder(account, 
                 parentContainer, reg);
               logger.appendMessage("parentExists: " +
                 parentExists);

               if (parentExists)
               {
//
//               [TODO]: permissions checks that the account is
//               permitted to write to the parent container might
//               be required here.

//                ...

//
//               Finally set the flag saying that the file can be
//               created.

                  canBeCreated = true;
               }
               else
               {  status.addCode(MySpaceStatusCode.AGMMCE00214,
                    MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                    this.getClassName() );
               }
            }
            else
            {  status.addCode(MySpaceStatusCode.AGMMCE00301,
                 MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                 this.getClassName() );
            }
         }
         else
         {  status.addCode(MySpaceStatusCode.AGMMCE00215,
              MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                 this.getClassName() );
         }
      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );
      }

      logger.appendMessage("canBeCreated: " + canBeCreated);
      return canBeCreated;
   }

// -----------------------------------------------------------------

/**
 * Internal convenience method to check if a dataHolder exists, and
 * if so then delete it.
 */

   private boolean deleteExistingDataHolder(String account,
     String dataItemName, RegistryManager reg)
   {  boolean deleteOk = false;

      MySpaceStatus status = new MySpaceStatus();

      Vector existingDataItemVector = 
        this.internalLookupDataHoldersDetails(account, dataItemName, reg);
      if (status.getSuccessStatus())
      {  status.reset();
      }

      if (existingDataItemVector != null)
      {  if (existingDataItemVector.size() > 0)
         {  DataItemRecord existingDataItem =
              (DataItemRecord)existingDataItemVector.firstElement();

            int existingId = existingDataItem.getDataItemID();

            deleteOk = this.deleteFile(account, existingId);

            if (deleteOk)
            {  logger.appendMessage(dataItemName + " overwritten.");
            }
         }
      }

      return deleteOk;
   }

// -----------------------------------------------------------------

/**
 * Internal convenience method to check whether a dataHolder exists.
 */

   private boolean existsDataHolder(String account, String dataItemName,
     RegistryManager reg)
   {  boolean exists = false;

      MySpaceStatus status = new MySpaceStatus();

      logger.appendMessage("existsDataHolder, dataItemName: " +
        dataItemName);

      Vector dataItems = 
        this.internalLookupDataHoldersDetails(account, dataItemName, reg);
      if (status.getSuccessStatus())
      {  status.reset();
      }

      logger.appendMessage("existsDataHolder, size: " + dataItems.size() );

      if (dataItems != null)
      {  if (dataItems.size() > 0)
         {  exists = true;
         }
      }

      logger.appendMessage("existsDataHolder, exists: " + exists);
      return exists;
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





