/**
 * ArrayOfMetaColumn.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class ArrayOfMetaColumn  implements java.io.Serializable {
    private net.ivoa.SkyNode.MetaColumn[] metaColumn;

    public ArrayOfMetaColumn() {
    }

    public net.ivoa.SkyNode.MetaColumn[] getMetaColumn() {
        return metaColumn;
    }

    public void setMetaColumn(net.ivoa.SkyNode.MetaColumn[] metaColumn) {
        this.metaColumn = metaColumn;
    }

    public net.ivoa.SkyNode.MetaColumn getMetaColumn(int i) {
        return metaColumn[i];
    }

    public void setMetaColumn(int i, net.ivoa.SkyNode.MetaColumn value) {
        this.metaColumn[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfMetaColumn)) return false;
        ArrayOfMetaColumn other = (ArrayOfMetaColumn) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.metaColumn==null && other.getMetaColumn()==null) || 
             (this.metaColumn!=null &&
              java.util.Arrays.equals(this.metaColumn, other.getMetaColumn())));
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
        if (getMetaColumn() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMetaColumn());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMetaColumn(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfMetaColumn.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfMetaColumn"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metaColumn");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "MetaColumn"));
        elemField.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "MetaColumn"));
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
