package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

import org.apache.log4j.Logger;

/**
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 * @TODO update logging statements to use commons-logging
 */

public class MySpaceActions
{  private static Logger logger = Logger.getLogger(MySpaceActions.class);
   private static boolean DEBUG = true;

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

   public DataItemRecord lookupDataHolderDetails(String userID,
     String communityID, String credentials, int dataItemID)
   {  DataItemRecord dataItem = new DataItemRecord();
      dataItem = null;

      try
      {  RegistryManager reg = new RegistryManager(registryName);

         dataItem = this.internalLookupDataHolderDetails(userID,
           communityID, credentials, dataItemID, reg);
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

   public Vector lookupDataHoldersDetails(String userID, 
     String communityID, String credentials, String query)
   {  Vector dataItemVector = new Vector();

      try
      {  RegistryManager reg = new RegistryManager(registryName);

         dataItemVector = this.internalLookupDataHoldersDetails(
           userID,  communityID, credentials, query, reg);
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
  * Generate a list of expired DataHolders.  The method is given a
  * query string (which may optionally include a wild card).  All the
  * dataholders which match this query are selected, their expiry dates
  * checked and a list of all those (if any) which have expired is
  * returned.
  */

   public Vector listExpiredDataHolders(String userID, 
     String communityID, String credentials, String query)
   {  Vector expiredDataItemVector = new Vector();

      try
      {  RegistryManager reg = new RegistryManager(registryName);

//
//      Lookup all the dataItems which match the query and proceed if
//      any were found.

         Vector dataItemVector = this.internalLookupDataHoldersDetails(
           userID,  communityID, credentials, query, reg);
         if (dataItemVector.size() > 0)
         {

//
//         Obtain the current date.

            Date currentDate = new Date();

//         Examine each of the returned dataItems, checking for
//         expired ones.

            DataItemRecord currentDataItem = new DataItemRecord();

            for (int loop=0; loop<dataItemVector.size(); loop++)
            {  currentDataItem =
                 (DataItemRecord)dataItemVector.elementAt(loop);
               Date expiryDate = currentDataItem.getExpiryDate();

               if (currentDate.after(expiryDate) )
               {  expiredDataItemVector.add(currentDataItem);
               }
            }
         }
      }
      catch (Exception all)
      {  MySpaceStatus status  = new MySpaceStatus(
           MySpaceStatusCode.AGMMCE00100, MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return expiredDataItemVector;
   }

// -----------------------------------------------------------------

/**
  * Copy a DataHolder from one location on a MySpace server to another
  * location on the same server.
  */

   public DataItemRecord copyDataHolder(String userID, String communityID,
     String credentials, int oldDataItemID, String newDataItemName)
   {  DataItemRecord returnedDataItem = new DataItemRecord();
      returnedDataItem = null;

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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Attempt to lookup the details of the original DataHolder
//            and proceed if ok.

               DataItemRecord oldDataItem = 
                 this.internalLookupDataHolderDetails(userID, communityID,
                   credentials, oldDataItemID, reg);
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
                       credentials, reg) == true)
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
                          dataItemFileName, userID, creation, creation,
                          dataItemSize, dataItemType, "permissions");

//
//                     Attempt to add this entry to the registry.

                        newDataItem = reg.addDataItemRecord(newDataItem);
                        if (newDataItem != null)
                        {  newdataItemID = newDataItem.getDataItemID();
                           dataItemFileName = newDataItem.getDataItemFile();

//
//                        Attempt to copy the DataHolder.

                           String oldServerName = oldDataItem.getServer();
                           String newServerName = newDataItem.getServer();

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
                              if(serverDriver.copyDataHolder(copyFrom,
                                copyTo) )
                              {

//
//                              The copy succeeded.  Copy the DataItemRecord
//                              for the new DataHolder to the return object.

                                 returnedDataItem = newDataItem;
                              }
                              else
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
                              if(serverDriver.importDataHolder(
                                oldDataHolderURI, copyTo) )
                              {

//
//                              The copy succeeded.  Copy the DataItemRecord
//                              for the new DataHolder to the return object.

                                 returnedDataItem = newDataItem;
                              }
                              else
                              {  copyOk = false;
                              }
                           }

//
//                        Report an error if the copy failed.

                           if (!copyOk)
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

      return returnedDataItem;
   }

// -----------------------------------------------------------------

/**
  * Move a DataHolder from one location on a MySpace server to another
  * location on the same server.
  *
  * The move operation is implemented entirely by manipulating the
  * entries in the MySpace registry.  The DataHolder itself is not
  * touched.
  */

   public DataItemRecord moveDataHolder(String userID, String communityID,
     String credentials, int oldDataItemID, String newDataItemName)
   {  DataItemRecord returnedDataItem = new DataItemRecord();
      returnedDataItem = null;

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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Attempt to lookup the details of the original DataHolder
//            and proceed if ok.

               DataItemRecord oldDataItem = 
                 this.internalLookupDataHolderDetails(userID, communityID,
                   credentials, oldDataItemID, reg);
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
                       credentials, reg) == true)
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
                          dataItemFileName, userID, creation, expiry,
                          dataItemSize, dataItemType, permissionsMask);

//
//                     Attempt to add this entry to the registry.

                        if (reg.updateDataItemRecord(newDataItem) )
                        {
//
//                        Set the return argument to the new dataItem.

                           returnedDataItem = newDataItem;
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

      return returnedDataItem;
   }

// -----------------------------------------------------------------

/**
 * Import a new dataHolder.  A remote file is imported into the MySpace
 * system as a DataHolder.  This remote file is identified by a URI,
 * which is passed as one of the input arguments.  In Iteration 3 this
 * URI must be a URL.
 *
 * upLoadDataHolder and importDataHolder are similar in that both allow 
 * new data to be introduced into a MySpace system.  The difference
 * between them is that importDataHolder is given the URL of a remote 
 * file, which is copied into MySpace, whereas upLoadDataHolder is
 * given a string as an input argument whose contents are written as a 
 * file in MySpace.
 */

   public DataItemRecord importDataHolder(String userID, String communityID,
     String credentials, String importURI, String newDataItemName,
     String contentsType)
   {  DataItemRecord returnedDataItem = new DataItemRecord();
      returnedDataItem = null;

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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Check that the specified dataHolder can be created.

               if(this.checkCanBeCreated(newDataItemName, userAcc,
                 credentials, reg) == true)
               {

//
//               Create a DataItemRecord for the new DataHolder.

                  Date creation = new Date();

                  int newdataItemID = -1;
                  String dataItemFileName = "";

                  int dataItemType =
                    DataItemRecord.translateType(contentsType);

//               fudge, pro. tem.

                  int dataItemSize = 10;

                  DataItemRecord newDataItem = new DataItemRecord
                    (newDataItemName, newdataItemID,
                    dataItemFileName, userID, creation, creation,
                    dataItemSize, dataItemType, "permissions");

//
//               Attempt to add this entry to the registry.

                  newDataItem = reg.addDataItemRecord(newDataItem);
                  if (newDataItem != null )
                  {  newdataItemID = newDataItem.getDataItemID();
                     dataItemFileName = newDataItem.getDataItemFile();
//
//                  Attempt to copy the contents of the input string as
//                  a new file on the server.

                     String serverName= newDataItem.getServer();
                     String serverDirectory =
                       reg.getServerDirectory(serverName);

                     String copyTo = serverDirectory + dataItemFileName;

                     ServerDriver serverDriver = new ServerDriver();
                     if(serverDriver.importDataHolder(importURI, copyTo) )
                     {

//
//                     The import succeeded.  Copy the DataItemRecord
//                     for the new DataHolder to the return object.

                        returnedDataItem = newDataItem;
                     }
                     else
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
                     }
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00203,
                       MySpaceStatusCode.ERROR,
                       MySpaceStatusCode.LOG, this.getClassName() );
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

      return returnedDataItem;
   }

// -----------------------------------------------------------------

/**
 * Up-load a dataHolder.  Save the contents of an input String as a
 * specified DataHolder in the MySpace system.  upLoadDataHolder and
 * importDataHolder are similar in that both allow new data to be
 * introduced into a MySpace system.  The difference between them is
 * that importDataHolder is given the URL of a remote file, which is
 * copied into MySpace, whereas upLoadDataHolder is given a string
 * as an input argument whose contents are written as a file in MySpace.
 */

   public DataItemRecord upLoadDataHolder(String userID, String communityID,
     String credentials, String newDataItemName, String contents,
     String contentsType)
   {  DataItemRecord returnedDataItem = new DataItemRecord();
      returnedDataItem = null;

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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Check that the specified dataHolder can be created.

               if(this.checkCanBeCreated(newDataItemName, userAcc,
                 credentials, reg) == true)
               {

//
//               Create a DataItemRecord for the new DataHolder.

                  Date creation = new Date();

                  int newdataItemID = -1;
                  String dataItemFileName = "";

                  int dataItemType =
                    DataItemRecord.translateType(contentsType);
                  int dataItemSize = contents.length();

                  DataItemRecord newDataItem = new DataItemRecord
                    (newDataItemName, newdataItemID,
                    dataItemFileName, userID, creation, creation,
                    dataItemSize, dataItemType, "permissions");

//
//               Attempt to add this entry to the registry.

                  newDataItem = reg.addDataItemRecord(newDataItem);

                  System.out.println("new file name: " +
                     newDataItem.getDataItemFile() );

                  if (newDataItem != null)
                  {  newdataItemID = newDataItem.getDataItemID();
                     dataItemFileName = newDataItem.getDataItemFile();

//
//                  Attempt to copy the contents of the input string as
//                  a new file on the server.

                     String serverName = newDataItem.getServer();
                     String serverDirectory =
                       reg.getServerDirectory(serverName);

                     String copyTo = serverDirectory + dataItemFileName;

                     ServerDriver serverDriver = new ServerDriver();
                     if(serverDriver.upLoadString(contents, copyTo) )
                     {

//
//                     The up-load succeeded.  Copy the DataItemRecord
//                     for the new DataHolder to the return object.

                        returnedDataItem = newDataItem;
                     }
                     else
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
                     }
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00203,
                       MySpaceStatusCode.ERROR,
                       MySpaceStatusCode.LOG, this.getClassName() );
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

      return returnedDataItem;
   }


// -----------------------------------------------------------------

/**
 * Export a DataHolder from the MySpace system.  In Iteration 2 a
 * DataHolder is exported by returning a URL from which a copy of it
 * can be retrieved.
 */

   public String exportDataHolder(String userID, String communityID,
     String credentials, int dataItemID)
   {  String dataHolderURI = null;

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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.READ) )
            {

//
//            Attempt to lookup the details of the DataHolder and proceed
//            if ok.

               DataItemRecord dataItem = 
                 this.internalLookupDataHolderDetails(userID, communityID,
                   credentials, dataItemID, reg);
               if (status.getSuccessStatus())
               {

//
//               Check that the original item is a DataHolder rather
//               than a container.

                  if (dataItem.getType() != DataItemRecord.CON)
                  {

//
//                  Check that the user is allowed to access the DataHolder.

                     String permissions;
                     permissions = dataItem.getPermissionsMask();
                     String ownerID;
                     ownerID = dataItem.getOwnerID();

                     if (userAcc.checkAuthorisation(UserAccount.READ,
                       ownerID, permissions))
                     {

//
//                     Assemble the URI for the DataHolder from the
//                     server URI and the file name of the DataHolder.

                        String serverName = dataItem.getServer();
                        String serverURI = reg.getServerURI(serverName);
                        dataHolderURI =
                          serverURI + dataItem.getDataItemFile();
                     }
                     else
                     {  status.addCode(MySpaceStatusCode.AGMMCE00207,
                          MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                          this.getClassName() );
                     }
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00208,
                       MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                       this.getClassName() );
                  }
               }
               else
               {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                    MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                    this.getClassName() );
               }
            }
         }

