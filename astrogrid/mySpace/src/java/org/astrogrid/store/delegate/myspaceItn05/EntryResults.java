/**
 * EntryResults.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.myspace.delegate;

public class EntryResults  implements java.io.Serializable {
    private long creationDate;
    private int entryId;
    private java.lang.String entryName;
    private java.lang.String entryUri;
    private long expiryDate;
    private java.lang.String ownerId;
    private java.lang.String permissionsMask;
    private int size;
    private int type;

    public EntryResults() {
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public java.lang.String getEntryName() {
        return entryName;
    }

    public void setEntryName(java.lang.String entryName) {
        this.entryName = entryName;
    }

    public java.lang.String getEntryUri() {
        return entryUri;
    }

    public void setEntryUri(java.lang.String entryUri) {
        this.entryUri = entryUri;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public java.lang.String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(java.lang.String ownerId) {
        this.ownerId = ownerId;
    }

    public java.lang.String getPermissionsMask() {
        return permissionsMask;
    }

    public void setPermissionsMask(java.lang.String permissionsMask) {
        this.permissionsMask = permissionsMask;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EntryResults)) return false;
        EntryResults other = (EntryResults) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.creationDate == other.getCreationDate() &&
            this.entryId == other.getEntryId() &&
            ((this.entryName==null && other.getEntryName()==null) || 
             (this.entryName!=null &&
              this.entryName.equals(other.getEntryName()))) &&
            ((this.entryUri==null && other.getEntryUri()==null) || 
             (this.entryUri!=null &&
              this.entryUri.equals(other.getEntryUri()))) &&
            this.expiryDate == other.getExpiryDate() &&
            ((this.ownerId==null && other.getOwnerId()==null) || 
             (this.ownerId!=null &&
              this.ownerId.equals(other.getOwnerId()))) &&
            ((this.permissionsMask==null && other.getPermissionsMask()==null) || 
             (this.permissionsMask!=null &&
              this.permissionsMask.equals(other.getPermissionsMask()))) &&
            this.size == other.getSize() &&
            this.type == other.getType();
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
        _hashCode += new Long(getCreationDate()).hashCode();
        _hashCode += getEntryId();
        if (getEntryName() != null) {
            _hashCode += getEntryName().hashCode();
        }
        if (getEntryUri() != null) {
            _hashCode += getEntryUri().hashCode();
        }
        _hashCode += new Long(getExpiryDate()).hashCode();
        if (getOwnerId() != null) {
            _hashCode += getOwnerId().hashCode();
        }
        if (getPermissionsMask() != null) {
            _hashCode += getPermissionsMask().hashCode();
        }
        _hashCode += getSize();
        _hashCode += getType();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EntryResults.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Kernel", "EntryResults"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "creationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entryId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entryName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entryName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entryUri");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entryUri"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expiryDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "expiryDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ownerId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ownerId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("permissionsMask");
        elemField.setXmlName(new javax.xml.namespace.QName("", "permissionsMask"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
