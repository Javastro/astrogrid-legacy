/**
 * ArrayOfDownloadRelatedDownload.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public class ArrayOfDownloadRelatedDownload  implements java.io.Serializable {
    private com.microsoft.www.DownloadRelatedDownload[] relatedDownload;

    public ArrayOfDownloadRelatedDownload() {
    }

    public com.microsoft.www.DownloadRelatedDownload[] getRelatedDownload() {
        return relatedDownload;
    }

    public void setRelatedDownload(com.microsoft.www.DownloadRelatedDownload[] relatedDownload) {
        this.relatedDownload = relatedDownload;
    }

    public com.microsoft.www.DownloadRelatedDownload getRelatedDownload(int i) {
        return relatedDownload[i];
    }

    public void setRelatedDownload(int i, com.microsoft.www.DownloadRelatedDownload value) {
        this.relatedDownload[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfDownloadRelatedDownload)) return false;
        ArrayOfDownloadRelatedDownload other = (ArrayOfDownloadRelatedDownload) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.relatedDownload==null && other.getRelatedDownload()==null) || 
             (this.relatedDownload!=null &&
              java.util.Arrays.equals(this.relatedDownload, other.getRelatedDownload())));
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
        if (getRelatedDownload() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRelatedDownload());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRelatedDownload(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArrayOfDownloadRelatedDownload.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadRelatedDownload"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relatedDownload");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "RelatedDownload"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "DownloadRelatedDownload"));
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
