/**
 * ManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.store.delegate.myspaceItn05;

public class ManagerServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.store.delegate.myspaceItn05.ManagerService {

    // Use to get a proxy class for AstrogridMyspace
    private final java.lang.String AstrogridMyspace_address = "http://grendel12.roe.ac.uk:8080/astrogrid-myspace";

    public java.lang.String getAstrogridMyspaceAddress() {
        return AstrogridMyspace_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AstrogridMyspaceWSDDServiceName = "AstrogridMyspace";

    public java.lang.String getAstrogridMyspaceWSDDServiceName() {
        return AstrogridMyspaceWSDDServiceName;
    }

    public void setAstrogridMyspaceWSDDServiceName(java.lang.String name) {
        AstrogridMyspaceWSDDServiceName = name;
    }

    public org.astrogrid.store.delegate.myspaceItn05.Manager getAstrogridMyspace() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AstrogridMyspace_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAstrogridMyspace(endpoint);
    }

    public org.astrogrid.store.delegate.myspaceItn05.Manager getAstrogridMyspace(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.store.delegate.myspaceItn05.AstrogridMyspaceSoapBindingStub _stub = new org.astrogrid.store.delegate.myspaceItn05.AstrogridMyspaceSoapBindingStub(portAddress, this);
            _stub.setPortName(getAstrogridMyspaceWSDDServiceName());
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
            if (org.astrogrid.store.delegate.myspaceItn05.Manager.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.store.delegate.myspaceItn05.AstrogridMyspaceSoapBindingStub _stub = new org.astrogrid.store.delegate.myspaceItn05.AstrogridMyspaceSoapBindingStub(new java.net.URL(AstrogridMyspace_address), this);
                _stub.setPortName(getAstrogridMyspaceWSDDServiceName());
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
        if ("AstrogridMyspace".equals(inputPortName)) {
            return getAstrogridMyspace();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:Kernel", "ManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("AstrogridMyspace"));
        }
        return ports.iterator();
    }

}
