/**
 * CircleRegionType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VODataService.v0_4;

public class CircleRegionType  extends net.ivoa.www.xml.VODataService.v0_4.RegionType  implements java.io.Serializable {
    private net.ivoa.www.xml.VODataService.v0_4.CoordFrameType coordFrame;
    private net.ivoa.www.xml.VODataService.v0_4.PositionType centerPosition;
    private float radius;

    public CircleRegionType() {
    }

    public net.ivoa.www.xml.VODataService.v0_4.CoordFrameType getCoordFrame() {
        return coordFrame;
    }

    public void setCoordFrame(net.ivoa.www.xml.VODataService.v0_4.CoordFrameType coordFrame) {
        this.coordFrame = coordFrame;
    }

    public net.ivoa.www.xml.VODataService.v0_4.PositionType getCenterPosition() {
        return centerPosition;
    }

    public void setCenterPosition(net.ivoa.www.xml.VODataService.v0_4.PositionType centerPosition) {
        this.centerPosition = centerPosition;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CircleRegionType)) return false;
        CircleRegionType other = (CircleRegionType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.coordFrame==null && other.getCoordFrame()==null) || 
             (this.coordFrame!=null &&
              this.coordFrame.equals(other.getCoordFrame()))) &&
            ((this.centerPosition==null && other.getCenterPosition()==null) || 
             (this.centerPosition!=null &&
              this.centerPosition.equals(other.getCenterPosition()))) &&
            this.radius == other.getRadius();
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
        if (getCoordFrame() != null) {
            _hashCode += getCoordFrame().hashCode();
        }
        if (getCenterPosition() != null) {
            _hashCode += getCenterPosition().hashCode();
        }
        _hashCode += new Float(getRadius()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CircleRegionType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CircleRegionType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordFrame");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CoordFrame"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CoordFrameType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("centerPosition");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CenterPosition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "positionType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("radius");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "radius"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
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
