package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatus;
import org.astrogrid.mySpace.mySpaceStatus.MySpaceStatusCode;

/**
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class MySpaceActions
{  private static String registryName;

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
     String communityID, String jobID, int dataItemID)
   {  RegistryManager reg = new RegistryManager(registryName);

      DataItemRecord dataItem = this.internalLookupDataHolderDetails(
        userID, communityID, jobID, dataItemID, reg);

      return dataItem;
   }

// -----------------------------------------------------------------

/**
  * Lookup the details of a named set of DataHolders.
  */

   public Vector lookupDataHoldersDetails(String userID, 
     String communityID, String jobID, String query)
   {  RegistryManager reg = new RegistryManager(registryName);

      Vector dataItemVector = this.internalLookupDataHoldersDetails(
        userID,  communityID, jobID, query, reg);

      return dataItemVector;
   }

// -----------------------------------------------------------------

/**
  * Copy a DataHolder from one location on a MySpace server to another
  * location on the same server.
  */

   public DataItemRecord copyDataHolder(String userID, String communityID,
     String jobID, int oldDataItemID, String newDataItemName)
   {  DataItemRecord returnedDataItem = new DataItemRecord();
      returnedDataItem = null;

//
//   Attempt to open the registry and proceed if ok.

      RegistryManager reg = new RegistryManager(registryName);
      MySpaceStatus status = new MySpaceStatus();
      if (status.getSuccessStatus())
      {

//
//      Assemble the UserAccount from the UserID and CommunityID.

         UserAccount userAcc = new UserAccount(userID, communityID);

//
//      Check the user's authentication and proceed if ok.

         if (userAcc.checkAuthentication() )
         {

//
//         Attempt to lookup the details of the original DataHolder
//         and proceed if ok.

            DataItemRecord oldDataItem = 
              this.internalLookupDataHolderDetails(userID, communityID,
                jobID, oldDataItemID, reg);
            if (status.getSuccessStatus())
            {

//
//            Check that the original item is a DataHolder rather
//            than a container.

               if (oldDataItem.getType() != DataItemRecord.CON)
               {

//
//               Check that the specified output container can be
//               created.

                  if(this.checkCanBeCreated(newDataItemName, userAcc,
                    jobID, reg) == true)
                  {

//
//                  Create a DataItemRecord for the new DataHolder.

                     Date creation = new Date();

                     Calendar cal = Calendar.getInstance();
                     cal.setTime(creation);
                     cal.add(Calendar.DATE, reg.getExpiryPeriod() );
                     Date expiry = cal.getTime();

                     int newdataItemID = reg.getNextDataItemID();
                     String dataItemFileName = "f" + newdataItemID;

                     int dataItemType = oldDataItem.getType();

                     DataItemRecord newDataItem = new DataItemRecord
                       (newDataItemName, newdataItemID,
                       dataItemFileName, userID, creation, expiry,
                       99999, dataItemType, "permissions");

//
//                  Attempt to add this entry to the registry.

                     if (reg.addDataItemRecord(newDataItem) )
                     {

//
//                     Attempt to copy the DataHolder.

                        int containSepPos1 = newDataItemName.indexOf("/");
                        int containSepPos2 =
                          newDataItemName.indexOf("/", containSepPos1+1);
                        int containSepPos3 =
                          newDataItemName.indexOf("/", containSepPos2+1);

                        String serverName;

                        if (containSepPos3 > 0)
                        {

//
//                        Check that the server name is valid.

                           serverName = 
                             newDataItemName.substring(containSepPos2+1,
                               containSepPos3);
                        }
                        else
                        {  serverName = "";
                        }

                        String serverDirectory =
                          reg.getServerDirectory(serverName);

                        String copyFrom = serverDirectory +
                          oldDataItem.getDataItemFile();
                        String copyTo = serverDirectory + dataItemFileName;

                        ServerDriver serverDriver = new ServerDriver();
                        if(serverDriver.copyDataHolder(copyFrom, copyTo) )
                        {

//
//                        The copy succeeded.  Copy the DataItemRecord
//                        for the new DataHolder to the return object.

                           returnedDataItem = newDataItem;
                        }
                        else
                        {

//
//                        The actual copy of the DataHolder failed.
//                        Delete its entry from the registry, to bring
//                        the registry back into line with reality and
//                        report an error.

                           reg.deleteDataItemRecord(newdataItemID);
                           status.addCode("MS-E-FLCRTDH",
                             MySpaceStatusCode.ERROR);
                        }
                     }
                     else
                     {  status.addCode("MS-E-FCRTDHR",
                          MySpaceStatusCode.ERROR);
                     }
                  }
               }
               else
               {  status.addCode("MS-E-CPYDHCN",
                    MySpaceStatusCode.ERROR);
               }
            }
         }
      }

//
//   Re-write the registry file.

      reg.rewriteRegistryFile();

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
     String jobID, int oldDataItemID, String newDataItemName)
   {  DataItemRecord returnedDataItem = new DataItemRecord();
      returnedDataItem = null;

//
//   Attempt to open the registry and proceed if ok.

      RegistryManager reg = new RegistryManager(registryName);
      MySpaceStatus status = new MySpaceStatus();
      if (status.getSuccessStatus())
      {

//
//      Assemble the UserAccount from the UserID and CommunityID.

         UserAccount userAcc = new UserAccount(userID, communityID);

//
//      Check the user's authentication and proceed if ok.

         if (userAcc.checkAuthentication() )
         {

//
//         Attempt to lookup the details of the original DataHolder
//         and proceed if ok.

            DataItemRecord oldDataItem = 
              this.internalLookupDataHolderDetails(userID, communityID,
                jobID, oldDataItemID, reg);
            if (status.getSuccessStatus())
            {

//
//            Check that the original item is a DataHolder rather
//            than a container.

               if (oldDataItem.getType() != DataItemRecord.CON)
               {

//
//               Check that the specified output container can be
//               created.

                  if(this.checkCanBeCreated(newDataItemName, userAcc,
                    jobID, reg) == true)
                  { 

//
//                  Create a DataItemRecord for the new DataHolder.

                     Date creation = new Date();

                     Calendar cal = Calendar.getInstance();
                     cal.setTime(creation);
                     cal.add(Calendar.DATE, reg.getExpiryPeriod() );
                     Date expiry = cal.getTime();

                     int newdataItemID = reg.getNextDataItemID();
                     String dataItemFileName = 
                       oldDataItem.getDataItemFile();
                     int dataItemType = oldDataItem.getType();

                     DataItemRecord newDataItem = new DataItemRecord
                       (newDataItemName, newdataItemID,
                       dataItemFileName, userID, creation, expiry,
                       99999, dataItemType, "permissions");

//
//                  Attempt to add this entry to the registry.

                     if (reg.addDataItemRecord(newDataItem) )
                     {

//
//                     Delete the original entry from the registry.

                        reg.deleteDataItemRecord(oldDataItemID);

//
//                     Set the return argument to the new dataItem.

                        returnedDataItem = newDataItem;
                     }
                     else
                     {  status.addCode("MS-E-FLMOVDH",
                          MySpaceStatusCode.ERROR);
                     }
                  }
               }
               else
               {  status.addCode("MS-E-MOVDHCN",
                     MySpaceStatusCode.ERROR);
               }
            }
         }
      }

//
//   Re-write the registry file.

      reg.rewriteRegistryFile();

      return returnedDataItem;
   }

