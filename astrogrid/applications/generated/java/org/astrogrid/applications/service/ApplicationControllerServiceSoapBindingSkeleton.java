/**
 * ApplicationControllerServiceSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service;

public class ApplicationControllerServiceSoapBindingSkeleton implements org.astrogrid.applications.service.ApplicationController, org.apache.axis.wsdl.Skeleton {
    private org.astrogrid.applications.service.ApplicationController impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();
    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
        };
        _oper = new org.apache.axis.description.OperationDesc("listApplications", _params, new javax.xml.namespace.QName("", "listApplicationsReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:manager.applications.astrogrid.org", "ArrayOf_xsd_string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:manager.applications.astrogrid.org", "listApplications"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listApplications") == null) {
            _myOperations.put("listApplications", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listApplications")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getApplicationDescription", _params, new javax.xml.namespace.QName("", "getApplicationDescriptionReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:beans.applications.astrogrid.org", "SimpleApplicationDescription"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:manager.applications.astrogrid.org", "getApplicationDescription"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("getApplicationDescription") == null) {
            _myOperations.put("getApplicationDescription", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getApplicationDescription")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "applicationID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "jobstepID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "jobMonitorURL"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "user"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:beans.applications.astrogrid.org", "User"), org.astrogrid.applications.delegate.beans.User.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "parameters"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:beans.applications.astrogrid.org", "ParameterValues"), org.astrogrid.applications.delegate.beans.ParameterValues.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("initializeApplication", _params, new javax.xml.namespace.QName("", "initializeApplicationReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:manager.applications.astrogrid.org", "initializeApplication"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("initializeApplication") == null) {
            _myOperations.put("initializeApplication", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("initializeApplication")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "executionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("executeApplication", _params, new javax.xml.namespace.QName("", "executeApplicationReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:manager.applications.astrogrid.org", "executeApplication"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("executeApplication") == null) {
            _myOperations.put("executeApplication", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("executeApplication")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "executionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("queryApplicationExecutionStatus", _params, new javax.xml.namespace.QName("", "queryApplicationExecutionStatusReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:manager.applications.astrogrid.org", "queryApplicationExecutionStatus"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("queryApplicationExecutionStatus") == null) {
            _myOperations.put("queryApplicationExecutionStatus", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("queryApplicationExecutionStatus")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
        };
        _oper = new org.apache.axis.description.OperationDesc("returnRegistryEntry", _params, new javax.xml.namespace.QName("", "returnRegistryEntryReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:manager.applications.astrogrid.org", "returnRegistryEntry"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("returnRegistryEntry") == null) {
            _myOperations.put("returnRegistryEntry", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("returnRegistryEntry")).add(_oper);
    }

    public ApplicationControllerServiceSoapBindingSkeleton() {
        this.impl = new org.astrogrid.applications.service.ApplicationControllerServiceSoapBindingImpl();
    }

    public ApplicationControllerServiceSoapBindingSkeleton(org.astrogrid.applications.service.ApplicationController impl) {
        this.impl = impl;
    }
    public java.lang.String[] listApplications() throws java.rmi.RemoteException
    {
        java.lang.String[] ret = impl.listApplications();
        return ret;
    }

    public org.astrogrid.applications.delegate.beans.SimpleApplicationDescription getApplicationDescription(java.lang.String applicationID) throws java.rmi.RemoteException
    {
        org.astrogrid.applications.delegate.beans.SimpleApplicationDescription ret = impl.getApplicationDescription(applicationID);
        return ret;
    }

    public int initializeApplication(java.lang.String applicationID, java.lang.String jobstepID, java.lang.String jobMonitorURL, org.astrogrid.applications.delegate.beans.User user, org.astrogrid.applications.delegate.beans.ParameterValues parameters) throws java.rmi.RemoteException
    {
        int ret = impl.initializeApplication(applicationID, jobstepID, jobMonitorURL, user, parameters);
        return ret;
    }

    public boolean executeApplication(int executionId) throws java.rmi.RemoteException
    {
        boolean ret = impl.executeApplication(executionId);
        return ret;
    }

    public java.lang.String queryApplicationExecutionStatus(int executionId) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.queryApplicationExecutionStatus(executionId);
        return ret;
    }

    public java.lang.String returnRegistryEntry() throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.returnRegistryEntry();
        return ret;
    }

}
