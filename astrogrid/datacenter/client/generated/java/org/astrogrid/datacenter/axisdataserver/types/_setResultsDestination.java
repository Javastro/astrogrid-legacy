/**
 * _setResultsDestination.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.axisdataserver.types;

public class _setResultsDestination  implements java.io.Serializable {
    private org.astrogrid.datacenter.axisdataserver.types._QueryId queryId;
    private org.apache.axis.types.URI myspaceUrl;

    public _setResultsDestination() {
    }

    public org.astrogrid.datacenter.axisdataserver.types._QueryId getQueryId() {
        return queryId;
    }

    public void setQueryId(org.astrogrid.datacenter.axisdataserver.types._QueryId queryId) {
        this.queryId = queryId;
    }

    public org.apache.axis.types.URI getMyspaceUrl() {
        return myspaceUrl;
    }

    public void setMyspaceUrl(org.apache.axis.types.URI myspaceUrl) {
        this.myspaceUrl = myspaceUrl;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _setResultsDestination)) return false;
        _setResultsDestination other = (_setResultsDestination) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.queryId==null && other.getQueryId()==null) || 
             (this.queryId!=null &&
              this.queryId.equals(other.getQueryId()))) &&
            ((this.myspaceUrl==null && other.getMyspaceUrl()==null) || 
             (this.myspaceUrl!=null &&
              this.myspaceUrl.equals(other.getMyspaceUrl())));
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
        if (getQueryId() != null) {
            _hashCode += getQueryId().hashCode();
        }
        if (getMyspaceUrl() != null) {
            _hashCode += getMyspaceUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_setResultsDestination.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", ">setResultsDestination"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", "QueryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/datacenter/It04/dataserver/v1/types", ">QueryId"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("myspaceUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "myspaceUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
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
