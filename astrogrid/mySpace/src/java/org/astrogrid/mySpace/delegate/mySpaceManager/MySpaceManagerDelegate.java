/**
 * This is the access point to MySpace
 * @usage: create an instance of MySpaceManagerDelegate by passing targetEndPoint then call the method you desire.
 * @author: CLQ
 */

package org.astrogrid.mySpace.delegate.mySpaceManager;

import java.util.Vector;

public class MySpaceManagerDelegate {
   
	private String targetEndPoint = null;
	private String value ="";
	public MySpaceManagerDelegate(String targetEndPoint) {
	  this.targetEndPoint = targetEndPoint;
	}
    
	/**
	 * 
	 * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userID/communityID
	 * @return
	 * @throws Exception
	 */
	public String authorise(String jobDetails) throws Exception {
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
        	value = binding.authorise(jobDetails);    
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
	public String changeOwner(String oldID,String newID) throws Exception {
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
        	value = binding.changeOwner(oldID,newID);
        }catch(java.rmi.RemoteException re) {
		re.printStackTrace();
	}
		return (String)value;
	}	
	
	/**
	 * 
	 * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userID/communityID/jobID/newDataItemName/serverFileName
	 * @return
	 * @throws Exception
	 */
	public String copyDataHolder(String jobDetails) throws Exception {
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
		    value = binding.copyDataHolder(jobDetails);
        }catch(java.rmi.RemoteException re) {
		re.printStackTrace();
	}
		return (String)value;
	}	
	
	/**
	 * 
	 * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userID/communityID/jobID/newContainerName
	 * @return
	 * @throws Exception
	 */
	public String createContainer(String jobDetails) throws Exception {
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
		    value = binding.createContainer(jobDetails);
		}catch(java.rmi.RemoteException re) {
				re.printStackTrace();
			}
		return (String)value;
	}		
	
	/**
	 * 
	 * @param userID: userid@communityid
	 * @param subfolders: subfolders names user wants to create
	 * @return
	 * @throws Exception
	 */
	public String createUser(String userID, Vector subfolders) throws Exception {
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
		    value = binding.createUser(userID, subfolders);
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
	public String deleteDataHolder(String jobDetails) throws Exception {
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
            value = binding.deleteDataHolder(jobDetails);
		}catch(java.rmi.RemoteException re) {
				re.printStackTrace();
			}
		return (String)value;
	}	
	
	/**
	 * 
	 * @param userID: userid@communityid
	 * @param subfolders: subfolders names user wants to delete
	 * @return
	 * @throws Exception
	 */
	
	public String deleteUser(String userID, Vector subfolders) throws Exception {
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
		    value = binding.deleteUser(userID, subfolders);
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
	public String exportDataHolder(String jobDetails) throws Exception {
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
		    value = binding.exportDataHolder(jobDetails);
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
	public String extendLease(String jobDetails) throws Exception {
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
	public String importDataHolder(String jobDetails) throws Exception {
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
		    value = binding.importDataHolder(jobDetails);
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
	public String lookupDataHolderDetailse(String jobDetails) throws Exception {
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
		    value = binding.lookupDataHolderDetails(jobDetails);
		}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
		}
		return (String)value;
	}
	
	/**
	 * 
	 * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userID/communityID/jobID/query
	 * @return
	 * @throws Exception
	 */	
	
	public String lookupDataHoldersDetailse(String jobDetails) throws Exception {
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
		    value = binding.lookupDataHoldersDetails(jobDetails);
		}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
		}
		return (String)value;
	}	
	
	/**
	 * 
	 * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userID/communityID/jobID/newDataItemName/serverFileName
	 * @return
	 * @throws Exception
	 */
	public String moveDataHolder(String jobDetails) throws Exception {
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
		    value = binding.moveDataHolder(jobDetails);
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
		
	public String structureMySpace(String jobDetails) throws Exception {
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
		    value = binding.structureMySpace(jobDetails);
		}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
		}
		return (String)value;
	}		
	
	/**
	 * 
	 * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userID/communityID/jobID/serverFileName/newDataHolderName/fileSize
	 * @return
	 * @throws Exception
	 */
	public String upLoad(String jobDetails) throws Exception {
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
		    value = binding.upLoad(jobDetails);
		}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
		}
		return (String)value;
	}
		
}
