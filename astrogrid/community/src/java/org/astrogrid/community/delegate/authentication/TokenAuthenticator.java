/**
 * TokenAuthenticator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.delegate.authentication;

public interface TokenAuthenticator extends java.rmi.Remote {
    public org.astrogrid.community.service.authentication.data.SecurityToken authenticateLogin(java.lang.String account, java.lang.String password) throws java.rmi.RemoteException;
    public org.astrogrid.community.service.authentication.data.SecurityToken authenticateToken(org.astrogrid.community.service.authentication.data.SecurityToken token) throws java.rmi.RemoteException;
    public org.astrogrid.community.service.authentication.data.SecurityToken createToken(java.lang.String account, org.astrogrid.community.service.authentication.data.SecurityToken token, java.lang.String target) throws java.rmi.RemoteException;
}
