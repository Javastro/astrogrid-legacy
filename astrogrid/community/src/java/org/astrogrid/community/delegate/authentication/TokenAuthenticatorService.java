/**
 * TokenAuthenticatorService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.delegate.authentication;

public interface TokenAuthenticatorService extends javax.xml.rpc.Service {
    public java.lang.String getAuthenticationServiceAddress();

    public org.astrogrid.community.delegate.authentication.TokenAuthenticator getAuthenticationService() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.community.delegate.authentication.TokenAuthenticator getAuthenticationService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
