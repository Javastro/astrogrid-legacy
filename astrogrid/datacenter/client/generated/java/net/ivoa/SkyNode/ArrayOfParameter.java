/**
 * ArrayOfParameter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class ArrayOfParameter  implements java.io.Serializable {
    private net.ivoa.SkyNode.Parameter[] parameter;

    public ArrayOfParameter() {
    }

    public net.ivoa.SkyNode.Parameter[] getParameter() {
        return parameter;
    }

    public void setParameter(net.ivoa.SkyNode.Parameter[] parameter) {
        this.parameter = parameter;
    }

    public net.ivoa.SkyNode.Parameter getParameter(int i) {
        return parameter[i];
    }

    public void setParameter(int i, net.ivoa.SkyNode.Parameter value) {
        this.parameter[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfParameter)) return false;
        ArrayOfParameter other = (ArrayOfParameter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.parameter==null && other.getParameter()==null) || 
             (this.parameter!=null &&
              java.util.Arrays.equals(this.parameter, other.getParameter())));
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
        if (getParameter() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getParameter());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getParameter(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfParameter.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfParameter"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameter");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Parameter"));
        elemField.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Parameter"));
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
