/**
 * PlanElement.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class PlanElement  implements java.io.Serializable {
    private net.ivoa.www.xml.ADQL.v0_7_4.SelectType statement;
    private java.lang.String target;
    private net.ivoa.SkyNode.ArrayOfString hosts;

    public PlanElement() {
    }

    public net.ivoa.www.xml.ADQL.v0_7_4.SelectType getStatement() {
        return statement;
    }

    public void setStatement(net.ivoa.www.xml.ADQL.v0_7_4.SelectType statement) {
        this.statement = statement;
    }

    public java.lang.String getTarget() {
        return target;
    }

    public void setTarget(java.lang.String target) {
        this.target = target;
    }

    public net.ivoa.SkyNode.ArrayOfString getHosts() {
        return hosts;
    }

    public void setHosts(net.ivoa.SkyNode.ArrayOfString hosts) {
        this.hosts = hosts;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PlanElement)) return false;
        PlanElement other = (PlanElement) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.statement==null && other.getStatement()==null) || 
             (this.statement!=null &&
              this.statement.equals(other.getStatement()))) &&
            ((this.target==null && other.getTarget()==null) || 
             (this.target!=null &&
              this.target.equals(other.getTarget()))) &&
            ((this.hosts==null && other.getHosts()==null) || 
             (this.hosts!=null &&
              this.hosts.equals(other.getHosts())));
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
        if (getStatement() != null) {
            _hashCode += getStatement().hashCode();
        }
        if (getTarget() != null) {
            _hashCode += getTarget().hashCode();
        }
        if (getHosts() != null) {
            _hashCode += getHosts().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PlanElement.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "PlanElement"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statement");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Statement"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("target");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Target"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hosts");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Hosts"));
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
