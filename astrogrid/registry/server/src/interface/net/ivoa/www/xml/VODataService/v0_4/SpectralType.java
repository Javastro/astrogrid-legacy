/**
 * SpectralType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VODataService.v0_4;

public class SpectralType  implements java.io.Serializable {
    private net.ivoa.www.xml.VODataService.v0_4.WavebandType[] waveband;
    private net.ivoa.www.xml.VODataService.v0_4.WavelengthRangeType wavelengthRange;
    private float spectralResolution;

    public SpectralType() {
    }

    public net.ivoa.www.xml.VODataService.v0_4.WavebandType[] getWaveband() {
        return waveband;
    }

    public void setWaveband(net.ivoa.www.xml.VODataService.v0_4.WavebandType[] waveband) {
        this.waveband = waveband;
    }

    public net.ivoa.www.xml.VODataService.v0_4.WavebandType getWaveband(int i) {
        return waveband[i];
    }

    public void setWaveband(int i, net.ivoa.www.xml.VODataService.v0_4.WavebandType value) {
        this.waveband[i] = value;
    }

    public net.ivoa.www.xml.VODataService.v0_4.WavelengthRangeType getWavelengthRange() {
        return wavelengthRange;
    }

    public void setWavelengthRange(net.ivoa.www.xml.VODataService.v0_4.WavelengthRangeType wavelengthRange) {
        this.wavelengthRange = wavelengthRange;
    }

    public float getSpectralResolution() {
        return spectralResolution;
    }

    public void setSpectralResolution(float spectralResolution) {
        this.spectralResolution = spectralResolution;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SpectralType)) return false;
        SpectralType other = (SpectralType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.waveband==null && other.getWaveband()==null) || 
             (this.waveband!=null &&
              java.util.Arrays.equals(this.waveband, other.getWaveband()))) &&
            ((this.wavelengthRange==null && other.getWavelengthRange()==null) || 
             (this.wavelengthRange!=null &&
              this.wavelengthRange.equals(other.getWavelengthRange()))) &&
            this.spectralResolution == other.getSpectralResolution();
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
        if (getWaveband() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWaveband());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWaveband(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWavelengthRange() != null) {
            _hashCode += getWavelengthRange().hashCode();
        }
        _hashCode += new Float(getSpectralResolution()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SpectralType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SpectralType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waveband");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "Waveband"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "WavebandType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wavelengthRange");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "WavelengthRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "WavelengthRangeType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spectralResolution");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SpectralResolution"));
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
