/**
 * WSTestService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.testservice;

public interface WSTestService extends javax.xml.rpc.Service {
    public java.lang.String getWSTestAddress();

    public org.astrogrid.applications.testservice.WSTest getWSTest() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.applications.testservice.WSTest getWSTest(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
