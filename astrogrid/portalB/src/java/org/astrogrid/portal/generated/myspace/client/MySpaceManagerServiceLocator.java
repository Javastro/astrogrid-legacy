/**
 * MySpaceManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.portal.generated.myspace.client;

public class MySpaceManagerServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.portal.generated.myspace.client.MySpaceManagerService {

    // Use to get a proxy class for MySpaceManager
    private final java.lang.String MySpaceManager_address = "http://localhost:8080/axis/services/MySpaceManager";

    public java.lang.String getMySpaceManagerAddress() {
        return MySpaceManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MySpaceManagerWSDDServiceName = "MySpaceManager";

    public java.lang.String getMySpaceManagerWSDDServiceName() {
        return MySpaceManagerWSDDServiceName;
    }

    public void setMySpaceManagerWSDDServiceName(java.lang.String name) {
        MySpaceManagerWSDDServiceName = name;
    }

    public org.astrogrid.portal.generated.myspace.client.MySpaceManager getMySpaceManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MySpaceManager_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getMySpaceManager(endpoint);
    }

    public org.astrogrid.portal.generated.myspace.client.MySpaceManager getMySpaceManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.portal.generated.myspace.client.MySpaceManagerSoapBindingStub _stub = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getMySpaceManagerWSDDServiceName());
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
            if (org.astrogrid.portal.generated.myspace.client.MySpaceManager.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.portal.generated.myspace.client.MySpaceManagerSoapBindingStub _stub = new org.astrogrid.portal.generated.myspace.client.MySpaceManagerSoapBindingStub(new java.net.URL(MySpaceManager_address), this);
                _stub.setPortName(getMySpaceManagerWSDDServiceName());
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
        java.rmi.Remote _stub = getPort(serviceEndpointInterface);
        ((org.apache.axis.client.Stub) _stub).setPortName(portName);
        return _stub;
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("mySpace:MySpaceManager", "MySpaceManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("MySpaceManager"));
        }
        return ports.iterator();
    }

}
