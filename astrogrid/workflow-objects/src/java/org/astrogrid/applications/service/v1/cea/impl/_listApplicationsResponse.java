/**
 * _listApplicationsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service.v1.cea.impl;

public class _listApplicationsResponse  implements java.io.Serializable {
    private java.lang.String listApplicationsReturn;

    public _listApplicationsResponse() {
    }

    public java.lang.String getListApplicationsReturn() {
        return listApplicationsReturn;
    }

    public void setListApplicationsReturn(java.lang.String listApplicationsReturn) {
        this.listApplicationsReturn = listApplicationsReturn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _listApplicationsResponse)) return false;
        _listApplicationsResponse other = (_listApplicationsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.listApplicationsReturn==null && other.getListApplicationsReturn()==null) || 
             (this.listApplicationsReturn!=null &&
              this.listApplicationsReturn.equals(other.getListApplicationsReturn())));
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
        if (getListApplicationsReturn() != null) {
            _hashCode += getListApplicationsReturn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_listApplicationsResponse.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "listApplicationsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listApplicationsReturn");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "listApplicationsReturn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
