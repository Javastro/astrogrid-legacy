/*
 * $Id: MySpaceClient.java,v 1.2 2003/12/08 20:33:40 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.mySpace.delegate;

import java.io.*;

import java.net.URL;
import java.util.Vector;

/**
 * These are the methods that myspace delegates must implement.
 * <p>
 * It exists so that th delegates have
 * a common reference point - if the MySpaceManagerDelegate interface changes, this
 * will have to be changed as well, and so all delegates that implement it.  Thus we
 * can spot & fix such changes at build time.
 *
 * @todo throw proper exceptions not the generic Exception...
 *
 */

public interface MySpaceClient {
   
   public static final String OVERWRITE = "Overwrite";
   public static final String APPEND = "Append";
   
   /**
    * Search a list of MSSs and return the MySpace names of entries which
    * match the query.
    *
    * @param userId User identifier.
    * @param communityId Community identifier.
    * @param query Query which MySpace names should match, eg:
    *    /userid@communityid/server/workflows/A*
    *
    * @return A list of MySpace names which match the query.  Note that
    * short (and incomplete) MySpace names are returned, with the details
    * of their enclosing containers removed.  A Vector is returned, each
    * element of which corresponds to one of the MSS searched.  Each of
    * these elements is itself a Vector, whose elements are Strings
    * containing the MySpace names which matched the query.
    */
   
   public Vector listDataHoldings(String userId, String communityId, String credential, String query)throws Exception;
   
   /**
    * Search a list of MSSs and return the details of all the MySpace
    * entries which match the query.  The details for each entry are
    * a string containing XML.
    *
    * @param userId User identifier.
    * @param communityId Community identifier.
    * @param query Query which MySpace names should match, eg:
    *    /userid@communityid/server/workflows/A*
    *
    * @return A list of MySpace entries which match the query.  For each
    * matching entry an XML string containing the details is returned.
    * A Vector is returned, each element of which corresponds to one of
    * the MSS searched.  Each of these elements is itself a Vector,
    * whose elements are Strings containing the details of the entry
    *which matched the query.
    */
   
   public Vector listDataHoldingsGen(String userId, String communityId, String credential, String query)throws Exception;
   
   /**
    *
    * @param userId
    * @param communityId
    * @param serverFileName: full file name eg: /clq/serv1/File1.xml
    * @return
    * @throws Exception
    */
   public String listDataHolding(String userId, String communityId, String credential, String serverFileName) throws Exception;
   
   /**
    * @param userId
    * @param communityId
    * @param serverFileName: full file name copy from
    * @param newDataItemName: full file name copy to
    * @return
    * @throws Exception
    */
   public String copyDataHolding(String userId, String communityId, String credential, String serverFileName, String newDataItemName) throws Exception;
   
   /**
    * Retrieve a copy of a dataHolder on a remote MSS and save it with a
    * specified MySpace name on the current MSS.
    *
    * @param userId User identifier.
    * @param communityId Community identifier.
    * @param remoteMssUrl URL of the remote MSS.
    * @param remoteMySpaceName MySpace name of the dataHolder to be
    *   retrieved from the remote MSS.
    * @param newMySpaceName MySpace name for the copy of the dataHolder on
    *   the current MSS.
    *
    * @return boolean; true if the copy succeeded.
    * @throws Exception
    */
   
   public boolean copyRemoteDataHolding(String userId, String communityId, String credential, String remoteMssUrl, String remoteMySpaceName, String newMySpaceName) throws Exception;
   
   /**
    *
    * @param userId
    * @param communityId
    * @param serverFileName: ole file full name
    * @param newDataItemName: new file full name
    * @return
    * @throws Exception
    */
   
   public String renameDataHolding(String userId, String communityId, String credential, String serverFileName, String newDataItemName) throws Exception;
   
   /**
    *
    * @param userId
    * @param communityId
    * @param serverFileName: Full file name which you want to delete
    * @return
    * @throws Exception
    */
   public String deleteDataHolding(String userId, String communityId, String credential, String serverFileName) throws Exception;
   
