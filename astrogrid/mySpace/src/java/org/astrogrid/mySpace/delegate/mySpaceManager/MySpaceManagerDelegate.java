package org.astrogrid.mySpace.delegate.mySpaceManager;

import java.util.Vector;

public class MySpaceManagerDelegate {
   
	private String targetEndPoint = null;
	public MySpaceManagerDelegate(String targetEndPoint) {
	  this.targetEndPoint = targetEndPoint;
	}
    
	
	public String authorise(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.authorise(jobDetails);
		return (String)value;
	}	
	
	public String changeOwner(String oldID,String newID) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.changeOwner(oldID,newID);
		return (String)value;
	}	
	
	public String copyDataHolder(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.copyDataHolder(jobDetails);
		return (String)value;
	}	
	
	public String createContainer(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.createContainer(jobDetails);
		return (String)value;
	}		
	
	public String createUser(String userID, Vector subfolders) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.createUser(userID, subfolders);
		return (String)value;
	}		
	
	public String deleteDataHolder(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.deleteDataHolder(jobDetails);
		return (String)value;
	}	
	
	public String deleteUser(String userID, Vector subfolders) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.deleteUser(userID, subfolders);
		return (String)value;
	}	
	
	public String exportDataHolder(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.exportDataHolder(jobDetails);
		return (String)value;
	}		
	
	public String extendLease(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.extendLease(jobDetails);
		return (String)value;
	}		
	
	public String importDataHolder(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.importDataHolder(jobDetails);
		return (String)value;
	}		
	
	public String lookupDataHolderDetailse(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.lookupDataHolderDetails(jobDetails);
		return (String)value;
	}	
		
	public String lookupDataHoldersDetailse(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.lookupDataHoldersDetails(jobDetails);
		return (String)value;
	}	
	
	public String moveDataHolder(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.moveDataHolder(jobDetails);
		return (String)value;
	}	
	
	public String publish(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.publish(jobDetails);
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
				//do something
		}


		String value = binding.structureMySpace(jobDetails);
		return (String)value;
	}		
	
	public String upLoad(String jobDetails) throws Exception {
		org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub binding = null;
		try {
			binding = (org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub)
						  new org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator().getMySpaceManager(new java.net.URL(targetEndPoint));
		}
		catch (javax.xml.rpc.ServiceException jre) {
			if(jre.getLinkedCause()!=null)
				jre.getLinkedCause().printStackTrace();
				//do something
		}


		String value = binding.upLoad(jobDetails);
		return (String)value;
	}
		
}
