/**
 * AxisDataServer_v05_Port.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver.v05;

public interface AxisDataServer_v05_Port extends java.rmi.Remote {
    public java.lang.String getMetadata() throws java.rmi.RemoteException;
    public java.lang.String askAdqlQuery(java.lang.String query, java.lang.String requestedFormat) throws java.rmi.RemoteException;
    public java.lang.String askSql(java.lang.String sql, java.lang.String requestedFormat) throws java.rmi.RemoteException;
    public java.lang.String askCone(double ra, double dec, double radius, java.lang.String requestedFormat) throws java.rmi.RemoteException;
    public java.lang.String submitAdqlQuery(java.lang.String query, java.lang.String resultsTarget, java.lang.String requestedFormat) throws java.rmi.RemoteException;
    public void abortQuery(java.lang.String queryId) throws java.rmi.RemoteException;
    public org.astrogrid.datacenter.axisdataserver.v05.QueryStatusSoapyBean getQueryStatus(java.lang.String queryId) throws java.rmi.RemoteException;
    public void throwFault() throws java.rmi.RemoteException;
}
