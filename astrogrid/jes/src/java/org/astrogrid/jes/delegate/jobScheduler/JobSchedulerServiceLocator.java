/**
 * JobSchedulerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.delegate.jobScheduler;

public class JobSchedulerServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.jes.delegate.jobScheduler.JobSchedulerService {

    // Use to get a proxy class for JobSchedulerService
    private final java.lang.String JobSchedulerService_address = "http://localhost:8080/axis/services/JobSchedulerService";

    public java.lang.String getJobSchedulerServiceAddress() {
        return JobSchedulerService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String JobSchedulerServiceWSDDServiceName = "JobSchedulerService";

    public java.lang.String getJobSchedulerServiceWSDDServiceName() {
        return JobSchedulerServiceWSDDServiceName;
    }

    public void setJobSchedulerServiceWSDDServiceName(java.lang.String name) {
        JobSchedulerServiceWSDDServiceName = name;
    }

    public org.astrogrid.jes.delegate.jobScheduler.JobScheduler getJobSchedulerService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(JobSchedulerService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getJobSchedulerService(endpoint);
    }

    public org.astrogrid.jes.delegate.jobScheduler.JobScheduler getJobSchedulerService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.jes.delegate.jobScheduler.JobSchedulerServiceSoapBindingStub _stub = new org.astrogrid.jes.delegate.jobScheduler.JobSchedulerServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getJobSchedulerServiceWSDDServiceName());
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
            if (org.astrogrid.jes.delegate.jobScheduler.JobScheduler.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.jes.delegate.jobScheduler.JobSchedulerServiceSoapBindingStub _stub = new org.astrogrid.jes.delegate.jobScheduler.JobSchedulerServiceSoapBindingStub(new java.net.URL(JobSchedulerService_address), this);
                _stub.setPortName(getJobSchedulerServiceWSDDServiceName());
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
        if ("JobSchedulerService".equals(inputPortName)) {
            return getJobSchedulerService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("jobScheduler.delegate.jes.astrogrid.org", "JobSchedulerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("JobSchedulerService"));
        }
        return ports.iterator();
    }

}
