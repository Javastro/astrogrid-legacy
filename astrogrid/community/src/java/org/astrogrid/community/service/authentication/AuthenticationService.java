/**
 * SimpleAuthenticationSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 * 
 * suggest actually doing the implementation by calling a simple authenticator object.
 */

package org.astrogrid.community.service.authentication;

public class AuthenticationService {
   
	public java.lang.String authenticateLogin(java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException {
    	return ("testtoken" + userName + password);
        //return auth.authenticateLogin(userName, password);
    }

    public boolean authenticateToken(java.lang.String token) throws java.rmi.RemoteException {
    	return true;
        //return auth.authenticateToken(token);
    }

}
