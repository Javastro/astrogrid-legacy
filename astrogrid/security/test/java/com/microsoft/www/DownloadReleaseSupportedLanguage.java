/**
 * DownloadReleaseSupportedLanguage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public class DownloadReleaseSupportedLanguage  implements java.io.Serializable {
    private java.lang.String cultureID;
    private java.lang.String LCID;  // attribute

    public DownloadReleaseSupportedLanguage() {
    }

    public java.lang.String getCultureID() {
        return cultureID;
    }

    public void setCultureID(java.lang.String cultureID) {
        this.cultureID = cultureID;
    }

    public java.lang.String getLCID() {
        return LCID;
    }

    public void setLCID(java.lang.String LCID) {
        this.LCID = LCID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DownloadReleaseSupportedLanguage)) return false;
        DownloadReleaseSupportedLanguage other = (DownloadReleaseSupportedLanguage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cultureID==null && other.getCultureID()==null) || 
             (this.cultureID!=null &&
              this.cultureID.equals(other.getCultureID()))) &&
            ((this.LCID==null && other.getLCID()==null) || 
             (this.LCID!=null &&
              this.LCID.equals(other.getLCID())));
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
        if (getCultureID() != null) {
            _hashCode += getCultureID().hashCode();
        }
        if (getLCID() != null) {
            _hashCode += getLCID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DownloadReleaseSupportedLanguage.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "DownloadReleaseSupportedLanguage"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("LCID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "LCID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cultureID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "CultureID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
