/**
 * MsComServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public class MsComServicesLocator extends org.apache.axis.client.Service implements com.microsoft.www.MsComServices {

    // <Table><TR><TD><IMG src='ws.gif' /></TD><TD><B>Microsoft.Com Platform
    // WebServices </B> provide opportunities to syndicate Microsoft Content,
    // Products Catalog, Downloads  and Communities.</TD></TR></Table>

    // Use to get a proxy class for MsComServicesSoap
    private final java.lang.String MsComServicesSoap_address = "http://ws.microsoft.com/mscomservice/mscom.asmx";

    public java.lang.String getMsComServicesSoapAddress() {
        return MsComServicesSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MsComServicesSoapWSDDServiceName = "MsComServicesSoap";

    public java.lang.String getMsComServicesSoapWSDDServiceName() {
        return MsComServicesSoapWSDDServiceName;
    }

    public void setMsComServicesSoapWSDDServiceName(java.lang.String name) {
        MsComServicesSoapWSDDServiceName = name;
    }

    public com.microsoft.www.MsComServicesSoap getMsComServicesSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MsComServicesSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMsComServicesSoap(endpoint);
    }

    public com.microsoft.www.MsComServicesSoap getMsComServicesSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.microsoft.www.MsComServicesSoapStub _stub = new com.microsoft.www.MsComServicesSoapStub(portAddress, this);
            _stub.setPortName(getMsComServicesSoapWSDDServiceName());
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
            if (com.microsoft.www.MsComServicesSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.microsoft.www.MsComServicesSoapStub _stub = new com.microsoft.www.MsComServicesSoapStub(new java.net.URL(MsComServicesSoap_address), this);
                _stub.setPortName(getMsComServicesSoapWSDDServiceName());
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
        if ("MsComServicesSoap".equals(inputPortName)) {
            return getMsComServicesSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.microsoft.com", "MsComServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("MsComServicesSoap"));
        }
        return ports.iterator();
    }

}
