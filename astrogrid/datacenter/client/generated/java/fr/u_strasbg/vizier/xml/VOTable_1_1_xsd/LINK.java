/**
 * LINK.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package fr.u_strasbg.vizier.xml.VOTable_1_1_xsd;

public class LINK  implements java.io.Serializable {
    private org.apache.axis.types.Id ID;  // attribute
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.LINKContentrole contentRole;  // attribute
    private org.apache.axis.types.Token contentType;  // attribute
    private java.lang.String title;  // attribute
    private java.lang.String value;  // attribute
    private org.apache.axis.types.URI href;  // attribute
    private org.apache.axis.types.Token gref;  // attribute
    private org.apache.axis.types.URI action;  // attribute

    public LINK() {
    }

    public org.apache.axis.types.Id getID() {
        return ID;
    }

    public void setID(org.apache.axis.types.Id ID) {
        this.ID = ID;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.LINKContentrole getContentRole() {
        return contentRole;
    }

    public void setContentRole(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.LINKContentrole contentRole) {
        this.contentRole = contentRole;
    }

    public org.apache.axis.types.Token getContentType() {
        return contentType;
    }

    public void setContentType(org.apache.axis.types.Token contentType) {
        this.contentType = contentType;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    public org.apache.axis.types.URI getHref() {
        return href;
    }

    public void setHref(org.apache.axis.types.URI href) {
        this.href = href;
    }

    public org.apache.axis.types.Token getGref() {
        return gref;
    }

    public void setGref(org.apache.axis.types.Token gref) {
        this.gref = gref;
    }

    public org.apache.axis.types.URI getAction() {
        return action;
    }

    public void setAction(org.apache.axis.types.URI action) {
        this.action = action;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LINK)) return false;
        LINK other = (LINK) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.contentRole==null && other.getContentRole()==null) || 
             (this.contentRole!=null &&
              this.contentRole.equals(other.getContentRole()))) &&
            ((this.contentType==null && other.getContentType()==null) || 
             (this.contentType!=null &&
              this.contentType.equals(other.getContentType()))) &&
            ((this.title==null && other.getTitle()==null) || 
             (this.title!=null &&
              this.title.equals(other.getTitle()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue()))) &&
            ((this.href==null && other.getHref()==null) || 
             (this.href!=null &&
              this.href.equals(other.getHref()))) &&
            ((this.gref==null && other.getGref()==null) || 
             (this.gref!=null &&
              this.gref.equals(other.getGref()))) &&
            ((this.action==null && other.getAction()==null) || 
             (this.action!=null &&
              this.action.equals(other.getAction())));
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
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getContentRole() != null) {
            _hashCode += getContentRole().hashCode();
        }
        if (getContentType() != null) {
            _hashCode += getContentType().hashCode();
        }
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        if (getHref() != null) {
            _hashCode += getHref().hashCode();
        }
        if (getGref() != null) {
            _hashCode += getGref().hashCode();
        }
        if (getAction() != null) {
            _hashCode += getAction().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LINK.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "LINK"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("contentRole");
        attrField.setXmlName(new javax.xml.namespace.QName("", "content-role"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "LINKContentrole"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("contentType");
        attrField.setXmlName(new javax.xml.namespace.QName("", "content-type"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("title");
        attrField.setXmlName(new javax.xml.namespace.QName("", "title"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("value");
        attrField.setXmlName(new javax.xml.namespace.QName("", "value"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("href");
        attrField.setXmlName(new javax.xml.namespace.QName("", "href"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("gref");
        attrField.setXmlName(new javax.xml.namespace.QName("", "gref"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("action");
        attrField.setXmlName(new javax.xml.namespace.QName("", "action"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
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
