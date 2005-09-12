/**
 * AstronTimeTypeRelativeTime.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class AstronTimeTypeRelativeTime  implements java.io.Serializable, org.apache.axis.encoding.SimpleType {
    private double value;
    private org.astrogrid.desktop.modules.ivoa.adql.AstronTimeTypeRelativeTimeUnit unit;  // attribute

    public AstronTimeTypeRelativeTime() {
    }

    public AstronTimeTypeRelativeTime(double value) {
        this.value = value;
    }

    // Simple Types must have a String constructor
    public AstronTimeTypeRelativeTime(java.lang.String value) {
        this.value = new Double(value).doubleValue();
    }

    // Simple Types must have a toString for serializing the value
    public java.lang.String toString() {
        return new Double(value).toString();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.AstronTimeTypeRelativeTimeUnit getUnit() {
        return unit;
    }

    public void setUnit(org.astrogrid.desktop.modules.ivoa.adql.AstronTimeTypeRelativeTimeUnit unit) {
        this.unit = unit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AstronTimeTypeRelativeTime)) return false;
        AstronTimeTypeRelativeTime other = (AstronTimeTypeRelativeTime) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.value == other.getValue() &&
            ((this.unit==null && other.getUnit()==null) || 
             (this.unit!=null &&
              this.unit.equals(other.getUnit())));
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
        _hashCode += new Double(getValue()).hashCode();
        if (getUnit() != null) {
            _hashCode += getUnit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AstronTimeTypeRelativeTime.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeRelativeTime"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("unit");
        attrField.setXmlName(new javax.xml.namespace.QName("", "unit"));
        attrField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeTypeRelativeTimeUnit"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
          new  org.apache.axis.encoding.ser.SimpleSerializer(
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
          new  org.apache.axis.encoding.ser.SimpleDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
