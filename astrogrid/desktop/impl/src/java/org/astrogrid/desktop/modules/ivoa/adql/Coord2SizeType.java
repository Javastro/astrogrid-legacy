/**
 * Coord2SizeType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class Coord2SizeType  implements java.io.Serializable {
    private double value;
    private org.apache.axis.types.IDRef posAngleRef;
    private org.apache.axis.types.IDRefs reference;
    private double posAngle;
    private double matrix;
    private org.astrogrid.desktop.modules.ivoa.adql.PosUnitType pos1_unit;  // attribute
    private org.astrogrid.desktop.modules.ivoa.adql.PosUnitType pos2_unit;  // attribute

    public Coord2SizeType() {
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public org.apache.axis.types.IDRef getPosAngleRef() {
        return posAngleRef;
    }

    public void setPosAngleRef(org.apache.axis.types.IDRef posAngleRef) {
        this.posAngleRef = posAngleRef;
    }

    public org.apache.axis.types.IDRefs getReference() {
        return reference;
    }

    public void setReference(org.apache.axis.types.IDRefs reference) {
        this.reference = reference;
    }

    public double getPosAngle() {
        return posAngle;
    }

    public void setPosAngle(double posAngle) {
        this.posAngle = posAngle;
    }

    public double getMatrix() {
        return matrix;
    }

    public void setMatrix(double matrix) {
        this.matrix = matrix;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.PosUnitType getPos1_unit() {
        return pos1_unit;
    }

    public void setPos1_unit(org.astrogrid.desktop.modules.ivoa.adql.PosUnitType pos1_unit) {
        this.pos1_unit = pos1_unit;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.PosUnitType getPos2_unit() {
        return pos2_unit;
    }

    public void setPos2_unit(org.astrogrid.desktop.modules.ivoa.adql.PosUnitType pos2_unit) {
        this.pos2_unit = pos2_unit;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Coord2SizeType)) return false;
        Coord2SizeType other = (Coord2SizeType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.value == other.getValue() &&
            ((this.posAngleRef==null && other.getPosAngleRef()==null) || 
             (this.posAngleRef!=null &&
              this.posAngleRef.equals(other.getPosAngleRef()))) &&
            ((this.reference==null && other.getReference()==null) || 
             (this.reference!=null &&
              this.reference.equals(other.getReference()))) &&
            this.posAngle == other.getPosAngle() &&
            this.matrix == other.getMatrix() &&
            ((this.pos1_unit==null && other.getPos1_unit()==null) || 
             (this.pos1_unit!=null &&
              this.pos1_unit.equals(other.getPos1_unit()))) &&
            ((this.pos2_unit==null && other.getPos2_unit()==null) || 
             (this.pos2_unit!=null &&
              this.pos2_unit.equals(other.getPos2_unit())));
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
        if (getPosAngleRef() != null) {
            _hashCode += getPosAngleRef().hashCode();
        }
        if (getReference() != null) {
            _hashCode += getReference().hashCode();
        }
        _hashCode += new Double(getPosAngle()).hashCode();
        _hashCode += new Double(getMatrix()).hashCode();
        if (getPos1_unit() != null) {
            _hashCode += getPos1_unit().hashCode();
        }
        if (getPos2_unit() != null) {
            _hashCode += getPos2_unit().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Coord2SizeType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coord2SizeType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("pos1_unit");
        attrField.setXmlName(new javax.xml.namespace.QName("", "pos1_unit"));
        attrField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "posUnitType"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("pos2_unit");
        attrField.setXmlName(new javax.xml.namespace.QName("", "pos2_unit"));
        attrField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "posUnitType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("posAngleRef");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "PosAngleRef"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "IDREF"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reference");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Reference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "IDREFS"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("posAngle");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "PosAngle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matrix");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Matrix"));
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
