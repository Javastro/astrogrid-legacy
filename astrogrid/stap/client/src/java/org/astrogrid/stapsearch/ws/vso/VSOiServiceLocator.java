/**
 * VSOiServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public class VSOiServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.stapsearch.ws.vso.VSOiService {

    public VSOiServiceLocator() {
    }


    public VSOiServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public VSOiServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for sdacVSOi
    private java.lang.String sdacVSOi_address = "http://vso.nascom.nasa.gov/cgi/VSOi_strict.temp";

    public java.lang.String getsdacVSOiAddress() {
        return sdacVSOi_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String sdacVSOiWSDDServiceName = "sdacVSOi";

    public java.lang.String getsdacVSOiWSDDServiceName() {
        return sdacVSOiWSDDServiceName;
    }

    public void setsdacVSOiWSDDServiceName(java.lang.String name) {
        sdacVSOiWSDDServiceName = name;
    }

    public org.astrogrid.stapsearch.ws.vso.VSOiPort getsdacVSOi() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(sdacVSOi_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getsdacVSOi(endpoint);
    }

    public org.astrogrid.stapsearch.ws.vso.VSOiPort getsdacVSOi(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.stapsearch.ws.vso.VSOiBindingStub _stub = new org.astrogrid.stapsearch.ws.vso.VSOiBindingStub(portAddress, this);
            _stub.setPortName(getsdacVSOiWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setsdacVSOiEndpointAddress(java.lang.String address) {
        sdacVSOi_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.astrogrid.stapsearch.ws.vso.VSOiPort.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.stapsearch.ws.vso.VSOiBindingStub _stub = new org.astrogrid.stapsearch.ws.vso.VSOiBindingStub(new java.net.URL(sdacVSOi_address), this);
                _stub.setPortName(getsdacVSOiWSDDServiceName());
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
        java.lang.String inputPortName = portName.getLocalPart();
        if ("sdacVSOi".equals(inputPortName)) {
            return getsdacVSOi();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "VSOiService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "sdacVSOi"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("sdacVSOi".equals(portName)) {
            setsdacVSOiEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
