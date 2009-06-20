/**
 * VSOiBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public class VSOiBindingStub extends org.apache.axis.client.Stub implements org.astrogrid.stapsearch.ws.vso.VSOiPort {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[2];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Query");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "body"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "QueryRequest"), org.astrogrid.stapsearch.ws.vso.QueryRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "VSOQueryResponse"));
        oper.setReturnClass(org.astrogrid.stapsearch.ws.vso.ProviderQueryResponse[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "body"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetData");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "body"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "VSOGetDataRequest"), org.astrogrid.stapsearch.ws.vso.VSOGetDataRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "VSOGetDataResponse"));
        oper.setReturnClass(org.astrogrid.stapsearch.ws.vso.ProviderGetDataResponse[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "body"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

    }

    public VSOiBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public VSOiBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public VSOiBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Data");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.Data.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "DataArray");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.Data[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Data");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "DataRequest");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.DataRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "DataRequestArray");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.DataRequest[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "DataRequest");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Extent");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.Extent.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Extra");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.Extra.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "GetDataRequest");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.GetDataRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Info");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.Info.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "ProviderGetDataResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.ProviderGetDataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "ProviderQueryResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.ProviderQueryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "QueryRequest");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.QueryRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "QueryRequestBlock");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.QueryRequestBlock.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "QueryResponseBlock");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.QueryResponseBlock.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "QueryResponseBlockArray");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.QueryResponseBlock[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "QueryResponseBlock");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "StringArray");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Thumbnail");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.Thumbnail.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Time");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.Time.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "VSOGetDataRequest");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.VSOGetDataRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "VSOGetDataResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.ProviderGetDataResponse[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "ProviderGetDataResponse");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "VSOQueryResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.ProviderQueryResponse[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "ProviderQueryResponse");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Wave");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.stapsearch.ws.vso.Wave.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
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
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public org.astrogrid.stapsearch.ws.vso.ProviderQueryResponse[] query(org.astrogrid.stapsearch.ws.vso.QueryRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://virtualsolar.org/VSO/VSOi_strict#Query");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Query"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.stapsearch.ws.vso.ProviderQueryResponse[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.stapsearch.ws.vso.ProviderQueryResponse[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.stapsearch.ws.vso.ProviderQueryResponse[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.astrogrid.stapsearch.ws.vso.ProviderGetDataResponse[] getData(org.astrogrid.stapsearch.ws.vso.VSOGetDataRequest body) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://virtualsolar.org/VSO/VSOi_strict#GetData");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "GetData"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.stapsearch.ws.vso.ProviderGetDataResponse[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.stapsearch.ws.vso.ProviderGetDataResponse[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.stapsearch.ws.vso.ProviderGetDataResponse[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
