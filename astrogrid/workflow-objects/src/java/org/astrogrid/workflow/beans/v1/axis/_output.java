/**
 * _output.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.workflow.beans.v1.axis;

public class _output  implements java.io.Serializable {
    private org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] parameter;

    public _output() {
    }

    public org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] getParameter() {
        return parameter;
    }

    public void setParameter(org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] parameter) {
        this.parameter = parameter;
    }

    public org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue getParameter(int i) {
        return parameter[i];
    }

    public void setParameter(int i, org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue value) {
        this.parameter[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _output)) return false;
        _output other = (_output) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.parameter==null && other.getParameter()==null) || 
             (this.parameter!=null &&
              java.util.Arrays.equals(this.parameter, other.getParameter())));
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
        if (getParameter() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getParameter());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getParameter(), i);
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
        new org.apache.axis.description.TypeDesc(_output.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "output"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "parameter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGParameterDefinition/v1", "parameterValue"));
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
