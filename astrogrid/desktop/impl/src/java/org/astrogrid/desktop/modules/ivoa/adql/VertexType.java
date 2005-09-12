/**
 * VertexType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class VertexType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.CoordsType position;
    private org.astrogrid.desktop.modules.ivoa.adql.SmallCircleType smallCircle;

    public VertexType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordsType getPosition() {
        return position;
    }

    public void setPosition(org.astrogrid.desktop.modules.ivoa.adql.CoordsType position) {
        this.position = position;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.SmallCircleType getSmallCircle() {
        return smallCircle;
    }

    public void setSmallCircle(org.astrogrid.desktop.modules.ivoa.adql.SmallCircleType smallCircle) {
        this.smallCircle = smallCircle;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VertexType)) return false;
        VertexType other = (VertexType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.position==null && other.getPosition()==null) || 
             (this.position!=null &&
              this.position.equals(other.getPosition()))) &&
            ((this.smallCircle==null && other.getSmallCircle()==null) || 
             (this.smallCircle!=null &&
              this.smallCircle.equals(other.getSmallCircle())));
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
        if (getPosition() != null) {
            _hashCode += getPosition().hashCode();
        }
        if (getSmallCircle() != null) {
            _hashCode += getSmallCircle().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VertexType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-region", "vertexType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("position");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-region", "Position"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordsType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("smallCircle");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-region", "SmallCircle"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-region", "smallCircleType"));
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
