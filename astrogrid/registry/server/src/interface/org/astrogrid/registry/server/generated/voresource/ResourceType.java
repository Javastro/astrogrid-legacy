/**
 * ResourceType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class ResourceType  implements java.io.Serializable {
    private org.astrogrid.registry.server.generated.voresource.IdentifierType identifier;
    private java.lang.String title;
    private java.lang.String shortName;
    private org.astrogrid.registry.server.generated.voresource.SummaryType summary;
    private org.astrogrid.registry.server.generated.voresource.CategoryType[] type;
    private org.astrogrid.registry.server.generated.voresource.RelatedResourceType[] relatedResource;
    private org.astrogrid.registry.server.generated.voresource.LogicalIdentifierType[] logicalIdentifier;
    private org.astrogrid.registry.server.generated.voresource.CurationType curation;
    private java.lang.String[] subject;
    private org.astrogrid.registry.server.generated.voresource.ContentLevelType[] contentLevel;
    private java.util.Date created;  // attribute
    private java.util.Date updated;  // attribute
    private org.astrogrid.registry.server.generated.voresource.ResourceType_status status;  // attribute

    public ResourceType() {
    }

    public org.astrogrid.registry.server.generated.voresource.IdentifierType getIdentifier() {
        return identifier;
    }

    public void setIdentifier(org.astrogrid.registry.server.generated.voresource.IdentifierType identifier) {
        this.identifier = identifier;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getShortName() {
        return shortName;
    }

    public void setShortName(java.lang.String shortName) {
        this.shortName = shortName;
    }

    public org.astrogrid.registry.server.generated.voresource.SummaryType getSummary() {
        return summary;
    }

    public void setSummary(org.astrogrid.registry.server.generated.voresource.SummaryType summary) {
        this.summary = summary;
    }

    public org.astrogrid.registry.server.generated.voresource.CategoryType[] getType() {
        return type;
    }

    public void setType(org.astrogrid.registry.server.generated.voresource.CategoryType[] type) {
        this.type = type;
    }

    public org.astrogrid.registry.server.generated.voresource.CategoryType getType(int i) {
        return type[i];
    }

    public void setType(int i, org.astrogrid.registry.server.generated.voresource.CategoryType value) {
        this.type[i] = value;
    }

    public org.astrogrid.registry.server.generated.voresource.RelatedResourceType[] getRelatedResource() {
        return relatedResource;
    }

    public void setRelatedResource(org.astrogrid.registry.server.generated.voresource.RelatedResourceType[] relatedResource) {
        this.relatedResource = relatedResource;
    }

    public org.astrogrid.registry.server.generated.voresource.RelatedResourceType getRelatedResource(int i) {
        return relatedResource[i];
    }

    public void setRelatedResource(int i, org.astrogrid.registry.server.generated.voresource.RelatedResourceType value) {
        this.relatedResource[i] = value;
    }

    public org.astrogrid.registry.server.generated.voresource.LogicalIdentifierType[] getLogicalIdentifier() {
        return logicalIdentifier;
    }

    public void setLogicalIdentifier(org.astrogrid.registry.server.generated.voresource.LogicalIdentifierType[] logicalIdentifier) {
        this.logicalIdentifier = logicalIdentifier;
    }

    public org.astrogrid.registry.server.generated.voresource.LogicalIdentifierType getLogicalIdentifier(int i) {
        return logicalIdentifier[i];
    }

    public void setLogicalIdentifier(int i, org.astrogrid.registry.server.generated.voresource.LogicalIdentifierType value) {
        this.logicalIdentifier[i] = value;
    }

    public org.astrogrid.registry.server.generated.voresource.CurationType getCuration() {
        return curation;
    }

    public void setCuration(org.astrogrid.registry.server.generated.voresource.CurationType curation) {
        this.curation = curation;
    }

    public java.lang.String[] getSubject() {
        return subject;
    }

    public void setSubject(java.lang.String[] subject) {
        this.subject = subject;
    }

    public java.lang.String getSubject(int i) {
        return subject[i];
    }

    public void setSubject(int i, java.lang.String value) {
        this.subject[i] = value;
    }

    public org.astrogrid.registry.server.generated.voresource.ContentLevelType[] getContentLevel() {
        return contentLevel;
    }

    public void setContentLevel(org.astrogrid.registry.server.generated.voresource.ContentLevelType[] contentLevel) {
        this.contentLevel = contentLevel;
    }

    public org.astrogrid.registry.server.generated.voresource.ContentLevelType getContentLevel(int i) {
        return contentLevel[i];
    }

    public void setContentLevel(int i, org.astrogrid.registry.server.generated.voresource.ContentLevelType value) {
        this.contentLevel[i] = value;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public void setCreated(java.util.Date created) {
        this.created = created;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public void setUpdated(java.util.Date updated) {
        this.updated = updated;
    }

    public org.astrogrid.registry.server.generated.voresource.ResourceType_status getStatus() {
        return status;
    }

    public void setStatus(org.astrogrid.registry.server.generated.voresource.ResourceType_status status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResourceType)) return false;
        ResourceType other = (ResourceType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identifier==null && other.getIdentifier()==null) || 
             (this.identifier!=null &&
              this.identifier.equals(other.getIdentifier()))) &&
            ((this.title==null && other.getTitle()==null) || 
             (this.title!=null &&
              this.title.equals(other.getTitle()))) &&
            ((this.shortName==null && other.getShortName()==null) || 
             (this.shortName!=null &&
              this.shortName.equals(other.getShortName()))) &&
            ((this.summary==null && other.getSummary()==null) || 
             (this.summary!=null &&
              this.summary.equals(other.getSummary()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              java.util.Arrays.equals(this.type, other.getType()))) &&
            ((this.relatedResource==null && other.getRelatedResource()==null) || 
             (this.relatedResource!=null &&
              java.util.Arrays.equals(this.relatedResource, other.getRelatedResource()))) &&
            ((this.logicalIdentifier==null && other.getLogicalIdentifier()==null) || 
             (this.logicalIdentifier!=null &&
              java.util.Arrays.equals(this.logicalIdentifier, other.getLogicalIdentifier()))) &&
            ((this.curation==null && other.getCuration()==null) || 
             (this.curation!=null &&
              this.curation.equals(other.getCuration()))) &&
            ((this.subject==null && other.getSubject()==null) || 
             (this.subject!=null &&
              java.util.Arrays.equals(this.subject, other.getSubject()))) &&
            ((this.contentLevel==null && other.getContentLevel()==null) || 
             (this.contentLevel!=null &&
              java.util.Arrays.equals(this.contentLevel, other.getContentLevel()))) &&
            ((this.created==null && other.getCreated()==null) || 
             (this.created!=null &&
              this.created.equals(other.getCreated()))) &&
            ((this.updated==null && other.getUpdated()==null) || 
             (this.updated!=null &&
              this.updated.equals(other.getUpdated()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        if (getIdentifier() != null) {
            _hashCode += getIdentifier().hashCode();
        }
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        if (getShortName() != null) {
            _hashCode += getShortName().hashCode();
        }
        if (getSummary() != null) {
            _hashCode += getSummary().hashCode();
        }
        if (getType() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getType());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getType(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRelatedResource() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRelatedResource());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRelatedResource(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLogicalIdentifier() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLogicalIdentifier());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLogicalIdentifier(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCuration() != null) {
            _hashCode += getCuration().hashCode();
        }
        if (getSubject() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubject());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSubject(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getContentLevel() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContentLevel());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContentLevel(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCreated() != null) {
            _hashCode += getCreated().hashCode();
        }
        if (getUpdated() != null) {
            _hashCode += getUpdated().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResourceType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("created");
        attrField.setXmlName(new javax.xml.namespace.QName("", "created"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("updated");
        attrField.setXmlName(new javax.xml.namespace.QName("", "updated"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "date"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("status");
        attrField.setXmlName(new javax.xml.namespace.QName("", "status"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceType>status"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Identifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "IdentifierType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shortName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ShortName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("summary");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Summary"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "SummaryType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "categoryType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relatedResource");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "RelatedResource"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "RelatedResourceType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logicalIdentifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "LogicalIdentifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "LogicalIdentifierType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("curation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Curation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "CurationType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subject");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Subject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ContentLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ContentLevelType"));
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
