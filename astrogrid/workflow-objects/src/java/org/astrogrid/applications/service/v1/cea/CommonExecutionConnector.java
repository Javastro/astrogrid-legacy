/**
 * CommonExecutionConnector.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service.v1.cea;

public interface CommonExecutionConnector extends java.rmi.Remote {
    public java.lang.String execute(org.astrogrid.workflow.beans.v1.axis._tool tool, org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType jobstepID, java.lang.String jobMonitorURL) throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.CeaFault;
    public boolean abort(java.lang.String executionId) throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.CeaFault;
    public org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList listApplications() throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.CeaFault;
    public org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase getApplicationDescription(java.lang.String applicationID) throws java.rmi.RemoteException;
    public org.astrogrid.jes.types.v1.cea.axis.MessageType queryExecutionStatus(java.lang.String executionId) throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.CeaFault;
    public java.lang.String returnRegistryEntry() throws java.rmi.RemoteException;
}
