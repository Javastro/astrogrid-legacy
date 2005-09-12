/**
 * ADQLTranslatorSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class ADQLTranslatorSoapStub extends org.apache.axis.client.Stub implements org.astrogrid.desktop.modules.ivoa.adql.ADQLTranslatorSoap {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[4];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SQLtoADQL");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "sql"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType"));
        oper.setReturnClass(org.astrogrid.desktop.modules.ivoa.adql.SelectType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Select"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SQLtoADQLString");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "sql"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "SQLtoADQLStringResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ADQLtoSQL");
        oper.addParameter(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Select"), new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType"), org.astrogrid.desktop.modules.ivoa.adql.SelectType.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "ADQLtoSQLResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ADQLStringtoSQL");
        oper.addParameter(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "adql"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "ADQLStringtoSQLResult"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[3] = oper;

    }

    public ADQLTranslatorSoapStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ADQLTranslatorSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ADQLTranslatorSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("urn:nvo-coords", "velTimeUnitType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.VelTimeUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeReference");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AstronTimeTypeReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "regionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.RegionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "intoType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.IntoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderOptionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.OrderOptionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coord2SizeType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.Coord2SizeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unionSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.UnionSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "fromTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.FromTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "jointTableQualifierType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.JointTableQualifierType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "allSelectionItemType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AllSelectionItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aliasSelectionItemType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AliasSelectionItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "vertexType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.VertexType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "unionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.UnionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "intersectionSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.IntersectionSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "scalarExpressionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderDirectionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.OrderDirectionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "realType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.RealType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "constraintType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ConstraintType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionOptionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SelectionOptionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionItemType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SelectionItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "comparisonType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ComparisonType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "betweenPredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.BetweenPredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "notBetweenPredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.NotBetweenPredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "circleType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.CircleType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "angleUnitType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AngleUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "posUnitType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.PosUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "sectorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "allOrDistinctType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AllOrDistinctType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "notLikePredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.NotLikePredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "exclusiveSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ExclusiveSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coord3SizeType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.Coord3SizeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "binaryExprType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.BinaryExprType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "likePredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.LikePredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeReferenceUnit");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AstronTimeTypeReferenceUnit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SelectType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "tableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.TableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordsType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.CoordsType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "ConstantListSet");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ConstantListSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "convexHullType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ConvexHullType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "posScalarType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.PosScalarType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "columnReferenceType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ColumnReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "velCoordType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.VelCoordType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "groupByType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.GroupByType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "closedSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ClosedSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aggregateFunctionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AggregateFunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionListType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SelectionListType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "ellipseType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.EllipseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unaryOperatorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.UnaryOperatorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeRelativeTimeUnit");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AstronTimeTypeRelativeTimeUnit.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "xMatchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.XMatchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "dropTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.DropTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "ArrayOfFromTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ArrayOfFromTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "fromType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.FromType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "mathFunctionNameType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.MathFunctionNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coord3ValueType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.Coord3ValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "subQuerySet");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SubQuerySet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionLimitType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SelectionLimitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "searchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "polygonType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.PolygonType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "vel2VectorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.Vel2VectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "joinTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.JoinTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "ArrayOfDouble");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ArrayOfDouble.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "shapeType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ShapeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "timeScaleType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.TimeScaleType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coord2ValueType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.Coord2ValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "havingType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.HavingType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "mathFunctionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.MathFunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeRelativeTime");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AstronTimeTypeRelativeTime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "literalType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.LiteralType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "inclusiveSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.InclusiveSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeValueType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unaryExprType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.UnaryExprType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AstronTimeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "integerType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.IntegerType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "stringType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.StringType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "includeTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.IncludeTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "archiveTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ArchiveTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "velScalarType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.VelScalarType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "pos3VectorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.Pos3VectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordSpectralValueType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.CoordSpectralValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "pos2VectorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.Pos2VectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "functionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.FunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "negationType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.NegationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "closedExprType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ClosedExprType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderExpressionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.OrderExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "trigonometricFunctionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.TrigonometricFunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "spectralUnitType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SpectralUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "smallCircleType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.SmallCircleType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "binaryOperatorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.BinaryOperatorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "vel3VectorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.Vel3VectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "xMatchTableAliasType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.XMatchTableAliasType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.CoordTimeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "regionSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.RegionSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "atomType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AtomType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "intersectionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.IntersectionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordSpectralType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.CoordSpectralType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "coordValueType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.CoordValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeReferenceTime_base");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AstronTimeTypeReferenceTime_base.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "convexType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ConvexType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "numberType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.NumberType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "whereType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.WhereType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.OrderType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "inclusionSetType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.InclusionSetType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aggregateFunctionNameType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.AggregateFunctionNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "inverseSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.InverseSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "trigonometricFunctionNameType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.TrigonometricFunctionNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "posCoordType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.PosCoordType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "comparisonPredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.desktop.modules.ivoa.adql.ComparisonPredType.class;
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

    public org.astrogrid.desktop.modules.ivoa.adql.SelectType SQLtoADQL(java.lang.String sql) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.parser.adql.ivoa.net/SQLtoADQL");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "SQLtoADQL"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sql});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.desktop.modules.ivoa.adql.SelectType) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.desktop.modules.ivoa.adql.SelectType) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.desktop.modules.ivoa.adql.SelectType.class);
            }
        }
    }

    public java.lang.String SQLtoADQLString(java.lang.String sql) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.parser.adql.ivoa.net/SQLtoADQLString");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "SQLtoADQLString"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sql});

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

    public java.lang.String ADQLtoSQL(org.astrogrid.desktop.modules.ivoa.adql.SelectType select) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.parser.adql.ivoa.net/ADQLtoSQL");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "ADQLtoSQL"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {select});

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

    public java.lang.String ADQLStringtoSQL(java.lang.String adql) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://ws.parser.adql.ivoa.net/ADQLStringtoSQL");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://ws.parser.adql.ivoa.net/", "ADQLStringtoSQL"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {adql});

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

}
