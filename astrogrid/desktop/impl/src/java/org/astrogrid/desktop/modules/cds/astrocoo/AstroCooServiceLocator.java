/**
 * AstroCooServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.cds.astrocoo;

public class AstroCooServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.desktop.modules.cds.astrocoo.AstroCooService {

    // Use to get a proxy class for AstroCoo
    private final java.lang.String AstroCoo_address = "http://cdsws.u-strasbg.fr/axis/services/AstroCoo";

    public java.lang.String getAstroCooAddress() {
        return AstroCoo_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AstroCooWSDDServiceName = "AstroCoo";

    public java.lang.String getAstroCooWSDDServiceName() {
        return AstroCooWSDDServiceName;
    }

    public void setAstroCooWSDDServiceName(java.lang.String name) {
        AstroCooWSDDServiceName = name;
    }

    public org.astrogrid.desktop.modules.cds.astrocoo.AstroCoo getAstroCoo() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AstroCoo_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAstroCoo(endpoint);
    }

    public org.astrogrid.desktop.modules.cds.astrocoo.AstroCoo getAstroCoo(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.desktop.modules.cds.astrocoo.AstroCooSoapBindingStub _stub = new org.astrogrid.desktop.modules.cds.astrocoo.AstroCooSoapBindingStub(portAddress, this);
            _stub.setPortName(getAstroCooWSDDServiceName());
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
            if (org.astrogrid.desktop.modules.cds.astrocoo.AstroCoo.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.desktop.modules.cds.astrocoo.AstroCooSoapBindingStub _stub = new org.astrogrid.desktop.modules.cds.astrocoo.AstroCooSoapBindingStub(new java.net.URL(AstroCoo_address), this);
                _stub.setPortName(getAstroCooWSDDServiceName());
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
        if ("AstroCoo".equals(inputPortName)) {
            return getAstroCoo();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:AstroCoo", "AstroCooService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("AstroCoo"));
        }
        return ports.iterator();
    }

}
