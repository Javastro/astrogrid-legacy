/**
 * WSTestServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.testservice;

public class WSTestServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.applications.testservice.WSTestService {

    // Use to get a proxy class for WSTest
    private final java.lang.String WSTest_address = "http://localhost:8080/axis/services/ResultsListenerService";

    public java.lang.String getWSTestAddress() {
        return WSTest_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WSTestWSDDServiceName = "WSTest";

    public java.lang.String getWSTestWSDDServiceName() {
        return WSTestWSDDServiceName;
    }

    public void setWSTestWSDDServiceName(java.lang.String name) {
        WSTestWSDDServiceName = name;
    }

    public org.astrogrid.applications.testservice.WSTest getWSTest() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WSTest_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWSTest(endpoint);
    }

    public org.astrogrid.applications.testservice.WSTest getWSTest(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.applications.testservice.WSTestSOAPBindingStub _stub = new org.astrogrid.applications.testservice.WSTestSOAPBindingStub(portAddress, this);
            _stub.setPortName(getWSTestWSDDServiceName());
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
            if (org.astrogrid.applications.testservice.WSTest.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.applications.testservice.WSTestSOAPBindingStub _stub = new org.astrogrid.applications.testservice.WSTestSOAPBindingStub(new java.net.URL(WSTest_address), this);
                _stub.setPortName(getWSTestWSDDServiceName());
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
        if ("WSTest".equals(inputPortName)) {
            return getWSTest();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:astrogrid:cea:testwebapp", "WSTestService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("WSTest"));
        }
        return ports.iterator();
    }

}
