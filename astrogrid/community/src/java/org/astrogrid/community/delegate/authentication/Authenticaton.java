/**
 * SimpleAuthenticaton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.delegate.authentication;

public interface Authenticaton extends java.rmi.Remote {
    public java.lang.String authenticateLogin(java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException;
    public boolean authenticateToken(java.lang.String token) throws java.rmi.RemoteException;
}
