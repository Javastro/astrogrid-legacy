/**
 * CoordSpectralValueType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class CoordSpectralValueType  implements java.io.Serializable {
    private double value;
    private org.apache.axis.types.IDRef reference;
    private org.astrogrid.desktop.modules.ivoa.adql.SpectralUnitType spectral_unit;  // attribute

    public CoordSpectralValueType() {
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public org.apache.axis.types.IDRef getReference() {
        return reference;
    }

    public void setReference(org.apache.axis.types.IDRef reference) {
        this.reference = reference;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.SpectralUnitType getSpectral_unit() {
        return spectral_unit;
    }

    public void setSpectral_unit(org.astrogrid.desktop.modules.ivoa.adql.SpectralUnitType spectral_unit) {
        this.spectral_unit = spectral_unit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CoordSpectralValueType)) return false;
        CoordSpectralValueType other = (CoordSpectralValueType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.value == other.getValue() &&
            ((this.reference==null && other.getReference()==null) || 
             (this.reference!=null &&
              this.reference.equals(other.getReference()))) &&
            ((this.spectral_unit==null && other.getSpectral_unit()==null) || 
             (this.spectral_unit!=null &&
              this.spectral_unit.equals(other.getSpectral_unit())));
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
        if (getReference() != null) {
            _hashCode += getReference().hashCode();
        }
        if (getSpectral_unit() != null) {
            _hashCode += getSpectral_unit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CoordSpectralValueType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordSpectralValueType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("spectral_unit");
        attrField.setXmlName(new javax.xml.namespace.QName("", "spectral_unit"));
        attrField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "spectralUnitType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reference");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Reference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "IDREF"));
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
