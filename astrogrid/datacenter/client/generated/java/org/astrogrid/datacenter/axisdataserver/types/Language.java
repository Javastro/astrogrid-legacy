/**
 * Language.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver.types;

public class Language  implements java.io.Serializable {
    private org.apache.axis.types.URI namespace;
    private org.apache.axis.types.URI schemaLocation;
    private java.lang.String implementingClass;

    public Language() {
    }

    public org.apache.axis.types.URI getNamespace() {
        return namespace;
    }

    public void setNamespace(org.apache.axis.types.URI namespace) {
        this.namespace = namespace;
    }

    public org.apache.axis.types.URI getSchemaLocation() {
        return schemaLocation;
    }

    public void setSchemaLocation(org.apache.axis.types.URI schemaLocation) {
        this.schemaLocation = schemaLocation;
    }

    public java.lang.String getImplementingClass() {
        return implementingClass;
    }

    public void setImplementingClass(java.lang.String implementingClass) {
        this.implementingClass = implementingClass;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Language)) return false;
        Language other = (Language) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.namespace==null && other.getNamespace()==null) || 
             (this.namespace!=null &&
              this.namespace.equals(other.getNamespace()))) &&
            ((this.schemaLocation==null && other.getSchemaLocation()==null) || 
             (this.schemaLocation!=null &&
              this.schemaLocation.equals(other.getSchemaLocation()))) &&
            ((this.implementingClass==null && other.getImplementingClass()==null) || 
             (this.implementingClass!=null &&
              this.implementingClass.equals(other.getImplementingClass())));
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
        if (getNamespace() != null) {
            _hashCode += getNamespace().hashCode();
        }
        if (getSchemaLocation() != null) {
            _hashCode += getSchemaLocation().hashCode();
        }
        if (getImplementingClass() != null) {
            _hashCode += getImplementingClass().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Language.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "Language"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("namespace");
        elemField.setXmlName(new javax.xml.namespace.QName("", "namespace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("schemaLocation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "schema-location"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("implementingClass");
        elemField.setXmlName(new javax.xml.namespace.QName("", "implementing-class"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
