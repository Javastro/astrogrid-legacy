/**
 * SimpleAuthenticatonServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.services.authentication;

public class SimpleAuthenticatonServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.community.services.authentication.SimpleAuthenticatonService {

    // Use to get a proxy class for SimpleAuthentication
    private final java.lang.String SimpleAuthentication_address = "http://localhost:8080/axis/services/SimpleAuthentication";

    public java.lang.String getSimpleAuthenticationAddress() {
        return SimpleAuthentication_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SimpleAuthenticationWSDDServiceName = "SimpleAuthentication";

    public java.lang.String getSimpleAuthenticationWSDDServiceName() {
        return SimpleAuthenticationWSDDServiceName;
    }

    public void setSimpleAuthenticationWSDDServiceName(java.lang.String name) {
        SimpleAuthenticationWSDDServiceName = name;
    }

    public org.astrogrid.community.services.authentication.SimpleAuthenticaton getSimpleAuthentication() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SimpleAuthentication_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSimpleAuthentication(endpoint);
    }

    public org.astrogrid.community.services.authentication.SimpleAuthenticaton getSimpleAuthentication(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.community.services.authentication.SimpleAuthenticationSoapBindingStub _stub = new org.astrogrid.community.services.authentication.SimpleAuthenticationSoapBindingStub(portAddress, this);
            _stub.setPortName(getSimpleAuthenticationWSDDServiceName());
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
            if (org.astrogrid.community.services.authentication.SimpleAuthenticaton.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.community.services.authentication.SimpleAuthenticationSoapBindingStub _stub = new org.astrogrid.community.services.authentication.SimpleAuthenticationSoapBindingStub(new java.net.URL(SimpleAuthentication_address), this);
                _stub.setPortName(getSimpleAuthenticationWSDDServiceName());
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
        if ("SimpleAuthentication".equals(inputPortName)) {
            return getSimpleAuthentication();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:authentication.services.community.astrogrid.org", "SimpleAuthenticatonService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("SimpleAuthentication"));
        }
        return ports.iterator();
    }

}
