/**
 * InterfaceType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class InterfaceType  implements java.io.Serializable {
    private org.astrogrid.registry.server.generated.voresource.InvocationType invocation;
    private java.lang.String description;
    private org.astrogrid.registry.server.generated.voresource.AccessURLType accessURL;

    public InterfaceType() {
    }

    public org.astrogrid.registry.server.generated.voresource.InvocationType getInvocation() {
        return invocation;
    }

    public void setInvocation(org.astrogrid.registry.server.generated.voresource.InvocationType invocation) {
        this.invocation = invocation;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public org.astrogrid.registry.server.generated.voresource.AccessURLType getAccessURL() {
        return accessURL;
    }

    public void setAccessURL(org.astrogrid.registry.server.generated.voresource.AccessURLType accessURL) {
        this.accessURL = accessURL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InterfaceType)) return false;
        InterfaceType other = (InterfaceType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.invocation==null && other.getInvocation()==null) || 
             (this.invocation!=null &&
              this.invocation.equals(other.getInvocation()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.accessURL==null && other.getAccessURL()==null) || 
             (this.accessURL!=null &&
              this.accessURL.equals(other.getAccessURL())));
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
        if (getInvocation() != null) {
            _hashCode += getInvocation().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getAccessURL() != null) {
            _hashCode += getAccessURL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InterfaceType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "InterfaceType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("invocation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Invocation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "InvocationType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accessURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AccessURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AccessURLType"));
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
