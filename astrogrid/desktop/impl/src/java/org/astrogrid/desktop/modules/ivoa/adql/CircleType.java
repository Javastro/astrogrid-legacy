/**
 * CircleType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class CircleType  extends org.astrogrid.desktop.modules.ivoa.adql.ShapeType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.CoordsType center;
    private double radius;
    private org.astrogrid.desktop.modules.ivoa.adql.PosUnitType radius_unit;  // attribute

    public CircleType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordsType getCenter() {
        return center;
    }

    public void setCenter(org.astrogrid.desktop.modules.ivoa.adql.CoordsType center) {
        this.center = center;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.PosUnitType getRadius_unit() {
        return radius_unit;
    }

    public void setRadius_unit(org.astrogrid.desktop.modules.ivoa.adql.PosUnitType radius_unit) {
        this.radius_unit = radius_unit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CircleType)) return false;
        CircleType other = (CircleType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.center==null && other.getCenter()==null) || 
             (this.center!=null &&
              this.center.equals(other.getCenter()))) &&
            this.radius == other.getRadius() &&
            ((this.radius_unit==null && other.getRadius_unit()==null) || 
             (this.radius_unit!=null &&
              this.radius_unit.equals(other.getRadius_unit())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getCenter() != null) {
            _hashCode += getCenter().hashCode();
        }
        _hashCode += new Double(getRadius()).hashCode();
        if (getRadius_unit() != null) {
            _hashCode += getRadius_unit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CircleType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-region", "circleType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("radius_unit");
        attrField.setXmlName(new javax.xml.namespace.QName("", "radius_unit"));
        attrField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "posUnitType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("center");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-region", "Center"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordsType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("radius");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-region", "Radius"));
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
