/**
 * _getApplicationDescriptionResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service.v1.cea.impl;

public class _getApplicationDescriptionResponse  implements java.io.Serializable {
    private org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase applicationDesciption;

    public _getApplicationDescriptionResponse() {
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase getApplicationDesciption() {
        return applicationDesciption;
    }

    public void setApplicationDesciption(org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase applicationDesciption) {
        this.applicationDesciption = applicationDesciption;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _getApplicationDescriptionResponse)) return false;
        _getApplicationDescriptionResponse other = (_getApplicationDescriptionResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.applicationDesciption==null && other.getApplicationDesciption()==null) || 
             (this.applicationDesciption!=null &&
              this.applicationDesciption.equals(other.getApplicationDesciption())));
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
        if (getApplicationDesciption() != null) {
            _hashCode += getApplicationDesciption().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_getApplicationDescriptionResponse.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "getApplicationDescriptionResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applicationDesciption");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:impl.cea.applications.astrogrid.org", "ApplicationDesciption"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationBase"));
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
