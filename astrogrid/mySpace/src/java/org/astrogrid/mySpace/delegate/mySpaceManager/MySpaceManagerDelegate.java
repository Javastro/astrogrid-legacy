/**
 * This is the access point to MySpace
 * @usage: create an instance of MySpaceManagerDelegate by passing targetEndPoint then call the method you desire.
 * @author: CLQ
 */

package org.astrogrid.mySpace.delegate.mySpaceManager;

import java.util.Vector;
import org.astrogrid.mySpace.delegate.helper.*;

public class MySpaceManagerDelegate {
   
	private String targetEndPoint = null;
	private String value ="";
	public MySpaceManagerDelegate(String targetEndPoint) {
	  this.targetEndPoint = targetEndPoint;
	}    
	
	/**
	* lookupDataHoldings(modified lookupDataHolders(jobDetails) which returns a list of workflows.
	* @param: userid: userid
	* @param: communityid
	* @param: criteria: eg./userid/communityid/workflows/A*
	* @param: path: file path you want to download to.
	* @return: Vector of String of fileNames
	*/
	public Vector listDataHoldings(String userid, String communityid, String criteria)throws Exception {
		Vector vector = new Vector();
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}
		try{
			MySpaceHelper helper = new MySpaceHelper();
			String jobDetails = helper.buildListDataHoldings(userid, communityid, criteria);
			String response = binding.lookupDataHoldersDetails(jobDetails);
			vector = helper.getList(response);
			
		}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
		}
		return vector;
	}		
	
	/**
	 * 
	 * @param userid
	 * @param communityid
	 * @param serverFileName: full file name eg: /clq/serv1/File1.xml
	 * @return
	 * @throws Exception
	 */
	public String listDataHolding(String userid, String communityid, String serverFileName) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}
        
		try{
			MySpaceHelper helper = new MySpaceHelper();
			String jobDetails = helper.buildListDataHolding(userid, communityid, serverFileName);
			value = binding.lookupDataHolderDetails(jobDetails);
		}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
		}
		return (String)value;
	}	
	
	/**
	 * 
	 * @param userid
	 * @param communityid
	 * @param serverFileName: full file name copy from
	 * @param newDataItemName: full file name copy to
	 * @return
	 * @throws Exception
	 */
	public String copyDataHolding(String userid, String communityid, String serverFileName, String newDataItemName) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}
        try{
        	MySpaceHelper helper = new MySpaceHelper();
        	String jobDetails = helper.buildCopy(userid, communityid, serverFileName, newDataItemName);
		    value = binding.copyDataHolder(jobDetails);
        }catch(java.rmi.RemoteException re) {
		re.printStackTrace();
	}
		return (String)value;
	}	
	
	/**
	 * 
	 * @param userid
	 * @param communityid
	 * @param serverFileName: ole file full name
	 * @param newDataItemName: new file full name
	 * @return
	 * @throws Exception
	 */	
 
	public String renameDataHolding(String userid, String communityid, String serverFileName, String newDataItemName) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}

		try{
			MySpaceHelper helper = new MySpaceHelper();
			String jobDetails = helper.buildRename(userid, communityid, serverFileName, newDataItemName);
			value = binding.moveDataHolder(jobDetails);
		}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
		}
		return (String)value;
	}	
	
	/**
	 * 
	 * @param userid
	 * @param communityid
	 * @param serverFileName: Full file name which you want to delete
	 * @return
	 * @throws Exception
	 */
	public String deleteDataHolding(String userid, String communityid, String serverFileName) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}
        
		try{
			MySpaceHelper helper = new MySpaceHelper();
			String jobDetails = helper.buildDelete(userid, communityid, serverFileName);
			value = binding.deleteDataHolder(jobDetails);
		}catch(java.rmi.RemoteException re) {
				re.printStackTrace();
			}
		return (String)value;
	}	
	
	/** saveDataHolding(upLoad),this function will save workflow/query into MySpace system.
	* @param: userid: userid
	* @param: communityid
	* @param: fileName: unique file name for Workflow or Query you want to store.
	* @param: fileContent: content of workflow or data query
	* @param: category "WF" or "QUERY", if not set, default is "VOTable"
	* @param: action "Overwrite" or "Append", if not set, default is "Overwrite"
	* @return: boolean true if file successfully stored in MySapce false otherwise.
	*/
	
	public boolean saveDataHolding(String userid, String communityid, String fileName, String fileContent, 
	                               String category, String action) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		boolean isSaved = false;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
			          new org.astrogrid.mySpace.delegate.mySpaceManager.
			                 MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			isSaved = false;
			if(jre.getLinkedCause()!=null){
				jre.getLinkedCause().printStackTrace();
			}
		}
		try{
			MySpaceHelper helper = new MySpaceHelper();
			String jobDetails = helper.buildSave(userid, communityid, fileName, fileContent, category, action);
			binding.upLoad(jobDetails);
			isSaved = true;
		}catch(java.rmi.RemoteException re) {
			isSaved = false;
			re.printStackTrace();
		}
		return isSaved;
	}
	
	/**
	 * saveDataHoldingURL is different from saveDataHolding since it is taking a URL where MySpace will pull the file from. 
	 * @param userid
	 * @param communityid
	 * @param fileName
	 * @param importURI - url that save the dataholding from
	 * @param category
	 * @param action
	 * @return
	 * @throws Exception
	 */
	
	public boolean saveDataHoldingURL(String userid, String communityid, String fileName, String importURL, 
								   String category, String action) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		boolean isSaved = false;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
					  new org.astrogrid.mySpace.delegate.mySpaceManager.
							 MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			isSaved = false;
			if(jre.getLinkedCause()!=null){
				jre.getLinkedCause().printStackTrace();
			}
		}
		try{
			MySpaceHelper helper = new MySpaceHelper();
			String jobDetails = helper.buildSaveURL(userid, communityid, fileName, importURL, category, action);
			binding.upLoadURL(jobDetails);
			isSaved = true;
		}catch(java.rmi.RemoteException re) {
			isSaved = false;
			re.printStackTrace();
		}
		return isSaved;
	}
	
	/** This is read/download
	* getDataHolding(modified download which returns file content instead of boolean),this function will download workflow/query from MySpace system.
	* @param: userid: userid
    * @param: communityid: communityid
	* @param: fullFileName: full file name in mySpace for Workflow or Query you want be downloaded.
	* @return: file content in String format
	*/
	public String getDataHolding(String userid, String communityid, String fullFileName)throws Exception{
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().
						      getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}

		try{
			MySpaceHelper helper = new MySpaceHelper();
			String jobDetails = helper.buildDownload(userid, communityid, fullFileName);
			String URI = binding.exportDataHolder(jobDetails); //URI will be the URI where the file is stored eg: http://localhost:8080/mySpace/f34
			//value = invoke FileTransfer class to return the content of the file from this URI
		}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
		}
		return (String)value;
	}		
	
	/**
	 * 
	 * @param userid
	 * @param communityid
	 * @param serverFileName: full file name 
	 * @param extentionPeriod: number of days you would like to extend this item
	 * @return
	 * @throws Exception
	 */
	public String extendLease(String userid, String communityid, String serverFileName, int extentionPeriod) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}

        try{
        	MySpaceHelper helper = new MySpaceHelper();
        	String jobDetails = helper.buildExtendlease(userid, communityid, serverFileName, extentionPeriod);
            value = binding.extendLease(jobDetails);
	    }catch(java.rmi.RemoteException re) {
				re.printStackTrace();
	    }        
		return (String)value;
	}		

	
	
	/**
	 * 
	 * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userID/communityID/jobID/serverFileName
	 * @return
	 * @throws Exception
	 */
	
	public String publish(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
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
	
	/**
	 * 
	 * @param userid
	 * @param communityid
	 * @param newContainerName
	 * @return
	 */
	public String createContainer(String userid, String communityid, String newContainerName)throws Exception {
	//public String createFolder(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}
		try{
			MySpaceHelper helper = new MySpaceHelper();
			String jobDetails = helper.buildContainer(userid, communityid, newContainerName);
			value = binding.createContainer(jobDetails);
		}catch(java.rmi.RemoteException re) {
				re.printStackTrace();
			}
		return (String)value;
	}			
	
	/**
	 * 
	 * @param userID: userid@communityid
	 * @param servers: server names user wants to create
	 * @return
	 * @throws Exception
	 */
	public String createUser(String userID, Vector servers) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}
		try{
			String userid = userID.substring(0,userID.indexOf("@"));
			String communityid = userID.substring(userID.indexOf("@")+1, userID.length());
			value = binding.createUser(userid, communityid, servers);
		}catch(java.rmi.RemoteException re) {
				re.printStackTrace();
			}
		return (String)value;
	}	
	
	/**
	 * 
	 * @param userID: userid@communityid
	 * @param servers: server names user wants to delete
	 * @return
	 * @throws Exception
	 */
	//need to delete the second argument and rebuild the delegate supporting classes.
	
	public String deleteUser(String userID) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}
        
		try{
			String userid = userID.substring(0,userID.indexOf("@"));
			String communityid = userID.substring(userID.indexOf("@")+1, userID.length());
			value = binding.deleteUser(userid, communityid);
		}catch(java.rmi.RemoteException re) {
				re.printStackTrace();
			}
		return (String)value;
	}		
	
	/**
	 * 
	 * @param oldID: userID changing from
	 * @param newID: userID changing to
	 * @return
	 * @throws Exception
	 */
	public String changeOwner(String userid, String communityid, String dataHolderName,String newOwnerID) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
		}
		try{
			//FixME to MSManager to match these set of argument!		
			value = binding.changeOwner(userid, communityid, dataHolderName, newOwnerID);
			//value = binding.changeOwner(userid,communityid, dataHolderName, newOwnerID);
		}catch(java.rmi.RemoteException re) {
		re.printStackTrace();
	}
		return (String)value;
	}		
		
}
