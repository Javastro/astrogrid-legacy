/**
 * AladinServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.aladinimage;

public class AladinServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.datacenter.cdsdelegate.aladinimage.AladinService {

    // Use to get a proxy class for AladinImage
    private final java.lang.String AladinImage_address = "http://cdsws.u-strasbg.fr/axis/services/AladinImage";

    public java.lang.String getAladinImageAddress() {
        return AladinImage_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AladinImageWSDDServiceName = "AladinImage";

    public java.lang.String getAladinImageWSDDServiceName() {
        return AladinImageWSDDServiceName;
    }

    public void setAladinImageWSDDServiceName(java.lang.String name) {
        AladinImageWSDDServiceName = name;
    }

    public org.astrogrid.datacenter.cdsdelegate.aladinimage.AladinImage getAladinImage() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AladinImage_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAladinImage(endpoint);
    }

    public org.astrogrid.datacenter.cdsdelegate.aladinimage.AladinImage getAladinImage(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.datacenter.cdsdelegate.aladinimage.AladinImageSoapBindingStub _stub = new org.astrogrid.datacenter.cdsdelegate.aladinimage.AladinImageSoapBindingStub(portAddress, this);
            _stub.setPortName(getAladinImageWSDDServiceName());
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
            if (org.astrogrid.datacenter.cdsdelegate.aladinimage.AladinImage.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.datacenter.cdsdelegate.aladinimage.AladinImageSoapBindingStub _stub = new org.astrogrid.datacenter.cdsdelegate.aladinimage.AladinImageSoapBindingStub(new java.net.URL(AladinImage_address), this);
                _stub.setPortName(getAladinImageWSDDServiceName());
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
        if ("AladinImage".equals(inputPortName)) {
            return getAladinImage();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:AladinImage", "AladinService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("AladinImage"));
        }
        return ports.iterator();
    }

}
