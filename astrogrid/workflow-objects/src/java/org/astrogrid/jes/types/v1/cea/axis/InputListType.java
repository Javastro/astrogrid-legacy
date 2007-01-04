/**
 * InputListType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.types.v1.cea.axis;

public class InputListType  implements java.io.Serializable {
    private org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] input;

    public InputListType() {
    }

    public org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] getInput() {
        return input;
    }

    public void setInput(org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue[] input) {
        this.input = input;
    }

    public org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue getInput(int i) {
        return input[i];
    }

    public void setInput(int i, org.astrogrid.applications.beans.v1.axis.ceaparameters.ParameterValue value) {
        this.input[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InputListType)) return false;
        InputListType other = (InputListType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.input==null && other.getInput()==null) || 
             (this.input!=null &&
              java.util.Arrays.equals(this.input, other.getInput())));
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
        if (getInput() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInput());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInput(), i);
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
        new org.apache.axis.description.TypeDesc(InputListType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "input-list-type"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("input");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "input"));
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
