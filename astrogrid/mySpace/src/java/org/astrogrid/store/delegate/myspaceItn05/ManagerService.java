/**
 * ManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.myspace.delegate;

public interface ManagerService extends javax.xml.rpc.Service {
    public java.lang.String getAstrogridMyspaceAddress();

    public org.astrogrid.myspace.delegate.Manager getAstrogridMyspace() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.myspace.delegate.Manager getAstrogridMyspace(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
