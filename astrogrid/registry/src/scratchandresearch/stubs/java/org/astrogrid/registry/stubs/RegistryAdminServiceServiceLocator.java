/**
 * RegistryAdminServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.stubs;

public class RegistryAdminServiceServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.registry.stubs.RegistryAdminServiceService {

    // Use to get a proxy class for RegistryAdminService
    private final java.lang.String RegistryAdminService_address = "http://msslxy.mssl.ucl.ac.uk:8080/axis/services/RegistryAdminService";

    public java.lang.String getRegistryAdminServiceAddress() {
        return RegistryAdminService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegistryAdminServiceWSDDServiceName = "RegistryAdminService";

    public java.lang.String getRegistryAdminServiceWSDDServiceName() {
        return RegistryAdminServiceWSDDServiceName;
    }

    public void setRegistryAdminServiceWSDDServiceName(java.lang.String name) {
        RegistryAdminServiceWSDDServiceName = name;
    }

    public org.astrogrid.registry.stubs.RegistryAdminService getRegistryAdminService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegistryAdminService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegistryAdminService(endpoint);
    }

    public org.astrogrid.registry.stubs.RegistryAdminService getRegistryAdminService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.registry.stubs.RegistryAdminServiceSoapBindingStub _stub = new org.astrogrid.registry.stubs.RegistryAdminServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getRegistryAdminServiceWSDDServiceName());
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
            if (org.astrogrid.registry.stubs.RegistryAdminService.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.registry.stubs.RegistryAdminServiceSoapBindingStub _stub = new org.astrogrid.registry.stubs.RegistryAdminServiceSoapBindingStub(new java.net.URL(RegistryAdminService_address), this);
                _stub.setPortName(getRegistryAdminServiceWSDDServiceName());
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
        if ("RegistryAdminService".equals(inputPortName)) {
            return getRegistryAdminService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://msslxy.mssl.ucl.ac.uk:8080/axis/services/RegistryAdminService", "RegistryAdminServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("RegistryAdminService"));
        }
        return ports.iterator();
    }

}