// -----------------------------------------------------------------

/**
  * Import a new dataHolder.  In Iteration 2 this action is something of
  * kludge.  The corresponding file is assumed to have already been
  * placed in the server.  The action merely creates an entry for it in
  * the MySpace registry.  The MySpace server is not touched.
  *
  * @param fileSize is the size of the file in bytes.
  */

   public DataItemRecord importDataHolder(String userID, String communityID,
     String jobID, String newDataHolderName, String serverFileName,
     int fileSize)
   {  DataItemRecord newDataHolder = new DataItemRecord();
      newDataHolder = null;

//
//   Attempt to open the registry and proceed if ok.

      RegistryManager reg = new RegistryManager(registryName);
      MySpaceStatus status = new MySpaceStatus();
      if (status.getSuccessStatus())
      {

//
//      Assemble the UserAccount from the UserID and CommunityID.

         UserAccount userAcc = new UserAccount(userID, communityID);

//
//      Check the user's authentication and proceed if ok.

         if (userAcc.checkAuthentication() )
         {

//
//         Check that the specified dataHolder can be created.

            if(this.checkCanBeCreated(newDataHolderName, userAcc,
              jobID, reg) == true)
            {

//
//            Create a DataItemRecord for the container.

               Date creation = new Date();

               Calendar cal = Calendar.getInstance();
               cal.setTime(creation);
               cal.add(Calendar.DATE, reg.getExpiryPeriod() );
               Date expiry = cal.getTime();

               int newdataItemID = reg.getNextDataItemID();
               int dataItemType = DataItemRecord.VOT;

               DataItemRecord newDataItem = new DataItemRecord
                 (newDataHolderName, newdataItemID, serverFileName,
                 userID, creation, expiry, fileSize, dataItemType,
                 "permissions");

//
//            Attempt to add this entry to the registry.


               if (reg.addDataItemRecord(newDataItem) )
               {  newDataHolder = newDataItem;
               }
               else
               {  status.addCode("MS-E-FCRTDHR",
                    MySpaceStatusCode.ERROR);
               }
            }
         }
      }

//
//   Re-write the registry file.

      reg.rewriteRegistryFile();

      return newDataHolder;
   }

