/**
 * _returnRegistryEntryResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service.v1.cea.impl;

public class _returnRegistryEntryResponse  implements java.io.Serializable {
    private org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn returnRegistryEntryReturn;

    public _returnRegistryEntryResponse() {
    }

    public org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn getReturnRegistryEntryReturn() {
        return returnRegistryEntryReturn;
    }

    public void setReturnRegistryEntryReturn(org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn returnRegistryEntryReturn) {
        this.returnRegistryEntryReturn = returnRegistryEntryReturn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _returnRegistryEntryResponse)) return false;
        _returnRegistryEntryResponse other = (_returnRegistryEntryResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.returnRegistryEntryReturn==null && other.getReturnRegistryEntryReturn()==null) || 
             (this.returnRegistryEntryReturn!=null &&
              this.returnRegistryEntryReturn.equals(other.getReturnRegistryEntryReturn())));
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
        if (getReturnRegistryEntryReturn() != null) {
            _hashCode += getReturnRegistryEntryReturn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_returnRegistryEntryResponse.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "returnRegistryEntryResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnRegistryEntryReturn");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "returnRegistryEntryReturn"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "returnRegistryEntryResponse>returnRegistryEntryReturn"));
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
