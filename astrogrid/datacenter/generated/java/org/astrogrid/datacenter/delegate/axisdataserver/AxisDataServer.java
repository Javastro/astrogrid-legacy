/**
 * AxisDataServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.delegate.axisdataserver;

public interface AxisDataServer extends java.rmi.Remote {
    public java.lang.String getStatus(java.lang.Object soapBody) throws java.rmi.RemoteException;
    public org.w3c.dom.Element getMetadata() throws java.rmi.RemoteException;
    public java.lang.Object getMetadata(java.lang.String xpathExpression) throws java.rmi.RemoteException;
    public org.w3c.dom.Element getVoRegistryMetadata() throws java.rmi.RemoteException;
    public org.w3c.dom.Element doQuery(org.w3c.dom.Element soapBody) throws java.rmi.RemoteException, org.astrogrid.datacenter.query.QueryException, org.astrogrid.datacenter.delegate.axisdataserver.DatabaseAccessException;
    public org.w3c.dom.Element makeQuery(org.w3c.dom.Element soapBody) throws java.rmi.RemoteException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException, org.astrogrid.datacenter.delegate.axisdataserver.DatabaseAccessException;
    public org.w3c.dom.Element startQuery(org.w3c.dom.Element soapBody) throws java.rmi.RemoteException;
    public org.w3c.dom.Element getResultsAndClose(org.w3c.dom.Element soapBody) throws java.rmi.RemoteException, org.xml.sax.SAXException;
    public void abortQuery(org.w3c.dom.Element soapBody) throws java.rmi.RemoteException;
    public void registerWebListener(org.w3c.dom.Element soapBody, org.astrogrid.datacenter.delegate.WebNotifyServiceListener listener) throws java.rmi.RemoteException;
}
