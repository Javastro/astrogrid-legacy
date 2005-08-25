/**
 * UCDServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.cds.ucd;

public class UCDServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.desktop.modules.cds.ucd.UCDService {

    // Use to get a proxy class for UCD
    private final java.lang.String UCD_address = "http://cdsws.u-strasbg.fr/axis/services/UCD";

    public java.lang.String getUCDAddress() {
        return UCD_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UCDWSDDServiceName = "UCD";

    public java.lang.String getUCDWSDDServiceName() {
        return UCDWSDDServiceName;
    }

    public void setUCDWSDDServiceName(java.lang.String name) {
        UCDWSDDServiceName = name;
    }

    public org.astrogrid.desktop.modules.cds.ucd.UCD getUCD() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UCD_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUCD(endpoint);
    }

    public org.astrogrid.desktop.modules.cds.ucd.UCD getUCD(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.desktop.modules.cds.ucd.UCDSoapBindingStub _stub = new org.astrogrid.desktop.modules.cds.ucd.UCDSoapBindingStub(portAddress, this);
            _stub.setPortName(getUCDWSDDServiceName());
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
            if (org.astrogrid.desktop.modules.cds.ucd.UCD.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.desktop.modules.cds.ucd.UCDSoapBindingStub _stub = new org.astrogrid.desktop.modules.cds.ucd.UCDSoapBindingStub(new java.net.URL(UCD_address), this);
                _stub.setPortName(getUCDWSDDServiceName());
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
        if ("UCD".equals(inputPortName)) {
            return getUCD();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:UCD", "UCDService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("UCD"));
        }
        return ports.iterator();
    }

}
