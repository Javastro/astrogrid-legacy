/**
 * Node.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.filemanager.common;

public class Node  implements java.io.Serializable {
    private org.astrogrid.filemanager.common.NodeIvorn ivorn;
    private org.astrogrid.filemanager.common.NodeIvorn parent;
    private org.astrogrid.filemanager.common.Child[] child;
    private org.astrogrid.filemanager.common.NodeName name;
    private org.astrogrid.filemanager.common.NodeTypes type;
    private java.util.Calendar createDate;
    private java.util.Calendar modifyDate;
    private org.astrogrid.filemanager.common.DataLocation location;
    private java.lang.Long size;
    private org.astrogrid.filemanager.common.Attribute[] attributes;

    public Node() {
    }

    public org.astrogrid.filemanager.common.NodeIvorn getIvorn() {
        return ivorn;
    }

    public void setIvorn(org.astrogrid.filemanager.common.NodeIvorn ivorn) {
        this.ivorn = ivorn;
    }

    public org.astrogrid.filemanager.common.NodeIvorn getParent() {
        return parent;
    }

    public void setParent(org.astrogrid.filemanager.common.NodeIvorn parent) {
        this.parent = parent;
    }

    public org.astrogrid.filemanager.common.Child[] getChild() {
        return child;
    }

    public void setChild(org.astrogrid.filemanager.common.Child[] child) {
        this.child = child;
    }

    public org.astrogrid.filemanager.common.Child getChild(int i) {
        return child[i];
    }

    public void setChild(int i, org.astrogrid.filemanager.common.Child value) {
        this.child[i] = value;
    }

    public org.astrogrid.filemanager.common.NodeName getName() {
        return name;
    }

    public void setName(org.astrogrid.filemanager.common.NodeName name) {
        this.name = name;
    }

    public org.astrogrid.filemanager.common.NodeTypes getType() {
        return type;
    }

    public void setType(org.astrogrid.filemanager.common.NodeTypes type) {
        this.type = type;
    }

    public java.util.Calendar getCreateDate() {
        return createDate;
    }

    public void setCreateDate(java.util.Calendar createDate) {
        this.createDate = createDate;
    }

    public java.util.Calendar getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(java.util.Calendar modifyDate) {
        this.modifyDate = modifyDate;
    }

    public org.astrogrid.filemanager.common.DataLocation getLocation() {
        return location;
    }

    public void setLocation(org.astrogrid.filemanager.common.DataLocation location) {
        this.location = location;
    }

    public java.lang.Long getSize() {
        return size;
    }

    public void setSize(java.lang.Long size) {
        this.size = size;
    }

    public org.astrogrid.filemanager.common.Attribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(org.astrogrid.filemanager.common.Attribute[] attributes) {
        this.attributes = attributes;
    }

    public org.astrogrid.filemanager.common.Attribute getAttributes(int i) {
        return attributes[i];
    }

    public void setAttributes(int i, org.astrogrid.filemanager.common.Attribute value) {
        this.attributes[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Node)) return false;
        Node other = (Node) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ivorn==null && other.getIvorn()==null) || 
             (this.ivorn!=null &&
              this.ivorn.equals(other.getIvorn()))) &&
            ((this.parent==null && other.getParent()==null) || 
             (this.parent!=null &&
              this.parent.equals(other.getParent()))) &&
            ((this.child==null && other.getChild()==null) || 
             (this.child!=null &&
              java.util.Arrays.equals(this.child, other.getChild()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.createDate==null && other.getCreateDate()==null) || 
             (this.createDate!=null &&
              this.createDate.equals(other.getCreateDate()))) &&
            ((this.modifyDate==null && other.getModifyDate()==null) || 
             (this.modifyDate!=null &&
              this.modifyDate.equals(other.getModifyDate()))) &&
            ((this.location==null && other.getLocation()==null) || 
             (this.location!=null &&
              this.location.equals(other.getLocation()))) &&
            ((this.size==null && other.getSize()==null) || 
             (this.size!=null &&
              this.size.equals(other.getSize()))) &&
            ((this.attributes==null && other.getAttributes()==null) || 
             (this.attributes!=null &&
              java.util.Arrays.equals(this.attributes, other.getAttributes())));
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
        if (getIvorn() != null) {
            _hashCode += getIvorn().hashCode();
        }
        if (getParent() != null) {
            _hashCode += getParent().hashCode();
        }
        if (getChild() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getChild());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getChild(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getCreateDate() != null) {
            _hashCode += getCreateDate().hashCode();
        }
        if (getModifyDate() != null) {
            _hashCode += getModifyDate().hashCode();
        }
        if (getLocation() != null) {
            _hashCode += getLocation().hashCode();
        }
        if (getSize() != null) {
            _hashCode += getSize().hashCode();
        }
        if (getAttributes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Node.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "Node"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ivorn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ivorn"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parent");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parent"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeIvorn"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("child");
        elemField.setXmlName(new javax.xml.namespace.QName("", "child"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "child"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeName"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "NodeTypes"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "createDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modifyDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "modifyDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("location");
        elemField.setXmlName(new javax.xml.namespace.QName("", "location"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "DataLocation"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "long"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:astrogrid:schema:filemanager:FileManager:v0.1", "attribute"));
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
