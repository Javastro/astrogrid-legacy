/**
 * ApplicationControllerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.manager;

public class ApplicationControllerServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.applications.manager.ApplicationControllerService {

    // Use to get a proxy class for ApplicationContrllerService
    private final java.lang.String ApplicationContrllerService_address = "http://localhost:8080/axis/services/ApplicationContrllerService";

    public java.lang.String getApplicationContrllerServiceAddress() {
        return ApplicationContrllerService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ApplicationContrllerServiceWSDDServiceName = "ApplicationContrllerService";

    public java.lang.String getApplicationContrllerServiceWSDDServiceName() {
        return ApplicationContrllerServiceWSDDServiceName;
    }

    public void setApplicationContrllerServiceWSDDServiceName(java.lang.String name) {
        ApplicationContrllerServiceWSDDServiceName = name;
    }

    public org.astrogrid.applications.manager.ApplicationController getApplicationContrllerService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ApplicationContrllerService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getApplicationContrllerService(endpoint);
    }

    public org.astrogrid.applications.manager.ApplicationController getApplicationContrllerService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub _stub = new org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getApplicationContrllerServiceWSDDServiceName());
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
            if (org.astrogrid.applications.manager.ApplicationController.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub _stub = new org.astrogrid.applications.manager.ApplicationContrllerServiceSoapBindingStub(new java.net.URL(ApplicationContrllerService_address), this);
                _stub.setPortName(getApplicationContrllerServiceWSDDServiceName());
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
        if ("ApplicationContrllerService".equals(inputPortName)) {
            return getApplicationContrllerService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:manager.applications.astrogrid.org", "ApplicationControllerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("ApplicationContrllerService"));
        }
        return ports.iterator();
    }

}
