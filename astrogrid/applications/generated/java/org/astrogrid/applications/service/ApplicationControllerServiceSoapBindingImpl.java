/**
 * ApplicationControllerServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service;

public class ApplicationControllerServiceSoapBindingImpl implements org.astrogrid.applications.service.ApplicationController{
    public java.lang.String[] listApplications() throws java.rmi.RemoteException {
        return null;
    }

    public org.astrogrid.applications.delegate.beans.ApplicationDescription getApplicationDescription(java.lang.String applicationID) throws java.rmi.RemoteException {
        return null;
    }

    public int initializeApplication(java.lang.String applicationID, java.lang.String jobstepID, java.lang.String jobMonitorURL, org.astrogrid.applications.delegate.beans.ParameterValues parameters) throws java.rmi.RemoteException {
        return -3;
    }

    public void executeApplication(int executionId) throws java.rmi.RemoteException {
    }

    public java.lang.String queryApplicationExecutionStatus(int executionId) throws java.rmi.RemoteException {
        return null;
    }

    public java.lang.String returnRegistryEntry() throws java.rmi.RemoteException {
        return null;
    }

}
