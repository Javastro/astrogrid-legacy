/**
 * MySpaceManager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.myspace;

public interface MySpaceManager extends java.rmi.Remote {
    public java.lang.String upLoad(java.lang.String jobDetails) throws java.rmi.RemoteException;
    public java.lang.String lookupDataHolderDetails(java.lang.String jobDetails) throws java.rmi.RemoteException;
    public java.lang.String lookupDataHoldersDetails(java.lang.String jobDetails) throws java.rmi.RemoteException;
    public java.lang.String copyDataHolder(java.lang.String jobDetails) throws java.rmi.RemoteException;
    public java.lang.String moveDataHolder(java.lang.String jobDetails) throws java.rmi.RemoteException;
    public java.lang.String exportDataHolder(java.lang.String jobDetails) throws java.rmi.RemoteException;
    public java.lang.String createContainer(java.lang.String jobDetails) throws java.rmi.RemoteException;
    public java.lang.String deleteDataHolder(java.lang.String jobDetails) throws java.rmi.RemoteException;
}
