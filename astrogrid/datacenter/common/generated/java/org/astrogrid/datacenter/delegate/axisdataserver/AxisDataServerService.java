/**
 * AxisDataServerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.delegate.axisdataserver;

public interface AxisDataServerService extends javax.xml.rpc.Service {
    public java.lang.String getAxisDataServerAddress();

    public org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServer getAxisDataServer() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServer getAxisDataServer(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
