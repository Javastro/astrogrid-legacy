/**
 * ArrayOfMetaFunction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class ArrayOfMetaFunction  implements java.io.Serializable {
    private net.ivoa.SkyNode.MetaFunction[] metaFunction;

    public ArrayOfMetaFunction() {
    }

    public net.ivoa.SkyNode.MetaFunction[] getMetaFunction() {
        return metaFunction;
    }

    public void setMetaFunction(net.ivoa.SkyNode.MetaFunction[] metaFunction) {
        this.metaFunction = metaFunction;
    }

    public net.ivoa.SkyNode.MetaFunction getMetaFunction(int i) {
        return metaFunction[i];
    }

    public void setMetaFunction(int i, net.ivoa.SkyNode.MetaFunction value) {
        this.metaFunction[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfMetaFunction)) return false;
        ArrayOfMetaFunction other = (ArrayOfMetaFunction) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.metaFunction==null && other.getMetaFunction()==null) || 
             (this.metaFunction!=null &&
              java.util.Arrays.equals(this.metaFunction, other.getMetaFunction())));
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
        if (getMetaFunction() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMetaFunction());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMetaFunction(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfMetaFunction.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfMetaFunction"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metaFunction");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "MetaFunction"));
        elemField.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "MetaFunction"));
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
