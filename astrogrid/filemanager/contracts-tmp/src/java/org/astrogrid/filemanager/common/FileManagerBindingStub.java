/**
 * FileManagerBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.filemanager.common;

public class FileManagerBindingStub extends org.apache.axis.client.Stub implements org.astrogrid.filemanager.common.FileManagerPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[15];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("dummyMessageWorkAroundForAxis");
        oper.addParameter(new javax.xml.namespace.QName("", "ignored"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Ivorn"), org.astrogrid.filemanager.common.Ivorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "ignoredToo"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "TransferInfo"), org.astrogrid.filemanager.common.TransferInfo.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getNode");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "hints"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "BundlePreferences"), org.astrogrid.filemanager.common.BundlePreferences.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Node[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("addAccount");
        oper.addParameter(new javax.xml.namespace.QName("", "ident"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "AccountIdent"), org.astrogrid.filemanager.common.AccountIdent.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "hints"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "BundlePreferences"), org.astrogrid.filemanager.common.BundlePreferences.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Node[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.DuplicateNodeFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAccount");
        oper.addParameter(new javax.xml.namespace.QName("", "ident"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "AccountIdent"), org.astrogrid.filemanager.common.AccountIdent.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "hints"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "BundlePreferences"), org.astrogrid.filemanager.common.BundlePreferences.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Node[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getIdentifier");
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Ivorn"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Ivorn.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "ivorn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("copy");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "newParent"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "newNodeName"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeName"), org.astrogrid.filemanager.common.NodeName.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "newLocation"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"), org.apache.axis.types.URI.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Node[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.DuplicateNodeFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("move");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "newParent"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "newNodeName"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeName"), org.astrogrid.filemanager.common.NodeName.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "newLocation"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"), org.apache.axis.types.URI.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Node[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.DuplicateNodeFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("delete");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Node[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("readContent");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "TransferInfo"));
        oper.setReturnClass(org.astrogrid.filemanager.common.TransferInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "info"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("writeContent");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "TransferInfo"));
        oper.setReturnClass(org.astrogrid.filemanager.common.TransferInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "info"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[9] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("appendContent");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "TransferInfo"));
        oper.setReturnClass(org.astrogrid.filemanager.common.TransferInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "info"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("copyURLToContent");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "url"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"), org.apache.axis.types.URI.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Node[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("copyContentToURL");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "url"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "TransferInfo"), org.astrogrid.filemanager.common.TransferInfo.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("refresh");
        oper.addParameter(new javax.xml.namespace.QName("", "nodeIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "hints"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "BundlePreferences"), org.astrogrid.filemanager.common.BundlePreferences.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Node[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("addNode");
        oper.addParameter(new javax.xml.namespace.QName("", "parentIvorn"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"), org.astrogrid.filemanager.common.NodeIvorn.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "newNodeName"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeName"), org.astrogrid.filemanager.common.NodeName.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "nodeType"), new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeTypes"), org.astrogrid.filemanager.common.NodeTypes.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        oper.setReturnClass(org.astrogrid.filemanager.common.Node[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "node"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.DuplicateNodeFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.FileManagerFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.filemanager.common.NodeNotFoundFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[14] = oper;

    }

    public FileManagerBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public FileManagerBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public FileManagerBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "BundlePreferences");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.BundlePreferences.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "DataLocation");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.DataLocation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "child");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.Child.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Ivorn");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.Ivorn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.Node.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "attribute");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.Attribute.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "TransferInfo");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.TransferInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeName");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.NodeName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeTypes");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.NodeTypes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.NodeIvorn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "AccountIdent");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.filemanager.common.AccountIdent.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

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

    public void dummyMessageWorkAroundForAxis(org.astrogrid.filemanager.common.Ivorn ignored, org.astrogrid.filemanager.common.TransferInfo ignoredToo) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("dummyMessageWorkAroundForAxis");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "dummyMessageWorkAroundForAxis"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {ignored, ignoredToo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public org.astrogrid.filemanager.common.Node[] getNode(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.BundlePreferences hints) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getNode");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "getNode"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn, hints});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Node[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Node[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Node[].class);
            }
        }
    }

    public org.astrogrid.filemanager.common.Node[] addAccount(org.astrogrid.filemanager.common.AccountIdent ident, org.astrogrid.filemanager.common.BundlePreferences hints) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.DuplicateNodeFault, org.astrogrid.filemanager.common.FileManagerFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("addAccount");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "addAccount"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {ident, hints});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Node[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Node[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Node[].class);
            }
        }
    }

    public org.astrogrid.filemanager.common.Node[] getAccount(org.astrogrid.filemanager.common.AccountIdent ident, org.astrogrid.filemanager.common.BundlePreferences hints) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAccount");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "getAccount"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {ident, hints});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Node[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Node[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Node[].class);
            }
        }
    }

    public org.astrogrid.filemanager.common.Ivorn getIdentifier() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getIdentifier");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "getIdentifier"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Ivorn) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Ivorn) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Ivorn.class);
            }
        }
    }

    public org.astrogrid.filemanager.common.Node[] copy(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.NodeIvorn newParent, org.astrogrid.filemanager.common.NodeName newNodeName, org.apache.axis.types.URI newLocation) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.DuplicateNodeFault, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("copy");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "copy"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn, newParent, newNodeName, newLocation});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Node[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Node[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Node[].class);
            }
        }
    }

    public org.astrogrid.filemanager.common.Node[] move(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.NodeIvorn newParent, org.astrogrid.filemanager.common.NodeName newNodeName, org.apache.axis.types.URI newLocation) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.DuplicateNodeFault, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("move");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "move"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn, newParent, newNodeName, newLocation});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Node[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Node[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Node[].class);
            }
        }
    }

    public org.astrogrid.filemanager.common.Node[] delete(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("delete");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "delete"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Node[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Node[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Node[].class);
            }
        }
    }

    public org.astrogrid.filemanager.common.TransferInfo readContent(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("readContent");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "readContent"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.TransferInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.TransferInfo) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.TransferInfo.class);
            }
        }
    }

    public org.astrogrid.filemanager.common.TransferInfo writeContent(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("writeContent");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "writeContent"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.TransferInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.TransferInfo) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.TransferInfo.class);
            }
        }
    }

    public org.astrogrid.filemanager.common.TransferInfo appendContent(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("appendContent");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "appendContent"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.TransferInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.TransferInfo) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.TransferInfo.class);
            }
        }
    }

    public org.astrogrid.filemanager.common.Node[] copyURLToContent(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.apache.axis.types.URI url) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("copyURLToContent");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "copyURLToContent"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn, url});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Node[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Node[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Node[].class);
            }
        }
    }

    public void copyContentToURL(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.TransferInfo url) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("copyContentToURL");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "copyContentToURL"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn, url});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public org.astrogrid.filemanager.common.Node[] refresh(org.astrogrid.filemanager.common.NodeIvorn nodeIvorn, org.astrogrid.filemanager.common.BundlePreferences hints) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("refresh");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "refresh"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {nodeIvorn, hints});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Node[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Node[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Node[].class);
            }
        }
    }

    public org.astrogrid.filemanager.common.Node[] addNode(org.astrogrid.filemanager.common.NodeIvorn parentIvorn, org.astrogrid.filemanager.common.NodeName newNodeName, org.astrogrid.filemanager.common.NodeTypes nodeType) throws java.rmi.RemoteException, org.astrogrid.filemanager.common.DuplicateNodeFault, org.astrogrid.filemanager.common.FileManagerFault, org.astrogrid.filemanager.common.NodeNotFoundFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("addNode");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "addNode"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parentIvorn, newNodeName, nodeType});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.filemanager.common.Node[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.filemanager.common.Node[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.filemanager.common.Node[].class);
            }
        }
    }

}
