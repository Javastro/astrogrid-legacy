/**
 * _tool.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.workflow.beans.v1.axis;

public class _tool  implements java.io.Serializable {
    private org.astrogrid.workflow.beans.v1.axis._input input;
    private org.astrogrid.workflow.beans.v1.axis._output output;
    private java.lang.String name;  // attribute
    private java.lang.String _interface;  // attribute

    public _tool() {
    }

    public org.astrogrid.workflow.beans.v1.axis._input getInput() {
        return input;
    }

    public void setInput(org.astrogrid.workflow.beans.v1.axis._input input) {
        this.input = input;
    }

    public org.astrogrid.workflow.beans.v1.axis._output getOutput() {
        return output;
    }

    public void setOutput(org.astrogrid.workflow.beans.v1.axis._output output) {
        this.output = output;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String get_interface() {
        return _interface;
    }

    public void set_interface(java.lang.String _interface) {
        this._interface = _interface;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _tool)) return false;
        _tool other = (_tool) obj;
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
              this.input.equals(other.getInput()))) &&
            ((this.output==null && other.getOutput()==null) || 
             (this.output!=null &&
              this.output.equals(other.getOutput()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this._interface==null && other.get_interface()==null) || 
             (this._interface!=null &&
              this._interface.equals(other.get_interface())));
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
            _hashCode += getInput().hashCode();
        }
        if (getOutput() != null) {
            _hashCode += getOutput().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (get_interface() != null) {
            _hashCode += get_interface().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_tool.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "tool"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("_interface");
        attrField.setXmlName(new javax.xml.namespace.QName("", "interface"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("input");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "input"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "input"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("output");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "output"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "output"));
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
