/**
 * GetDataRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public class GetDataRequest  implements java.io.Serializable {
    private java.lang.String[] method;

    private org.astrogrid.stapsearch.ws.vso.Info info;

    private org.astrogrid.stapsearch.ws.vso.DataRequest[] data;

    public GetDataRequest() {
    }

    public GetDataRequest(
           java.lang.String[] method,
           org.astrogrid.stapsearch.ws.vso.Info info,
           org.astrogrid.stapsearch.ws.vso.DataRequest[] data) {
           this.method = method;
           this.info = info;
           this.data = data;
    }


    /**
     * Gets the method value for this GetDataRequest.
     * 
     * @return method
     */
    public java.lang.String[] getMethod() {
        return method;
    }


    /**
     * Sets the method value for this GetDataRequest.
     * 
     * @param method
     */
    public void setMethod(java.lang.String[] method) {
        this.method = method;
    }


    /**
     * Gets the info value for this GetDataRequest.
     * 
     * @return info
     */
    public org.astrogrid.stapsearch.ws.vso.Info getInfo() {
        return info;
    }


    /**
     * Sets the info value for this GetDataRequest.
     * 
     * @param info
     */
    public void setInfo(org.astrogrid.stapsearch.ws.vso.Info info) {
        this.info = info;
    }


    /**
     * Gets the data value for this GetDataRequest.
     * 
     * @return data
     */
    public org.astrogrid.stapsearch.ws.vso.DataRequest[] getData() {
        return data;
    }


    /**
     * Sets the data value for this GetDataRequest.
     * 
     * @param data
     */
    public void setData(org.astrogrid.stapsearch.ws.vso.DataRequest[] data) {
        this.data = data;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetDataRequest)) return false;
        GetDataRequest other = (GetDataRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.method==null && other.getMethod()==null) || 
             (this.method!=null &&
              java.util.Arrays.equals(this.method, other.getMethod()))) &&
            ((this.info==null && other.getInfo()==null) || 
             (this.info!=null &&
              this.info.equals(other.getInfo()))) &&
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              java.util.Arrays.equals(this.data, other.getData())));
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
        if (getMethod() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMethod());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMethod(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getInfo() != null) {
            _hashCode += getInfo().hashCode();
        }
        if (getData() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getData(), i);
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
        new org.apache.axis.description.TypeDesc(GetDataRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "GetDataRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("method");
        elemField.setXmlName(new javax.xml.namespace.QName("", "method"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("info");
        elemField.setXmlName(new javax.xml.namespace.QName("", "info"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Info"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "DataRequest"));
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