// -----------------------------------------------------------------

/**
  * Export a DataHolder from the MySpace system.  In Iteration 2 a
  * DataHolder is exported by returning a URL from which a copy of it
  * can be retrieved.
  */

   public String exportDataHolder(String userID, String communityID,
     String jobID, int dataItemID)
   {  String dataHolderURI = null;

//
//   Attempt to open the registry and proceed if ok.

      RegistryManager reg = new RegistryManager(registryName);
      MySpaceStatus status = new MySpaceStatus();
      if (status.getSuccessStatus())
      {

//
//      Assemble the UserAccount from the UserID and CommunityID.

         UserAccount userAcc = new UserAccount(userID, communityID);

//
//      Check the user's authentication and proceed if ok.

         if (userAcc.checkAuthentication() )
         {

//
//         Attempt to lookup the details of the DataHolder and proceed
//         if ok.

            DataItemRecord dataItem = 
              this.internalLookupDataHolderDetails(userID, communityID,
                jobID, dataItemID, reg);
            if (status.getSuccessStatus())
            {

//
//            Check that the original item is a DataHolder rather
//            than a container.

               if (dataItem.getType() != DataItemRecord.CON)
               {

//
//               Check that the user is allowed to access the DataHolder.

                  String permissions;
                  permissions = dataItem.getPermissionsMask();
                  String ownerID;
                  ownerID = dataItem.getOwnerID();

                  if (userAcc.checkAuthorisation(UserAccount.READ,
                    ownerID, permissions))
                  {

//
//                  Assemble the URI for the DataHolder from the
//                  server URI and the file name of the DataHolder.

                     String dataItemName = dataItem.getDataItemName();

                     int containSepPos1 = dataItemName.indexOf("/");
                     int containSepPos2 =
                       dataItemName.indexOf("/", containSepPos1+1);
                     int containSepPos3 =
                       dataItemName.indexOf("/", containSepPos2+1);

                     String serverURI;

                     if (containSepPos3 > 0)
                     {  String serverName =
                          dataItemName.substring
                            (containSepPos2+1, containSepPos3);
                        serverURI = reg.getServerURI(serverName);
                     }
                     else
                     {  serverURI = "bad_URI/";
                     }

                     dataHolderURI =
                       serverURI + dataItem.getDataItemFile();
                  }
                  else
                  {  status.addCode("MS-E-FACCSDH",
                       MySpaceStatusCode.ERROR);
                  }
               }
               else
               {  status.addCode("MS-E-FNTCNDH",
                    MySpaceStatusCode.ERROR);
               }
            }
            else
            {  status.addCode("MS-E-DHNTFND",
                 MySpaceStatusCode.ERROR);
            }
         }
      }

//
//   Do not re-write the registry; unlike most of the other Action
//   methods, this method does not need to re-write the registry at
//   this point because it has not modified it.

      return dataHolderURI;
   }

