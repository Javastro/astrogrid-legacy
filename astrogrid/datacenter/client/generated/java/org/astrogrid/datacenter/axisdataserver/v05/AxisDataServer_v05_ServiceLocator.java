/**
 * AxisDataServer_v05_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver.v05;

public class AxisDataServer_v05_ServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.datacenter.axisdataserver.v05.AxisDataServer_v05_Service {

    // Use to get a proxy class for AxisDataServer_v05
    private final java.lang.String AxisDataServer_v05_address = "http://localhost:8080/axis/AxisDataServer_v05";

    public java.lang.String getAxisDataServer_v05Address() {
        return AxisDataServer_v05_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AxisDataServer_v05WSDDServiceName = "AxisDataServer_v05";

    public java.lang.String getAxisDataServer_v05WSDDServiceName() {
        return AxisDataServer_v05WSDDServiceName;
    }

    public void setAxisDataServer_v05WSDDServiceName(java.lang.String name) {
        AxisDataServer_v05WSDDServiceName = name;
    }

    public org.astrogrid.datacenter.axisdataserver.v05.AxisDataServer_v05_Port getAxisDataServer_v05() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AxisDataServer_v05_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAxisDataServer_v05(endpoint);
    }

    public org.astrogrid.datacenter.axisdataserver.v05.AxisDataServer_v05_Port getAxisDataServer_v05(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.datacenter.axisdataserver.v05.AxisDataServerV05SoapBindingStub _stub = new org.astrogrid.datacenter.axisdataserver.v05.AxisDataServerV05SoapBindingStub(portAddress, this);
            _stub.setPortName(getAxisDataServer_v05WSDDServiceName());
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
            if (org.astrogrid.datacenter.axisdataserver.v05.AxisDataServer_v05_Port.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.datacenter.axisdataserver.v05.AxisDataServerV05SoapBindingStub _stub = new org.astrogrid.datacenter.axisdataserver.v05.AxisDataServerV05SoapBindingStub(new java.net.URL(AxisDataServer_v05_address), this);
                _stub.setPortName(getAxisDataServer_v05WSDDServiceName());
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
        if ("AxisDataServer_v05".equals(inputPortName)) {
            return getAxisDataServer_v05();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://astrogrid.org/datacenter/axisdataserver/v05", "AxisDataServer_v05");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("AxisDataServer_v05"));
        }
        return ports.iterator();
    }

}
