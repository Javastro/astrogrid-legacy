package org.astrogrid.registry.delegate; 

import java.net.URL ;
import java.net.MalformedURLException ;
import java.rmi.RemoteException;

public class RegistryAdminDelegate { 
	private String targetEndPoint = null; 
	public RegistryAdminDelegate(String targetEndPoint) {
		 this.targetEndPoint = targetEndPoint;
    }
	public String adminQuery(String adminQuery) throws MalformedURLException, RemoteException {
	  	 org.astrogrid.registry.stubs.RegistryAdminServiceSoapBindingStub binding;
         String value;
	  	 try { binding = (org.astrogrid.registry.stubs.RegistryAdminServiceSoapBindingStub)
	  	 	 new org.astrogrid.registry.stubs.RegistryAdminServiceServiceLocator().getRegistryAdminService(new URL(targetEndPoint));
			 value = (String)binding.adminQuery(adminQuery);
		 }
		 catch( MalformedURLException mex ) {
		 	 value = mex.toString();
		 }
		catch( RemoteException rex ) {
			value = rex.toString();
		}
	  	 catch (javax.xml.rpc.ServiceException jre) {
	  	 	 value = jre.toString();
	  	 	 if(jre.getLinkedCause()!=null) jre.getLinkedCause().printStackTrace();
	  	 	  //do something } // Time out after a minute binding.setTimeout(60000);
	     }

	  	 return value; 
	  	 	   //After returning the response you may have validate or 
	  	 	   //some other helper methods in this delegate to help you.
    } 	    
} 
