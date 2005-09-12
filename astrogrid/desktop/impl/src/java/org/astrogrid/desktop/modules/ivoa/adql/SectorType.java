/**
 * SectorType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class SectorType  extends org.astrogrid.desktop.modules.ivoa.adql.ShapeType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.CoordsType position;
    private double posAngle1;
    private double posAngle2;
    private org.astrogrid.desktop.modules.ivoa.adql.PosUnitType pos_angle_unit;  // attribute

    public SectorType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordsType getPosition() {
        return position;
    }

    public void setPosition(org.astrogrid.desktop.modules.ivoa.adql.CoordsType position) {
        this.position = position;
    }

    public double getPosAngle1() {
        return posAngle1;
    }

    public void setPosAngle1(double posAngle1) {
        this.posAngle1 = posAngle1;
    }

    public double getPosAngle2() {
        return posAngle2;
    }

    public void setPosAngle2(double posAngle2) {
        this.posAngle2 = posAngle2;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.PosUnitType getPos_angle_unit() {
        return pos_angle_unit;
    }

    public void setPos_angle_unit(org.astrogrid.desktop.modules.ivoa.adql.PosUnitType pos_angle_unit) {
        this.pos_angle_unit = pos_angle_unit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SectorType)) return false;
        SectorType other = (SectorType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.position==null && other.getPosition()==null) || 
             (this.position!=null &&
              this.position.equals(other.getPosition()))) &&
            this.posAngle1 == other.getPosAngle1() &&
            this.posAngle2 == other.getPosAngle2() &&
            ((this.pos_angle_unit==null && other.getPos_angle_unit()==null) || 
             (this.pos_angle_unit!=null &&
              this.pos_angle_unit.equals(other.getPos_angle_unit())));
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
        if (getPosition() != null) {
            _hashCode += getPosition().hashCode();
        }
        _hashCode += new Double(getPosAngle1()).hashCode();
        _hashCode += new Double(getPosAngle2()).hashCode();
        if (getPos_angle_unit() != null) {
            _hashCode += getPos_angle_unit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SectorType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-region", "sectorType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("pos_angle_unit");
        attrField.setXmlName(new javax.xml.namespace.QName("", "pos_angle_unit"));
        attrField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "posUnitType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("position");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-region", "Position"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordsType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("posAngle1");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-region", "PosAngle1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("posAngle2");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-region", "PosAngle2"));
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
