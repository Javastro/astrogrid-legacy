/**
 * BaseParameterDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceaparameters;

public class BaseParameterDefinition  implements java.io.Serializable {
    private java.lang.String UI_Name;
    private org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation UI_Description;
    private java.lang.String UCD;
    private java.lang.Object defaultValue;
    private java.lang.String units;
    private java.lang.String name;  // attribute
    private org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterTypes type;  // attribute

    public BaseParameterDefinition() {
    }

    public java.lang.String getUI_Name() {
        return UI_Name;
    }

    public void setUI_Name(java.lang.String UI_Name) {
        this.UI_Name = UI_Name;
    }

    public org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation getUI_Description() {
        return UI_Description;
    }

    public void setUI_Description(org.astrogrid.applications.beans.v1.axis.ceaparameters.XhtmlDocumentation UI_Description) {
        this.UI_Description = UI_Description;
    }

    public java.lang.String getUCD() {
        return UCD;
    }

    public void setUCD(java.lang.String UCD) {
        this.UCD = UCD;
    }

    public java.lang.Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(java.lang.Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public java.lang.String getUnits() {
        return units;
    }

    public void setUnits(java.lang.String units) {
        this.units = units;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterTypes getType() {
        return type;
    }

    public void setType(org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterTypes type) {
        this.type = type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BaseParameterDefinition)) return false;
        BaseParameterDefinition other = (BaseParameterDefinition) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.UI_Name==null && other.getUI_Name()==null) || 
             (this.UI_Name!=null &&
              this.UI_Name.equals(other.getUI_Name()))) &&
            ((this.UI_Description==null && other.getUI_Description()==null) || 
             (this.UI_Description!=null &&
              this.UI_Description.equals(other.getUI_Description()))) &&
            ((this.UCD==null && other.getUCD()==null) || 
             (this.UCD!=null &&
              this.UCD.equals(other.getUCD()))) &&
            ((this.defaultValue==null && other.getDefaultValue()==null) || 
             (this.defaultValue!=null &&
              this.defaultValue.equals(other.getDefaultValue()))) &&
            ((this.units==null && other.getUnits()==null) || 
             (this.units!=null &&
              this.units.equals(other.getUnits()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType())));
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
        if (getUI_Name() != null) {
            _hashCode += getUI_Name().hashCode();
        }
        if (getUI_Description() != null) {
            _hashCode += getUI_Description().hashCode();
        }
        if (getUCD() != null) {
            _hashCode += getUCD().hashCode();
        }
        if (getDefaultValue() != null) {
            _hashCode += getDefaultValue().hashCode();
        }
        if (getUnits() != null) {
            _hashCode += getUnits().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BaseParameterDefinition.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "BaseParameterDefinition"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("type");
        attrField.setXmlName(new javax.xml.namespace.QName("", "type"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "parameterTypes"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UI_Name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "UI_Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UI_Description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "UI_Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "xhtmlDocumentation"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UCD");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "UCD"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("defaultValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "DefaultValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("units");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "Units"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
