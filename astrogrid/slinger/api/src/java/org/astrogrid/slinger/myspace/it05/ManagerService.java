/**
 * ManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.slinger.myspace.it05;

public interface ManagerService extends javax.xml.rpc.Service {
    public java.lang.String getAstrogridMyspaceAddress();

    public Manager getAstrogridMyspace() throws javax.xml.rpc.ServiceException;

    public Manager getAstrogridMyspace(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
