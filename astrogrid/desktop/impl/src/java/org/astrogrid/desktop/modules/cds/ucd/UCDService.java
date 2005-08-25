/**
 * UCDService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.cds.ucd;

public interface UCDService extends javax.xml.rpc.Service {
    public java.lang.String getUCDAddress();

    public org.astrogrid.desktop.modules.cds.ucd.UCD getUCD() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.desktop.modules.cds.ucd.UCD getUCD(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
