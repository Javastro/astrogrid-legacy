/**
 * QueryRegistryServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.service;

public class QueryRegistryServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.registry.server.service.QueryRegistryService {

    // Use to get a proxy class for QueryRegistrySOAPPort
    private final java.lang.String QueryRegistrySOAPPort_address = "http://localhost:8080/AxisWeb/services/QueryRegistrySOAPPort";

    public java.lang.String getQueryRegistrySOAPPortAddress() {
        return QueryRegistrySOAPPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String QueryRegistrySOAPPortWSDDServiceName = "QueryRegistrySOAPPort";

    public java.lang.String getQueryRegistrySOAPPortWSDDServiceName() {
        return QueryRegistrySOAPPortWSDDServiceName;
    }

    public void setQueryRegistrySOAPPortWSDDServiceName(java.lang.String name) {
        QueryRegistrySOAPPortWSDDServiceName = name;
    }

    public org.astrogrid.registry.server.service.QueryRegistryPortType getQueryRegistrySOAPPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(QueryRegistrySOAPPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getQueryRegistrySOAPPort(endpoint);
    }

    public org.astrogrid.registry.server.service.QueryRegistryPortType getQueryRegistrySOAPPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.registry.server.service.QueryRegistrySOAPBindingStub _stub = new org.astrogrid.registry.server.service.QueryRegistrySOAPBindingStub(portAddress, this);
            _stub.setPortName(getQueryRegistrySOAPPortWSDDServiceName());
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
            if (org.astrogrid.registry.server.service.QueryRegistryPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.registry.server.service.QueryRegistrySOAPBindingStub _stub = new org.astrogrid.registry.server.service.QueryRegistrySOAPBindingStub(new java.net.URL(QueryRegistrySOAPPort_address), this);
                _stub.setPortName(getQueryRegistrySOAPPortWSDDServiceName());
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
        if ("QueryRegistrySOAPPort".equals(inputPortName)) {
            return getQueryRegistrySOAPPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.ivoa.net/schemas/services/QueryRegistry/wsdl", "QueryRegistryService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("QueryRegistrySOAPPort"));
        }
        return ports.iterator();
    }

}
