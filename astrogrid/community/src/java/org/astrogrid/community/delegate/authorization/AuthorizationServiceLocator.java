/**
 * AuthorizationServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.delegate.authorization;

public class AuthorizationServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.community.delegate.authorization.AuthorizationService {

    // Use to get a proxy class for Authorization
    private final java.lang.String Authorization_address = "http://localhost:8080/axis/services/Authorization";

    public java.lang.String getAuthorizationAddress() {
        return Authorization_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AuthorizationWSDDServiceName = "Authorization";

    public java.lang.String getAuthorizationWSDDServiceName() {
        return AuthorizationWSDDServiceName;
    }

    public void setAuthorizationWSDDServiceName(java.lang.String name) {
        AuthorizationWSDDServiceName = name;
    }

    public org.astrogrid.community.delegate.authorization.Authorization getAuthorization() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Authorization_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAuthorization(endpoint);
    }

    public org.astrogrid.community.delegate.authorization.Authorization getAuthorization(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.community.delegate.authorization.AuthorizationSoapBindingStub _stub = new org.astrogrid.community.delegate.authorization.AuthorizationSoapBindingStub(portAddress, this);
            _stub.setPortName(getAuthorizationWSDDServiceName());
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
            if (org.astrogrid.community.delegate.authorization.Authorization.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.community.delegate.authorization.AuthorizationSoapBindingStub _stub = new org.astrogrid.community.delegate.authorization.AuthorizationSoapBindingStub(new java.net.URL(Authorization_address), this);
                _stub.setPortName(getAuthorizationWSDDServiceName());
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
        if ("Authorization".equals(inputPortName)) {
            return getAuthorization();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/axis/Authorization.jws", "AuthorizationService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("Authorization"));
        }
        return ports.iterator();
    }

}
