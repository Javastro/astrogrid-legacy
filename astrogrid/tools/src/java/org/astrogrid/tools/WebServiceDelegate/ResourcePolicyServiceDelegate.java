package org.astrogrid.tools.WebServiceDelegate;

import javax.xml.parsers.*;
import org.w3c.dom.*;

public class ResourcePolicyServiceDelegate extends DocWebServiceDelegate {


	//Okay this needs to be the location of the webservice probably read from a config file.
	private static final String targetEndPoint = null;
	
	//These will probably be the same ex: DTC,JES
	private String subSystemCode = null;
	private String resourceType = null;
	
	//Name of the resource your querying
	private String resourceName = null;
	
	//Userid.
	private String userID = null;
	
	private boolean canAccess = false;
	private boolean canRead = false;
	private boolean canWrite = false;
	private boolean canExecute = false;

	public ResourcePolicyServiceDelegate() {
		super();
		super.setTargetEndPoint(targetEndPoint);
	}
	
	public ResourcePolicyServiceDelegate(String userID,String resourceName,String subSystemCode,String resourceType) {
		super();
		this.setUserID(userID);
		this.setResourceName(resourceName);
		this.setSubSystemCode(subSystemCode);
		this.setResourceType(resourceType);		
	}
	
	public boolean canRead() {
		return this.canRead;
	}
	
	public boolean canWrite() {
		return this.canWrite
	}
	
	public boolean canExecute() {
		return this.canExecute;
	}
	
	public boolean canAccess() {
		return this.canAccess;
	}
	
	public void setSubSystemCode(String subSystemCode) {
		this.subSystemCode = subSystemCode;
	}
	
	public String getSubSystemCode() {
		return this.subSystemCode;
	}
	
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	public String getResourceType() {
		return this.resourceType;	
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getUserID() {
		return this.userID;
	}
	
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public String getResourceName() {
		return this.resourceName;
	}
	
	public Object invoke(Object []param) throws Exception {
		String response = (String)super.invoke(param);
		String val = null;
		Boolean blnAccess = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(response);
		NodeList nl = document.getElementsByTagName("access");
		Node nd = nl.item(0);
		System.out.println(( val = getValue(nd) ) );
		if(isValidString(val)) {
			this.canAccess = (blnAccess = new Boolean(val)).booleanValue();
		}
		return blnAccess;
	}
	
	private String getValue(Node nd) {
		Node nval = null;
		if(nd.hasAttributes()) {
			NamedNodeMap nnm = nd.getAttributes();
			nval = nnm.getNamedItem("value");
			return nval.getNodeValue();
		}
		return null;
	}	
	
	public boolean isValid() {
		if(isValidString(this.resourceName) && isValidString(this.userID) && isValidString(this.resourceType) &&
		   isValidString(this.subSystemCode)) {
			return super.isValid();   	
		}
		return false;
	}
	
	public boolean isValidString(String str) {
		if(str != null && str.length() > 0) {
			return true;
		}
		return false;		
	}
	
}