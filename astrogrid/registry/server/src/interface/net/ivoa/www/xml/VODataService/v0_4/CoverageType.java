/**
 * CoverageType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VODataService.v0_4;

public class CoverageType  implements java.io.Serializable {
    private net.ivoa.www.xml.VODataService.v0_4.SpatialType spatial;
    private net.ivoa.www.xml.VODataService.v0_4.SpectralType spectral;
    private net.ivoa.www.xml.VODataService.v0_4.TemporalType temporal;

    public CoverageType() {
    }

    public net.ivoa.www.xml.VODataService.v0_4.SpatialType getSpatial() {
        return spatial;
    }

    public void setSpatial(net.ivoa.www.xml.VODataService.v0_4.SpatialType spatial) {
        this.spatial = spatial;
    }

    public net.ivoa.www.xml.VODataService.v0_4.SpectralType getSpectral() {
        return spectral;
    }

    public void setSpectral(net.ivoa.www.xml.VODataService.v0_4.SpectralType spectral) {
        this.spectral = spectral;
    }

    public net.ivoa.www.xml.VODataService.v0_4.TemporalType getTemporal() {
        return temporal;
    }

    public void setTemporal(net.ivoa.www.xml.VODataService.v0_4.TemporalType temporal) {
        this.temporal = temporal;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CoverageType)) return false;
        CoverageType other = (CoverageType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.spatial==null && other.getSpatial()==null) || 
             (this.spatial!=null &&
              this.spatial.equals(other.getSpatial()))) &&
            ((this.spectral==null && other.getSpectral()==null) || 
             (this.spectral!=null &&
              this.spectral.equals(other.getSpectral()))) &&
            ((this.temporal==null && other.getTemporal()==null) || 
             (this.temporal!=null &&
              this.temporal.equals(other.getTemporal())));
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
        if (getSpatial() != null) {
            _hashCode += getSpatial().hashCode();
        }
        if (getSpectral() != null) {
            _hashCode += getSpectral().hashCode();
        }
        if (getTemporal() != null) {
            _hashCode += getTemporal().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CoverageType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CoverageType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spatial");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "Spatial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SpatialType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spectral");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "Spectral"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SpectralType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("temporal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "Temporal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "TemporalType"));
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
