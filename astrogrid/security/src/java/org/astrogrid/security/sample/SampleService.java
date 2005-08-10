/**
 * SampleService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.security.sample;

/**
 * Client-side proxy for the service.
 */
public interface SampleService extends javax.xml.rpc.Service {
    public java.lang.String getSecuritySampleAddress();

    public org.astrogrid.security.sample.SamplePortType getSecuritySamplePort() throws javax.xml.rpc.ServiceException;

    public org.astrogrid.security.sample.SamplePortType getSecuritySamplePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
