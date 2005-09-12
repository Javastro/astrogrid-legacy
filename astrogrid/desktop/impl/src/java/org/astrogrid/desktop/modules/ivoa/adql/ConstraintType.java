/**
 * ConstraintType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class ConstraintType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.CoordsType vector;
    private double offset;

    public ConstraintType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordsType getVector() {
        return vector;
    }

    public void setVector(org.astrogrid.desktop.modules.ivoa.adql.CoordsType vector) {
        this.vector = vector;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConstraintType)) return false;
        ConstraintType other = (ConstraintType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.vector==null && other.getVector()==null) || 
             (this.vector!=null &&
              this.vector.equals(other.getVector()))) &&
            this.offset == other.getOffset();
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
        if (getVector() != null) {
            _hashCode += getVector().hashCode();
        }
        _hashCode += new Double(getOffset()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConstraintType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-region", "constraintType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vector");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-region", "Vector"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordsType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offset");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-region", "Offset"));
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
