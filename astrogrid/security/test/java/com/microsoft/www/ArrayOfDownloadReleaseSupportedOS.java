/**
 * ArrayOfDownloadReleaseSupportedOS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public class ArrayOfDownloadReleaseSupportedOS  implements java.io.Serializable {
    private com.microsoft.www.DownloadReleaseSupportedOS[] supportedOS;

    public ArrayOfDownloadReleaseSupportedOS() {
    }

    public com.microsoft.www.DownloadReleaseSupportedOS[] getSupportedOS() {
        return supportedOS;
    }

    public void setSupportedOS(com.microsoft.www.DownloadReleaseSupportedOS[] supportedOS) {
        this.supportedOS = supportedOS;
    }

    public com.microsoft.www.DownloadReleaseSupportedOS getSupportedOS(int i) {
        return supportedOS[i];
    }

    public void setSupportedOS(int i, com.microsoft.www.DownloadReleaseSupportedOS value) {
        this.supportedOS[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfDownloadReleaseSupportedOS)) return false;
        ArrayOfDownloadReleaseSupportedOS other = (ArrayOfDownloadReleaseSupportedOS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.supportedOS==null && other.getSupportedOS()==null) || 
             (this.supportedOS!=null &&
              java.util.Arrays.equals(this.supportedOS, other.getSupportedOS())));
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
        if (getSupportedOS() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSupportedOS());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSupportedOS(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfDownloadReleaseSupportedOS.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadReleaseSupportedOS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("supportedOS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "SupportedOS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "DownloadReleaseSupportedOS"));
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
