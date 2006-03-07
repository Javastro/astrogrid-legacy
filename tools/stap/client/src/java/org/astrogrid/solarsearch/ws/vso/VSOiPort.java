/**
 * VSOiPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.solarsearch.ws.vso;

public interface VSOiPort extends java.rmi.Remote {
    public org.astrogrid.solarsearch.ws.vso.ProviderQueryResponse[] query(org.astrogrid.solarsearch.ws.vso.QueryRequest body) throws java.rmi.RemoteException;
    public org.astrogrid.solarsearch.ws.vso.ProviderGetDataResponse[] getData(org.astrogrid.solarsearch.ws.vso.VSOGetDataRequest body) throws java.rmi.RemoteException;
}
