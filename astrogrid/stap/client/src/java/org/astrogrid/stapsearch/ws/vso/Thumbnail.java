/**
 * Thumbnail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public class Thumbnail  implements java.io.Serializable {
    private java.lang.String hires;

    private java.lang.String lowres;

    public Thumbnail() {
    }

    public Thumbnail(
           java.lang.String hires,
           java.lang.String lowres) {
           this.hires = hires;
           this.lowres = lowres;
    }


    /**
     * Gets the hires value for this Thumbnail.
     * 
     * @return hires
     */
    public java.lang.String getHires() {
        return hires;
    }


    /**
     * Sets the hires value for this Thumbnail.
     * 
     * @param hires
     */
    public void setHires(java.lang.String hires) {
        this.hires = hires;
    }


    /**
     * Gets the lowres value for this Thumbnail.
     * 
     * @return lowres
     */
    public java.lang.String getLowres() {
        return lowres;
    }


    /**
     * Sets the lowres value for this Thumbnail.
     * 
     * @param lowres
     */
    public void setLowres(java.lang.String lowres) {
        this.lowres = lowres;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Thumbnail)) return false;
        Thumbnail other = (Thumbnail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.hires==null && other.getHires()==null) || 
             (this.hires!=null &&
              this.hires.equals(other.getHires()))) &&
            ((this.lowres==null && other.getLowres()==null) || 
             (this.lowres!=null &&
              this.lowres.equals(other.getLowres())));
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
        if (getHires() != null) {
            _hashCode += getHires().hashCode();
        }
        if (getLowres() != null) {
            _hashCode += getLowres().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Thumbnail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Thumbnail"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hires");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hires"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lowres");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lowres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
