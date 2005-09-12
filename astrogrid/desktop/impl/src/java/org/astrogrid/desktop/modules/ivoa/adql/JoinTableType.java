/**
 * JoinTableType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class JoinTableType  extends org.astrogrid.desktop.modules.ivoa.adql.FromTableType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.JointTableQualifierType qualifier;
    private org.astrogrid.desktop.modules.ivoa.adql.ArrayOfFromTableType tables;
    private org.astrogrid.desktop.modules.ivoa.adql.ComparisonPredType condition;

    public JoinTableType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.JointTableQualifierType getQualifier() {
        return qualifier;
    }

    public void setQualifier(org.astrogrid.desktop.modules.ivoa.adql.JointTableQualifierType qualifier) {
        this.qualifier = qualifier;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.ArrayOfFromTableType getTables() {
        return tables;
    }

    public void setTables(org.astrogrid.desktop.modules.ivoa.adql.ArrayOfFromTableType tables) {
        this.tables = tables;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.ComparisonPredType getCondition() {
        return condition;
    }

    public void setCondition(org.astrogrid.desktop.modules.ivoa.adql.ComparisonPredType condition) {
        this.condition = condition;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JoinTableType)) return false;
        JoinTableType other = (JoinTableType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.qualifier==null && other.getQualifier()==null) || 
             (this.qualifier!=null &&
              this.qualifier.equals(other.getQualifier()))) &&
            ((this.tables==null && other.getTables()==null) || 
             (this.tables!=null &&
              this.tables.equals(other.getTables()))) &&
            ((this.condition==null && other.getCondition()==null) || 
             (this.condition!=null &&
              this.condition.equals(other.getCondition())));
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
        if (getQualifier() != null) {
            _hashCode += getQualifier().hashCode();
        }
        if (getTables() != null) {
            _hashCode += getTables().hashCode();
        }
        if (getCondition() != null) {
            _hashCode += getCondition().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JoinTableType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "joinTableType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qualifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Qualifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "jointTableQualifierType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tables");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Tables"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "ArrayOfFromTableType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("condition");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Condition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "comparisonPredType"));
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
