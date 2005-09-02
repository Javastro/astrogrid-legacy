/**
 * SampleService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.astrogrid.security.sample;

public interface SampleService extends javax.xml.rpc.Service {
    public java.lang.String getSamplePortAddress();

    public org.astrogrid.security.sample.SamplePortType getSamplePort() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.security.sample.SamplePortType getSamplePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