   /** saveDataHolding(upLoad),this function will save workflow/query into MySpace system.
    * @param: userId: userid
    * @param: communityId
    * @param: fileName: unique file name for Workflow or Query you want to store.
    * @param: fileContent: content of workflow or data query
    * @param: category "WF" or "QUERY", if not set, default is "VOTable"
    * @param: action "Overwrite" or "Append", if not set, default is "Overwrite"
    * @return: boolean true if file successfully stored in MySapce false otherwise.
    */
   public boolean saveDataHolding(String userId, String communityId, String credential, String fileName, String fileContent,
                                  String category, String action) throws Exception;
   
   /**
    * saveDataHoldingURL is different from saveDataHolding since it is taking a URL where MySpace will pull the file from.
    * @param userId
    * @param communityId
    * @param fileName
    * @param importURI - url that save the dataholding from
    * @param category
    * @param action
    * @return
    * @throws Exception
    */
   
   public boolean saveDataHoldingURL(String userId, String communityId, String credential, String fileName, String importURL,
                                     String category, String action) throws Exception;
   
   /**
    * Retrieve the contents of a dataHolder and supply them as the String
    * returned by the method.
    *
    * @param userId User identifier.
    * @param communityId community identifier.
    * @param  mySpaceName MySpace name of the dataHolder whose contents are
    *   to be retrieved.
    *
    * @return: A String containing the contents of the specified dataHolder.
    */
   public String getDataHolding(String userId, String communityId,
                                String credential, String mySpaceName) throws Exception;
   
   /**
    * Return the URL of a dataHolder.
    *
    * @param userId User identifier.
    * @param communityId community identifier.
    * @param  mySpaceName MySpace name of the dataHolder whose contents are
    *   to be retrieved.
    *
    * @return: The URL for the specified  dataHolder.
    */
   
   public String getDataHoldingUrl(String userId, String communityId,
                                   String credential, String mySpaceName) throws Exception;
   
   /**
    *
    * @param userId
    * @param communityId
    * @param serverFileName: full file name
    * @param extentionPeriod: number of days you would like to extend this item
    * @return
    * @throws Exception
    */
   public String extendLease(String userId, String communityId, String credential, String serverFileName, int extentionPeriod) throws Exception;
   
   
   /**
    *
    * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userId/communityId/jobID/serverFileName
    * @return
    * @throws Exception
    */
   
   public String publish(String userId, String communityId, String credential, String mySpaceName) throws Exception;
   
   /**
    *
    * @param userId
    * @param communityId
    * @param newContainerName
    * @return
    */
   public String createContainer(String userId, String communityId, String credential, String newContainerName)throws Exception;
   
   /**
    * Create a new user on the current MSS.
    *
    * @param userId User identifier.
    * @param servers Vector of server names on which containers will be
    *   created for the user.
    *
    * @return boolean; true the user was created successfully.
    * @throws Exception
    */
   
   public boolean createUser(String userId, String communityId, String credential, Vector servers) throws Exception;
   
   /**
    * Delete a user from the current MSS.
    *
    * @param userId User identifier.
    *
    * @return boolean; true is the user was deleted successfully.
    * @throws Exception
    */
   
   public boolean deleteUser(String userId, String communityId, String credential) throws Exception;
   
   /**
    *
    * @param dataHolderName: file working on
    * @param newOwnerID: userId changing to
    * @return
    * @throws Exception
    */
   public String changeOwner(String userId, String communityId, String credential, String dataHolderName,String newOwnerID) throws Exception;
   
   
}

/*
 $Log: MySpaceClient.java,v $
 Revision 1.2  2003/12/08 20:33:40  mch
 Updated documentation

 Revision 1.1  2003/12/03 17:26:00  mch
 Added generalised myspaceFactory with MySpaceClient interface and Myspace-like FTP server delegate

 Revision 1.1  2003/12/02 18:03:53  mch
 Moved MySpaceDummyDelegate

 */

