/**
 * RegistryInterfaceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.client.stubbs;

public class RegistryInterfaceServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.registry.client.stubbs.RegistryInterfaceService {

    // Use to get a proxy class for Registry
    private final java.lang.String Registry_address = "http://localhost:8080/axis/services/Registry";

    public java.lang.String getRegistryAddress() {
        return Registry_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegistryWSDDServiceName = "Registry";

    public java.lang.String getRegistryWSDDServiceName() {
        return RegistryWSDDServiceName;
    }

    public void setRegistryWSDDServiceName(java.lang.String name) {
        RegistryWSDDServiceName = name;
    }

    public org.astrogrid.registry.RegistryInterface getRegistry() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Registry_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegistry(endpoint);
    }

    public org.astrogrid.registry.RegistryInterface getRegistry(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.registry.client.stubbs.RegistrySoapBindingStub _stub = new org.astrogrid.registry.client.stubbs.RegistrySoapBindingStub(portAddress, this);
            _stub.setPortName(getRegistryWSDDServiceName());
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
            if (org.astrogrid.registry.RegistryInterface.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.registry.client.stubbs.RegistrySoapBindingStub _stub = new org.astrogrid.registry.client.stubbs.RegistrySoapBindingStub(new java.net.URL(Registry_address), this);
                _stub.setPortName(getRegistryWSDDServiceName());
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
        if ("Registry".equals(inputPortName)) {
            return getRegistry();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:org.astrogrid.registry", "RegistryInterfaceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("Registry"));
        }
        return ports.iterator();
    }

}
