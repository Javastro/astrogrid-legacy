package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.AxisProperties;
import org.astrogrid.mySpace.mySpaceStatus.*;

import org.apache.log4j.Logger;

/**
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 2.
 */

public class MySpaceActions
{  
	private static Logger logger = Logger.getLogger(MySpaceActions.class);
	private static boolean DEBUG = true;	
	private static String registryName;
	Call call = null;

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
   {  
   	  DataItemRecord dataItem = new DataItemRecord();
      dataItem = null;

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
               {    logger.debug("NOTFOUND DATAITEM HOW WIRED!!!");
               	    status.addCode("MS-E-DHNTFND",
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
//   Re-write the registry.

      reg.finalize();

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
      if (DEBUG) logger.debug("IN MYSPACEACTIONS REGISTRYNAME = "+registryName);
      RegistryManager reg = new RegistryManager(registryName);
      MySpaceStatus status = new MySpaceStatus();
      if (status.getSuccessStatus())
      {

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
      }

//
//   Re-write the registry.

      reg.finalize();

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
    try{
//
//   Attempt to open the registry and proceed if ok.

//      reg = new RegistryManager(registryName);
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
//            Check that the original item is a DataHolder rather
//            than a container.

               if (oldDataItem.getType() != DataItemRecord.CON)
               {

//
//               Check that the specified output container can be
//               created.
                  if ( DEBUG ) logger.debug("CHECKCANBECREATED: "+newDataItemName);
                  if(this.checkCanBeCreated(newDataItemName, userAcc, jobID)
                    == true)
                  {

                     RegistryManager reg = new RegistryManager(registryName);

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
                     if (newDataItem!=null){
                     	if ( DEBUG ) logger.debug("NEWDATAITEMID: "+newDataItem.getDataItemID());
                     }else{
                     	if ( DEBUG ) logger.debug("NEWDATAITEM IS NULL!!");
                     }

//
//                  Attempt to add this entry to the registry.

                     if (reg.addDataItemRecord(newDataItem) )
                     {
                     	if ( DEBUG )logger.debug("ATTEMPT ADDING TO REGISTRY...");
//
//                     Attempt to copy the DataHolder.

//                     ADD CODE TO INVOKE A WEB SERVICE TO INDUCE THE
//                     MYSPACE SERVER TO COPY THE DataHolder.  THEN
//                     PERFORM A TEST THAT ALL IS WELL (THE FAKE TEST
//                     INSERTED BELOW SHOULD BE REMOVED).

    					int containSepPos1 = newDataItemName.indexOf("/");
						int containSepPos2 = newDataItemName.indexOf("/", containSepPos1+1);
						int containSepPos3 = newDataItemName.indexOf("/", containSepPos2+1);

                        String serverName;
						if (containSepPos3 > 0)
						{

//
//						  Check that the server name is valid.

						   serverName = 
							 newDataItemName.substring(containSepPos2+1, containSepPos3);
						}
						else
						{  serverName = "";
						}

                        String serverDirectory = reg.getServerDirectory(serverName);

                        String a = serverDirectory + oldDataItem.getDataItemFile();
                        String b = serverDirectory + dataItemFileName;
                        if ( DEBUG ) logger.debug("COPYDATAHOLDER CALLING SERVERMANAGER: a = "+ a + ", b =" +b);
//							 System.out.println("serverName: " + serverName);
					   call = createServerCall();
					   call.setOperationName( "copyDataHolder" );			
					   call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
					   call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
					   call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
					   String serverResponse = (String)call.invoke( new Object[] {a,b} );
					   if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: "+serverResponse);

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
                           status.addCode("MS-E-FLCRTDH",
                             MySpaceStatusCode.ERROR);
                        }
                     }
                     else
                     {  status.addCode("MS-E-FCRTDHR",
                          MySpaceStatusCode.ERROR);
                     }
                     reg.finalize();
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
//   Re-write the registry.

//      reg.finalize();
    }catch(Exception e){
     //...
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
     String jobID, int oldDataItemID, String newDataItemName)
   {  DataItemRecord returnedDataItem = new DataItemRecord();
      returnedDataItem = null;

//
//   Attempt to open the registry and proceed if ok.

//      reg = new RegistryManager(registryName);
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
//            Check that the original item is a DataHolder rather
//            than a container.

               if (oldDataItem.getType() != DataItemRecord.CON)
               {

//
//               Check that the specified output container can be
//               created.

                  if(this.checkCanBeCreated(newDataItemName, userAcc, jobID)
                    == true)
                  { 

                     RegistryManager reg = new RegistryManager(registryName);

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
                     }
                     else
                     {  status.addCode("MS-E-FLMOVDH",
                          MySpaceStatusCode.ERROR);
                     }
                     reg.finalize();
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
//   Re-write the registry.

//      reg.finalize();

      return returnedDataItem;
   }

// -----------------------------------------------------------------

/**
  * Import a new container.  In Iteration 2 this action is something of
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

//      reg = new RegistryManager(registryName);
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

            if(this.checkCanBeCreated(newDataHolderName, userAcc, jobID)
              == true)
            {

               RegistryManager reg = new RegistryManager(registryName);
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
               reg.finalize();
            }
         }
      }

//
//   Re-write the registry.

//      reg.finalize();

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

//      reg = new RegistryManager(registryName);
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

                        RegistryManager reg =
                          new RegistryManager(registryName);
                        serverURI = reg.getServerURI(serverName);
                        reg.finalize();
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
//   Re-write the registry.

//      reg.finalize();

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

//      reg = new RegistryManager(registryName);
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

            if(this.checkCanBeCreated(newContainerName, userAcc, jobID)
              == true)
            {

               RegistryManager reg = new RegistryManager(registryName);
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
                 userID, creation, expiry, 99999, dataItemType,
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
               reg.finalize();
            }
         }
      }

//
//   Re-write the registry.

//      reg.finalize();

      return newContainer;
   }

// -----------------------------------------------------------------

/**
  * Delete a DataHolder or container from a MySpace server.
  */

   public boolean deleteDataHolder(String userID, String communityID,
     String jobID, int dataItemID)
   {  boolean returnStatus = false;
    try{
//
//   Attempt to open the registry and proceed if ok.

//      reg = new RegistryManager(registryName);
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

//                  ADD CODE TO INVOKE A WEB SERVICE TO INDUCE THE MYSPACE
//                  SERVER TO DELETE THE DataHolder.  THEN PERFORM A TEST
//                  THAT ALL IS WELL (THE FAKE TEST INSERTED BELOW SHOULD
//                  BE REMOVED).

                    String dataItemName = dataItem.getDataItemName();
                    if(DEBUG) logger.debug("DATAITMENAME in ACTION IS: " +dataItemName);
                    
					int containSepPos1 = dataItemName.indexOf("/");
					int containSepPos2 = dataItemName.indexOf("/", containSepPos1+1);
					int containSepPos3 = dataItemName.indexOf("/", containSepPos2+1);

					String serverName;
					if (containSepPos3 > 0)
					{

//
//					  Check that the server name is valid.

					   serverName = 
						 dataItemName.substring(containSepPos2+1, containSepPos3);
						if(DEBUG) logger.debug("SERVERNAME in ACTION IS: " +serverName);
					}
					else
					{  serverName = "";
					}
					
					RegistryManager reg2 =
					  new RegistryManager(registryName);
					String serverDirectory = reg2.getServerDirectory(serverName);
					reg2.finalize();

					String a = serverDirectory + dataItem.getDataItemFile();
					if(DEBUG) logger.debug("a in ACTION IS: " +a);
                        
//						 System.out.println("serverName: " + serverName);
				   call = createServerCall();
				   call.setOperationName( "deleteDataHolder" );			
				   call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
		
				   call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
				   String serverResponse = (String)call.invoke( new Object[] {a} );
				   if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: "+serverResponse);


                     if (status.getSuccessStatus() )
                     {

//
//                     Delete the entry for the DataHolder in the registry,
//                     to bring the registry into line with reality.

                        RegistryManager reg =
                          new RegistryManager(registryName);
                        reg.deleteDataItemRecord(dataItemID);
                        reg.finalize();

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
//   Re-write the registry.

//      reg.finalize();
    }catch(Exception e){
     //...
    }
      return returnStatus;
   }


// -----------------------------------------------------------------

/**
 * Set the registry name.
 */

   public void setRegistryName(String registryName)
   {  this.registryName = registryName;
   }

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
     UserAccount userAcc, String jobID)
   {  boolean canBeCreated = false;
      MySpaceStatus status = new MySpaceStatus();

//
//   Check that the output DataHolder does not already exist.

      String userID = userAcc.getUserID();
      String communityID = userAcc.getCommunityID();

      Vector checkOutputContainter = 
        this.lookupDataHoldersDetails(userID, communityID, jobID,
        newDataItemName);
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

            RegistryManager reg = new RegistryManager(registryName);
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
                 this.lookupDataHoldersDetails(userID, communityID,
                 jobID, parentContainer);

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
            reg.finalize();
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
   
   private Call createServerCall(){
	   Call call = null;
	   try{
		   String endpoint  = "http://localhost:8080/axis/services/ServerManager";
		   Service service = new Service();
		   call = (Call)service.createCall();
		   call.setTargetEndpointAddress( new java.net.URL(endpoint) );
		
		   /*
		   call.setOperationName( "moveDataHolder" );			
		   call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
		   call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
		   call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
		   String serverResponse = call.invoke( new Object[] {content,path} );
		   */
	   }catch(Exception e){
		   MySpaceMessage message = new MySpaceMessage("ERROR_CALL_SERVER_MANAGER");
		   message.getMessage(e.toString());
	   }	
	   return call;
   }   
}
