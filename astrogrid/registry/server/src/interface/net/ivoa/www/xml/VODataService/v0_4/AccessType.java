/**
 * AccessType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VODataService.v0_4;

public class AccessType  implements java.io.Serializable {
    private net.ivoa.www.xml.VODataService.v0_4.FormatType[] format;
    private net.ivoa.www.xml.VODataService.v0_4.RightsType rights;
    private org.astrogrid.registry.server.generated.voresource.AccessURLType accessURL;

    public AccessType() {
    }

    public net.ivoa.www.xml.VODataService.v0_4.FormatType[] getFormat() {
        return format;
    }

    public void setFormat(net.ivoa.www.xml.VODataService.v0_4.FormatType[] format) {
        this.format = format;
    }

    public net.ivoa.www.xml.VODataService.v0_4.FormatType getFormat(int i) {
        return format[i];
    }

    public void setFormat(int i, net.ivoa.www.xml.VODataService.v0_4.FormatType value) {
        this.format[i] = value;
    }

    public net.ivoa.www.xml.VODataService.v0_4.RightsType getRights() {
        return rights;
    }

    public void setRights(net.ivoa.www.xml.VODataService.v0_4.RightsType rights) {
        this.rights = rights;
    }

    public org.astrogrid.registry.server.generated.voresource.AccessURLType getAccessURL() {
        return accessURL;
    }

    public void setAccessURL(org.astrogrid.registry.server.generated.voresource.AccessURLType accessURL) {
        this.accessURL = accessURL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AccessType)) return false;
        AccessType other = (AccessType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.format==null && other.getFormat()==null) || 
             (this.format!=null &&
              java.util.Arrays.equals(this.format, other.getFormat()))) &&
            ((this.rights==null && other.getRights()==null) || 
             (this.rights!=null &&
              this.rights.equals(other.getRights()))) &&
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
        if (getFormat() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFormat());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFormat(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRights() != null) {
            _hashCode += getRights().hashCode();
        }
        if (getAccessURL() != null) {
            _hashCode += getAccessURL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AccessType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "AccessType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("format");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "Format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "FormatType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rights");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "Rights"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "RightsType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accessURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AccessURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AccessURLType"));
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