//
//      Do not re-write the registry; unlike most of the other Action
//      methods, this method does not need to re-write the registry at
//      this point because it has not modified it.

      }
      catch (Exception all)
      {  status.addCode(MySpaceStatusCode.AGMMCE00100,
           MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
           this.getClassName() );
      }

      return dataHolderURI;
   }

// -----------------------------------------------------------------

/**
 * Create a new container.  The  operation is implemented by creating
 * a new entry in the MySpace registry.  The MySpace server is not
 * touched.
 */

   public DataItemRecord createContainer(String userID, String communityID,
     String credentials, String newContainerName)
   {  DataItemRecord newContainer = new DataItemRecord();
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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Check that the specified container can be created.

               if(this.checkCanBeCreated(newContainerName, userAcc,
                 credentials, reg) == true)
               {

//
//               Create a DataItemRecord for the container.

                  Date creation = new Date();

                  int newdataItemID = -1;
                  String dataItemFileName = "";
                  int dataItemType = DataItemRecord.CON;

                  DataItemRecord newDataItem = new DataItemRecord
                    (newContainerName, newdataItemID, dataItemFileName,
                    userID, creation, creation, 0, dataItemType,
                    "permissions");

//
//               Attempt to add this entry to the registry.

                  newDataItem = reg.addDataItemRecord(newDataItem);
                  if (newDataItem != null)
                  {  newContainer = newDataItem;
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

      return newContainer;
   }

// -----------------------------------------------------------------

/**
  * Delete a DataHolder or container from a MySpace server.
  */

   public boolean deleteDataHolder(String userID, String communityID,
     String credentials, int dataItemID)
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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Attempt to lookup the details of the DataHolder and proceed
//            if ok.

               DataItemRecord dataItem = 
                 this.internalLookupDataHolderDetails(userID, communityID,
                   credentials, dataItemID, reg);
               if (dataItem != null)
               {

//
//               If the DataHolder is a container then check that it is
//               empty.  Containers which are not empty cannot be deleted.

                  boolean proceedToDelete = true;

                  if (dataItem.getType() == DataItemRecord.CON)
                  {  String query = dataItem.getDataItemName() + "/*";

                     Vector childrenDataItemVector = 
                       this.internalLookupDataHoldersDetails(userID,
                         communityID, credentials, query, reg);
                     if (status.getSuccessStatus())
                     {  status.reset();
                     }

                     if (childrenDataItemVector != null)
                     {  proceedToDelete = false;

                        status.addCode(MySpaceStatusCode.AGMMCE00210,
                          MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                          this.getClassName() );
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
                     {

//
//                     Attempt to delete the DataHolder.

                        String serverName = dataItem.getServer();
                        String serverDirectory =
                          reg.getServerDirectory(serverName);

                        String a = serverDirectory +
                          dataItem.getDataItemFile();

                        ServerDriver serverDriver = new ServerDriver();
                        if (serverDriver.deleteDataHolder(a) )
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

// -----------------------------------------------------------------

/**
  * Change the owner of a DataHolder.
  */

   public DataItemRecord changeOwnerDataHolder(String userID,
     String communityID, String credentials, int dataItemID, String newOwnerID)
   {  DataItemRecord returnedDataItem = new DataItemRecord();
      returnedDataItem = null;

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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Attempt to lookup the details of the DataHolder and proceed
//            if ok.

               DataItemRecord dataItem = 
                 this.internalLookupDataHolderDetails(userID, communityID,
                   credentials, dataItemID, reg);
               if (status.getSuccessStatus())
               {

//
//               Check that the user is allowed to access the DataHolder.

                  String permissions;
                  permissions = dataItem.getPermissionsMask();
                  String ownerID;
                  ownerID = dataItem.getOwnerID();

                  if (userAcc.checkAuthorisation(UserAccount.WRITE,
                    ownerID, permissions))
                  {

//
//                  Create a DataItemRecord for the replacement
//                  DataHolder, setting the new owner.

                     String dataItemName = dataItem.getDataItemName();
                     String dataItemFile = dataItem.getDataItemFile();
                     Date creationDate = dataItem.getCreationDate();
                     Date expiryDate = dataItem.getExpiryDate();
                     int size = dataItem.getSize();
                     int type = dataItem.getType();
                     String permissionsMask = dataItem.getPermissionsMask();

                     DataItemRecord newDataItem = new
                        DataItemRecord(dataItemName, dataItemID,
                          dataItemFile, newOwnerID, creationDate,
                          expiryDate, size, type, permissionsMask);

//
//                  Update the entry with this new DataItemRecord.

                     if (reg.updateDataItemRecord(newDataItem) )
                     {  returnedDataItem  = newDataItem;
                     }
                     else
                     {  status.addCode(MySpaceStatusCode.AGMMCE00203,
                          MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                          this.getClassName() );
                     }
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00207,
                       MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                       this.getClassName() );
                  }
               }
               else
               {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                    MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
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

      return returnedDataItem;
   }

// -----------------------------------------------------------------

/**
 * Advance the expiry date of a DataHolder.
 *
 * @param advance The number of days by which the expiry date is to
 * be advanced into the future.  A negative value will bring the
 * expiry date closer to the present.
 */

   public DataItemRecord advanceExpiryDataHolder(String userID,
     String communityID, String credentials, int dataItemID, int advance)
   {  DataItemRecord returnedDataItem = new DataItemRecord();
      returnedDataItem = null;

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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Attempt to lookup the details of the DataHolder and proceed
//            if ok.

               DataItemRecord dataItem = 
                 this.internalLookupDataHolderDetails(userID, communityID,
                   credentials, dataItemID, reg);
               if (status.getSuccessStatus())
               {

//
//               Check that the user is allowed to access the DataHolder.

                  String permissions;
                  permissions = dataItem.getPermissionsMask();
                  String ownerID;
                  ownerID = dataItem.getOwnerID();

                  if (userAcc.checkAuthorisation(UserAccount.WRITE,
                    ownerID, permissions))
                  {

//
//                  Create a DataItemRecord for the replacement
//                  DataHolder, calculating the new expiry date.

                     String dataItemName = dataItem.getDataItemName();
                     String dataItemFile = dataItem.getDataItemFile();
                     Date creationDate = dataItem.getCreationDate();
                     int size = dataItem.getSize();
                     int type = dataItem.getType();
                     String permissionsMask = dataItem.getPermissionsMask();

                     Date currentExpiryDate = dataItem.getExpiryDate();

                     Calendar cal = Calendar.getInstance();
                     cal.setTime(currentExpiryDate);
                     cal.add(Calendar.DATE, advance);
                     Date newExpiryDate = cal.getTime();

                     DataItemRecord newDataItem = new
                        DataItemRecord(dataItemName, dataItemID,
                          dataItemFile, ownerID, creationDate,
                          newExpiryDate, size, type, permissionsMask);

//
//                  Update the entry with this new DataItemRecord.

                     if (reg.updateDataItemRecord(newDataItem) )
                     {  returnedDataItem  = newDataItem;
                     }
                     else
                     {  status.addCode(MySpaceStatusCode.AGMMCE00203,
                          MySpaceStatusCode.ERROR, MySpaceStatusCode.LOG,
                          this.getClassName() );
                     }
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00207,
                       MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                       this.getClassName() );
                  }
               }
               else
               {  status.addCode(MySpaceStatusCode.AGMMCE00201,
                    MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
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

      return returnedDataItem;
   }


// -----------------------------------------------------------------

/**
  * Add a new user.  The  operation is implemented by creating
  * a new top-level containers in the MySpace registry corresponding to
  * the new user and the servers to which he has been allocated.  The
  * MySpace server is not touched.
  *
  * @param userID The user ID of the new user who is to be added.  Note
  * that in a sense this argument is performing two roles: it is both
  * the ID of the user performing the operation and the ID of the user
  * being added.  Thus the user is `adding himself'.
  *
  * @param servers Vector of one or more servers to which the user
  * has been allocated.
  */

   public boolean createUser(String userID, String communityID,
     String credentials, Vector servers)
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
//         Assemble the UserAccount from the UserID and CommunityID.

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Check that the user is permitted to add new users.

               if (userAcc.checkCanModifyUsers() )
               {  Vector containerNames = new Vector();


//
//               Create a container name for the top-level container
//               of the new user.

                  String containerName = userAcc.getBaseContainer();
                  containerNames.add(containerName);

//
//               Create the container name for every server.

                  for (int loop = 0; loop < servers.size(); loop++)
                  {  containerName = userAcc.getBaseContainer() +  "/" + 
                       (String)servers.get(loop);
                     containerNames.add(containerName);
                  }

//
//               Create the standard workflow, query and votable containers.

                  containerName = userAcc.getBaseContainer() +  "/" +
                    (String)servers.elementAt(0) +  "/query";
                  containerNames.add(containerName);

                  containerName = userAcc.getBaseContainer() +  "/" +
                    (String)servers.elementAt(0) +  "/workflow";
                  containerNames.add(containerName);

                  containerName = userAcc.getBaseContainer() +  "/" +
                    (String)servers.elementAt(0) +  "/votable";
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
                          creation, 0, DataItemRecord.CON, "permissions");

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
      }

      return returnStatus;
   }


// -----------------------------------------------------------------

/**
 * Delete a user.  The  operation is implemented by deleting the
 * entire tree of containers belonging to the user.  Before attempting
 * to delete any of the containers a check is made that the tree
 * does not contain any dataHolders (ie. that it is solely containers).
 * If any dataHolders are found operation is aborted without touching
 * the tree.
 *
 * @param userID The user ID of the user who is to be deleted.  Note
 * that in a sense this argument is performing two roles: it is both
 * the ID of the user performing the operation and the ID of the user
 * being deleted.  Thus the user is `deleting himself'.
 */

   public boolean deleteUser(String userID, String communityID,
     String credentials)
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

            UserAccount userAcc = new UserAccount(userID, communityID,
              credentials);

//
//         Check the user's system authorisation and proceed if ok.

            if (userAcc.checkSystemAuthorisation(UserAccount.WRITE) )
            {

//
//            Check that the user is permitted to delete users.

               if (userAcc.checkCanModifyUsers() )
               {  

//
//               Lookup all the entries for the user in the registry and
//               proceed if some entries were found.

                  String query = userAcc.getBaseContainer() + "*";

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

                        if (itemRec.getType() != DataItemRecord.CON)
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
     String communityID, String credentials, int dataItemID,
     RegistryManager reg)
   {  DataItemRecord dataItem = new DataItemRecord();
      dataItem = null;

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Assemble the UserAccount from the UserID and CommunityID.

         UserAccount userAcc = new UserAccount(userID, communityID,
           credentials);

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

   private Vector internalLookupDataHoldersDetails(String userID, 
     String communityID, String credentials, String query,
     RegistryManager reg)
   {  Vector dataItemVector = new Vector();
      dataItemVector = null;

      MySpaceStatus status = new MySpaceStatus();

      try
      {

//
//      Assemble the UserAccount from the UserID and CommunityID.

         UserAccount userAcc = new UserAccount(userID, communityID,
           credentials);

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
 *  <li> that the user is allowed to write to the parent.
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

      try
      {

//
//      Check that the output DataHolder does not already exist.

         String userID = userAcc.getUserId();
         String communityID = userAcc.getCommunityId();

         Vector checkOutputContainter = 
           this.internalLookupDataHoldersDetails(userID, communityID,
             credentials, newDataItemName, reg);
         if (status.getSuccessStatus())
         {  status.reset();
         }
         if (checkOutputContainter == null)
         {

//
//         Check that the container hierarchy is at least three levels
//         deep; ie. that an attempt is not being made to create a
//         top-level user container or a second-level server container.

            int containSepPos1 = newDataItemName.indexOf("/");
            int containSepPos2 = newDataItemName.indexOf("/",
              containSepPos1+1);
            int containSepPos3 = newDataItemName.indexOf("/",
              containSepPos2+1);

            if (containSepPos3 > 0)
            {

//
//            Check that the server name is valid.

               String serverName = 
                 newDataItemName.substring(containSepPos2+1, containSepPos3);

               if(reg.isServerName(serverName))
               {
//
//               Check that the user is permitted to create the output
//               DataHolder:
//                 obtain the name of its parent container,
//                 check that this container exists,
//                 check that the user is allowed to write to this container.

                  String parentContainer = newDataItemName.substring
                    (0, newDataItemName.lastIndexOf("/") );
                  Vector parentDataItemVector = 
                    this.internalLookupDataHoldersDetails(userID,
                      communityID, credentials, parentContainer, reg);
                  if (status.getSuccessStatus())
                  {  status.reset();
                  }

                  if (parentDataItemVector != null)
                  {  DataItemRecord parentDataItem =
                       (DataItemRecord)parentDataItemVector.firstElement();

                     String permissions;
                     permissions = parentDataItem.getPermissionsMask();
                     String ownerID;
                     ownerID = parentDataItem.getOwnerID();

                     if (userAcc.checkAuthorisation(UserAccount.WRITE,
                       ownerID, permissions))
                     {  canBeCreated = true;
                     }
                     else
                     {  status.addCode(MySpaceStatusCode.AGMMCE00213,
                           MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                           this.getClassName() );
                     }
                  }
                  else
                  {  status.addCode(MySpaceStatusCode.AGMMCE00214,
                       MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG,
                           this.getClassName() );
                  }
               }
               else
               {  status.addCode(MySpaceStatusCode.AGMMCE00300,
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

      return canBeCreated;
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