// -----------------------------------------------------------------

/**
  * Create a new container.  The  operation is implemented by creating
  * a new entry in the MySpace registry.  The MySpace server is not
  * touched.
  */

   public DataItemRecord createContainer(String userID, String communityID,
     String jobID, String newContainerName)
   {  DataItemRecord newContainer = new DataItemRecord();
      newContainer = null;

//
//   Attempt to open the registry and proceed if ok.

      RegistryManager reg = new RegistryManager(registryName);
      MySpaceStatus status = new MySpaceStatus();
      if (status.getSuccessStatus())
      {

//
//      Assemble the UserAccount from the UserID and CommunityID.

         UserAccount userAcc = new UserAccount(userID, communityID);

//
//      Check the user's authentication and proceed if ok.

         if (userAcc.checkAuthentication() )
         {

//
//         Check that the specified container can be created.

            if(this.checkCanBeCreated(newContainerName, userAcc,
              jobID, reg) == true)
            {

//
//            Create a DataItemRecord for the container.

               Date creation = new Date();

               Calendar cal = Calendar.getInstance();
               cal.setTime(creation);
               cal.add(Calendar.DATE, reg.getExpiryPeriod() );
               Date expiry = cal.getTime();

               int newdataItemID = reg.getNextDataItemID();
               String dataItemFileName = "f" + newdataItemID;
               int dataItemType = DataItemRecord.CON;

               DataItemRecord newDataItem = new DataItemRecord
                 (newContainerName, newdataItemID, dataItemFileName,
                 userID, creation, expiry, 0, dataItemType,
                 "permissions");

//
//            Attempt to add this entry to the registry.

               if (reg.addDataItemRecord(newDataItem) )
               {  newContainer = newDataItem;
               }
               else
               {  status.addCode("MS-E-FLCRTCN",
                    MySpaceStatusCode.ERROR);
               }
            }
         }
      }

//
//   Re-write the registry file.

      reg.rewriteRegistryFile();

      return newContainer;
   }

// -----------------------------------------------------------------

/**
  * Delete a DataHolder or container from a MySpace server.
  */

   public boolean deleteDataHolder(String userID, String communityID,
     String jobID, int dataItemID)
   {  boolean returnStatus = false;

//
//   Attempt to open the registry and proceed if ok.

      RegistryManager reg = new RegistryManager(registryName);
      MySpaceStatus status = new MySpaceStatus();
      if (status.getSuccessStatus())
      {

//
//      Assemble the UserAccount from the UserID and CommunityID.

         UserAccount userAcc = new UserAccount(userID, communityID);

//
//      Check the user's authentication and proceed if ok.

         if (userAcc.checkAuthentication() )
         {

//
//         Attempt to lookup the details of the DataHolder and proceed
//         if ok.

            DataItemRecord dataItem = 
              this.internalLookupDataHolderDetails(userID, communityID,
                jobID, dataItemID, reg);
            if (dataItem != null)
            {

//
//            If the DataHolder is a container then check that it is
//            empty.  Containers which are not empty cannot be deleted.

               boolean proceedToDelete = true;

               if (dataItem.getType() == DataItemRecord.CON)
               {  String query = dataItem.getDataItemName() + "/*";

                  Vector childrenDataItemVector = 
                    this.internalLookupDataHoldersDetails(userID,
                      communityID, jobID, query, reg);
                  if (status.getSuccessStatus())
                  {  status.reset();
                  }

                  if (childrenDataItemVector != null)
                  {  proceedToDelete = false;

                     status.addCode("MS-E-DNEMPCN",
                       MySpaceStatusCode.ERROR);
                  }
               }

               if (proceedToDelete)
               {

//
//               Check that the user is permitted to delete the DataHolder:

                  String permissions;
                  permissions = dataItem.getPermissionsMask();
                  String ownerID;
                  ownerID = dataItem.getOwnerID();

                  if (userAcc.checkAuthorisation(UserAccount.READ,
                    ownerID, permissions))
                  {

//
//                  Attempt to delete the DataHolder.

                     String dataItemName = dataItem.getDataItemName();

                     int containSepPos1 = dataItemName.indexOf("/");
                     int containSepPos2 =
                       dataItemName.indexOf("/", containSepPos1+1);
                     int containSepPos3 =
                       dataItemName.indexOf("/", containSepPos2+1);

                     String serverName;
                     if (containSepPos3 > 0)
                     {

//
//                     Check that the server name is valid.

                        serverName = 
                          dataItemName.substring(containSepPos2+1,
                            containSepPos3);
                     }
                     else
                     {  serverName = "";
                     }

                     String serverDirectory =
                       reg.getServerDirectory(serverName);

                     String a = serverDirectory + dataItem.getDataItemFile();

                     ServerDriver serverDriver = new ServerDriver();
                     if (serverDriver.deleteDataHolder(a) )
                     {

//
//                     Delete the entry for the DataHolder in the registry,
//                     to bring the registry into line with reality.

                        reg.deleteDataItemRecord(dataItemID);

//
//                     Set the return status to success.

                       returnStatus = true;
                     }
                     else
                     {  status.addCode("MS-E-FLDELDH",
                          MySpaceStatusCode.ERROR);
                     }
                  }
                  else
                  {  status.addCode("MS-E-NTPERDH",
                       MySpaceStatusCode.ERROR);
                  }
               }
            }
            else
            {  status.addCode("MS-E-DHNTFND",
                 MySpaceStatusCode.ERROR);
            }
         }
      }

//
//   Re-write the registry file.

      reg.rewriteRegistryFile();

      return returnStatus;
   }


