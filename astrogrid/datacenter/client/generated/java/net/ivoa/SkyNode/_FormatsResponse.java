/**
 * _FormatsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class _FormatsResponse  implements java.io.Serializable {
    private net.ivoa.SkyNode.ArrayOfString formatsResult;

    public _FormatsResponse() {
    }

    public net.ivoa.SkyNode.ArrayOfString getFormatsResult() {
        return formatsResult;
    }

    public void setFormatsResult(net.ivoa.SkyNode.ArrayOfString formatsResult) {
        this.formatsResult = formatsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _FormatsResponse)) return false;
        _FormatsResponse other = (_FormatsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.formatsResult==null && other.getFormatsResult()==null) || 
             (this.formatsResult!=null &&
              this.formatsResult.equals(other.getFormatsResult())));
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
        if (getFormatsResult() != null) {
            _hashCode += getFormatsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_FormatsResponse.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", ">FormatsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("formatsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "FormatsResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfString"));
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
