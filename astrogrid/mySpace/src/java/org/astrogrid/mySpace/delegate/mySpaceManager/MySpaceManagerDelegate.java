/**
 * This is the access point to MySpace
 * @usage: create an instance of MySpaceManagerDelegate by passing targetEndPoint then call the method you desire.
 * @author: CLQ
 */

package org.astrogrid.mySpace.delegate.mySpaceManager;

import java.io.*;
import java.net.*;
import java.util.Vector;
import org.astrogrid.mySpace.delegate.helper.*;

//
// Note: Throughout this class the acronym `MSS' denotes a MySpace System.

public class MySpaceManagerDelegate {

    private String mssUrl = " ";       // MSS the delegate is operating on.
    private Vector queryMssUrl = new Vector(); // Vector of MSSs to query.
    private boolean DEBUG = true;
   
    private String value ="";

//
// Constructors.

/**
 * Constructor with no arguments.  Both the MSS to operate on and the
 * vector of MSSs to query are set to null.  These items need to be
 * specified using the appropriate set methods before the delegate can
 * invoke a MySpace system.
 */

    public MySpaceManagerDelegate() {
        this.mssUrl = mssUrl;
        this.queryMssUrl = queryMssUrl;
    }

/**
 * Constructor with a single argument.  This argument is the URL of the
 * MSS which the delegate is to invoke.  The vector of MSSs to query is
 * set to a single element vector contining this same MSS.
 */

    public MySpaceManagerDelegate(String mssUrl) {
        this.mssUrl = mssUrl;
        System.out.println("initializor: mssUrl: "+mssUrl);
        (this.queryMssUrl).add(mssUrl);
    }    
    
/**
 * Constructor with two arguments.  The first argument is the URL of the
 * MSS which the delegate is to invoke.  The second is a Vector
 * containing the URLs of all the MSSs which are to be queried.
 */

    public MySpaceManagerDelegate(String mssUrl, Vector queryMssUrl) {
        this.mssUrl = mssUrl;
        this.queryMssUrl = queryMssUrl;
    }

//
// Get and set methods.
//
// These method are provided to get and set the URL of the MSS to invoke
// and the Vector of MSSs to query.

/**
 * Get the URL of the current MSS.
 */

    public String getMssUrl() {
        return this.mssUrl;
    }

/**
 * Set the URL of the MSS to invoke.
 */

    public void   setMssUrl(String mssUrl) {
        this.mssUrl = mssUrl;
    }

/**
 * Get a Vector containing the URLs of the MSSs which are currently
 * queried.
 */

    public Vector getQueryMssUrl() {
       return queryMssUrl;
    }

/**
 * Specify the list of MSSs to be queried.  A Vector containing the
 * URLs of the required MSSs is supplied.
 *
 * <p>
 * There is a special syntax to indicate that all the MSS known to
 * the AstroGrid system are to be queried.  This option is invoked
 * by supplying a single element Vector in which the first element is
 * a single asterisk.
 */

    public void setQueryMssUrl(Vector queryMssUrl) throws Exception{
        if (queryMssUrl.size() == 1) {
            if ( ((String)queryMssUrl.elementAt(0)).equals("*") ) {
                this.queryMssUrl = this.getAllMssUrl();
            }
            else {
                this.queryMssUrl = queryMssUrl;
            } 
        }
        else {
            this.queryMssUrl = queryMssUrl;
        } 
    }

/**
 * Get a Vector containing the URLs of all the MSSs known to AstroGrid.
 */

    public Vector getAllMssUrl() throws Exception {
        Vector allMssUrls = null;

        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;

        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
              new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator()
                .getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
        
        try{
        	System.out.println("before call getServerURLs");
           allMssUrls  = binding.getServerURLs();
           
		   System.out.println("after call getServerURLs  "+allMssUrls.size());
		   for (int i=0;i<allMssUrls.size(); i++)
		   {
		   	System.out.println("after... "+allMssUrls.elementAt(i));
		   }
        }catch(java.rmi.RemoteException re) {
           re.printStackTrace();
        }

        return allMssUrls;
    }    

//
// --------------------------------------------------------------------------

//
// Action methods.
//
// The following methods invoke an MSS to perform some action.  All
// the methods invoke a single MSS apart from
// <code>listDataHoldings</code> and <code>listDataHoldingsGen</code>
// which query a group of them.

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
 
