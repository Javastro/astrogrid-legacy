/**
 * Manager.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.slinger.myspace.it05;

public interface Manager extends java.rmi.Remote {
    public KernelResults getBytes(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public KernelResults getString(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public java.lang.String heartBeat() throws java.rmi.RemoteException;
    public KernelResults getEntriesList(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public KernelResults putString(java.lang.String in0, java.lang.String in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException;
    public KernelResults putBytes(java.lang.String in0, byte[] in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException;
    public KernelResults putUri(java.lang.String in0, java.lang.String in1, int in2, int in3, boolean in4) throws java.rmi.RemoteException;
    public KernelResults createContainer(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public KernelResults copyFile(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
    public KernelResults moveFile(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
    public KernelResults deleteFile(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public KernelResults extendLifetime(java.lang.String in0, long in1, boolean in2) throws java.rmi.RemoteException;
    public KernelResults changeOwner(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
    public KernelResults createAccount(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
    public KernelResults deleteAccount(java.lang.String in0, boolean in1) throws java.rmi.RemoteException;
}
