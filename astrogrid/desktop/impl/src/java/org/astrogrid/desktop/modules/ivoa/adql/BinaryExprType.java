/**
 * BinaryExprType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class BinaryExprType  extends org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType[] arg;
    private org.astrogrid.desktop.modules.ivoa.adql.BinaryOperatorType oper;  // attribute

    public BinaryExprType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType[] getArg() {
        return arg;
    }

    public void setArg(org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType[] arg) {
        this.arg = arg;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType getArg(int i) {
        return arg[i];
    }

    public void setArg(int i, org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType value) {
        this.arg[i] = value;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.BinaryOperatorType getOper() {
        return oper;
    }

    public void setOper(org.astrogrid.desktop.modules.ivoa.adql.BinaryOperatorType oper) {
        this.oper = oper;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BinaryExprType)) return false;
        BinaryExprType other = (BinaryExprType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.arg==null && other.getArg()==null) || 
             (this.arg!=null &&
              java.util.Arrays.equals(this.arg, other.getArg()))) &&
            ((this.oper==null && other.getOper()==null) || 
             (this.oper!=null &&
              this.oper.equals(other.getOper())));
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
        if (getArg() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getArg());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getArg(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOper() != null) {
            _hashCode += getOper().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BinaryExprType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "binaryExprType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("oper");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Oper"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "binaryOperatorType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arg");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Arg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "scalarExpressionType"));
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
