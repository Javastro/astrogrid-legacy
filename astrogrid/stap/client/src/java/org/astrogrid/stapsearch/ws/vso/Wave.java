/**
 * Wave.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public class Wave  implements java.io.Serializable {
    private float wavemin;

    private float wavemax;

    private java.lang.String waveunit;

    private java.lang.String wavetype;

    public Wave() {
    }

    public Wave(
           float wavemin,
           float wavemax,
           java.lang.String waveunit,
           java.lang.String wavetype) {
           this.wavemin = wavemin;
           this.wavemax = wavemax;
           this.waveunit = waveunit;
           this.wavetype = wavetype;
    }


    /**
     * Gets the wavemin value for this Wave.
     * 
     * @return wavemin
     */
    public float getWavemin() {
        return wavemin;
    }


    /**
     * Sets the wavemin value for this Wave.
     * 
     * @param wavemin
     */
    public void setWavemin(float wavemin) {
        this.wavemin = wavemin;
    }


    /**
     * Gets the wavemax value for this Wave.
     * 
     * @return wavemax
     */
    public float getWavemax() {
        return wavemax;
    }


    /**
     * Sets the wavemax value for this Wave.
     * 
     * @param wavemax
     */
    public void setWavemax(float wavemax) {
        this.wavemax = wavemax;
    }


    /**
     * Gets the waveunit value for this Wave.
     * 
     * @return waveunit
     */
    public java.lang.String getWaveunit() {
        return waveunit;
    }


    /**
     * Sets the waveunit value for this Wave.
     * 
     * @param waveunit
     */
    public void setWaveunit(java.lang.String waveunit) {
        this.waveunit = waveunit;
    }


    /**
     * Gets the wavetype value for this Wave.
     * 
     * @return wavetype
     */
    public java.lang.String getWavetype() {
        return wavetype;
    }


    /**
     * Sets the wavetype value for this Wave.
     * 
     * @param wavetype
     */
    public void setWavetype(java.lang.String wavetype) {
        this.wavetype = wavetype;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Wave)) return false;
        Wave other = (Wave) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.wavemin == other.getWavemin() &&
            this.wavemax == other.getWavemax() &&
            ((this.waveunit==null && other.getWaveunit()==null) || 
             (this.waveunit!=null &&
              this.waveunit.equals(other.getWaveunit()))) &&
            ((this.wavetype==null && other.getWavetype()==null) || 
             (this.wavetype!=null &&
              this.wavetype.equals(other.getWavetype())));
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
        _hashCode += new Float(getWavemin()).hashCode();
        _hashCode += new Float(getWavemax()).hashCode();
        if (getWaveunit() != null) {
            _hashCode += getWaveunit().hashCode();
        }
        if (getWavetype() != null) {
            _hashCode += getWavetype().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Wave.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Wave"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wavemin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "wavemin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wavemax");
        elemField.setXmlName(new javax.xml.namespace.QName("", "wavemax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("waveunit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "waveunit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wavetype");
        elemField.setXmlName(new javax.xml.namespace.QName("", "wavetype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
