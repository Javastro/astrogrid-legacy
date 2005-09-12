/**
 * ArrayOfFromTableType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class ArrayOfFromTableType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.FromTableType[] fromTableType;

    public ArrayOfFromTableType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.FromTableType[] getFromTableType() {
        return fromTableType;
    }

    public void setFromTableType(org.astrogrid.desktop.modules.ivoa.adql.FromTableType[] fromTableType) {
        this.fromTableType = fromTableType;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.FromTableType getFromTableType(int i) {
        return fromTableType[i];
    }

    public void setFromTableType(int i, org.astrogrid.desktop.modules.ivoa.adql.FromTableType value) {
        this.fromTableType[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfFromTableType)) return false;
        ArrayOfFromTableType other = (ArrayOfFromTableType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fromTableType==null && other.getFromTableType()==null) || 
             (this.fromTableType!=null &&
              java.util.Arrays.equals(this.fromTableType, other.getFromTableType())));
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
        if (getFromTableType() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFromTableType());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFromTableType(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfFromTableType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "ArrayOfFromTableType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fromTableType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "fromTableType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "fromTableType"));
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
