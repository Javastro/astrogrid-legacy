/**
 * RegistryInterface3_0ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.uml03.axis.services.RegistryInterface3_0;

public class RegistryInterface3_0ServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0Service {

    // Use to get a proxy class for RegistryInterface3_0
    private final java.lang.String RegistryInterface3_0_address = "http://uml03.astrogrid.org:8080/axis/services/RegistryInterface3_0";

    public java.lang.String getRegistryInterface3_0Address() {
        return RegistryInterface3_0_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegistryInterface3_0WSDDServiceName = "RegistryInterface3_0";

    public java.lang.String getRegistryInterface3_0WSDDServiceName() {
        return RegistryInterface3_0WSDDServiceName;
    }

    public void setRegistryInterface3_0WSDDServiceName(java.lang.String name) {
        RegistryInterface3_0WSDDServiceName = name;
    }

    public org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0 getRegistryInterface3_0() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegistryInterface3_0_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegistryInterface3_0(endpoint);
    }

    public org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0 getRegistryInterface3_0(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0SoapBindingStub _stub = new org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0SoapBindingStub(portAddress, this);
            _stub.setPortName(getRegistryInterface3_0WSDDServiceName());
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
            if (org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0SoapBindingStub _stub = new org.astrogrid.uml03.axis.services.RegistryInterface3_0.RegistryInterface3_0SoapBindingStub(new java.net.URL(RegistryInterface3_0_address), this);
                _stub.setPortName(getRegistryInterface3_0WSDDServiceName());
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
        if ("RegistryInterface3_0".equals(inputPortName)) {
            return getRegistryInterface3_0();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://uml03.astrogrid.org:8080/axis/services/RegistryInterface3_0", "RegistryInterface3_0Service");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("RegistryInterface3_0"));
        }
        return ports.iterator();
    }

}
