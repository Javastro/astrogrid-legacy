/**
 * SesameService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.ucdlist;

public interface SesameService extends javax.xml.rpc.Service {
    public java.lang.String getUCDListAddress();

    public org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDList getUCDList() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDList getUCDList(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
