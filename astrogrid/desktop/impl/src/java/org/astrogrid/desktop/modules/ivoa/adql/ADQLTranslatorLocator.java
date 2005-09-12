/**
 * ADQLTranslatorLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class ADQLTranslatorLocator extends org.apache.axis.client.Service implements org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslator {

    // Use to get a proxy class for ADQLTranslatorSoap
    private final java.lang.String ADQLTranslatorSoap_address = "http://openskyquery.net/AdqlTranslator/ADQLTrans.asmx";

    public java.lang.String getADQLTranslatorSoapAddress() {
        return ADQLTranslatorSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ADQLTranslatorSoapWSDDServiceName = "ADQLTranslatorSoap";

    public java.lang.String getADQLTranslatorSoapWSDDServiceName() {
        return ADQLTranslatorSoapWSDDServiceName;
    }

    public void setADQLTranslatorSoapWSDDServiceName(java.lang.String name) {
        ADQLTranslatorSoapWSDDServiceName = name;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorSoap getADQLTranslatorSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ADQLTranslatorSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getADQLTranslatorSoap(endpoint);
    }

    public org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorSoap getADQLTranslatorSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorSoapStub _stub = new org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorSoapStub(portAddress, this);
            _stub.setPortName(getADQLTranslatorSoapWSDDServiceName());
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
            if (org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorSoapStub _stub = new org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorSoapStub(new java.net.URL(ADQLTranslatorSoap_address), this);
                _stub.setPortName(getADQLTranslatorSoapWSDDServiceName());
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
        if ("ADQLTranslatorSoap".equals(inputPortName)) {
            return getADQLTranslatorSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "ADQLTranslator");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("ADQLTranslatorSoap"));
        }
        return ports.iterator();
    }

}
