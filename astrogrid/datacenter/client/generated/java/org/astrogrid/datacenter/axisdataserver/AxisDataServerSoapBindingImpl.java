/**
 * AxisDataServerSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver;

public class AxisDataServerSoapBindingImpl implements org.astrogrid.datacenter.axisdataserver.AxisDataServer{
    public org.w3c.dom.Element getMetadata(java.lang.Object parameters) throws java.rmi.RemoteException {
        return null;
    }

    public org.w3c.dom.Element doQuery(java.lang.String resultsFormat, org.astrogrid.datacenter.adql.generated.Select select) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
        return null;
    }

    public java.lang.String makeQuery(org.astrogrid.datacenter.adql.generated.Select select) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
        return null;
    }

    public java.lang.String makeQueryWithId(org.astrogrid.datacenter.adql.generated.Select select, java.lang.String assignedId) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
        return null;
    }

    public void setResultsDestination(java.lang.String myspaceUrl) throws java.rmi.RemoteException {
    }

    public void startQuery(java.lang.String id) throws java.rmi.RemoteException {
    }

    public java.lang.String getResultsAndClose(java.lang.String queryId) throws java.rmi.RemoteException, org.xml.sax.SAXException {
        return null;
    }

    public void abortQuery(java.lang.String queryId) throws java.rmi.RemoteException {
    }

    public java.lang.String getStatus(java.lang.String queryId) throws java.rmi.RemoteException {
        return null;
    }

    public void registerWebListener(java.lang.String queryId, java.lang.String url) throws java.rmi.RemoteException, java.net.MalformedURLException {
    }

    public void registerJobMonitor(java.lang.String queryId, java.lang.String url) throws java.rmi.RemoteException, java.net.MalformedURLException {
    }

}
