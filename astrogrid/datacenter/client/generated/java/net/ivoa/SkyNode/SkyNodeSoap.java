/**
 * SkyNodeSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public interface SkyNodeSoap extends java.rmi.Remote {

    // [BASIC] Run a query, get back VOData
    public net.ivoa.SkyNode.VOData performQuery(net.ivoa.www.xml.ADQL.v0_7_4.SelectType select, java.lang.String format) throws java.rmi.RemoteException;

    // [FULL] Run an ExecPlan, get back a VOData
    public net.ivoa.SkyNode.VOData executePlan(net.ivoa.SkyNode.ExecPlan plan) throws java.rmi.RemoteException;

    // [BASIC] Returns MetaColumn[] info for a given table.
    public net.ivoa.SkyNode.MetaColumn column(java.lang.String table, java.lang.String column) throws java.rmi.RemoteException;

    // [BASIC] Returns MetaColumn[] info for a given table.
    public net.ivoa.SkyNode.ArrayOfMetaColumn columns(java.lang.String table) throws java.rmi.RemoteException;

    // [FULL] **NOT IMPLEMENTED** Returns a region which is the intersection
    // of the survey and the given region.
    public nvo_region.RegionType footprint(nvo_region.RegionType region) throws java.rmi.RemoteException;

    // [BASIC] Returns a string[] of available query result formats.
    public net.ivoa.SkyNode.ArrayOfString formats() throws java.rmi.RemoteException;

    // [BASIC] Returns MetaFunction[] with info for each function supported
    // by the service. These are functions other than the standard ones defined
    // in ADQL.
    public net.ivoa.SkyNode.ArrayOfMetaFunction functions() throws java.rmi.RemoteException;

    // [BASIC] Returns uptime infomration about the is service
    public net.ivoa.SkyNode.Availability getAvailability() throws java.rmi.RemoteException;

    // [FULL] Returns object count for a given criteria.
    public float queryCost(long planid, net.ivoa.www.xml.ADQL.v0_7_4.SelectType select) throws java.rmi.RemoteException;

    // [BASIC] Returns a MetaTable
    public net.ivoa.SkyNode.MetaTable table(java.lang.String table) throws java.rmi.RemoteException;

    // [BASIC] Returns an array of MetaTable
    public net.ivoa.SkyNode.ArrayOfMetaTable tables() throws java.rmi.RemoteException;
}
