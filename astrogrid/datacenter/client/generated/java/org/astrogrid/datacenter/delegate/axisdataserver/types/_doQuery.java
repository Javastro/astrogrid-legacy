/**
 * _doQuery.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.delegate.axisdataserver.types;

public class _doQuery  implements java.io.Serializable {
    private java.lang.String resultsFormat;
    private org.astrogrid.datacenter.adql.generated.Select adql;

    public _doQuery() {
    }

    public java.lang.String getResultsFormat() {
        return resultsFormat;
    }

    public void setResultsFormat(java.lang.String resultsFormat) {
        this.resultsFormat = resultsFormat;
    }

    public org.astrogrid.datacenter.adql.generated.Select getAdql() {
        return adql;
    }

    public void setAdql(org.astrogrid.datacenter.adql.generated.Select adql) {
        this.adql = adql;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _doQuery)) return false;
        _doQuery other = (_doQuery) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.resultsFormat==null && other.getResultsFormat()==null) || 
             (this.resultsFormat!=null &&
              this.resultsFormat.equals(other.getResultsFormat()))) &&
            ((this.adql==null && other.getAdql()==null) || 
             (this.adql!=null &&
              this.adql.equals(other.getAdql())));
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
        if (getResultsFormat() != null) {
            _hashCode += getResultsFormat().hashCode();
        }
        if (getAdql() != null) {
            _hashCode += getAdql().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_doQuery.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", ">doQuery"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultsFormat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resultsFormat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("adql");
        elemField.setXmlName(new javax.xml.namespace.QName("", "adql"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/adql", "Select"));
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
