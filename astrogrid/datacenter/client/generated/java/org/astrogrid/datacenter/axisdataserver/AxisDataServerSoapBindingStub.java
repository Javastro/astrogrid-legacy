/**
 * AxisDataServerSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver;

public class AxisDataServerSoapBindingStub extends org.apache.axis.client.Stub implements org.astrogrid.datacenter.axisdataserver.AxisDataServer {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[11];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getMetadata");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getMetadata"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"), java.lang.Object.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getMetadataReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("doQuery");
        oper.addParameter(new javax.xml.namespace.QName("", "resultsFormat"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), org.astrogrid.datacenter.axisdataserver.types._query.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "doQueryReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "fault"),
                      "java.io.IOException",
                      new javax.xml.namespace.QName("http://io.java", "IOException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "fault"),
                      "org.astrogrid.datacenter.query.QueryException",
                      new javax.xml.namespace.QName("http://query.datacenter.astrogrid.org", "QueryException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "fault"),
                      "org.xml.sax.SAXException",
                      new javax.xml.namespace.QName("http://sax.xml.org", "SAXException"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("makeQuery");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), org.astrogrid.datacenter.axisdataserver.types._query.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"));
        oper.setReturnClass(org.astrogrid.datacenter.axisdataserver.types._QueryId.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "fault"),
                      "java.io.IOException",
                      new javax.xml.namespace.QName("http://io.java", "IOException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "fault"),
                      "org.astrogrid.datacenter.query.QueryException",
                      new javax.xml.namespace.QName("http://query.datacenter.astrogrid.org", "QueryException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "fault"),
                      "org.xml.sax.SAXException",
                      new javax.xml.namespace.QName("http://sax.xml.org", "SAXException"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("makeQueryWithId");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), org.astrogrid.datacenter.axisdataserver.types._query.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "assignedId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"));
        oper.setReturnClass(org.astrogrid.datacenter.axisdataserver.types._QueryId.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "fault"),
                      "java.io.IOException",
                      new javax.xml.namespace.QName("http://io.java", "IOException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "fault"),
                      "org.astrogrid.datacenter.query.QueryException",
                      new javax.xml.namespace.QName("http://query.datacenter.astrogrid.org", "QueryException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "fault"),
                      "org.xml.sax.SAXException",
                      new javax.xml.namespace.QName("http://sax.xml.org", "SAXException"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setResultsDestination");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.astrogrid.datacenter.axisdataserver.types._QueryId.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "myspaceUrl"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"), org.apache.axis.types.URI.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("startQuery");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.astrogrid.datacenter.axisdataserver.types._QueryId.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getResultsAndClose");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.astrogrid.datacenter.axisdataserver.types._QueryId.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getResultsAndCloseReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("abortQuery");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.astrogrid.datacenter.axisdataserver.types._QueryId.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getStatus");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.astrogrid.datacenter.axisdataserver.types._QueryId.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getStatusReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("registerWebListener");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.astrogrid.datacenter.axisdataserver.types._QueryId.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "url"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"), org.apache.axis.types.URI.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[9] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("registerJobMonitor");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"), org.astrogrid.datacenter.axisdataserver.types._QueryId.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "url"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"), org.apache.axis.types.URI.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[10] = oper;

    }

    public AxisDataServerSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public AxisDataServerSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public AxisDataServerSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "registerWebListener");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._registerWebListener.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getMetadataResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._getMetadataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://sax.xml.org", "SAXException");
            cachedSerQNames.add(qName);
            cls = org.xml.sax.SAXException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "setResultsDestination");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._setResultsDestination.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQueryResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._makeQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://query.datacenter.astrogrid.org", "QueryException");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.query.QueryException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "registerJobMonitor");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._registerJobMonitor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://astrogrid.org", "AstroGridException");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.AstroGridException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getResultsAndCloseResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._getResultsAndCloseResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._query.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "abortQuery");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._abortQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "startQuery");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._startQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "doQueryResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._doQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQuery");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._makeQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "doQuery");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._doQuery.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getResultsAndClose");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._getResultsAndClose.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._QueryId.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://io.java", "IOException");
            cachedSerQNames.add(qName);
            cls = java.io.IOException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getStatusResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._getStatusResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQueryWithId");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._makeQueryWithId.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getStatus");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types._getStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public java.lang.String getMetadata(java.lang.Object parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getMetadata"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public java.lang.String doQuery(java.lang.String resultsFormat, org.astrogrid.datacenter.axisdataserver.types._query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "doQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {resultsFormat, query});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public org.astrogrid.datacenter.axisdataserver.types._QueryId makeQuery(org.astrogrid.datacenter.axisdataserver.types._query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {query});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.datacenter.axisdataserver.types._QueryId) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.datacenter.axisdataserver.types._QueryId) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.datacenter.axisdataserver.types._QueryId.class);
            }
        }
    }

    public org.astrogrid.datacenter.axisdataserver.types._QueryId makeQueryWithId(org.astrogrid.datacenter.axisdataserver.types._query query, java.lang.String assignedId) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQueryWithId"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {query, assignedId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.datacenter.axisdataserver.types._QueryId) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.datacenter.axisdataserver.types._QueryId) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.datacenter.axisdataserver.types._QueryId.class);
            }
        }
    }

    public void setResultsDestination(org.astrogrid.datacenter.axisdataserver.types._QueryId queryId, org.apache.axis.types.URI myspaceUrl) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "setResultsDestination"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryId, myspaceUrl});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void startQuery(org.astrogrid.datacenter.axisdataserver.types._QueryId queryId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "startQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public java.lang.String getResultsAndClose(org.astrogrid.datacenter.axisdataserver.types._QueryId queryId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getResultsAndClose"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public void abortQuery(org.astrogrid.datacenter.axisdataserver.types._QueryId queryId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "abortQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public java.lang.String getStatus(org.astrogrid.datacenter.axisdataserver.types._QueryId queryId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public void registerWebListener(org.astrogrid.datacenter.axisdataserver.types._QueryId queryId, org.apache.axis.types.URI url) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "registerWebListener"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryId, url});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void registerJobMonitor(org.astrogrid.datacenter.axisdataserver.types._QueryId queryId, org.apache.axis.types.URI url) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "registerJobMonitor"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {queryId, url});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

}
