/**
 * VizieR.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.vizier;

public interface VizieR extends java.rmi.Remote {
    public java.lang.String cataloguesMetaData(java.lang.String target, double radius, java.lang.String unit, java.lang.String text) throws java.rmi.RemoteException;
    public java.lang.String cataloguesMetaData(java.lang.String target, double radius, java.lang.String unit, java.lang.String text, java.lang.String wavelength) throws java.rmi.RemoteException;
    public java.lang.String cataloguesData(java.lang.String target, double radius, java.lang.String unit, java.lang.String text) throws java.rmi.RemoteException;
    public java.lang.String cataloguesData(java.lang.String target, double radius, java.lang.String unit, java.lang.String text, java.lang.String wavelength) throws java.rmi.RemoteException;
    public java.lang.String metaAll() throws java.rmi.RemoteException;
}
