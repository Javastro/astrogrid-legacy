/**
 * AliasSelectionItemType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class AliasSelectionItemType  extends org.astrogrid.desktop.modules.ivoa.adql.SelectionItemType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType expression;
    private java.lang.String as;  // attribute

    public AliasSelectionItemType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType getExpression() {
        return expression;
    }

    public void setExpression(org.astrogrid.desktop.modules.ivoa.adql.ScalarExpressionType expression) {
        this.expression = expression;
    }

    public java.lang.String getAs() {
        return as;
    }

    public void setAs(java.lang.String as) {
        this.as = as;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AliasSelectionItemType)) return false;
        AliasSelectionItemType other = (AliasSelectionItemType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.expression==null && other.getExpression()==null) || 
             (this.expression!=null &&
              this.expression.equals(other.getExpression()))) &&
            ((this.as==null && other.getAs()==null) || 
             (this.as!=null &&
              this.as.equals(other.getAs())));
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
        if (getExpression() != null) {
            _hashCode += getExpression().hashCode();
        }
        if (getAs() != null) {
            _hashCode += getAs().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AliasSelectionItemType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "aliasSelectionItemType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("as");
        attrField.setXmlName(new javax.xml.namespace.QName("", "As"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expression");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Expression"));
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
