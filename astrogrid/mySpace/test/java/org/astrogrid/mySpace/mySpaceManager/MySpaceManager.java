package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceStatus.*;

/**
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class MySpaceManager
{  private static String registryName;
   private RegistryManager reg = new RegistryManager();
                                    // MySpace registry for this mySpace.

//
// Constructor.

/**
 * Default constructor.
 */

   public MySpaceManager()
   {  super();
   }

//
// Action methods.
//
// Each of the following methods implements one of the actions which
// the MySpaceManager can perform.

// -----------------------------------------------------------------

/**
  * Lookup the details of a single DataHolder.
  */

   public DataItemRecord lookupDataHolderDetails(String userID,
     String communityID, String jobID, int dataItemID)
   {  DataItemRecord dataItem = new DataItemRecord();
      dataItem = null;

//
//   Attempt to open the registry and proceed if ok.

      reg = new RegistryManager(registryName);
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
//               All is ok; copy the DataItemRecord to the return
//               argument.

                  dataItem = dataItemFound;
               }
               else
               {  status.addMessage(
                    "Requested DataHolder not found.", MySpaceMessage.ERROR);
               }
            }
            else
            {  status.addMessage(
                 "Requested DataHolder not found.", MySpaceMessage.ERROR);
            }
         }
      }

      return dataItem;
   }

// -----------------------------------------------------------------

/**
  * Lookup the details of a named set of DataHolders.
  */

   public Vector lookupDataHoldersDetails(String userID, 
     String communityID, String jobID, String query)
   {  Vector dataItemVector = new Vector();
      dataItemVector = null;

//
//   Attempt to open the registry and proceed if ok.

      reg = new RegistryManager(registryName);
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
                  {  status.addMessage(
                      "No DataHolders matched string: " + query,
                      MySpaceMessage.INFO);
                  }
               }
               else
               {  status.addMessage(
                    "No DataHolders matched string: " + query,
                    MySpaceMessage.INFO);
               }
            }
         }
      }

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

      reg = new RegistryManager(registryName);
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
              this.lookupDataHolderDetails(userID, communityID, jobID,
                oldDataItemID);
            if (status.getSuccessStatus())
            {

//
//            Check that the user is permitted to create the output
//            DataHolder:
//              obtain the name of its parent container,
//              check that the user is allowed to write to this container.

               String parentContainer = newDataItemName.substring
                                   (0, newDataItemName.lastIndexOf("/") );
               Vector parentDataItemVector = 
                 this.lookupDataHoldersDetails(userID, communityID, jobID,
                   parentContainer);

               if (parentDataItemVector != null)
               {  DataItemRecord parentDataItem =
                    (DataItemRecord)parentDataItemVector.firstElement();

                  String permissions;
                  permissions = parentDataItem.getPermissionsMask();
                  String ownerID;
                  ownerID = parentDataItem.getOwnerID();

                  if (userAcc.checkAuthorisation(UserAccount.WRITE,
                    ownerID, permissions))
                  {

//
//                  Create a DataItemRecord for the new DataHolder.

                     Date creation = new Date();
                     creation = Calendar.getInstance().getTime();

                     int newdataItemID = reg.getNextDataItemID();
                     String dataItemFileName = "f" + newdataItemID;

                     DataItemRecord newDataItem = new DataItemRecord
                       (newDataItemName, newdataItemID,
                       dataItemFileName, userID, creation, creation,
                       99999, DataItemRecord.VOT, "permissions");

//
//                  Attempt to add this entry to the registry.

                     if (reg.addDataItemRecord(newDataItem) )
                     {

//
//                     Attempt to copy the DataHolder.

//                     ADD CODE TO INVOKE A WEB SERVICE TO INDUCE THE
//                     MYSPACE SERVER TO COPY THE DataHolder.  THEN
//                     PERFORM A TEST THAT ALL IS WELL (THE FAKE TEST
//                     INSERTED BELOW SHOULD BE REMOVED).

                        if (status.getSuccessStatus() )
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
                           status.addMessage(
                             "Failed to create DataHolder "
                             + newDataItemName + ".",
                             MySpaceMessage.ERROR);
                        }
                     }
                     else
                     {  status.addMessage(
                          "Failed to create DataHolder "
                          + newDataItemName + 
                          " (already exists).", MySpaceMessage.ERROR);
                     }
                  }
                  else
                  {  status.addMessage(
                       "The user is not authorised to write to container "
                       + parentContainer + ".", MySpaceMessage.ERROR);
                  }
               }
               else
               {  status.addMessage(
                    "The parent container " + parentContainer
                    + " does not exist.", MySpaceMessage.ERROR);
               }
            }
         }
      }

//
//   Re-write the registry.

      reg.finalize();

      return returnedDataItem;
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

      reg = new RegistryManager(registryName);
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
              this.lookupDataHolderDetails(userID, communityID, jobID,
                dataItemID);
            if (dataItem != null)
            {

//
//            If the DataHolder is a container then check that it is
//            empty.  Containers which are not empty cannot be deleted.

               boolean proceedToDelete = true;

               if (dataItem.getType() == DataItemRecord.CON)
               {  String query = dataItem.getDataItemName() + "/*";

                  Vector childrenDataItemVector = 
                    this.lookupDataHoldersDetails(userID, communityID,
                    jobID, query);

                  if (childrenDataItemVector != null)
                  {  proceedToDelete = false;

                     status.addMessage(
                       "Container " + dataItem.getDataItemName()
                       + " is not empty.", MySpaceMessage.ERROR);
                     status.addMessage(
                       "Only empty containers can be deleted.",
                       MySpaceMessage.ERROR);
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

//                  ADD CODE TO INVOKE A WEB SERVICE TO INDUCE THE MYSPACE
//                  SERVER TO DELETE THE DataHolder.  THEN PERFORM A TEST
//                  THAT ALL IS WELL (THE FAKE TEST INSERTED BELOW SHOULD
//                  BE REMOVED).

                     if (status.getSuccessStatus() )
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
                     {  status.addMessage(
                          "Failed to delete DataHolder "
                          + dataItem.getDataItemName() + ".",
                          MySpaceMessage.ERROR);
                     }
                  }
                  else
                  {  status.addMessage(
                       "The user is not permitted to delete "
                       + dataItem.getDataItemName() + ".",
                       MySpaceMessage.ERROR);
                  }
               }
            }
            else
            {  status.addMessage(
                 "Unable to find DataHolder with identifier"
                 + dataItemID + ".", MySpaceMessage.ERROR);
            }
         }
      }

//
//   Re-write the registry.

      reg.finalize();

      return returnStatus;
   }


// -----------------------------------------------------------------

/**
 * Set the registry name.
 */

   public void setRegistryName(String registryName)
   {  this.registryName = registryName;
   }
}
