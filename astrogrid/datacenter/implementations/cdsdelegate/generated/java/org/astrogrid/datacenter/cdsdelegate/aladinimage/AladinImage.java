/**
 * AladinImage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.aladinimage;

public interface AladinImage extends java.rmi.Remote {
    public org.astrogrid.datacenter.cdsdelegate.aladinimage.ObservingProgramDescription[] getObservingProgramsDescription(java.lang.String position, java.lang.String radius) throws java.rmi.RemoteException;
    public org.astrogrid.datacenter.cdsdelegate.aladinimage.ObservingProgram getObservingProgram(java.lang.String position, java.lang.String radius, java.lang.String program_name) throws java.rmi.RemoteException;
    public org.astrogrid.datacenter.cdsdelegate.aladinimage.Filter[] getFilters(java.lang.String position, java.lang.String radius, java.lang.String program_name) throws java.rmi.RemoteException;
    public org.astrogrid.datacenter.cdsdelegate.aladinimage.Observation[] getObservations(java.lang.String position, java.lang.String radius, java.lang.String program_name) throws java.rmi.RemoteException;
}
