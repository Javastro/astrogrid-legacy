/**
 * AxisDataServerSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver;

public class AxisDataServerSoapBindingSkeleton implements org.astrogrid.datacenter.axisdataserver.AxisDataServer, org.apache.axis.wsdl.Skeleton {
    private org.astrogrid.datacenter.axisdataserver.AxisDataServer impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getMetadata"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"), java.lang.Object.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getMetadata", _params, new javax.xml.namespace.QName("", "getMetadataReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getMetadata"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getMetadata") == null) {
            _myOperations.put("getMetadata", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getMetadata")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getLanguageInfo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"), java.lang.Object.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getLanguageInfo", _params, new javax.xml.namespace.QName("", "language"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "Language"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getLanguageInfo"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getLanguageInfo") == null) {
            _myOperations.put("getLanguageInfo", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getLanguageInfo")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "resultsFormat"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "query"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "Query"), org.astrogrid.datacenter.axisdataserver.types.Query.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("doQuery", _params, new javax.xml.namespace.QName("", "doQueryReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "doQuery"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("doQuery") == null) {
            _myOperations.put("doQuery", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("doQuery")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("IOException");
        _fault.setQName(new javax.xml.namespace.QName("", "fault"));
        _fault.setClassName("java.io.IOException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://io.java", "IOException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("QueryException");
        _fault.setQName(new javax.xml.namespace.QName("", "fault"));
        _fault.setClassName("org.astrogrid.datacenter.query.QueryException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://query.datacenter.astrogrid.org", "QueryException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("SAXException");
        _fault.setQName(new javax.xml.namespace.QName("", "fault"));
        _fault.setClassName("org.xml.sax.SAXException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://sax.xml.org", "SAXException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "query"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "Query"), org.astrogrid.datacenter.axisdataserver.types.Query.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("makeQuery", _params, new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQuery"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("makeQuery") == null) {
            _myOperations.put("makeQuery", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("makeQuery")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("IOException");
        _fault.setQName(new javax.xml.namespace.QName("", "fault"));
        _fault.setClassName("java.io.IOException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://io.java", "IOException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("QueryException");
        _fault.setQName(new javax.xml.namespace.QName("", "fault"));
        _fault.setClassName("org.astrogrid.datacenter.query.QueryException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://query.datacenter.astrogrid.org", "QueryException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("SAXException");
        _fault.setQName(new javax.xml.namespace.QName("", "fault"));
        _fault.setClassName("org.xml.sax.SAXException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://sax.xml.org", "SAXException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "query"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "Query"), org.astrogrid.datacenter.axisdataserver.types.Query.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "assignedId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("makeQueryWithId", _params, new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQueryWithId"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("makeQueryWithId") == null) {
            _myOperations.put("makeQueryWithId", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("makeQueryWithId")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("IOException");
        _fault.setQName(new javax.xml.namespace.QName("", "fault"));
        _fault.setClassName("java.io.IOException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://io.java", "IOException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("QueryException");
        _fault.setQName(new javax.xml.namespace.QName("", "fault"));
        _fault.setClassName("org.astrogrid.datacenter.query.QueryException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://query.datacenter.astrogrid.org", "QueryException"));
        _oper.addFault(_fault);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("SAXException");
        _fault.setQName(new javax.xml.namespace.QName("", "fault"));
        _fault.setClassName("org.xml.sax.SAXException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://sax.xml.org", "SAXException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "myspaceUrl"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"), org.apache.axis.types.URI.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("setResultsDestination", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "setResultsDestination"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("setResultsDestination") == null) {
            _myOperations.put("setResultsDestination", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("setResultsDestination")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("startQuery", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "startQuery"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("startQuery") == null) {
            _myOperations.put("startQuery", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("startQuery")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getResultsAndClose", _params, new javax.xml.namespace.QName("", "getResultsAndCloseReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getResultsAndClose"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getResultsAndClose") == null) {
            _myOperations.put("getResultsAndClose", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getResultsAndClose")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("abortQuery", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "abortQuery"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("abortQuery") == null) {
            _myOperations.put("abortQuery", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("abortQuery")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("getStatus", _params, new javax.xml.namespace.QName("", "getStatusReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getStatus"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("getStatus") == null) {
            _myOperations.put("getStatus", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("getStatus")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "url"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"), org.apache.axis.types.URI.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("registerWebListener", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "registerWebListener"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("registerWebListener") == null) {
            _myOperations.put("registerWebListener", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("registerWebListener")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "url"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"), org.apache.axis.types.URI.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("registerJobMonitor", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "registerJobMonitor"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("registerJobMonitor") == null) {
            _myOperations.put("registerJobMonitor", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("registerJobMonitor")).add(_oper);
    }

    public AxisDataServerSoapBindingSkeleton() {
        this.impl = new org.astrogrid.datacenter.axisdataserver.AxisDataServerSoapBindingImpl();
    }

    public AxisDataServerSoapBindingSkeleton(org.astrogrid.datacenter.axisdataserver.AxisDataServer impl) {
        this.impl = impl;
    }
    public java.lang.String getMetadata(java.lang.Object parameters) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getMetadata(parameters);
        return ret;
    }

    public org.astrogrid.datacenter.axisdataserver.types.Language[] getLanguageInfo(java.lang.Object parameters) throws java.rmi.RemoteException
    {
        org.astrogrid.datacenter.axisdataserver.types.Language[] ret = impl.getLanguageInfo(parameters);
        return ret;
    }

    public java.lang.String doQuery(java.lang.String resultsFormat, org.astrogrid.datacenter.axisdataserver.types.Query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException
    {
        java.lang.String ret = impl.doQuery(resultsFormat, query);
        return ret;
    }

    public java.lang.String makeQuery(org.astrogrid.datacenter.axisdataserver.types.Query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException
    {
        java.lang.String ret = impl.makeQuery(query);
        return ret;
    }

    public java.lang.String makeQueryWithId(org.astrogrid.datacenter.axisdataserver.types.Query query, java.lang.String assignedId) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException
    {
        java.lang.String ret = impl.makeQueryWithId(query, assignedId);
        return ret;
    }

    public void setResultsDestination(java.lang.String queryId, org.apache.axis.types.URI myspaceUrl) throws java.rmi.RemoteException
    {
        impl.setResultsDestination(queryId, myspaceUrl);
    }

    public void startQuery(java.lang.String queryId) throws java.rmi.RemoteException
    {
        impl.startQuery(queryId);
    }

    public java.lang.String getResultsAndClose(java.lang.String queryId) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getResultsAndClose(queryId);
        return ret;
    }

    public void abortQuery(java.lang.String queryId) throws java.rmi.RemoteException
    {
        impl.abortQuery(queryId);
    }

    public java.lang.String getStatus(java.lang.String queryId) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.getStatus(queryId);
        return ret;
    }

    public void registerWebListener(java.lang.String queryId, org.apache.axis.types.URI url) throws java.rmi.RemoteException
    {
        impl.registerWebListener(queryId, url);
    }

    public void registerJobMonitor(java.lang.String queryId, org.apache.axis.types.URI url) throws java.rmi.RemoteException
    {
        impl.registerJobMonitor(queryId, url);
    }

}
