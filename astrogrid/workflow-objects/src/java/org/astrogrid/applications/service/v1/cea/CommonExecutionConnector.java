/**
 * CommonExecutionConnector.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service.v1.cea;

public interface CommonExecutionConnector extends java.rmi.Remote {

    // Run an application asynchronously
    public java.lang.String execute(org.astrogrid.workflow.beans.v1.axis._tool tool, org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType jobstepID, java.lang.String jobMonitorURL) throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.impl._ceaFault;

    // Abort a running application
    public boolean abort(java.lang.String executionId) throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.impl._ceaFault;

    // List the applications that this CommonExecutionController manages
    public org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList listApplications() throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.impl._ceaFault;

    // Get a particular applicationID
    public org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase getApplicationDescription(java.lang.String applicationID) throws java.rmi.RemoteException;

    // uery the status of a running application
    public org.astrogrid.jes.types.v1.cea.axis.MessageType queryExecutionStatus(java.lang.String executionId) throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.impl._ceaFault;

    // return an entry for the registry - this really belongs in an interface
    // that the registry should be providing
    public org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn returnRegistryEntry() throws java.rmi.RemoteException;
}
