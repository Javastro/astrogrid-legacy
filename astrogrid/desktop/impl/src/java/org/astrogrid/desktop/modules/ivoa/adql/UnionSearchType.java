/**
 * UnionSearchType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class UnionSearchType  extends org.astrogrid.desktop.modules.ivoa.adql.SearchType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.SearchType[] condition;

    public UnionSearchType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.SearchType[] getCondition() {
        return condition;
    }

    public void setCondition(org.astrogrid.desktop.modules.ivoa.adql.SearchType[] condition) {
        this.condition = condition;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.SearchType getCondition(int i) {
        return condition[i];
    }

    public void setCondition(int i, org.astrogrid.desktop.modules.ivoa.adql.SearchType value) {
        this.condition[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnionSearchType)) return false;
        UnionSearchType other = (UnionSearchType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.condition==null && other.getCondition()==null) || 
             (this.condition!=null &&
              java.util.Arrays.equals(this.condition, other.getCondition())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getCondition() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCondition());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCondition(), i);
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
        new org.apache.axis.description.TypeDesc(UnionSearchType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "unionSearchType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("condition");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Condition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "searchType"));
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
