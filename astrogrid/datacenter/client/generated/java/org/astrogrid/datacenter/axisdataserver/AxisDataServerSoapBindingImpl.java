/**
 * AxisDataServerSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver;

public class AxisDataServerSoapBindingImpl implements org.astrogrid.datacenter.axisdataserver.AxisDataServer{
    public java.lang.String getMetadata(java.lang.Object parameters) throws java.rmi.RemoteException {
        return null;
    }

    public org.astrogrid.datacenter.axisdataserver.types._language[] getLanguageInfo(java.lang.Object parameters) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String doQuery(java.lang.String resultsFormat, org.astrogrid.datacenter.axisdataserver.types._query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
        return null;
    }

    public java.lang.String makeQuery(org.astrogrid.datacenter.axisdataserver.types._query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
        return null;
    }

    public java.lang.String makeQueryWithId(org.astrogrid.datacenter.axisdataserver.types._query query, java.lang.String assignedId) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
        return null;
    }

    public void setResultsDestination(java.lang.String queryId, org.apache.axis.types.URI myspaceUrl) throws java.rmi.RemoteException {
    }

    public void startQuery(java.lang.String queryId) throws java.rmi.RemoteException {
    }

    public java.lang.String getResultsAndClose(java.lang.String queryId) throws java.rmi.RemoteException {
        return null;
    }

    public void abortQuery(java.lang.String queryId) throws java.rmi.RemoteException {
    }

    public java.lang.String getStatus(java.lang.String queryId) throws java.rmi.RemoteException {
        return null;
    }

    public void registerWebListener(java.lang.String queryId, org.apache.axis.types.URI url) throws java.rmi.RemoteException {
    }

    public void registerJobMonitor(java.lang.String queryId, org.apache.axis.types.URI url) throws java.rmi.RemoteException {
    }

}
