/**
 * RegistryInterfaceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.client.stubbs;

public interface RegistryInterfaceService extends javax.xml.rpc.Service {
    public java.lang.String getRegistryAddress();

    public org.astrogrid.registry.RegistryInterface getRegistry() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.registry.RegistryInterface getRegistry(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
