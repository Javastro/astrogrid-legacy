/**
 * CurationType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class CurationType  implements java.io.Serializable {
    private org.astrogrid.registry.server.generated.voresource.ResourceReferenceType publisher;
    private org.astrogrid.registry.server.generated.voresource.ContactType contact;
    private org.astrogrid.registry.server.generated.voresource.DateType[] date;
    private org.astrogrid.registry.server.generated.voresource.CreatorType creator;
    private org.astrogrid.registry.server.generated.voresource.NameReferenceType[] contributor;
    private java.lang.String version;

    public CurationType() {
    }

    public org.astrogrid.registry.server.generated.voresource.ResourceReferenceType getPublisher() {
        return publisher;
    }

    public void setPublisher(org.astrogrid.registry.server.generated.voresource.ResourceReferenceType publisher) {
        this.publisher = publisher;
    }

    public org.astrogrid.registry.server.generated.voresource.ContactType getContact() {
        return contact;
    }

    public void setContact(org.astrogrid.registry.server.generated.voresource.ContactType contact) {
        this.contact = contact;
    }

    public org.astrogrid.registry.server.generated.voresource.DateType[] getDate() {
        return date;
    }

    public void setDate(org.astrogrid.registry.server.generated.voresource.DateType[] date) {
        this.date = date;
    }

    public org.astrogrid.registry.server.generated.voresource.DateType getDate(int i) {
        return date[i];
    }

    public void setDate(int i, org.astrogrid.registry.server.generated.voresource.DateType value) {
        this.date[i] = value;
    }

    public org.astrogrid.registry.server.generated.voresource.CreatorType getCreator() {
        return creator;
    }

    public void setCreator(org.astrogrid.registry.server.generated.voresource.CreatorType creator) {
        this.creator = creator;
    }

    public org.astrogrid.registry.server.generated.voresource.NameReferenceType[] getContributor() {
        return contributor;
    }

    public void setContributor(org.astrogrid.registry.server.generated.voresource.NameReferenceType[] contributor) {
        this.contributor = contributor;
    }

    public org.astrogrid.registry.server.generated.voresource.NameReferenceType getContributor(int i) {
        return contributor[i];
    }

    public void setContributor(int i, org.astrogrid.registry.server.generated.voresource.NameReferenceType value) {
        this.contributor[i] = value;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CurationType)) return false;
        CurationType other = (CurationType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.publisher==null && other.getPublisher()==null) || 
             (this.publisher!=null &&
              this.publisher.equals(other.getPublisher()))) &&
            ((this.contact==null && other.getContact()==null) || 
             (this.contact!=null &&
              this.contact.equals(other.getContact()))) &&
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              java.util.Arrays.equals(this.date, other.getDate()))) &&
            ((this.creator==null && other.getCreator()==null) || 
             (this.creator!=null &&
              this.creator.equals(other.getCreator()))) &&
            ((this.contributor==null && other.getContributor()==null) || 
             (this.contributor!=null &&
              java.util.Arrays.equals(this.contributor, other.getContributor()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion())));
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
        if (getPublisher() != null) {
            _hashCode += getPublisher().hashCode();
        }
        if (getContact() != null) {
            _hashCode += getContact().hashCode();
        }
        if (getDate() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDate());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDate(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCreator() != null) {
            _hashCode += getCreator().hashCode();
        }
        if (getContributor() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContributor());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContributor(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CurationType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "CurationType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publisher");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Publisher"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceReferenceType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contact");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Contact"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ContactType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "DateType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Creator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "CreatorType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contributor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Contributor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "NameReferenceType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
