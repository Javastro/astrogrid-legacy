/**
 * SEC_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.dataservice.impl.sec;

public class SEC_ServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.dataservice.impl.sec.SEC_Service {

    // Use to get a proxy class for SECPort
    private final java.lang.String SECPort_address = "http://radiosun.ts.astro.it/sec/sec_server.php";

    public java.lang.String getSECPortAddress() {
        return SECPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SECPortWSDDServiceName = "SECPort";

    public java.lang.String getSECPortWSDDServiceName() {
        return SECPortWSDDServiceName;
    }

    public void setSECPortWSDDServiceName(java.lang.String name) {
        SECPortWSDDServiceName = name;
    }

    public org.astrogrid.dataservice.impl.sec.SEC_Port getSECPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SECPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSECPort(endpoint);
    }

    public org.astrogrid.dataservice.impl.sec.SEC_Port getSECPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.dataservice.impl.sec.SECBindingStub _stub = new org.astrogrid.dataservice.impl.sec.SECBindingStub(portAddress, this);
            _stub.setPortName(getSECPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.astrogrid.dataservice.impl.sec.SEC_Port.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.dataservice.impl.sec.SECBindingStub _stub = new org.astrogrid.dataservice.impl.sec.SECBindingStub(new java.net.URL(SECPort_address), this);
                _stub.setPortName(getSECPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("SECPort".equals(inputPortName)) {
            return getSECPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:org.egso.sec/wsdl", "SEC");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("SECPort"));
        }
        return ports.iterator();
    }

}
