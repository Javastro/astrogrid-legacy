/**
 * BundlePreferences.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.filemanager.common;

public class BundlePreferences  implements java.io.Serializable {
    private java.lang.Integer prefetchDepth;
    private java.lang.Integer maxExtraNodes;
    private boolean fetchParents;

    public BundlePreferences() {
    }

    public java.lang.Integer getPrefetchDepth() {
        return prefetchDepth;
    }

    public void setPrefetchDepth(java.lang.Integer prefetchDepth) {
        this.prefetchDepth = prefetchDepth;
    }

    public java.lang.Integer getMaxExtraNodes() {
        return maxExtraNodes;
    }

    public void setMaxExtraNodes(java.lang.Integer maxExtraNodes) {
        this.maxExtraNodes = maxExtraNodes;
    }

    public boolean isFetchParents() {
        return fetchParents;
    }

    public void setFetchParents(boolean fetchParents) {
        this.fetchParents = fetchParents;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BundlePreferences)) return false;
        BundlePreferences other = (BundlePreferences) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.prefetchDepth==null && other.getPrefetchDepth()==null) || 
             (this.prefetchDepth!=null &&
              this.prefetchDepth.equals(other.getPrefetchDepth()))) &&
            ((this.maxExtraNodes==null && other.getMaxExtraNodes()==null) || 
             (this.maxExtraNodes!=null &&
              this.maxExtraNodes.equals(other.getMaxExtraNodes()))) &&
            this.fetchParents == other.isFetchParents();
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
        if (getPrefetchDepth() != null) {
            _hashCode += getPrefetchDepth().hashCode();
        }
        if (getMaxExtraNodes() != null) {
            _hashCode += getMaxExtraNodes().hashCode();
        }
        _hashCode += new Boolean(isFetchParents()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BundlePreferences.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "BundlePreferences"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prefetchDepth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "prefetchDepth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxExtraNodes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxExtraNodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "int"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fetchParents");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fetchParents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
