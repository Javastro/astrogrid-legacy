/**
 * SesameServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.sesame;

public class SesameServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.datacenter.cdsdelegate.sesame.SesameService {

    // Use to get a proxy class for Sesame
    private final java.lang.String Sesame_address = "http://cdsws.u-strasbg.fr/axis/services/Sesame";

    public java.lang.String getSesameAddress() {
        return Sesame_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SesameWSDDServiceName = "Sesame";

    public java.lang.String getSesameWSDDServiceName() {
        return SesameWSDDServiceName;
    }

    public void setSesameWSDDServiceName(java.lang.String name) {
        SesameWSDDServiceName = name;
    }

    public org.astrogrid.datacenter.cdsdelegate.sesame.Sesame getSesame() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Sesame_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSesame(endpoint);
    }

    public org.astrogrid.datacenter.cdsdelegate.sesame.Sesame getSesame(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.datacenter.cdsdelegate.sesame.SesameSoapBindingStub _stub = new org.astrogrid.datacenter.cdsdelegate.sesame.SesameSoapBindingStub(portAddress, this);
            _stub.setPortName(getSesameWSDDServiceName());
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
            if (org.astrogrid.datacenter.cdsdelegate.sesame.Sesame.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.datacenter.cdsdelegate.sesame.SesameSoapBindingStub _stub = new org.astrogrid.datacenter.cdsdelegate.sesame.SesameSoapBindingStub(new java.net.URL(Sesame_address), this);
                _stub.setPortName(getSesameWSDDServiceName());
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
        if ("Sesame".equals(inputPortName)) {
            return getSesame();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:Sesame", "SesameService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("Sesame"));
        }
        return ports.iterator();
    }

}