    public Vector listDataHoldings(String userId, String communityId, String credential,
      String query)throws Exception {
        Vector returnList = new Vector();
        if (DEBUG) System.out.println("listDataholdings..."+userId +":   :"+communityId+": query:"+query);
        try {
            for (int loop = 0; loop<queryMssUrl.size(); loop++) {
                String currentResponse = this.internalDataHoldings(
                  userId, communityId, credential, query,
                  (String)queryMssUrl.elementAt(loop) );
                MySpaceHelper helper = new MySpaceHelper();
				if (DEBUG) System.out.println("currentResponse from internalDataHoldings: "+currentResponse);
                Vector currentList  = helper.getList(currentResponse, "dataItemName");
				if (DEBUG){ 
					System.out.println("size: "+currentList.size()); 
					for (int j=0;j<currentList.size();j++){
						System.out.println(" currentlist: "+currentList.elementAt(j));
					}
				}
                returnList.add(currentList);
                if (DEBUG){
                	for (int i=0;i<returnList.size();i++){
                		System.out.println("returnList from delegate: "+returnList.elementAt(i));
                	}
                }
            }
           
        }catch(java.rmi.RemoteException re) {
            re.printStackTrace();
        }

        return returnList;
    }        

//
// --------------------------------------------------------------------------

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

    public Vector listDataHoldingsGen(String userId, String communityId, String credential,
      String query)throws Exception {
      	System.out.println("xxxxxxxxxxxxxxxxx");
        Vector returnList = new Vector();
        this.setQueryMssUrl(this.getAllMssUrl());
        //query = "/" +userId+"@"+communityId+"/"+"*";
        try {
            for (int loop = 0; loop<queryMssUrl.size(); loop++) {
            	
				System.out.println("before call internalDataHoldings "+ userId +"  :"+communityId +"  query: "+query);
                String currentResponse = this.internalDataHoldings(
                  userId, communityId, credential, query,
                  (String)queryMssUrl.elementAt(loop) );
                  System.out.println("yyyyyyyy"+(String)queryMssUrl.elementAt(loop) );

                returnList.add(currentResponse);
            }
           
        }catch(java.rmi.RemoteException re) {
            re.printStackTrace();
        }

        return returnList;
    }    
    
    public String lookupDataHoldersDetails(String jobDetails){
    	
    	return "";    
    }

//
// --------------------------------------------------------------------------

    /**
     * 
     * @param userId
     * @param communityId
     * @param serverFileName: full file name eg: /clq/serv1/File1.xml
     * @return
     * @throws Exception
     */
    public String listDataHolding(String userId, String communityId, String credential, String serverFileName) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
        
        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildListDataHolding(userId, communityId, credential, serverFileName);
            value = binding.lookupDataHolderDetails(jobDetails);
        }catch(java.rmi.RemoteException re) {
                    re.printStackTrace();
        }
        return (String)value;
    }    
   
//
// --------------------------------------------------------------------------
 
    /**
     * @param userId
     * @param communityId
     * @param serverFileName: full file name copy from
     * @param newDataItemName: full file name copy to
     * @return
     * @throws Exception
     */
    public String copyDataHolding(String userId, String communityId, String credential, String serverFileName, String newDataItemName) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildCopy(userId, communityId, credential, serverFileName, newDataItemName);
            value = binding.copyDataHolder(jobDetails);
        }catch(java.rmi.RemoteException re) {
        re.printStackTrace();
    }
        return (String)value;
    }    
    
//
// --------------------------------------------------------------------------

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

    public boolean copyRemoteDataHolding(String userId, String communityId, String credential,
      String remoteMssUrl, String remoteMySpaceName,
      String newMySpaceName) throws Exception {
        boolean isSaved = false;
        org.astrogrid.mySpace.delegate.mySpaceManager.
          MySpaceManagerSoapBindingStub binding = null;

//
//     Set up for accessing the remote MSS.

        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.
              MySpaceManagerSoapBindingStub)
              new org.astrogrid.mySpace.delegate.mySpaceManager.
                MySpaceManagerServiceLocator(). getMySpaceManager(
                  new java.net.URL(remoteMssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }

//
//     Obtain the URL to access the remote dataHolder.

        String remoteURL = null;
        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails =
              helper.buildDownload(userId, communityId, credential, remoteMySpaceName);
            remoteURL = binding.exportDataHolder(jobDetails);
        }catch(java.rmi.RemoteException re) {
            re.printStackTrace();
            isSaved = false;
        }

