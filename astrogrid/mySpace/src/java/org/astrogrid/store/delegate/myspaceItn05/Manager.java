/**
 * Manager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.myspace.delegate;

public interface Manager extends java.rmi.Remote {
    public org.astrogrid.myspace.delegate.KernelResults getBytes(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults getString(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public java.lang.String heartBeat() throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults getEntriesList(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults putString(java.lang.String in0, java.lang.String in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults putBytes(java.lang.String in0, byte[] in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults putUri(java.lang.String in0, java.lang.String in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults createContainer(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults copyFile(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults moveFile(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults deleteFile(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults extendLifetime(java.lang.String in0, long in1, boolean in2) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults changeOwner(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults createAccount(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.myspace.delegate.KernelResults deleteAccount(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
}
