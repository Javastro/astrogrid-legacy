/**
 * AuthenticatonService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.delegate.authentication;

public interface AuthenticatonService extends javax.xml.rpc.Service {
    public java.lang.String getAuthenticationAddress();

    public org.astrogrid.community.delegate.authentication.Authenticaton getAuthentication() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.community.delegate.authentication.Authenticaton getAuthentication(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
