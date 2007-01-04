/**
 * JobControllerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.delegate.v1.jobcontroller;

public class JobControllerServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerService {

    // Use to get a proxy class for JobControllerService
    private final java.lang.String JobControllerService_address = "http://localhost:8080/axis/services/JobControllerService";

    public java.lang.String getJobControllerServiceAddress() {
        return JobControllerService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String JobControllerServiceWSDDServiceName = "JobControllerService";

    public java.lang.String getJobControllerServiceWSDDServiceName() {
        return JobControllerServiceWSDDServiceName;
    }

    public void setJobControllerServiceWSDDServiceName(java.lang.String name) {
        JobControllerServiceWSDDServiceName = name;
    }

    public org.astrogrid.jes.delegate.v1.jobcontroller.JobController getJobControllerService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(JobControllerService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getJobControllerService(endpoint);
    }

    public org.astrogrid.jes.delegate.v1.jobcontroller.JobController getJobControllerService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceSoapBindingStub _stub = new org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getJobControllerServiceWSDDServiceName());
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
            if (org.astrogrid.jes.delegate.v1.jobcontroller.JobController.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceSoapBindingStub _stub = new org.astrogrid.jes.delegate.v1.jobcontroller.JobControllerServiceSoapBindingStub(new java.net.URL(JobControllerService_address), this);
                _stub.setPortName(getJobControllerServiceWSDDServiceName());
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
        if ("JobControllerService".equals(inputPortName)) {
            return getJobControllerService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:jes/jobcontroller/v1", "JobControllerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("JobControllerService"));
        }
        return ports.iterator();
    }

}
