/**
 * _ApplicationList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceabase;

public class _ApplicationList  implements java.io.Serializable {
    private org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase[] applicationDefn;

    public _ApplicationList() {
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase[] getApplicationDefn() {
        return applicationDefn;
    }

    public void setApplicationDefn(org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase[] applicationDefn) {
        this.applicationDefn = applicationDefn;
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase getApplicationDefn(int i) {
        return applicationDefn[i];
    }

    public void setApplicationDefn(int i, org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase value) {
        this.applicationDefn[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _ApplicationList)) return false;
        _ApplicationList other = (_ApplicationList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.applicationDefn==null && other.getApplicationDefn()==null) || 
             (this.applicationDefn!=null &&
              java.util.Arrays.equals(this.applicationDefn, other.getApplicationDefn())));
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
        if (getApplicationDefn() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getApplicationDefn());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getApplicationDefn(), i);
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
        new org.apache.axis.description.TypeDesc(_ApplicationList.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applicationDefn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationDefn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "ApplicationBase"));
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
