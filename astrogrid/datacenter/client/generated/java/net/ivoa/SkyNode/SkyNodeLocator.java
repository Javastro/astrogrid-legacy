/**
 * SkyNodeLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class SkyNodeLocator extends org.apache.axis.client.Service implements net.ivoa.SkyNode.SkyNode {

    // Interface for basic and full SkyNodes.

    // Use to get a proxy class for SkyNodeSoap
    private final java.lang.String SkyNodeSoap_address = "http://openskyquery.net/nodes/sdss/nodeb.asmx";

    public java.lang.String getSkyNodeSoapAddress() {
        return SkyNodeSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SkyNodeSoapWSDDServiceName = "SkyNodeSoap";

    public java.lang.String getSkyNodeSoapWSDDServiceName() {
        return SkyNodeSoapWSDDServiceName;
    }

    public void setSkyNodeSoapWSDDServiceName(java.lang.String name) {
        SkyNodeSoapWSDDServiceName = name;
    }

    public net.ivoa.SkyNode.SkyNodeSoap getSkyNodeSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SkyNodeSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSkyNodeSoap(endpoint);
    }

    public net.ivoa.SkyNode.SkyNodeSoap getSkyNodeSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            net.ivoa.SkyNode.SkyNodeSoapStub _stub = new net.ivoa.SkyNode.SkyNodeSoapStub(portAddress, this);
            _stub.setPortName(getSkyNodeSoapWSDDServiceName());
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
            if (net.ivoa.SkyNode.SkyNodeSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                net.ivoa.SkyNode.SkyNodeSoapStub _stub = new net.ivoa.SkyNode.SkyNodeSoapStub(new java.net.URL(SkyNodeSoap_address), this);
                _stub.setPortName(getSkyNodeSoapWSDDServiceName());
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
        if ("SkyNodeSoap".equals(inputPortName)) {
            return getSkyNodeSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("SkyNode.ivoa.net", "SkyNode");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("SkyNodeSoap"));
        }
        return ports.iterator();
    }

}
