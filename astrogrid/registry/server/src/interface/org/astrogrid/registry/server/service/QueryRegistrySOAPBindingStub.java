/**
 * QueryRegistrySOAPBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.service;

public class QueryRegistrySOAPBindingStub extends org.apache.axis.client.Stub implements org.astrogrid.registry.server.service.QueryRegistryPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[3];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Search");
        oper.addParameter(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Select"), new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType"), org.astrogrid.registry.server.generated.adql.SelectType.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.ivoa.net/schemas/services/QueryRegistry/wsdl", ">VOResources"));
        oper.setReturnClass(org.astrogrid.registry.server.service._VOResources.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.ivoa.net/schemas/services/QueryRegistry/wsdl", "VOResources"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("KeywordSearch");
        oper.addParameter(new javax.xml.namespace.QName("", "keywords"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "orValue"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.ivoa.net/schemas/services/QueryRegistry/wsdl", ">VOResources"));
        oper.setReturnClass(org.astrogrid.registry.server.service._VOResources.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.ivoa.net/schemas/services/QueryRegistry/wsdl", "VOResources"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetRegistries");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.ivoa.net/schemas/services/QueryRegistry/wsdl", ">VOResources"));
        oper.setReturnClass(org.astrogrid.registry.server.service._VOResources.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.ivoa.net/schemas/services/QueryRegistry/wsdl", "VOResources"));
        oper.setStyle(org.apache.axis.enum.Style.DOCUMENT);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[2] = oper;

    }

    public QueryRegistrySOAPBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public QueryRegistrySOAPBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public QueryRegistrySOAPBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionListType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.SelectionListType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "double2Type");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_coords.Double2Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "polygonType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.PolygonType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "FormatType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.FormatType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "whereType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.WhereType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CoordRangeType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.CoordRangeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "dropTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.DropTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionItemType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.SelectionItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CoordRangeType>long");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.CoordRangeType_long.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "trigonometricFunctionNameType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.TrigonometricFunctionNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unaryExprType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.UnaryExprType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unaryOperatorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.UnaryOperatorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CoordFrameType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.CoordFrameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "CapabilityType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.CapabilityType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "sectorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.SectorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aliasSelectionItemType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.AliasSelectionItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "regionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.RegionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderExpressionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.OrderExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "RightsType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.RightsType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "intersectionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.IntersectionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderOptionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.OrderOptionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOCommunity/v0.2", "OrganisationType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.vocommunity.OrganisationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "notLikePredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.NotLikePredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "RelatedResourceType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.RelatedResourceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "includeTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.IncludeTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "TableType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.TableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "angleUnitType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_coords.AngleUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceType>status");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.ResourceType_status.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "stringType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.StringType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "xMatchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.XMatchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "negationType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.NegationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "havingType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.HavingType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "IdentifierType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.IdentifierType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "unionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.UnionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "convexType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.ConvexType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "CreatorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.CreatorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "positionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.PositionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "shapeType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.ShapeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceReferenceType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.ResourceReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "literalType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.LiteralType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "binaryExprType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.BinaryExprType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SkyServiceType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.SkyServiceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.ResourceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "InterfaceType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.InterfaceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "scalarExpressionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.ScalarExpressionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "tableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.TableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/schemas/services/QueryRegistry/wsdl", ">VOResources");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.service._VOResources.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "TabularSkyServiceType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.TabularSkyServiceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "WavebandType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.WavebandType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "RegionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.RegionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "WavelengthRangeType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.WavelengthRangeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "allSelectionItemType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.AllSelectionItemType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SpectralType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.SpectralType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "vertexType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.VertexType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "intersectionSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.IntersectionSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "likePredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.LikePredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AuthorityIDType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.AuthorityIDType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "searchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.SearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionOptionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.SelectionOptionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "ellipseType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.EllipseType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "categoryType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.CategoryType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "CurationType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.CurationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "closedSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.ClosedSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AccessURLType>use");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.AccessURLType_use.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.OrderType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "double3Type");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_coords.Double3Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ServiceType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.ServiceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "constraintType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.ConstraintType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "posUnitType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_coords.PosUnitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionLimitType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.SelectionLimitType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "TemporalType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.TemporalType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "comparisonType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.ComparisonType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "convexHullType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.ConvexHullType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "realType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.RealType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "comparisonPredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.ComparisonPredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "fromType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.FromType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "DataCollectionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.DataCollectionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aggregateFunctionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.AggregateFunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CircleRegionType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.CircleRegionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "SummaryType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.SummaryType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "SourceType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.SourceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "archiveTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.ArchiveTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "mathFunctionNameType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.MathFunctionNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "inverseSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.InverseSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VORegistry/v0.2", "AuthorityType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.voregistry.AuthorityType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "numberType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.NumberType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "trigonometricFunctionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.TrigonometricFunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "notBetweenPredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.NotBetweenPredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "ParamType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.ParamType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "fromTableType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.FromTableType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "RelationshipType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.RelationshipType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "InvocationType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.InvocationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceKeyType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.ResourceKeyType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "regionSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.RegionSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "integerType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.IntegerType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "mathFunctionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.MathFunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "DateType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.DateType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CoordRangeType>lat");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.CoordRangeType_lat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "xMatchTableAliasType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.XMatchTableAliasType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "size3Type");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_coords.Size3Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "LogicalIdentifierType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.LogicalIdentifierType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SpatialType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.SpatialType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "NameReferenceType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.NameReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-coords", "size2Type");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_coords.Size2Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "closedExprType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.ClosedExprType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "columnReferenceType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.ColumnReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CoverageType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.CoverageType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", ">DataType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4._DataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "smallCircleType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.SmallCircleType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "circleType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.CircleType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unionSearchType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.UnionSearchType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:nvo-region", "regionType>fill_factor");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.nvo_region.RegionType_fill_factor.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ContentLevelType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.ContentLevelType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "betweenPredType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.BetweenPredType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "atomType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.AtomType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "allOrDistinctType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.AllOrDistinctType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "groupByType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.GroupByType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VORegistry/v0.2", "RegistryType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.voregistry.RegistryType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aggregateFunctionNameType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.AggregateFunctionNameType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.SelectType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AccessURLType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.AccessURLType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "AccessType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VODataService.v0_4.AccessType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderDirectionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.OrderDirectionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOTable/v1.0", "dataType");
            cachedSerQNames.add(qName);
            cls = net.ivoa.www.xml.VOTable.v1_0.DataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "binaryOperatorType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.BinaryOperatorType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOCommunity/v0.2", "ProjectType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.vocommunity.ProjectType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "functionType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.adql.FunctionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ContactType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.registry.server.generated.voresource.ContactType.class;
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

    public org.astrogrid.registry.server.service._VOResources search(org.astrogrid.registry.server.generated.adql.SelectType adql) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("Search");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "Search"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {adql});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.registry.server.service._VOResources) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.registry.server.service._VOResources) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.registry.server.service._VOResources.class);
            }
        }
    }

    public org.astrogrid.registry.server.service._VOResources keywordSearch(java.lang.String keywords, boolean orValue) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("KeywordSearch");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "KeywordSearch"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {keywords, new java.lang.Boolean(orValue)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.registry.server.service._VOResources) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.registry.server.service._VOResources) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.registry.server.service._VOResources.class);
            }
        }
    }

    public org.astrogrid.registry.server.service._VOResources getRegistries() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("GetRegistries");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "GetRegistries"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.registry.server.service._VOResources) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.registry.server.service._VOResources) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.registry.server.service._VOResources.class);
            }
        }
    }

}
