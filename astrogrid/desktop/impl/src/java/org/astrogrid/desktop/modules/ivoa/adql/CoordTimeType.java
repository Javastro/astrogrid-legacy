/**
 * CoordTimeType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class CoordTimeType  implements java.io.Serializable {
    private java.lang.String name;
    private org.astrogrid.desktop.modules.ivoa.adql.AstronTimeType coordValue;
    private org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType coordError;
    private org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType coordResolution;
    private org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType coordSize;
    private org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType coordPixsize;

    public CoordTimeType() {
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.AstronTimeType getCoordValue() {
        return coordValue;
    }

    public void setCoordValue(org.astrogrid.desktop.modules.ivoa.adql.AstronTimeType coordValue) {
        this.coordValue = coordValue;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType getCoordError() {
        return coordError;
    }

    public void setCoordError(org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType coordError) {
        this.coordError = coordError;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType getCoordResolution() {
        return coordResolution;
    }

    public void setCoordResolution(org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType coordResolution) {
        this.coordResolution = coordResolution;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType getCoordSize() {
        return coordSize;
    }

    public void setCoordSize(org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType coordSize) {
        this.coordSize = coordSize;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType getCoordPixsize() {
        return coordPixsize;
    }

    public void setCoordPixsize(org.astrogrid.desktop.modules.ivoa.adql.CoordTimeValueType coordPixsize) {
        this.coordPixsize = coordPixsize;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CoordTimeType)) return false;
        CoordTimeType other = (CoordTimeType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.coordValue==null && other.getCoordValue()==null) || 
             (this.coordValue!=null &&
              this.coordValue.equals(other.getCoordValue()))) &&
            ((this.coordError==null && other.getCoordError()==null) || 
             (this.coordError!=null &&
              this.coordError.equals(other.getCoordError()))) &&
            ((this.coordResolution==null && other.getCoordResolution()==null) || 
             (this.coordResolution!=null &&
              this.coordResolution.equals(other.getCoordResolution()))) &&
            ((this.coordSize==null && other.getCoordSize()==null) || 
             (this.coordSize!=null &&
              this.coordSize.equals(other.getCoordSize()))) &&
            ((this.coordPixsize==null && other.getCoordPixsize()==null) || 
             (this.coordPixsize!=null &&
              this.coordPixsize.equals(other.getCoordPixsize())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getCoordValue() != null) {
            _hashCode += getCoordValue().hashCode();
        }
        if (getCoordError() != null) {
            _hashCode += getCoordError().hashCode();
        }
        if (getCoordResolution() != null) {
            _hashCode += getCoordResolution().hashCode();
        }
        if (getCoordSize() != null) {
            _hashCode += getCoordSize().hashCode();
        }
        if (getCoordPixsize() != null) {
            _hashCode += getCoordPixsize().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CoordTimeType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordValue");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "CoordValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "astronTimeType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordError");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "CoordError"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeValueType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordResolution");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "CoordResolution"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeValueType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordSize");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "CoordSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeValueType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordPixsize");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "CoordPixsize"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeValueType"));
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
