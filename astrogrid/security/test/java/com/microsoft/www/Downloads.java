/**
 * Downloads.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public class Downloads  implements java.io.Serializable {
    private com.microsoft.www.DownloadSummary[] downloadSummary;
    private com.microsoft.www.Download[] download;
    private java.lang.String count;  // attribute

    public Downloads() {
    }

    public com.microsoft.www.DownloadSummary[] getDownloadSummary() {
        return downloadSummary;
    }

    public void setDownloadSummary(com.microsoft.www.DownloadSummary[] downloadSummary) {
        this.downloadSummary = downloadSummary;
    }

    public com.microsoft.www.DownloadSummary getDownloadSummary(int i) {
        return downloadSummary[i];
    }

    public void setDownloadSummary(int i, com.microsoft.www.DownloadSummary value) {
        this.downloadSummary[i] = value;
    }

    public com.microsoft.www.Download[] getDownload() {
        return download;
    }

    public void setDownload(com.microsoft.www.Download[] download) {
        this.download = download;
    }

    public com.microsoft.www.Download getDownload(int i) {
        return download[i];
    }

    public void setDownload(int i, com.microsoft.www.Download value) {
        this.download[i] = value;
    }

    public java.lang.String getCount() {
        return count;
    }

    public void setCount(java.lang.String count) {
        this.count = count;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Downloads)) return false;
        Downloads other = (Downloads) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.downloadSummary==null && other.getDownloadSummary()==null) || 
             (this.downloadSummary!=null &&
              java.util.Arrays.equals(this.downloadSummary, other.getDownloadSummary()))) &&
            ((this.download==null && other.getDownload()==null) || 
             (this.download!=null &&
              java.util.Arrays.equals(this.download, other.getDownload()))) &&
            ((this.count==null && other.getCount()==null) || 
             (this.count!=null &&
              this.count.equals(other.getCount())));
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
        if (getDownloadSummary() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDownloadSummary());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDownloadSummary(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDownload() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDownload());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDownload(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCount() != null) {
            _hashCode += getCount().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Downloads.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "Downloads"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("count");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Count"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("downloadSummary");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "DownloadSummary"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "DownloadSummary"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("download");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "Download"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "Download"));
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
