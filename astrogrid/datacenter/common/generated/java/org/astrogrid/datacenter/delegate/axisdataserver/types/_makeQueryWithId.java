/**
 * _makeQueryWithId.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.delegate.axisdataserver.types;

public class _makeQueryWithId  implements java.io.Serializable {
    private org.astrogrid.datacenter.adql.generated.Select adql;
    private java.lang.String assignedId;

    public _makeQueryWithId() {
    }

    public org.astrogrid.datacenter.adql.generated.Select getAdql() {
        return adql;
    }

    public void setAdql(org.astrogrid.datacenter.adql.generated.Select adql) {
        this.adql = adql;
    }

    public java.lang.String getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(java.lang.String assignedId) {
        this.assignedId = assignedId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _makeQueryWithId)) return false;
        _makeQueryWithId other = (_makeQueryWithId) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.adql==null && other.getAdql()==null) || 
             (this.adql!=null &&
              this.adql.equals(other.getAdql()))) &&
            ((this.assignedId==null && other.getAssignedId()==null) || 
             (this.assignedId!=null &&
              this.assignedId.equals(other.getAssignedId())));
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
        if (getAdql() != null) {
            _hashCode += getAdql().hashCode();
        }
        if (getAssignedId() != null) {
            _hashCode += getAssignedId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_makeQueryWithId.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", ">makeQueryWithId"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adql");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adql"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/adql", "Select"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assignedId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "assignedId"));
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
