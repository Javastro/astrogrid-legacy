/**
 * AuthorizationService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.delegate.authorization;

public interface AuthorizationService extends javax.xml.rpc.Service {
    public java.lang.String getAuthorizationAddress();

    public org.astrogrid.community.delegate.authorization.Authorization getAuthorization() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.community.delegate.authorization.Authorization getAuthorization(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
