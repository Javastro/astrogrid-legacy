/**
 * CapabilityType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class CapabilityType  implements java.io.Serializable {
    private org.apache.axis.types.URI standardURL;
    private org.astrogrid.registry.server.generated.voresource.IdentifierType standardID;

    public CapabilityType() {
    }

    public org.apache.axis.types.URI getStandardURL() {
        return standardURL;
    }

    public void setStandardURL(org.apache.axis.types.URI standardURL) {
        this.standardURL = standardURL;
    }

    public org.astrogrid.registry.server.generated.voresource.IdentifierType getStandardID() {
        return standardID;
    }

    public void setStandardID(org.astrogrid.registry.server.generated.voresource.IdentifierType standardID) {
        this.standardID = standardID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CapabilityType)) return false;
        CapabilityType other = (CapabilityType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.standardURL==null && other.getStandardURL()==null) || 
             (this.standardURL!=null &&
              this.standardURL.equals(other.getStandardURL()))) &&
            ((this.standardID==null && other.getStandardID()==null) || 
             (this.standardID!=null &&
              this.standardID.equals(other.getStandardID())));
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
        if (getStandardURL() != null) {
            _hashCode += getStandardURL().hashCode();
        }
        if (getStandardID() != null) {
            _hashCode += getStandardID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CapabilityType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "CapabilityType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("standardURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "StandardURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("standardID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "StandardID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "IdentifierType"));
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