//
//     Proceed if ok.

        if (isSaved) {

//
//         Set up for accessing the current MSS.
    
            try {
                binding = (org.astrogrid.mySpace.delegate.mySpaceManager.
                  MySpaceManagerSoapBindingStub)
                  new org.astrogrid.mySpace.delegate.mySpaceManager.
                     MySpaceManagerServiceLocator().getMySpaceManager(
                       new java.net.URL(mssUrl));
            }
            catch (javax.xml.rpc.ServiceException jre) {
                isSaved = false;
                if(jre.getLinkedCause()!=null){
                    jre.getLinkedCause().printStackTrace();
                }
            }

//
//         Import the dataHolder into the current MSS.

            try{
                MySpaceHelper helper = new MySpaceHelper();
                String jobDetails = helper.buildSaveURL(userId,
                  communityId, credential, newMySpaceName, remoteURL, " ", " ");
                binding.upLoadURL(jobDetails);
                isSaved = true;
            }catch(java.rmi.RemoteException re) {
                isSaved = false;
                re.printStackTrace();
            }
        }
        return isSaved;
    }

//
// --------------------------------------------------------------------------

    /**
     * 
     * @param userId
     * @param communityId
     * @param serverFileName: ole file full name
     * @param newDataItemName: new file full name
     * @return
     * @throws Exception
     */    
 
    public String renameDataHolding(String userId, String communityId, String credential, String serverFileName, String newDataItemName) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }

        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildRename(userId, communityId, credential, serverFileName, newDataItemName);
            value = binding.moveDataHolder(jobDetails);
        }catch(java.rmi.RemoteException re) {
                    re.printStackTrace();
        }
        return (String)value;
    }    
    
//
// --------------------------------------------------------------------------

    /**
     * 
     * @param userId
     * @param communityId
     * @param serverFileName: Full file name which you want to delete
     * @return
     * @throws Exception
     */
    public String deleteDataHolding(String userId, String communityId, String credential, String serverFileName) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
        
        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildDelete(userId, communityId, credential, serverFileName);
            value = binding.deleteDataHolder(jobDetails);
        }catch(java.rmi.RemoteException re) {
                re.printStackTrace();
            }
        return (String)value;
    }    
    
//
// --------------------------------------------------------------------------

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
                                   String category, String action) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        boolean isSaved = false;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                      new org.astrogrid.mySpace.delegate.mySpaceManager.
                             MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            isSaved = false;
            if(jre.getLinkedCause()!=null){
                jre.getLinkedCause().printStackTrace();
            }
        }
        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildSave(userId, communityId, credential, fileName, fileContent, category, action);
            binding.upLoad(jobDetails);
            isSaved = true;
        }catch(java.rmi.RemoteException re) {
            isSaved = false;
            re.printStackTrace();
        }
        return isSaved;
    }
    
//
// --------------------------------------------------------------------------

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
                                   String category, String action) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        boolean isSaved = false;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                      new org.astrogrid.mySpace.delegate.mySpaceManager.
                             MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            isSaved = false;
            if(jre.getLinkedCause()!=null){
                jre.getLinkedCause().printStackTrace();
            }
        }
        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildSaveURL(userId, communityId, credential, fileName, importURL, category, action);
            System.out.println("filename: "+fileName+ "importURL: "+importURL);
            binding.upLoadURL(jobDetails);
            isSaved = true;
        }catch(java.rmi.RemoteException re) {
            isSaved = false;
            re.printStackTrace();
        }
        return isSaved;
    }
 
//
// --------------------------------------------------------------------------

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
    public String getDataHolding(String userId, String communityId, String credential,
      String mySpaceName) throws Exception {
        String contents = null;
        org.astrogrid.mySpace.delegate.mySpaceManager.
          MySpaceManagerSoapBindingStub binding = null;

//
//     Set up for accessing the current MSS.

        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.
              MySpaceManagerSoapBindingStub)
              new org.astrogrid.mySpace.delegate.mySpaceManager.
                MySpaceManagerServiceLocator().
                  getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }

//
//     Obtain the URL of the required dataHolder.

        boolean isOk = false;
        String responsXML = " ";
        String dataHolderUrl = " ";

        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildDownload(userId,
              communityId, credential, mySpaceName);
			responsXML = binding.exportDataHolder(jobDetails);
			System.out.println("2222jobDetails : "+responsXML);
			Vector dataHolderUrlReturn  = helper.getList(responsXML, "dataHolderURI");
			for (int i=0;i<dataHolderUrlReturn.size();i++){
				//System.out.println("$*********size: "+dataHolderUrlReturn.size());
				dataHolderUrl = (String)dataHolderUrlReturn.elementAt(i);
				//System.out.println("$*********elementat i:: "+dataHolderUrl);
			}
			//System.out.println("$*********** dataHolderUrl:0000000"+dataHolderUrl);
            if (dataHolderUrl != null) {
               isOk = true;
            }
        }
        catch(java.rmi.RemoteException re) {
            re.printStackTrace();
        }

