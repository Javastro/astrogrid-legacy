/**
 * AuthenticationSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 * 
 * suggest actually doing the implementation by calling a simple authenticator object.
 */

package org.astrogrid.community.delegate.authentication;

public class AuthenticationDelegate implements org.astrogrid.community.delegate.authentication.Authenticaton{
   
    public java.lang.String authenticateLogin(java.lang.String userName, java.lang.String password){
		AuthenticationSoapBindingStub binding;
				try {
					binding = (AuthenticationSoapBindingStub) new AuthenticatonServiceLocator().getAuthentication();
				}catch (javax.xml.rpc.ServiceException jre) {
				if(jre.getLinkedCause()!=null)
					jre.getLinkedCause().printStackTrace();
					throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
				}
				// Time out after a minute
				binding.setTimeout(60000);

				// Test operation
				String value = null;
				try {
					value = binding.authenticateLogin(userName,password);
				}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
				}
				System.out.println("the value = " + value);
		return value;
        //return auth.authenticateLogin(userName, password);
    }

    public boolean authenticateToken(java.lang.String token) {
		AuthenticationSoapBindingStub binding;
				try {
					binding = (AuthenticationSoapBindingStub) new AuthenticatonServiceLocator().getAuthentication();
				}catch (javax.xml.rpc.ServiceException jre) {
					if(jre.getLinkedCause()!=null)
						jre.getLinkedCause().printStackTrace();
					throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
				}
				// Time out after a minute
				binding.setTimeout(60000);

				// Test operation
				boolean value = false;
				try {
					value = binding.authenticateToken(token);
				}catch(java.rmi.RemoteException re) {
					re.printStackTrace();
				}
				System.out.println("the value = " + value);
		return value;
        //return auth.authenticateToken(token);
    }
    
    public static void main(String []argv) {
			AuthenticationDelegate ad = new AuthenticationDelegate();
			ad.authenticateLogin("testuser","testpass");
			ad.authenticateToken("testtoken");	
    }

}
	