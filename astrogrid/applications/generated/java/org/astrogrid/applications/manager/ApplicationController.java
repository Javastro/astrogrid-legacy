/**
 * ApplicationController.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.manager;

public interface ApplicationController extends java.rmi.Remote {
    public java.lang.String[] listApplications() throws java.rmi.RemoteException;
    public org.astrogrid.applications.ApplicationDescription getApplicationDescription(java.lang.String applicationID) throws java.rmi.RemoteException;
    public int initializeApplication(java.lang.String applicationID, int jobstepID, org.astrogrid.applications.Parameter[] parameters) throws java.rmi.RemoteException;
    public void executeApplication() throws java.rmi.RemoteException;
    public java.lang.String queryApplicationStatus() throws java.rmi.RemoteException;
}
