/**
 * SEC_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.dataservice.impl.sec;

public interface SEC_Service extends javax.xml.rpc.Service {
    public java.lang.String getSECPortAddress();

    public org.astrogrid.dataservice.impl.sec.SEC_Port getSECPort() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.dataservice.impl.sec.SEC_Port getSECPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
