/**
 * ApplicationController.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service;

public interface ApplicationController extends java.rmi.Remote {
    public java.lang.String[] listApplications() throws java.rmi.RemoteException;
    public org.astrogrid.applications.delegate.beans.SimpleApplicationDescription getApplicationDescription(java.lang.String applicationID) throws java.rmi.RemoteException;
    public java.lang.String initializeApplication(java.lang.String applicationID, java.lang.String jobstepID, java.lang.String jobMonitorURL, org.astrogrid.applications.delegate.beans.User user, org.astrogrid.applications.delegate.beans.ParameterValues parameters) throws java.rmi.RemoteException;
    public boolean executeApplication(java.lang.String executionId) throws java.rmi.RemoteException;
    public java.lang.String queryApplicationExecutionStatus(java.lang.String executionId) throws java.rmi.RemoteException;
    public java.lang.String returnRegistryEntry() throws java.rmi.RemoteException;
}
