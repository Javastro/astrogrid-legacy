/**
 * SpatialType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VODataService.v0_4;

public class SpatialType  implements java.io.Serializable {
    private net.ivoa.www.xml.VODataService.v0_4.RegionType[] region;
    private float spatialResolution;
    private float regionOfRegard;

    public SpatialType() {
    }

    public net.ivoa.www.xml.VODataService.v0_4.RegionType[] getRegion() {
        return region;
    }

    public void setRegion(net.ivoa.www.xml.VODataService.v0_4.RegionType[] region) {
        this.region = region;
    }

    public net.ivoa.www.xml.VODataService.v0_4.RegionType getRegion(int i) {
        return region[i];
    }

    public void setRegion(int i, net.ivoa.www.xml.VODataService.v0_4.RegionType value) {
        this.region[i] = value;
    }

    public float getSpatialResolution() {
        return spatialResolution;
    }

    public void setSpatialResolution(float spatialResolution) {
        this.spatialResolution = spatialResolution;
    }

    public float getRegionOfRegard() {
        return regionOfRegard;
    }

    public void setRegionOfRegard(float regionOfRegard) {
        this.regionOfRegard = regionOfRegard;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SpatialType)) return false;
        SpatialType other = (SpatialType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.region==null && other.getRegion()==null) || 
             (this.region!=null &&
              java.util.Arrays.equals(this.region, other.getRegion()))) &&
            this.spatialResolution == other.getSpatialResolution() &&
            this.regionOfRegard == other.getRegionOfRegard();
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
        if (getRegion() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRegion());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRegion(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += new Float(getSpatialResolution()).hashCode();
        _hashCode += new Float(getRegionOfRegard()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SpatialType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SpatialType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "Region"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "RegionType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spatialResolution");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SpatialResolution"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("regionOfRegard");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "RegionOfRegard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
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
