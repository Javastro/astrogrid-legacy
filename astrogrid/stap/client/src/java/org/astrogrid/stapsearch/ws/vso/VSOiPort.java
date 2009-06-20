/**
 * VSOiPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public interface VSOiPort extends java.rmi.Remote {
    public org.astrogrid.stapsearch.ws.vso.ProviderQueryResponse[] query(org.astrogrid.stapsearch.ws.vso.QueryRequest body) throws java.rmi.RemoteException;
    public org.astrogrid.stapsearch.ws.vso.ProviderGetDataResponse[] getData(org.astrogrid.stapsearch.ws.vso.VSOGetDataRequest body) throws java.rmi.RemoteException;
}
