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

   private static int dataItemIDSeqNo

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

/**
  * Lookup the details of a single DataHolder.
  */

   public DataItemRecord lookupDataHolderDetails(String userID,
     String communityID, String jobID, int dataItemID)
   {  DataItemRecord dataItem = new DataItemRecord();

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
            if (status.getSuccessStatus())
            {

//
//            Check whether the user has the privileges to access the
//            DataHolder.

               String permissions;
               permissions = dataItemFound.getPermissionsMask();
               String ownerID;
               ownerID = dataItemFound.getOwnerID();

               if (userAcc.checkAuthorisation("r", ownerID, permissions))
               {

//
//               All is ok; copy the DataItemRecord to the return
//               argument.

                  dataItem = dataItemFound;
               }
               else
               {  dataItem = null;
                  status.addMessage(
                    "Requested DataHolder not found.", "e");
               }
            }
            else
            {  dataItem = null;
               status.addMessage(
                 "Requested DataHolder not found.", "e");
            }
         }
      }

      return dataItem;
   }

/**
  * Copy a DataHolder from one location on a MySpace server to another
  * location on the same server.
  */

   public DataItemRecord copyDataHolder(String userID, String communityID,
     String jobID, int oldDataItemID, String newDataItemName)
   {  DataItemRecord newDataItem = new DataItemRecord();

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
               DataItemRecord parentDataItem = 
                 this.lookupDataHoldersDetails(userID, communityID, jobID,
                   parentContainer);

               String permissions;
               permissions = parentDataItem.getPermissionsMask();
               String ownerID;
               ownerID = parentDataItem.getOwnerID();

               if (userAcc.checkAuthorisation("w", ownerID, permissions))
               {

//
//               Create a DataItemRecord for the new DataHolder.

                  Date creation = new Date();
                  creation = Calendar.getInstance().getTime();

                  dataItemIDSeqNo = dataItemIDSeqNo + 1;
                  String dataItemFileName = "f" + dataItemIDSeqNo;

                  DataItemRecord newDataItem = new DataItemRecord
                    (newDataItemName, dataItemIDSeqNo, 
                    dataItemFileName, userID, creation, creation,
                    99999, "VOT", "permissions");

//
//               Attempt to add this entry to the registry.

                  if (reg.addDataItemRecord(newDataItem) )
                  {

//
//                  Attempt to copy the DataHolder.

//                  ADD CODE TO INVOKE WEB SERVICE TO INDUCE THE SERVER
//                  MANAGER TO COPY THE DataHolder.  AND PERFORM A
//                  TEST THAT ALL IS WELL (FAKE TEST INSERTED BELOW
//                  SHOULD BE REMOVED)

                     if (!status.getSuccessStatus() )
                     {

//
//                     The actual copy of the DataHolder failed.
//                     Delete its entry from the registry, to bring
//                     the registry back into line with reality and
//                     report an error.

                        reg.addDataItemRecord(newDataItem);
                        status.addMessage(
                          "Failed to create DataHolder "
                          + newDataItemName + ".", "e");
                     }
                  }
                  else
                  {  status.addMessage(
                       "Failed to create DataHolder "
                       + newDataItemName + 
                       "(registry update failure).", "e");
                  }
               }
               else
               {  status.addMessage(
                    "The user is not authorised to write to container "
                    + parentContainer + ".", "e");
               }
            }
         }
      }

      return dataItem;
   }

/**
 * Set the registry name.
 */

   public void setRegistryName(String registryName)
   {  this.registryName = registryName;
   }
}
