/**
 * IdentifierType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class IdentifierType  implements java.io.Serializable {
    private org.astrogrid.registry.server.generated.voresource.AuthorityIDType authorityID;
    private org.astrogrid.registry.server.generated.voresource.ResourceKeyType resourceKey;

    public IdentifierType() {
    }

    public org.astrogrid.registry.server.generated.voresource.AuthorityIDType getAuthorityID() {
        return authorityID;
    }

    public void setAuthorityID(org.astrogrid.registry.server.generated.voresource.AuthorityIDType authorityID) {
        this.authorityID = authorityID;
    }

    public org.astrogrid.registry.server.generated.voresource.ResourceKeyType getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(org.astrogrid.registry.server.generated.voresource.ResourceKeyType resourceKey) {
        this.resourceKey = resourceKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdentifierType)) return false;
        IdentifierType other = (IdentifierType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.authorityID==null && other.getAuthorityID()==null) || 
             (this.authorityID!=null &&
              this.authorityID.equals(other.getAuthorityID()))) &&
            ((this.resourceKey==null && other.getResourceKey()==null) || 
             (this.resourceKey!=null &&
              this.resourceKey.equals(other.getResourceKey())));
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
        if (getAuthorityID() != null) {
            _hashCode += getAuthorityID().hashCode();
        }
        if (getResourceKey() != null) {
            _hashCode += getResourceKey().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdentifierType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "IdentifierType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorityID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AuthorityID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AuthorityIDType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceKeyType"));
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
