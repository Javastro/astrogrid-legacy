/**
 * SesameService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.sesame;

public interface SesameService extends javax.xml.rpc.Service {
    public java.lang.String getSesameAddress();

    public org.astrogrid.datacenter.cdsdelegate.sesame.Sesame getSesame() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.datacenter.cdsdelegate.sesame.Sesame getSesame(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
