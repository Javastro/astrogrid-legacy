/**
 * AxisDataServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver;

public interface AxisDataServer extends java.rmi.Remote {
    public java.lang.String getMetadata(java.lang.Object parameters) throws java.rmi.RemoteException;
    public org.astrogrid.datacenter.axisdataserver.types.Language[] getLanguageInfo(java.lang.Object parameters) throws java.rmi.RemoteException;
    public java.lang.String doQuery(java.lang.String resultsFormat, org.astrogrid.datacenter.axisdataserver.types.Query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException;
    public java.lang.String makeQuery(org.astrogrid.datacenter.axisdataserver.types.Query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException;
    public java.lang.String makeQueryWithId(org.astrogrid.datacenter.axisdataserver.types.Query query, java.lang.String assignedId) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException;
    public void setResultsDestination(java.lang.String queryId, org.apache.axis.types.URI myspaceUrl) throws java.rmi.RemoteException;
    public void startQuery(java.lang.String queryId) throws java.rmi.RemoteException;
    public java.lang.String getResultsAndClose(java.lang.String queryId) throws java.rmi.RemoteException;
    public void abortQuery(java.lang.String queryId) throws java.rmi.RemoteException;
    public java.lang.String getStatus(java.lang.String queryId) throws java.rmi.RemoteException;
    public void registerWebListener(java.lang.String queryId, org.apache.axis.types.URI url) throws java.rmi.RemoteException;
    public void registerJobMonitor(java.lang.String queryId, org.apache.axis.types.URI url) throws java.rmi.RemoteException;
}
