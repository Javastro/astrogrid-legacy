/**
 * ArrayOfDownloadProductOrTechnology.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public class ArrayOfDownloadProductOrTechnology  implements java.io.Serializable {
    private com.microsoft.www.DownloadProductOrTechnology[] productOrTechnology;

    public ArrayOfDownloadProductOrTechnology() {
    }

    public com.microsoft.www.DownloadProductOrTechnology[] getProductOrTechnology() {
        return productOrTechnology;
    }

    public void setProductOrTechnology(com.microsoft.www.DownloadProductOrTechnology[] productOrTechnology) {
        this.productOrTechnology = productOrTechnology;
    }

    public com.microsoft.www.DownloadProductOrTechnology getProductOrTechnology(int i) {
        return productOrTechnology[i];
    }

    public void setProductOrTechnology(int i, com.microsoft.www.DownloadProductOrTechnology value) {
        this.productOrTechnology[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfDownloadProductOrTechnology)) return false;
        ArrayOfDownloadProductOrTechnology other = (ArrayOfDownloadProductOrTechnology) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.productOrTechnology==null && other.getProductOrTechnology()==null) || 
             (this.productOrTechnology!=null &&
              java.util.Arrays.equals(this.productOrTechnology, other.getProductOrTechnology())));
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
        if (getProductOrTechnology() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProductOrTechnology());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProductOrTechnology(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfDownloadProductOrTechnology.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadProductOrTechnology"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productOrTechnology");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "ProductOrTechnology"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "DownloadProductOrTechnology"));
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
