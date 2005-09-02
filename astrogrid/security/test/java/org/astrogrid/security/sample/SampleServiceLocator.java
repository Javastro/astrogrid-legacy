/**
 * SampleServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.astrogrid.security.sample;

public class SampleServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.security.sample.SampleService {

    public SampleServiceLocator() {
    }


    public SampleServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SampleServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SamplePort
    private java.lang.String SamplePort_address = "http://localhost:8080/security/services/Sample";

    public java.lang.String getSamplePortAddress() {
        return SamplePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SamplePortWSDDServiceName = "SamplePort";

    public java.lang.String getSamplePortWSDDServiceName() {
        return SamplePortWSDDServiceName;
    }

    public void setSamplePortWSDDServiceName(java.lang.String name) {
        SamplePortWSDDServiceName = name;
    }

    public org.astrogrid.security.sample.SamplePortType getSamplePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SamplePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSamplePort(endpoint);
    }

    public org.astrogrid.security.sample.SamplePortType getSamplePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.security.sample.SamplePortSoapBindingStub _stub = new org.astrogrid.security.sample.SamplePortSoapBindingStub(portAddress, this);
            _stub.setPortName(getSamplePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSamplePortEndpointAddress(java.lang.String address) {
        SamplePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.astrogrid.security.sample.SamplePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.security.sample.SamplePortSoapBindingStub _stub = new org.astrogrid.security.sample.SamplePortSoapBindingStub(new java.net.URL(SamplePort_address), this);
                _stub.setPortName(getSamplePortWSDDServiceName());
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
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SamplePort".equals(inputPortName)) {
            return getSamplePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:this", "SampleService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:this", "SamplePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SamplePort".equals(portName)) {
            setSamplePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
