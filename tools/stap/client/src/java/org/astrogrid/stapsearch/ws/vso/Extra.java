/**
 * Extra.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public class Extra  implements java.io.Serializable {
    private org.astrogrid.stapsearch.ws.vso.Thumbnail thumbnail;

    public Extra() {
    }

    public Extra(
           org.astrogrid.stapsearch.ws.vso.Thumbnail thumbnail) {
           this.thumbnail = thumbnail;
    }


    /**
     * Gets the thumbnail value for this Extra.
     * 
     * @return thumbnail
     */
    public org.astrogrid.stapsearch.ws.vso.Thumbnail getThumbnail() {
        return thumbnail;
    }


    /**
     * Sets the thumbnail value for this Extra.
     * 
     * @param thumbnail
     */
    public void setThumbnail(org.astrogrid.stapsearch.ws.vso.Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Extra)) return false;
        Extra other = (Extra) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.thumbnail==null && other.getThumbnail()==null) || 
             (this.thumbnail!=null &&
              this.thumbnail.equals(other.getThumbnail())));
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
        if (getThumbnail() != null) {
            _hashCode += getThumbnail().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Extra.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Extra"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("thumbnail");
        elemField.setXmlName(new javax.xml.namespace.QName("", "thumbnail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Thumbnail"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
