/**
 * SkyNodeSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class SkyNodeSoapStub extends org.apache.axis.client.Stub implements net.ivoa.SkyNode.SkyNodeSoap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[11];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PerformQuery");
        oper.addParameter(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Select"), new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType"), net.ivoa.www.xml.ADQL.v0_7_4.SelectType.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("SkyNode.ivoa.net", "format"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "VOData"));
        oper.setReturnClass(net.ivoa.SkyNode.VOData.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "PerformQueryResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ExecutePlan");
        oper.addParameter(new javax.xml.namespace.QName("SkyNode.ivoa.net", "plan"), new javax.xml.namespace.QName("SkyNode.ivoa.net", "ExecPlan"), net.ivoa.SkyNode.ExecPlan.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "VOData"));
        oper.setReturnClass(net.ivoa.SkyNode.VOData.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ExecutePlanResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Column");
        oper.addParameter(new javax.xml.namespace.QName("SkyNode.ivoa.net", "table"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("SkyNode.ivoa.net", "column"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "MetaColumn"));
        oper.setReturnClass(net.ivoa.SkyNode.MetaColumn.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ColumnResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Columns");
        oper.addParameter(new javax.xml.namespace.QName("SkyNode.ivoa.net", "table"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfMetaColumn"));
        oper.setReturnClass(net.ivoa.SkyNode.ArrayOfMetaColumn.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ColumnsResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Footprint");
        oper.addParameter(new javax.xml.namespace.QName("SkyNode.ivoa.net", "region"), new javax.xml.namespace.QName("urn:nvo-region", "regionType"), nvo_region.RegionType.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:nvo-region", "regionType"));
        oper.setReturnClass(nvo_region.RegionType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "FootprintResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Formats");
        oper.setReturnType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfString"));
        oper.setReturnClass(net.ivoa.SkyNode.ArrayOfString.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "FormatsResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Functions");
        oper.setReturnType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfMetaFunction"));
        oper.setReturnClass(net.ivoa.SkyNode.ArrayOfMetaFunction.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "FunctionsResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetAvailability");
        oper.setReturnType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Availability"));
        oper.setReturnClass(net.ivoa.SkyNode.Availability.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "GetAvailabilityResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("QueryCost");
        oper.addParameter(new javax.xml.namespace.QName("SkyNode.ivoa.net", "planid"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Select"), new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType"), net.ivoa.www.xml.ADQL.v0_7_4.SelectType.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        oper.setReturnClass(float.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "QueryCostResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Table");
        oper.addParameter(new javax.xml.namespace.QName("SkyNode.ivoa.net", "table"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "MetaTable"));
        oper.setReturnClass(net.ivoa.SkyNode.MetaTable.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "TableResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[9] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Tables");
        oper.setReturnType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfMetaTable"));
        oper.setReturnClass(net.ivoa.SkyNode.ArrayOfMetaTable.class);
        oper.setReturnQName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "TablesResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[10] = oper;

    }

    public SkyNodeSoapStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public SkyNodeSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public SkyNodeSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("urn:nvo-coords", "angleUnitType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.AngleUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "unionType");
            cachedSerQNames.add(qName);
            cls = nvo_region.UnionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "LINK");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.LINK.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeValueType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.CoordTimeValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "ArrayOfDouble");
            cachedSerQNames.add(qName);
            cls = nvo_coords.ArrayOfDouble.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "velTimeUnitType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.VelTimeUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "ellipseType");
            cachedSerQNames.add(qName);
            cls = nvo_region.EllipseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">FormatsResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._FormatsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionOptionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.SelectionOptionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">GetAvailabilityResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._GetAvailabilityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.SelectType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "intersectionSearchType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.IntersectionSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordSpectralValueType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.CoordSpectralValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "MIN");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.MIN.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "DataSetData");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.DataSetData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordsType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.CoordsType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "convexHullType");
            cachedSerQNames.add(qName);
            cls = nvo_region.ConvexHullType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coord3ValueType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.Coord3ValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.OrderType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.CoordTimeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "fromTableType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.FromTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "Availability");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.Availability.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "atomType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.AtomType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "notLikePredType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.NotLikePredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unionSearchType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.UnionSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "DataSetData>DataSet");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.DataSetData_DataSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeReferenceUnit");
            cachedSerQNames.add(qName);
            cls = nvo_coords.AstronTimeTypeReferenceUnit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "xMatchType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.XMatchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "VOTABLE");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.VOTABLE.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "StringData");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.StringData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "binaryOperatorType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.BinaryOperatorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">ColumnsResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._ColumnsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "realType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.RealType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeReferenceTime_base");
            cachedSerQNames.add(qName);
            cls = nvo_coords.AstronTimeTypeReferenceTime_base.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "VOTABLEVersion");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.VOTABLEVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unaryExprType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.UnaryExprType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "inverseSearchType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.InverseSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "RESOURCE");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.RESOURCE.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "regionSearchType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.RegionSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "TD");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.TD.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">Column");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._Column.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "scalarExpressionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.ScalarExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aggregateFunctionNameType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.AggregateFunctionNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "tableType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.TableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "circleType");
            cachedSerQNames.add(qName);
            cls = nvo_region.CircleType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "PARAMref");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.PARAMref.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "groupByType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.GroupByType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coord2ValueType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.Coord2ValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderDirectionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.OrderDirectionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "anyTEXT");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.AnyTEXT.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "closedExprType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.ClosedExprType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "regionType");
            cachedSerQNames.add(qName);
            cls = nvo_region.RegionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionListType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.SelectionListType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "COOSYS");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.COOSYS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "TABLEDATA");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.TABLEDATA.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "searchType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.SearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeReference");
            cachedSerQNames.add(qName);
            cls = nvo_coords.AstronTimeTypeReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfMetaTable");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.ArrayOfMetaTable.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "archiveTableType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.ArchiveTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderExpressionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.OrderExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "ExecPlan");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.ExecPlan.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "includeTableType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.IncludeTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "functionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.FunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">TableResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._TableResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "velCoordType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.VelCoordType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderOptionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.OrderOptionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "fromType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.FromType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">Columns");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._Columns.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "velScalarType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.VelScalarType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "xMatchTableAliasType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.XMatchTableAliasType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "MetaTable");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.MetaTable.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "VOData");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.VOData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "LINKContentrole");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.LINKContentrole.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "GROUP");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.GROUP.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "ArrayOfChoice1");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.ArrayOfChoice1.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">ExecutePlan");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._ExecutePlan.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coord2SizeType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.Coord2SizeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "PlanElement");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.PlanElement.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "STREAMActuate");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.STREAMActuate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "allSelectionItemType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.AllSelectionItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "binaryExprType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.BinaryExprType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "FITS");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.FITS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">TablesResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._TablesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "constraintType");
            cachedSerQNames.add(qName);
            cls = nvo_region.ConstraintType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "polygonType");
            cachedSerQNames.add(qName);
            cls = nvo_region.PolygonType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "notBetweenPredType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.NotBetweenPredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "RESOURCEType");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.RESOURCEType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionLimitType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.SelectionLimitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "allOrDistinctType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.AllOrDistinctType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "vertexType");
            cachedSerQNames.add(qName);
            cls = nvo_region.VertexType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "yesno");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.Yesno.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "spectralUnitType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.SpectralUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "encodingType");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.EncodingType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "COOSYSSystem");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.COOSYSSystem.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfMetaColumn");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.ArrayOfMetaColumn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "pos2VectorType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.Pos2VectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "dropTableType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.DropTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "PARAM");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.PARAM.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">GetAvailability");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._GetAvailability.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "VOTableData");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.VOTableData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "STREAM");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.STREAM.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfString");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.ArrayOfString.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aliasSelectionItemType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.AliasSelectionItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionItemType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.SelectionItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coord3SizeType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.Coord3SizeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "DATA");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.DATA.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">Tables");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._Tables.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">Functions");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._Functions.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "Parameter");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.Parameter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unaryOperatorType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.UnaryOperatorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "FIELDref");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.FIELDref.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "STREAMType");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.STREAMType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "betweenPredType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.BetweenPredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "columnReferenceType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.ColumnReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">QueryCost");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._QueryCost.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "negationType");
            cachedSerQNames.add(qName);
            cls = nvo_region.NegationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "MAX");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.MAX.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfMetaFunction");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.ArrayOfMetaFunction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "ErrorData");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.ErrorData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "havingType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.HavingType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "literalType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.LiteralType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "dataType");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.DataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "numberType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.NumberType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">ExecutePlanResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._ExecutePlanResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "whereType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.WhereType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "smallCircleType");
            cachedSerQNames.add(qName);
            cls = nvo_region.SmallCircleType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "MetaFunction");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.MetaFunction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">ColumnResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._ColumnResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "shapeType");
            cachedSerQNames.add(qName);
            cls = nvo_region.ShapeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "comparisonType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.ComparisonType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "closedSearchType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.ClosedSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "integerType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.IntegerType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">Formats");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._Formats.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "intersectionType");
            cachedSerQNames.add(qName);
            cls = nvo_region.IntersectionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "VALUESType");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.VALUESType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "MetaColumn");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.MetaColumn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "vel3VectorType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.Vel3VectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "BINARY");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.BINARY.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfParameter");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.ArrayOfParameter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "posCoordType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.PosCoordType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "vel2VectorType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.Vel2VectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "trigonometricFunctionNameType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.TrigonometricFunctionNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "TABLE");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.TABLE.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">Footprint");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._Footprint.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "mathFunctionNameType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.MathFunctionNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "comparisonPredType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.ComparisonPredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">QueryCostResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._QueryCostResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">FootprintResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._FootprintResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordSpectralType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.CoordSpectralType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "VALUES");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.VALUES.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "OPTION");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.OPTION.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "timeScaleType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.TimeScaleType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "sectorType");
            cachedSerQNames.add(qName);
            cls = nvo_region.SectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordValueType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.CoordValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "INFO");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.INFO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "TR");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.TR.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aggregateFunctionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.AggregateFunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeRelativeTime");
            cachedSerQNames.add(qName);
            cls = nvo_coords.AstronTimeTypeRelativeTime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "mathFunctionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.MathFunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "trigonometricFunctionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.TrigonometricFunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.AstronTimeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">Table");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._Table.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfPlanElement");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode.ArrayOfPlanElement.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "pos3VectorType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.Pos3VectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("SkyNode.ivoa.net", ">FunctionsResponse");
            cachedSerQNames.add(qName);
            cls = net.ivoa.SkyNode._FunctionsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "likePredType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.LikePredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "posScalarType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.PosScalarType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "stringType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.ADQL.v0_7_4.StringType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeRelativeTimeUnit");
            cachedSerQNames.add(qName);
            cls = nvo_coords.AstronTimeTypeRelativeTimeUnit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "posUnitType");
            cachedSerQNames.add(qName);
            cls = nvo_coords.PosUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "FIELD");
            cachedSerQNames.add(qName);
            cls = fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.FIELD.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "convexType");
            cachedSerQNames.add(qName);
            cls = nvo_region.ConvexType.class;
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

    public net.ivoa.SkyNode.VOData performQuery(net.ivoa.www.xml.ADQL.v0_7_4.SelectType select, java.lang.String format) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/PerformQuery");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "PerformQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {select, format});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (net.ivoa.SkyNode.VOData) _resp;
            } catch (java.lang.Exception _exception) {
                return (net.ivoa.SkyNode.VOData) org.apache.axis.utils.JavaUtils.convert(_resp, net.ivoa.SkyNode.VOData.class);
            }
        }
    }

    public net.ivoa.SkyNode.VOData executePlan(net.ivoa.SkyNode.ExecPlan plan) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/ExecutePlan");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ExecutePlan"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {plan});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (net.ivoa.SkyNode.VOData) _resp;
            } catch (java.lang.Exception _exception) {
                return (net.ivoa.SkyNode.VOData) org.apache.axis.utils.JavaUtils.convert(_resp, net.ivoa.SkyNode.VOData.class);
            }
        }
    }

    public net.ivoa.SkyNode.MetaColumn column(java.lang.String table, java.lang.String column) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/Column");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Column"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {table, column});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (net.ivoa.SkyNode.MetaColumn) _resp;
            } catch (java.lang.Exception _exception) {
                return (net.ivoa.SkyNode.MetaColumn) org.apache.axis.utils.JavaUtils.convert(_resp, net.ivoa.SkyNode.MetaColumn.class);
            }
        }
    }

    public net.ivoa.SkyNode.ArrayOfMetaColumn columns(java.lang.String table) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/Columns");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Columns"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {table});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (net.ivoa.SkyNode.ArrayOfMetaColumn) _resp;
            } catch (java.lang.Exception _exception) {
                return (net.ivoa.SkyNode.ArrayOfMetaColumn) org.apache.axis.utils.JavaUtils.convert(_resp, net.ivoa.SkyNode.ArrayOfMetaColumn.class);
            }
        }
    }

    public nvo_region.RegionType footprint(nvo_region.RegionType region) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/Footprint");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Footprint"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {region});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (nvo_region.RegionType) _resp;
            } catch (java.lang.Exception _exception) {
                return (nvo_region.RegionType) org.apache.axis.utils.JavaUtils.convert(_resp, nvo_region.RegionType.class);
            }
        }
    }

    public net.ivoa.SkyNode.ArrayOfString formats() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/Formats");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Formats"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (net.ivoa.SkyNode.ArrayOfString) _resp;
            } catch (java.lang.Exception _exception) {
                return (net.ivoa.SkyNode.ArrayOfString) org.apache.axis.utils.JavaUtils.convert(_resp, net.ivoa.SkyNode.ArrayOfString.class);
            }
        }
    }

    public net.ivoa.SkyNode.ArrayOfMetaFunction functions() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/Functions");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Functions"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (net.ivoa.SkyNode.ArrayOfMetaFunction) _resp;
            } catch (java.lang.Exception _exception) {
                return (net.ivoa.SkyNode.ArrayOfMetaFunction) org.apache.axis.utils.JavaUtils.convert(_resp, net.ivoa.SkyNode.ArrayOfMetaFunction.class);
            }
        }
    }

    public net.ivoa.SkyNode.Availability getAvailability() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/GetAvailability");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "GetAvailability"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (net.ivoa.SkyNode.Availability) _resp;
            } catch (java.lang.Exception _exception) {
                return (net.ivoa.SkyNode.Availability) org.apache.axis.utils.JavaUtils.convert(_resp, net.ivoa.SkyNode.Availability.class);
            }
        }
    }

    public float queryCost(long planid, net.ivoa.www.xml.ADQL.v0_7_4.SelectType select) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/QueryCost");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "QueryCost"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Long(planid), select});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Float) _resp).floatValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Float) org.apache.axis.utils.JavaUtils.convert(_resp, float.class)).floatValue();
            }
        }
    }

    public net.ivoa.SkyNode.MetaTable table(java.lang.String table) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/Table");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Table"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {table});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (net.ivoa.SkyNode.MetaTable) _resp;
            } catch (java.lang.Exception _exception) {
                return (net.ivoa.SkyNode.MetaTable) org.apache.axis.utils.JavaUtils.convert(_resp, net.ivoa.SkyNode.MetaTable.class);
            }
        }
    }

    public net.ivoa.SkyNode.ArrayOfMetaTable tables() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("SkyNode.ivoa.net/Tables");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Tables"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (net.ivoa.SkyNode.ArrayOfMetaTable) _resp;
            } catch (java.lang.Exception _exception) {
                return (net.ivoa.SkyNode.ArrayOfMetaTable) org.apache.axis.utils.JavaUtils.convert(_resp, net.ivoa.SkyNode.ArrayOfMetaTable.class);
            }
        }
    }

}
