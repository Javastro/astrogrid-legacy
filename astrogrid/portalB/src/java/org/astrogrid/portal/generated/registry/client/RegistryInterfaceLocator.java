/**
 * RegistryInterfaceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.generated.registry.client;

public class RegistryInterfaceLocator extends org.apache.axis.client.Service implements org.astrogrid.wslink.registry.RegistryInterface {

    // Use to get a proxy class for RegistryInterfacePort
    private final java.lang.String RegistryInterfacePort_address = "http://localhost:3000/soap/servlet/rpcrouter";

    public java.lang.String getRegistryInterfacePortAddress() {
        return RegistryInterfacePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegistryInterfacePortWSDDServiceName = "RegistryInterfacePort";

    public java.lang.String getRegistryInterfacePortWSDDServiceName() {
        return RegistryInterfacePortWSDDServiceName;
    }

    public void setRegistryInterfacePortWSDDServiceName(java.lang.String name) {
        RegistryInterfacePortWSDDServiceName = name;
    }

    public org.astrogrid.wslink.registry.RegistryInterface_Port getRegistryInterfacePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegistryInterfacePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegistryInterfacePort(endpoint);
    }

    public org.astrogrid.wslink.registry.RegistryInterface_Port getRegistryInterfacePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.wslink.registry.RegistryInterface_BindingStub _stub = new org.astrogrid.wslink.registry.RegistryInterface_BindingStub(portAddress, this);
            _stub.setPortName(getRegistryInterfacePortWSDDServiceName());
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
            if (org.astrogrid.wslink.registry.RegistryInterface_Port.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.wslink.registry.RegistryInterface_BindingStub _stub = new org.astrogrid.wslink.registry.RegistryInterface_BindingStub(new java.net.URL(RegistryInterfacePort_address), this);
                _stub.setPortName(getRegistryInterfacePortWSDDServiceName());
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
        if ("RegistryInterfacePort".equals(inputPortName)) {
            return getRegistryInterfacePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:org.astrogrid.registry", "RegistryInterface");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("RegistryInterfacePort"));
        }
        return ports.iterator();
    }

}
