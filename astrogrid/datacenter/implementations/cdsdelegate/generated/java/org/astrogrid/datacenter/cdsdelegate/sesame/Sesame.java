/**
 * Sesame.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.sesame;

public interface Sesame extends java.rmi.Remote {
    public java.lang.String sesame(java.lang.String name) throws java.rmi.RemoteException;
    public java.lang.String sesameXML(java.lang.String name) throws java.rmi.RemoteException;
    public java.lang.String sesame(java.lang.String name, java.lang.String resultType, boolean all, java.lang.String service) throws java.rmi.RemoteException;
    public java.lang.String sesame(java.lang.String name, java.lang.String resultType) throws java.rmi.RemoteException;
    public java.lang.String sesame(java.lang.String in0, java.lang.String in1, boolean in2) throws java.rmi.RemoteException;
}
