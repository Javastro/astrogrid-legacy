/**
 * AxisDataServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.delegate.axisdataserver;

public interface AxisDataServer extends java.rmi.Remote {
    public org.w3c.dom.Element getMetadata(java.lang.Object parameters) throws java.rmi.RemoteException;
    public org.w3c.dom.Element doQuery(java.lang.String resultsFormat, org.astrogrid.datacenter.adql.generated.Select adql) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException;
    public java.lang.String makeQuery(org.astrogrid.datacenter.adql.generated.Select adql) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException;
    public java.lang.String makeQueryWithId(org.astrogrid.datacenter.adql.generated.Select adql, java.lang.String assignedId) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException;
    public void setResultsDestination(java.lang.String myspaceUrl) throws java.rmi.RemoteException;
    public void startQuery(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String getResultsAndClose(java.lang.String queryId) throws java.rmi.RemoteException, org.xml.sax.SAXException;
    public void abortQuery(java.lang.String queryId) throws java.rmi.RemoteException;
    public java.lang.String getStatus(java.lang.String queryId) throws java.rmi.RemoteException;
    public void registerWebListener(java.lang.String queryId, java.lang.String url) throws java.rmi.RemoteException, java.net.MalformedURLException;
    public void registerJobMonitor(java.lang.String queryId, java.lang.String url) throws java.rmi.RemoteException, java.net.MalformedURLException;
}