//
//     If the URL was obtained ok then attempt to retrieve the contents.

        if (isOk) {
            try {
                URL url = new URL(dataHolderUrl);
                //System.out.println("$*********** url: "+url);
                InputStream iStream = url.openStream();

                int b;
                StringBuffer buffer = new StringBuffer("");

                while( (b = iStream.read())  !=  -1) {
                    buffer.append(b);
                }

                contents = buffer.toString();

            }
            catch (Exception re) {
                re.printStackTrace();
            }

        }
         // System.out.println("  YYYYYYYYYYYY content: "+contents);
        return contents;
    }        
    
//
// --------------------------------------------------------------------------

    /**
     * 
     * @param userId
     * @param communityId
     * @param serverFileName: full file name 
     * @param extentionPeriod: number of days you would like to extend this item
     * @return
     * @throws Exception
     */
    public String extendLease(String userId, String communityId, String credential, String serverFileName, int extentionPeriod) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }

        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildExtendlease(userId, communityId, credential, serverFileName, extentionPeriod);
            value = binding.extendLease(jobDetails);
        }catch(java.rmi.RemoteException re) {
                re.printStackTrace();
        }        
        return (String)value;
    }        

    
    
    /**
     * 
     * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userId/communityId/jobID/serverFileName
     * @return
     * @throws Exception
     */
    
    public String publish(String jobDetails) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }

        try{
            value = binding.publish(jobDetails);
        }catch(java.rmi.RemoteException re) {
                    re.printStackTrace();
        }
        return (String)value;
    }        
    
//
// --------------------------------------------------------------------------

    /**
     * 
     * @param userId
     * @param communityId
     * @param newContainerName
     * @return
     */
    public String createContainer(String userId, String communityId, String credential, String newContainerName)throws Exception {
    //public String createFolder(String jobDetails) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildContainer(userId, communityId, credential, newContainerName);
            value = binding.createContainer(jobDetails);
        }catch(java.rmi.RemoteException re) {
                re.printStackTrace();
            }
        return (String)value;
    }            
    
//
// --------------------------------------------------------------------------

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

    public boolean createUser(String userId, String communityId, String credential, Vector servers) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        boolean isUserCreated = false;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
              new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().
                getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
        try{
            isUserCreated = binding.createUser(userId, communityId, servers);
        }catch(java.rmi.RemoteException re) {
                re.printStackTrace();
            }
        return isUserCreated;
    }    
    
//
// --------------------------------------------------------------------------

/**
 * Delete a user from the current MSS.
 *
 * @param userId User identifier.
 *
 * @return boolean; true is the user was deleted successfully.
 * @throws Exception
 */
    
    public boolean deleteUser(String userId, String communityId, String credential) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        boolean isUserDeleted = false;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
              new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().
                getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
        
        try{
            isUserDeleted = binding.deleteUser(userId, communityId);
        }catch(java.rmi.RemoteException re) {
                re.printStackTrace();
            }
        return isUserDeleted;
    }        
    
//
// --------------------------------------------------------------------------

    /**
     * 
     * @param dataHolderName: file working on
     * @param newOwnerID: userId changing to
     * @return
     * @throws Exception
     */
    public String changeOwner(String userId, String communityId, String credential, String dataHolderName,String newOwnerID) throws Exception {
        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
                          new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(mssUrl));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
        try{
            //FixME to MSManager to match these set of argument!        
            value = binding.changeOwner(userId, communityId, dataHolderName, newOwnerID);
            //value = binding.changeOwner(userId,communityId, dataHolderName, newOwnerID);
        }catch(java.rmi.RemoteException re) {
        re.printStackTrace();
    }
        return (String)value;
    }        

//
// ==========================================================================

    private String internalDataHoldings(String userId,
      String communityId, String credential, String criteria, String currentURL)
      throws Exception {

        String response = null;

        org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
        try {
            binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
              new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator()
                .getMySpaceManager(new java.net.URL(currentURL));
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
        try{
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildListDataHoldings(userId, communityId, credential, criteria);
            response = binding.lookupDataHoldersDetails(jobDetails);
            
        }   
        catch(java.rmi.RemoteException re) {
            re.printStackTrace();
        }
        return response;
    }        
}
