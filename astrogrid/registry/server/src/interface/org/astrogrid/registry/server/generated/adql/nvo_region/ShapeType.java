/**
 * ShapeType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.adql.nvo_region;

public abstract class ShapeType  extends org.astrogrid.registry.server.generated.adql.nvo_region.RegionType  implements java.io.Serializable {
    private org.apache.axis.types.IDRef coord_system_id;  // attribute
    private org.astrogrid.registry.server.generated.adql.nvo_coords.PosUnitType unit;  // attribute

    public ShapeType() {
    }

    public org.apache.axis.types.IDRef getCoord_system_id() {
        return coord_system_id;
    }

    public void setCoord_system_id(org.apache.axis.types.IDRef coord_system_id) {
        this.coord_system_id = coord_system_id;
    }

    public org.astrogrid.registry.server.generated.adql.nvo_coords.PosUnitType getUnit() {
        return unit;
    }

    public void setUnit(org.astrogrid.registry.server.generated.adql.nvo_coords.PosUnitType unit) {
        this.unit = unit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ShapeType)) return false;
        ShapeType other = (ShapeType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.coord_system_id==null && other.getCoord_system_id()==null) || 
             (this.coord_system_id!=null &&
              this.coord_system_id.equals(other.getCoord_system_id()))) &&
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
        int _hashCode = super.hashCode();
        if (getCoord_system_id() != null) {
            _hashCode += getCoord_system_id().hashCode();
        }
        if (getUnit() != null) {
            _hashCode += getUnit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ShapeType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-region", "shapeType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("coord_system_id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "coord_system_id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "IDREF"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("unit");
        attrField.setXmlName(new javax.xml.namespace.QName("", "unit"));
        attrField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "posUnitType"));
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
