/**
 * ParameterRef.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceabase;

public class ParameterRef  implements java.io.Serializable {
    private java.lang.String ref;  // attribute
    private java.math.BigInteger minoccurs;  // attribute
    private java.math.BigInteger maxoccurs;  // attribute

    public ParameterRef() {
    }

    public java.lang.String getRef() {
        return ref;
    }

    public void setRef(java.lang.String ref) {
        this.ref = ref;
    }

    public java.math.BigInteger getMinoccurs() {
        return minoccurs;
    }

    public void setMinoccurs(java.math.BigInteger minoccurs) {
        this.minoccurs = minoccurs;
    }

    public java.math.BigInteger getMaxoccurs() {
        return maxoccurs;
    }

    public void setMaxoccurs(java.math.BigInteger maxoccurs) {
        this.maxoccurs = maxoccurs;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ParameterRef)) return false;
        ParameterRef other = (ParameterRef) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ref==null && other.getRef()==null) || 
             (this.ref!=null &&
              this.ref.equals(other.getRef()))) &&
            ((this.minoccurs==null && other.getMinoccurs()==null) || 
             (this.minoccurs!=null &&
              this.minoccurs.equals(other.getMinoccurs()))) &&
            ((this.maxoccurs==null && other.getMaxoccurs()==null) || 
             (this.maxoccurs!=null &&
              this.maxoccurs.equals(other.getMaxoccurs())));
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
        if (getRef() != null) {
            _hashCode += getRef().hashCode();
        }
        if (getMinoccurs() != null) {
            _hashCode += getMinoccurs().hashCode();
        }
        if (getMaxoccurs() != null) {
            _hashCode += getMaxoccurs().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ParameterRef.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "parameterRef"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ref");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ref"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("minoccurs");
        attrField.setXmlName(new javax.xml.namespace.QName("", "minoccurs"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("maxoccurs");
        attrField.setXmlName(new javax.xml.namespace.QName("", "maxoccurs"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        typeDesc.addFieldDesc(attrField);
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
