/**
 * DownloadRelease.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public class DownloadRelease  implements java.io.Serializable {
    private com.microsoft.www.ArrayOfDownloadReleaseSupportedLanguage supportedLanguages;
    private com.microsoft.www.ArrayOfDownloadReleaseSupportedOS supportedOSs;
    private com.microsoft.www.ArrayOfDownloadReleaseFile files;
    private java.lang.String ID;  // attribute

    public DownloadRelease() {
    }

    public com.microsoft.www.ArrayOfDownloadReleaseSupportedLanguage getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(com.microsoft.www.ArrayOfDownloadReleaseSupportedLanguage supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public com.microsoft.www.ArrayOfDownloadReleaseSupportedOS getSupportedOSs() {
        return supportedOSs;
    }

    public void setSupportedOSs(com.microsoft.www.ArrayOfDownloadReleaseSupportedOS supportedOSs) {
        this.supportedOSs = supportedOSs;
    }

    public com.microsoft.www.ArrayOfDownloadReleaseFile getFiles() {
        return files;
    }

    public void setFiles(com.microsoft.www.ArrayOfDownloadReleaseFile files) {
        this.files = files;
    }

    public java.lang.String getID() {
        return ID;
    }

    public void setID(java.lang.String ID) {
        this.ID = ID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DownloadRelease)) return false;
        DownloadRelease other = (DownloadRelease) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.supportedLanguages==null && other.getSupportedLanguages()==null) || 
             (this.supportedLanguages!=null &&
              this.supportedLanguages.equals(other.getSupportedLanguages()))) &&
            ((this.supportedOSs==null && other.getSupportedOSs()==null) || 
             (this.supportedOSs!=null &&
              this.supportedOSs.equals(other.getSupportedOSs()))) &&
            ((this.files==null && other.getFiles()==null) || 
             (this.files!=null &&
              this.files.equals(other.getFiles()))) &&
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID())));
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
        if (getSupportedLanguages() != null) {
            _hashCode += getSupportedLanguages().hashCode();
        }
        if (getSupportedOSs() != null) {
            _hashCode += getSupportedOSs().hashCode();
        }
        if (getFiles() != null) {
            _hashCode += getFiles().hashCode();
        }
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DownloadRelease.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "DownloadRelease"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supportedLanguages");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "SupportedLanguages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadReleaseSupportedLanguage"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supportedOSs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "SupportedOSs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadReleaseSupportedOS"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("files");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "Files"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadReleaseFile"));
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
