/**
 * Manager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.store.delegate.myspaceItn05;

public interface Manager extends java.rmi.Remote {
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults getBytes(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults getString(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public java.lang.String heartBeat() throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults getEntriesList(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults putString(java.lang.String in0, java.lang.String in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults putBytes(java.lang.String in0, byte[] in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults putUri(java.lang.String in0, java.lang.String in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults createContainer(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults copyFile(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults moveFile(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults deleteFile(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults extendLifetime(java.lang.String in0, long in1, boolean in2) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults changeOwner(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults createAccount(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public org.astrogrid.store.delegate.myspaceItn05.KernelResults deleteAccount(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
}
