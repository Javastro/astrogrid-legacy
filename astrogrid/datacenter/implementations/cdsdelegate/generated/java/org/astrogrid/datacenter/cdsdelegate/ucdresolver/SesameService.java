/**
 * SesameService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.ucdresolver;

public interface SesameService extends javax.xml.rpc.Service {
    public java.lang.String getUCDResolverAddress();

    public org.astrogrid.datacenter.cdsdelegate.ucdresolver.UCDResolver getUCDResolver() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.datacenter.cdsdelegate.ucdresolver.UCDResolver getUCDResolver(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