// -----------------------------------------------------------------

//
// The following methods are not `action methods', that is they
// do not correspond to high-level functions of the MySpace system.
// Most of them are private.

/**
 * Set the registry name.
 */

   public void setRegistryName(String registryName)
   {  this.registryName = registryName;
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
     String communityID, String jobID, int dataItemID,
     RegistryManager reg)
   {  DataItemRecord dataItem = new DataItemRecord();
      dataItem = null;

      MySpaceStatus status = new MySpaceStatus();

//
//   Assemble the UserAccount from the UserID and CommunityID.

      UserAccount userAcc = new UserAccount(userID, communityID);

//
//   Check the user's authentication and proceed if ok.

      if (userAcc.checkAuthentication() )
      {

//
//      Attempt to lookup up the details of the DataHolder in the
//      mySpace registry.
//
//      Note that in the following clause two conditions are
//      checked for: the item not being in the registry and
//      the user not having the privilege to access it.  The
//      error messages set in these two cases are deliberately
//      identical.

         DataItemRecord dataItemFound =
           reg.lookupDataItemRecord(dataItemID);
         if (dataItemFound != null)
         {

//
//         Check whether the user has the privileges to access the
//         DataHolder.

            String permissions;
            permissions = dataItemFound.getPermissionsMask();
            String ownerID;
            ownerID = dataItemFound.getOwnerID();

            if (userAcc.checkAuthorisation(UserAccount.READ,
              ownerID, permissions))
            {

//
//            All is ok; copy the DataItemRecord to the return argument.

               dataItem = dataItemFound;
            }
            else
            {  status.addCode("MS-E-DHNTFND",
               MySpaceStatusCode.ERROR);
            }
         }
         else
         {  status.addCode("MS-E-DHNTFND",
              MySpaceStatusCode.ERROR);
         }
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

   public Vector internalLookupDataHoldersDetails(String userID, 
     String communityID, String jobID, String query,
     RegistryManager reg)
   {  Vector dataItemVector = new Vector();
      dataItemVector = null;

      MySpaceStatus status = new MySpaceStatus();

//
//  -- debug --------------------------------------------------
//
//      print out details of registry config. file.

//         int expiryPeriod = reg.getExpiryPeriod();
//         System.out.println("Expiry period (days): " + expiryPeriod);

//         Vector serverNames = reg.getServerNames();

//         if (serverNames != null)
//         {  int numServers = serverNames.size();

//            String serverName;
//            String serverURI;
//            String serverDirectory;

//            for (int loop=0; loop < numServers; loop++)
//            {  serverName = (String)serverNames.get(loop);
//               serverURI = reg.getServerURI(serverName);
//               serverDirectory = reg.getServerDirectory(serverName);

//               System.out.println("Server " + loop + " is called "
//                 + serverName + " at " + serverURI + " and "
//                 + serverDirectory);
//            }
//         }
//         else
//         {  System.out.println("No servers specified.");
//         }

//  -- end debug ----------------------------------------------

//
//   Assemble the UserAccount from the UserID and CommunityID.

      UserAccount userAcc = new UserAccount(userID, communityID);

//
//   Check the user's authentication and proceed if ok.

      if (userAcc.checkAuthentication() )
      {

//
//      Attempt lookup in the registry the entries for the DataHolders
//      which match the query string.
//
//      Note that in the following clauses items which the user
//      does not have the privilege to access are treated as though
//      they do not exist.

         Vector dataItemFoundVector =
           reg.lookupDataItemRecords(query);
         if (status.getSuccessStatus())
         {

//
//         Check whether any DataHolders were found (it is perfectly
//         possible that no entries will match the query string).

            if (dataItemFoundVector.size() > 0)
            {

//
//            Examine every entry and check that the user has the
//            privilege to access it.  If not remove it from the list.

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
//            If any DataItemRecords remain in the Vector then copy
//            the Vector to the return vector.  If no items are left
//            then issue an informational message.

               if (dataItemFoundVector.size() > 0)
               {  dataItemVector = dataItemFoundVector;
               }
               else
               {  status.addCode("MS-I-NDHMTCH",
                    MySpaceStatusCode.INFO);
               }
            }
            else
            {  status.addCode("MS-I-NDHMTCH",
                    MySpaceStatusCode.INFO);
            }
         }
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
     UserAccount userAcc, String jobID, RegistryManager reg)
   {  boolean canBeCreated = false;
      MySpaceStatus status = new MySpaceStatus();

//
//   Check that the output DataHolder does not already exist.

      String userID = userAcc.getUserID();
      String communityID = userAcc.getCommunityID();

      Vector checkOutputContainter = 
        this.internalLookupDataHoldersDetails(userID, communityID,
          jobID, newDataItemName, reg);
      if (status.getSuccessStatus())
      {  status.reset();
      }
      if (checkOutputContainter == null)
      {

//
//      Check that the container hierarchy is at least three levels
//      deep; ie. that an attempt is not being made to create a
//      top-level user container or a second-level server container.

         int containSepPos1 = newDataItemName.indexOf("/");
         int containSepPos2 = newDataItemName.indexOf("/", containSepPos1+1);
         int containSepPos3 = newDataItemName.indexOf("/", containSepPos2+1);

         if (containSepPos3 > 0)
         {

//
//         Check that the server name is valid.

            String serverName = 
              newDataItemName.substring(containSepPos2+1, containSepPos3);

//            System.out.println("serverName: " + serverName);

            if(reg.isServerName(serverName))
            {
//
//            Check that the user is permitted to create the output
//            DataHolder:
//              obtain the name of its parent container,
//              check that this container exists,
//              check that the user is allowed to write to this container.

               String parentContainer = newDataItemName.substring
                 (0, newDataItemName.lastIndexOf("/") );
               Vector parentDataItemVector = 
                 this.internalLookupDataHoldersDetails(userID,
                   communityID, jobID, parentContainer, reg);
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
                  {  status.addCode("MS-E-NPRWPCN",
                        MySpaceStatusCode.ERROR);
                  }
               }
               else
               {  status.addCode("MS-E-PCNNTEX",
                    MySpaceStatusCode.ERROR);
               }
            }
            else
            {  status.addCode("MS-E-SRVINVN",
                 MySpaceStatusCode.ERROR);
            }
         }
         else
         {  status.addCode("MS-E-ILLSRCN",
              MySpaceStatusCode.ERROR);
         }
      }
      else
      {  status.addCode("MS-E-DHCNAEX", MySpaceStatusCode.ERROR);
      }

      return canBeCreated;
   }
}
