/**
 * ADQLTranslatorSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public interface ADQLTranslatorSoap extends java.rmi.Remote {

    // Take an SQL string and convert it to the XML parsetree in ADQL
    public org.astrogrid.desktop.modules.ivoa.adql.SelectType SQLtoADQL(java.lang.String sql) throws java.rmi.RemoteException;

    // Take an SQL string and convert it to a string having the XML parsetree
    // in ADQL
    public java.lang.String SQLtoADQLString(java.lang.String sql) throws java.rmi.RemoteException;

    // Take an ADQL Select structure and convert to SQL string
    public java.lang.String ADQLtoSQL(org.astrogrid.desktop.modules.ivoa.adql.SelectType select) throws java.rmi.RemoteException;

    // Take an ADQL Select (as an XML string) and convert to SQL string
    public java.lang.String ADQLStringtoSQL(java.lang.String adql) throws java.rmi.RemoteException;
}
