/**
 * QueryRegistryPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.service;

public interface QueryRegistryPortType extends java.rmi.Remote {
    public org.astrogrid.registry.server.service._VOResources search(org.astrogrid.registry.server.generated.adql.SelectType adql) throws java.rmi.RemoteException;
    public org.astrogrid.registry.server.service._VOResources keywordSearch(java.lang.String keywords, boolean orValue) throws java.rmi.RemoteException;
    public org.astrogrid.registry.server.service._VOResources getRegistries() throws java.rmi.RemoteException;
}
