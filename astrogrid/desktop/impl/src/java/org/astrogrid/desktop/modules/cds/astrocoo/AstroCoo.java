/**
 * AstroCoo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.cds.astrocoo;

public interface AstroCoo extends java.rmi.Remote {
    public java.lang.String convert(double x, double y, double z, int precision) throws java.rmi.RemoteException;
    public java.lang.String convert(double lon, double lat, int precision) throws java.rmi.RemoteException;
    public java.lang.String convert(int frame1, int frame2, double x, double y, double z, int precision, double equinox1, double equinox2) throws java.rmi.RemoteException;
    public java.lang.String convert(int frame1, int frame2, double lon, double lat, int precision, double equinox1, double equinox2) throws java.rmi.RemoteException;
}
