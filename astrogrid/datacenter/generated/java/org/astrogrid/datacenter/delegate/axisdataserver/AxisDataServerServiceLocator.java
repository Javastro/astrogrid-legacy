/**
 * AxisDataServerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.delegate.axisdataserver;

public class AxisDataServerServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServerService {

    // Use to get a proxy class for AxisDataServer
    private final java.lang.String AxisDataServer_address = "http://localhost:8080/axis/services/AxisDataServer";

    public java.lang.String getAxisDataServerAddress() {
        return AxisDataServer_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AxisDataServerWSDDServiceName = "AxisDataServer";

    public java.lang.String getAxisDataServerWSDDServiceName() {
        return AxisDataServerWSDDServiceName;
    }

    public void setAxisDataServerWSDDServiceName(java.lang.String name) {
        AxisDataServerWSDDServiceName = name;
    }

    public org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServer getAxisDataServer() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AxisDataServer_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAxisDataServer(endpoint);
    }

    public org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServer getAxisDataServer(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServerSoapBindingStub _stub = new org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServerSoapBindingStub(portAddress, this);
            _stub.setPortName(getAxisDataServerWSDDServiceName());
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
            if (org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServer.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServerSoapBindingStub _stub = new org.astrogrid.datacenter.delegate.axisdataserver.AxisDataServerSoapBindingStub(new java.net.URL(AxisDataServer_address), this);
                _stub.setPortName(getAxisDataServerWSDDServiceName());
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
        if ("AxisDataServer".equals(inputPortName)) {
            return getAxisDataServer();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/axis/services/AxisDataServer", "AxisDataServerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("AxisDataServer"));
        }
        return ports.iterator();
    }

}
