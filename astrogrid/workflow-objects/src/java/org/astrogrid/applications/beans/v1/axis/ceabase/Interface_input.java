/**
 * Interface_input.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceabase;

public class Interface_input  implements java.io.Serializable {
    private org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef[] pref;

    public Interface_input() {
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef[] getPref() {
        return pref;
    }

    public void setPref(org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef[] pref) {
        this.pref = pref;
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef getPref(int i) {
        return pref[i];
    }

    public void setPref(int i, org.astrogrid.applications.beans.v1.axis.ceabase.ParameterRef value) {
        this.pref[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Interface_input)) return false;
        Interface_input other = (Interface_input) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.pref==null && other.getPref()==null) || 
             (this.pref!=null &&
              java.util.Arrays.equals(this.pref, other.getPref())));
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
        if (getPref() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPref());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPref(), i);
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
        new org.apache.axis.description.TypeDesc(Interface_input.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "Interface>input"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pref");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "pref"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "parameterRef"));
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
