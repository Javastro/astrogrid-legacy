package org.astrogrid.tools.WebServiceDelegate;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;
import java.net.URL;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.enum.Style;
import org.apache.axis.enum.Use;


public abstract class WebServiceDelegate {
	
	private Call call = null;
	private Service service = null;
	
	public WebServiceDelegate() {
		try {
			service = new Service();
			call = (Call)service.createCall();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void setOperationName(String operationName) {
		setOperationName("",operationName);
	}

	protected void setOperationName(String nameSpace,String operationName) {
		setOperationName(new QName(nameSpace,operationName));		
	}
	
	protected void setOperationName(QName operationName) {
		this.call.setOperationName(operationName);	
	}
	
	protected void setTargetEndPoint(URL targetAddress) {
		this.call.setTargetEndpointAddress(targetAddress);
	}
	
	protected void setTargetEndPoint(String targetAddress) {
		this.call.setTargetEndpointAddress(targetAddress);
	}

	protected void setReturnType(QName returnType) {
		this.call.setReturnType(returnType);
	}
	
	protected void addParameter(String paramName,
	QName xmlType,
	Class javaType,
	ParameterMode parameterMode) {
		this.call.addParameter(paramName,xmlType,javaType,parameterMode);
	}
	
	protected void setOperationStyle(Style opStyle) {
		this.call.setOperationStyle(opStyle);
	}
	
	protected void setOperationUse(Use opUse) {
		this.call.setOperationUse(opUse);
	}

	protected Object invoke(Object []param) throws Exception {
		if(isValid()) {
			return this.call.invoke(param);
		}else {
			throw new Exception("Your call to the webservice is not valid");
		}
	}
	
	private boolean isValid() {
		if(this.call.getOperationName() == null || this.call.getTargetEndpointAddress() == null ||
		   this.call.getTargetEndpointAddress().length() == 0) {
		     return false;
		   }
		return true;
	}	
}