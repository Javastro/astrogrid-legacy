/**
 * _QueryCost.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class _QueryCost  implements java.io.Serializable {
    private long planid;
    private net.ivoa.www.xml.ADQL.v0_7_4.SelectType select;

    public _QueryCost() {
    }

    public long getPlanid() {
        return planid;
    }

    public void setPlanid(long planid) {
        this.planid = planid;
    }

    public net.ivoa.www.xml.ADQL.v0_7_4.SelectType getSelect() {
        return select;
    }

    public void setSelect(net.ivoa.www.xml.ADQL.v0_7_4.SelectType select) {
        this.select = select;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _QueryCost)) return false;
        _QueryCost other = (_QueryCost) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.planid == other.getPlanid() &&
            ((this.select==null && other.getSelect()==null) || 
             (this.select!=null &&
              this.select.equals(other.getSelect())));
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
        _hashCode += new Long(getPlanid()).hashCode();
        if (getSelect() != null) {
            _hashCode += getSelect().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_QueryCost.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", ">QueryCost"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("planid");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "planid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("select");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Select"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType"));
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
