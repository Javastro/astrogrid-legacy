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
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), org.astrogrid.datacenter.axisdataserver.types.Query.class, org.apache.axis.description.ParameterDesc.IN, false, false);
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
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), org.astrogrid.datacenter.axisdataserver.types.Query.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getQueryReturn"));
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
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query"), org.astrogrid.datacenter.axisdataserver.types.Query.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "assignedId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getQueryReturn"));
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
        oper.addParameter(new javax.xml.namespace.QName("", "myspaceUrl"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("startQuery");
        oper.addParameter(new javax.xml.namespace.QName("", "id"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getResultsAndClose");
        oper.addParameter(new javax.xml.namespace.QName("", "queryId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getResultsAndCloseReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1", "fault"),
                      "org.xml.sax.SAXException",
                      new javax.xml.namespace.QName("http://sax.xml.org", "SAXException"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("abortQuery");
        oper.addParameter(new javax.xml.namespace.QName("", "queryId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getStatus");
        oper.addParameter(new javax.xml.namespace.QName("", "queryId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getStatusReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("registerWebListener");
        oper.addParameter(new javax.xml.namespace.QName("", "queryId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "url"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1", "fault"),
                      "java.net.MalformedURLException",
                      new javax.xml.namespace.QName("http://net.java", "MalformedURLException"), 
                      true
                     ));
        _operations[9] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("registerJobMonitor");
        oper.addParameter(new javax.xml.namespace.QName("", "queryId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "url"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1", "fault"),
                      "java.net.MalformedURLException",
                      new javax.xml.namespace.QName("http://net.java", "MalformedURLException"), 
                      true
                     ));
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
            javax.xml.namespace.QName qName;            java.lang.Class castorsf = org.apache.axis.encoding.ser.castor.CastorSerializerFactory.class;            java.lang.Class castordf = org.apache.axis.encoding.ser.castor.CastorDeserializerFactory.class;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Function");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Function.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "InverseSearch");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.InverseSearch.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "LikePred");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.LikePred.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "UnaryExpr");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.UnaryExpr.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "UnionSearch");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.UnionSearch.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "IntersectionSearch");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.IntersectionSearch.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ColumnReference");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ColumnReference.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "SingleColumnReference");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.SingleColumnReference.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ClosedSearch");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ClosedSearch.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Circle");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Circle.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Where");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Where.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "OrderOption");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.OrderOption.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ComparisonPred");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ComparisonPred.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "AllOrDistinct");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.types.AllOrDistinct.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "OrderExpression");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.OrderExpression.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "DistinctColumnFunction");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.DistinctColumnFunction.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getResultsAndClose");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.GetResultsAndClose.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ClosedExpr");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ClosedExpr.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQueryResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.MakeQueryResponse.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "BetweenPred");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.BetweenPred.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "registerJobMonitor");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.RegisterJobMonitor.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "MutipleColumnsFunction");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.MutipleColumnsFunction.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "From");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.From.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQuery");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.MakeQuery.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Area");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Area.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "PredicateSearch");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.PredicateSearch.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "AllExpressionsFunction");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.AllExpressionsFunction.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ColumnExpr");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ColumnExpr.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ApproxNum");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ApproxNum.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "setResultsDestination");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.SetResultsDestination.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ArrayOfTable");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ArrayOfTable.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "TableExpression");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.TableExpression.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ExpressionFunction");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ExpressionFunction.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "FunctionExpr");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.FunctionExpr.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getMetadataResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.GetMetadataResponse.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "AggregateFunction");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.types.AggregateFunction.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://astrogrid.org", "AstroGridException");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.AstroGridException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ArrayOfString");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ArrayOfString.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Predicate");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Predicate.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "registerWebListener");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.RegisterWebListener.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "doQuery");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.DoQuery.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Atom");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Atom.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Having");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Having.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "AllColumnReference");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.AllColumnReference.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ArchiveTable");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ArchiveTable.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://query.datacenter.astrogrid.org", "QueryException");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.query.QueryException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "StringLiteral");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.StringLiteral.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "BinaryExpr");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.BinaryExpr.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Search");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Search.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "makeQueryWithId");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.MakeQueryWithId.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://net.java", "MalformedURLException");
            cachedSerQNames.add(qName);
            cls = java.net.MalformedURLException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "startQuery");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.StartQuery.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://io.java", "IOException");
            cachedSerQNames.add(qName);
            cls = java.io.IOException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Selection");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Selection.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Comparison");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.types.Comparison.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "UnaryOperator");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.types.UnaryOperator.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Number");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Number.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ArrayOfOrder");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ArrayOfOrder.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "SelectionAll");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.SelectionAll.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "OrderDirection");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.types.OrderDirection.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getStatusResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.GetStatusResponse.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "AtomExpr");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.AtomExpr.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Table");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Table.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "doQueryResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.DoQueryResponse.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "GroupBy");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.GroupBy.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "abortQuery");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.AbortQuery.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Select");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Select.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getStatus");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.GetStatus.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Literal");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Literal.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "IntNum");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.IntNum.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "Order");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.Order.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "BinaryOperator");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.types.BinaryOperator.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "SelectionList");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.SelectionList.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "getResultsAndCloseResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.GetResultsAndCloseResponse.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "query");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.axisdataserver.types.Query.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "SelectionOption");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.SelectionOption.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://sax.xml.org", "SAXException");
            cachedSerQNames.add(qName);
            cls = org.xml.sax.SAXException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "NumberLiteral");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.NumberLiteral.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
            qName = new javax.xml.namespace.QName("http://tempuri.org/adql", "ScalarExpression");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.datacenter.adql.generated.ScalarExpression.class;
            cachedSerClasses.add(cls);
    cachedSerFactories.add(castorsf);    cachedDeserFactories.add(castordf);     
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

    public java.lang.String doQuery(java.lang.String resultsFormat, org.astrogrid.datacenter.axisdataserver.types.Query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
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

    public java.lang.String makeQuery(org.astrogrid.datacenter.axisdataserver.types.Query query) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
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
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public java.lang.String makeQueryWithId(org.astrogrid.datacenter.axisdataserver.types.Query query, java.lang.String assignedId) throws java.rmi.RemoteException, java.io.IOException, org.astrogrid.datacenter.query.QueryException, org.xml.sax.SAXException {
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
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public void setResultsDestination(java.lang.String myspaceUrl) throws java.rmi.RemoteException {
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
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {myspaceUrl});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void startQuery(java.lang.String id) throws java.rmi.RemoteException {
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
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {id});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public java.lang.String getResultsAndClose(java.lang.String queryId) throws java.rmi.RemoteException, org.xml.sax.SAXException {
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

    public void abortQuery(java.lang.String queryId) throws java.rmi.RemoteException {
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

    public java.lang.String getStatus(java.lang.String queryId) throws java.rmi.RemoteException {
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

    public void registerWebListener(java.lang.String queryId, java.lang.String url) throws java.rmi.RemoteException, java.net.MalformedURLException {
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

    public void registerJobMonitor(java.lang.String queryId, java.lang.String url) throws java.rmi.RemoteException, java.net.MalformedURLException {
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
