/**
 * CommonExecutionConnectorServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service.v1.cea;

public class CommonExecutionConnectorServiceSoapBindingStub extends org.apache.axis.client.Stub implements org.astrogrid.applications.service.v1.cea.CommonExecutionConnector {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[6];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("execute");
        oper.addParameter(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "tool"), new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "tool"), org.astrogrid.workflow.beans.v1.axis._tool.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "jobstepID"), new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "job-identifier-type"), org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "jobMonitorURL"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "executeReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.applications.service.v1.cea.CeaFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("abort");
        oper.addParameter(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "executionId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "abortReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.applications.service.v1.cea.CeaFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listApplications");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationList"));
        oper.setReturnClass(org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationList"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.applications.service.v1.cea.CeaFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getApplicationDescription");
        oper.addParameter(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "applicationID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationBase"));
        oper.setReturnClass(org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "ApplicationDesciption"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("queryExecutionStatus");
        oper.addParameter(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "executionId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "message-type"));
        oper.setReturnClass(org.astrogrid.jes.types.v1.cea.axis.MessageType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "queryExecutionStatusReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("", "message"),
                      "org.astrogrid.applications.service.v1.cea.CeaFault",
                      new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), 
                      false
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("returnRegistryEntry");
        oper.setReturnType(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "returnRegistryEntryResponse>returnRegistryEntryReturn"));
        oper.setReturnClass(org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "returnRegistryEntryReturn"));
        oper.setStyle(org.apache.axis.enum.Style.WRAPPED);
        oper.setUse(org.apache.axis.enum.Use.LITERAL);
        _operations[5] = oper;

    }

    public CommonExecutionConnectorServiceSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public CommonExecutionConnectorServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public CommonExecutionConnectorServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Credentials");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.community.beans.v1.axis.Credentials.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "DataCentreControllerConfig");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase._DataCentreControllerConfig.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "parameterValue");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "WebServiceApplication");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.WebServiceApplication.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "MySpace_VOTableReference");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.MySpace_VOTableReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "BaseIdentifier");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.community.beans.v1.axis.BaseIdentifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "MySpace_FileReference");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.MySpace_FileReference.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "SecurityToken");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.community.beans.v1.axis.SecurityToken.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "CmdLineParameterDefn");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase._CmdLineParameterDefn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "Interface");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase._interface.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "Dec");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.Dec.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "input");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis._input.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "parameterRef");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "execution-phase");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "log-level");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.jes.types.v1.cea.axis.LogLevel.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "tool");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis._tool.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "switchTypes");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.SwitchTypes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "Interface>input");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.Interface_input.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "step");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis._step.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "ADQL");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.ADQL.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "WebServiceExecutionControllerConfig");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase._WebServiceExecutionControllerConfig.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationBase>Parameters");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase_Parameters.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "CommandLineExecutionControllerConfig");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase._CommandLineExecutionControllerConfig.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "AbstractActivity");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis.AbstractActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "jobURN");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.jes.beans.vi.axis.executionrecord.JobURN.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "queryExecutionStatusResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.service.v1.cea.impl._queryExecutionStatusResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "DataCenterParameterDefinition");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.DataCenterParameterDefinition.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "SecurityToken");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.community.beans.v1.axis._SecurityToken.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "java-param");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.JavaParam.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "queryExecutionStatus");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.service.v1.cea.impl._queryExecutionStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "RA");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.RA.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "returnRegistryEntry");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "parameterTypes");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterTypes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "CommandLineParameterDefinition");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.CommandLineParameterDefinition.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "Interface>output");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.Interface_output.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "job-execution-record");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.jes.beans.vi.axis.executionrecord._jobExecutionRecord.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "java-class");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.JavaClass.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "workflow");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis._workflow.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "returnRegistryEntryResponse>returnRegistryEntryReturn");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "output");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis._output.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "returnRegistryEntryResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "CommonExecutionConnectorConfigType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.CommonExecutionConnectorConfigType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "java-package");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.JavaPackage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "message-type");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.jes.types.v1.cea.axis.MessageType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Identifier");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.community.beans.v1.axis.Identifier.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "tDocumentation");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.TDocumentation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "ActivityContainer");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis.ActivityContainer.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "flow");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis._flow.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "BaseParameterDefinition");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "execution-record-type");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.jes.beans.vi.axis.executionrecord.ExecutionRecordType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "job-identifier-type");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(simplesf);
            cachedDeserFactories.add(simpledf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "step-execution-record");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.jes.beans.vi.axis.executionrecord._stepExecutionRecord.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "sequence");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis._sequence.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Account");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.community.beans.v1.axis._Account.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "InterfacesType");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.InterfacesType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Group");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.community.beans.v1.axis._Group.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "join-type");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.workflow.beans.v1.axis.JoinType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "DataCentreApplication");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.DataCentreApplication.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationList");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "WebServiceParameterDefinition");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.WebServiceParameterDefinition.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "xhtmlDocumentation");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "getApplicationDescriptionResponse");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.service.v1.cea.impl._getApplicationDescriptionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "getApplicationDescription");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.service.v1.cea.impl._getApplicationDescription.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationBase");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "CommandLineApplication");
            cachedSerQNames.add(qName);
            cls = org.astrogrid.applications.beans.v1.axis.ceabase.CommandLineApplication.class;
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

    public java.lang.String execute(org.astrogrid.workflow.beans.v1.axis._tool tool, org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType jobstepID, java.lang.String jobMonitorURL) throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.CeaFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "execute"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {tool, jobstepID, jobMonitorURL});

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

    public boolean abort(java.lang.String executionId) throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.CeaFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "abort"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {executionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList listApplications() throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.CeaFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "listApplications"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList.class);
            }
        }
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase getApplicationDescription(java.lang.String applicationID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "getApplicationDescription"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {applicationID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase.class);
            }
        }
    }

    public org.astrogrid.jes.types.v1.cea.axis.MessageType queryExecutionStatus(java.lang.String executionId) throws java.rmi.RemoteException, org.astrogrid.applications.service.v1.cea.CeaFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "queryExecutionStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {executionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.jes.types.v1.cea.axis.MessageType) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.jes.types.v1.cea.axis.MessageType) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.jes.types.v1.cea.axis.MessageType.class);
            }
        }
    }

    public org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn returnRegistryEntry() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "returnRegistryEntry"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn) org.apache.axis.utils.JavaUtils.convert(_resp, org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn.class);
            }
        }
    }

}
