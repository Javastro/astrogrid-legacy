/**
 * AuthenticatonServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.delegate.authentication;

public class AuthenticatonServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.community.delegate.authentication.AuthenticatonService {

    // Use to get a proxy class for Authentication
    private final java.lang.String Authentication_address = "http://localhost:8080/axis/services/Authentication";

    public java.lang.String getAuthenticationAddress() {
        return Authentication_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AuthenticationWSDDServiceName = "Authentication";

    public java.lang.String getAuthenticationWSDDServiceName() {
        return AuthenticationWSDDServiceName;
    }

    public void setAuthenticationWSDDServiceName(java.lang.String name) {
        AuthenticationWSDDServiceName = name;
    }

    public org.astrogrid.community.delegate.authentication.Authenticaton getAuthentication() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Authentication_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAuthentication(endpoint);
    }

    public org.astrogrid.community.delegate.authentication.Authenticaton getAuthentication(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.community.delegate.authentication.AuthenticationSoapBindingStub _stub = new org.astrogrid.community.delegate.authentication.AuthenticationSoapBindingStub(portAddress, this);
            _stub.setPortName(getAuthenticationWSDDServiceName());
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
            if (org.astrogrid.community.delegate.authentication.Authenticaton.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.community.delegate.authentication.AuthenticationSoapBindingStub _stub = new org.astrogrid.community.delegate.authentication.AuthenticationSoapBindingStub(new java.net.URL(Authentication_address), this);
                _stub.setPortName(getAuthenticationWSDDServiceName());
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
        if ("Authentication".equals(inputPortName)) {
            return getAuthentication();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:org.astrogrid.community.authentication.service", "AuthenticatonService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("Authentication"));
        }
        return ports.iterator();
    }

}
