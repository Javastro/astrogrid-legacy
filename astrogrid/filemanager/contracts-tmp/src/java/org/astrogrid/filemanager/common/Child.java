/**
 * Child.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.filemanager.common;

public class Child  implements java.io.Serializable {
    private org.astrogrid.filemanager.common.NodeName name;
    private org.astrogrid.filemanager.common.NodeIvorn ivorn;

    public Child() {
    }

    public org.astrogrid.filemanager.common.NodeName getName() {
        return name;
    }

    public void setName(org.astrogrid.filemanager.common.NodeName name) {
        this.name = name;
    }

    public org.astrogrid.filemanager.common.NodeIvorn getIvorn() {
        return ivorn;
    }

    public void setIvorn(org.astrogrid.filemanager.common.NodeIvorn ivorn) {
        this.ivorn = ivorn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Child)) return false;
        Child other = (Child) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.ivorn==null && other.getIvorn()==null) || 
             (this.ivorn!=null &&
              this.ivorn.equals(other.getIvorn())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getIvorn() != null) {
            _hashCode += getIvorn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Child.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "child"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeName"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ivorn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ivorn"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"));
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
