/**
 * ApplicationBase.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceabase;

public class ApplicationBase  implements java.io.Serializable {
    private org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase_Parameters parameters;
    private org.astrogrid.applications.beans.v1.axis.ceabase.InterfacesType interfaces;
    private org.apache.axis.types.Id name;  // attribute
    private java.lang.String instanceClass;  // attribute

    public ApplicationBase() {
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase_Parameters getParameters() {
        return parameters;
    }

    public void setParameters(org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase_Parameters parameters) {
        this.parameters = parameters;
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.InterfacesType getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(org.astrogrid.applications.beans.v1.axis.ceabase.InterfacesType interfaces) {
        this.interfaces = interfaces;
    }

    public org.apache.axis.types.Id getName() {
        return name;
    }

    public void setName(org.apache.axis.types.Id name) {
        this.name = name;
    }

    public java.lang.String getInstanceClass() {
        return instanceClass;
    }

    public void setInstanceClass(java.lang.String instanceClass) {
        this.instanceClass = instanceClass;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ApplicationBase)) return false;
        ApplicationBase other = (ApplicationBase) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.parameters==null && other.getParameters()==null) || 
             (this.parameters!=null &&
              this.parameters.equals(other.getParameters()))) &&
            ((this.interfaces==null && other.getInterfaces()==null) || 
             (this.interfaces!=null &&
              this.interfaces.equals(other.getInterfaces()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.instanceClass==null && other.getInstanceClass()==null) || 
             (this.instanceClass!=null &&
              this.instanceClass.equals(other.getInstanceClass())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getParameters() != null) {
            _hashCode += getParameters().hashCode();
        }
        if (getInterfaces() != null) {
            _hashCode += getInterfaces().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getInstanceClass() != null) {
            _hashCode += getInstanceClass().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ApplicationBase.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationBase"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("instanceClass");
        attrField.setXmlName(new javax.xml.namespace.QName("", "instanceClass"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameters");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "Parameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationBase>Parameters"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interfaces");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "Interfaces"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "InterfacesType"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
