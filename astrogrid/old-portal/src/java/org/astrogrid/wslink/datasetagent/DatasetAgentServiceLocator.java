/**
 * DatasetAgentServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.wslink.datasetagent;

public class DatasetAgentServiceLocator extends org.apache.axis.client.Service implements org.astrogrid.wslink.datasetagent.DatasetAgentService {

    // Use to get a proxy class for DatasetAgent
    private final java.lang.String DatasetAgent_address = "http://hydra.star.le.ac.uk:8080/axis/services/DatasetAgent";

    public java.lang.String getDatasetAgentAddress() {
        return DatasetAgent_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DatasetAgentWSDDServiceName = "DatasetAgent";

    public java.lang.String getDatasetAgentWSDDServiceName() {
        return DatasetAgentWSDDServiceName;
    }

    public void setDatasetAgentWSDDServiceName(java.lang.String name) {
        DatasetAgentWSDDServiceName = name;
    }

    public org.astrogrid.wslink.datasetagent.DatasetAgent getDatasetAgent() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DatasetAgent_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDatasetAgent(endpoint);
    }

    public org.astrogrid.wslink.datasetagent.DatasetAgent getDatasetAgent(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.astrogrid.wslink.datasetagent.DatasetAgentSoapBindingStub _stub = new org.astrogrid.wslink.datasetagent.DatasetAgentSoapBindingStub(portAddress, this);
            _stub.setPortName(getDatasetAgentWSDDServiceName());
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
            if (org.astrogrid.wslink.datasetagent.DatasetAgent.class.isAssignableFrom(serviceEndpointInterface)) {
                org.astrogrid.wslink.datasetagent.DatasetAgentSoapBindingStub _stub = new org.astrogrid.wslink.datasetagent.DatasetAgentSoapBindingStub(new java.net.URL(DatasetAgent_address), this);
                _stub.setPortName(getDatasetAgentWSDDServiceName());
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
        if ("DatasetAgent".equals(inputPortName)) {
            return getDatasetAgent();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:org.astrogrid.datasetagent", "DatasetAgentService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("DatasetAgent"));
        }
        return ports.iterator();
    }

}
