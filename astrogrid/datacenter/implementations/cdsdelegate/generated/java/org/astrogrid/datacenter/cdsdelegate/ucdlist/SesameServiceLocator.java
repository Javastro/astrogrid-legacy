/**
 * SesameServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.ucdlist;

public class SesameServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.datacenter.cdsdelegate.ucdlist.SesameService {

    // Use to get a proxy class for UCDList
    private final java.lang.String UCDList_address = "http://cdsws.u-strasbg.fr/axis/services/UCDList";

    public java.lang.String getUCDListAddress() {
        return UCDList_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UCDListWSDDServiceName = "UCDList";

    public java.lang.String getUCDListWSDDServiceName() {
        return UCDListWSDDServiceName;
    }

    public void setUCDListWSDDServiceName(java.lang.String name) {
        UCDListWSDDServiceName = name;
    }

    public org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDList getUCDList() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UCDList_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUCDList(endpoint);
    }

    public org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDList getUCDList(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDListSoapBindingStub _stub = new org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDListSoapBindingStub(portAddress, this);
            _stub.setPortName(getUCDListWSDDServiceName());
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
            if (org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDList.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDListSoapBindingStub _stub = new org.astrogrid.datacenter.cdsdelegate.ucdlist.UCDListSoapBindingStub(new java.net.URL(UCDList_address), this);
                _stub.setPortName(getUCDListWSDDServiceName());
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
        if ("UCDList".equals(inputPortName)) {
            return getUCDList();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:UCDList", "SesameService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("UCDList"));
        }
        return ports.iterator();
    }

}
