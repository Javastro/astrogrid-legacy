package org.astrogrid.tools.WebServiceDelegate;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.io.StringReader;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;

public class ResourcePolicyServiceDelegate extends RPCWebServiceDelegate {


	//Okay this needs to be the location of the webservice probably read from a config file.
	private static final String targetEndPoint = "http://msslxy.mssl.ucl.ac.uk:8080/axis/PolicyWebService.jws";
	
	//These will probably be the same ex: DTC,JES
	private String resourceType = null;
	
	//Name of the resource your querying
	private String resourceName = null;
	
	//Userid.
	private String userID = null;
	private static final String operationName = "authenticate";
	

	public ResourcePolicyServiceDelegate(String userID,String resourceName,String resourceType) {
		super();
		super.setTargetEndPoint(targetEndPoint);
		super.setOperationName(operationName);
		super.setReturnType(XMLType.XSD_STRING);
		super.addParameter("userID",XMLType.XSD_STRING, ParameterMode.IN);
		super.addParameter("resourceName",XMLType.XSD_STRING, ParameterMode.IN);
		this.setUserID(userID);
		this.setResourceName(resourceName);
		this.setResourceType(resourceType);
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
	
	public Policy authenticate() throws Exception {
		Object []param = new Object []{this.resourceName,this.resourceName};
		String response = (String)super.invoke(param);
		String val = null;
		Boolean blnAccess = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		System.out.println(response);
		Document document = builder.parse(new InputSource(new StringReader(response)));
		NodeList nl = document.getElementsByTagName("access");
		Node nd = nl.item(0);
		System.out.println(( val = getValue(nd) ) );
		Policy pol = new Policy();
		if(isValidString(val)) {
			pol.setAccess(new Boolean(val).booleanValue());
		}
		return pol;
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
		if(isValidString(this.resourceName) && isValidString(this.userID) && isValidString(this.resourceType)) {
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