/**
 * _GetAvailabilityResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class _GetAvailabilityResponse  implements java.io.Serializable {
    private net.ivoa.SkyNode.Availability getAvailabilityResult;

    public _GetAvailabilityResponse() {
    }

    public net.ivoa.SkyNode.Availability getGetAvailabilityResult() {
        return getAvailabilityResult;
    }

    public void setGetAvailabilityResult(net.ivoa.SkyNode.Availability getAvailabilityResult) {
        this.getAvailabilityResult = getAvailabilityResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _GetAvailabilityResponse)) return false;
        _GetAvailabilityResponse other = (_GetAvailabilityResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getAvailabilityResult==null && other.getGetAvailabilityResult()==null) || 
             (this.getAvailabilityResult!=null &&
              this.getAvailabilityResult.equals(other.getGetAvailabilityResult())));
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
        if (getGetAvailabilityResult() != null) {
            _hashCode += getGetAvailabilityResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_GetAvailabilityResponse.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", ">GetAvailabilityResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getAvailabilityResult");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "GetAvailabilityResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Availability"));
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
