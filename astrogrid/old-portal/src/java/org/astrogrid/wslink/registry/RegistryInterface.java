/**
 * RegistryInterface.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.registry;

public interface RegistryInterface extends javax.xml.rpc.Service {
    public java.lang.String getRegistryInterfacePortAddress();

    public org.astrogrid.wslink.registry.RegistryInterface_Port getRegistryInterfacePort() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.wslink.registry.RegistryInterface_Port getRegistryInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
