/**
 * VizieRServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.cds.vizier;

public class VizieRServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.desktop.modules.cds.vizier.VizieRService {

    // Use to get a proxy class for VizieR
    private final java.lang.String VizieR_address = "http://cdsws.u-strasbg.fr/axis/services/VizieR";

    public java.lang.String getVizieRAddress() {
        return VizieR_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String VizieRWSDDServiceName = "VizieR";

    public java.lang.String getVizieRWSDDServiceName() {
        return VizieRWSDDServiceName;
    }

    public void setVizieRWSDDServiceName(java.lang.String name) {
        VizieRWSDDServiceName = name;
    }

    public org.astrogrid.desktop.modules.cds.vizier.VizieR getVizieR() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(VizieR_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getVizieR(endpoint);
    }

    public org.astrogrid.desktop.modules.cds.vizier.VizieR getVizieR(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.desktop.modules.cds.vizier.VizieRSoapBindingStub _stub = new org.astrogrid.desktop.modules.cds.vizier.VizieRSoapBindingStub(portAddress, this);
            _stub.setPortName(getVizieRWSDDServiceName());
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
            if (org.astrogrid.desktop.modules.cds.vizier.VizieR.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.desktop.modules.cds.vizier.VizieRSoapBindingStub _stub = new org.astrogrid.desktop.modules.cds.vizier.VizieRSoapBindingStub(new java.net.URL(VizieR_address), this);
                _stub.setPortName(getVizieRWSDDServiceName());
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
        if ("VizieR".equals(inputPortName)) {
            return getVizieR();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:VizieR", "VizieRService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("VizieR"));
        }
        return ports.iterator();
    }

}
