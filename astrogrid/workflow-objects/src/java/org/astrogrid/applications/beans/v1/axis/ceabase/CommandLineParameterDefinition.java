/**
 * CommandLineParameterDefinition.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceabase;

public class CommandLineParameterDefinition  extends org.astrogrid.applications.beans.v1.axis.ceaparameters.BaseParameterDefinition  implements java.io.Serializable {
    private java.lang.String commandSwitch;  // attribute
    private java.math.BigInteger commandPosition;  // attribute
    private boolean stdio;  // attribute
    private org.astrogrid.applications.beans.v1.axis.ceabase.SwitchTypes switchType;  // attribute

    public CommandLineParameterDefinition() {
    }

    public java.lang.String getCommandSwitch() {
        return commandSwitch;
    }

    public void setCommandSwitch(java.lang.String commandSwitch) {
        this.commandSwitch = commandSwitch;
    }

    public java.math.BigInteger getCommandPosition() {
        return commandPosition;
    }

    public void setCommandPosition(java.math.BigInteger commandPosition) {
        this.commandPosition = commandPosition;
    }

    public boolean isStdio() {
        return stdio;
    }

    public void setStdio(boolean stdio) {
        this.stdio = stdio;
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.SwitchTypes getSwitchType() {
        return switchType;
    }

    public void setSwitchType(org.astrogrid.applications.beans.v1.axis.ceabase.SwitchTypes switchType) {
        this.switchType = switchType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CommandLineParameterDefinition)) return false;
        CommandLineParameterDefinition other = (CommandLineParameterDefinition) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.commandSwitch==null && other.getCommandSwitch()==null) || 
             (this.commandSwitch!=null &&
              this.commandSwitch.equals(other.getCommandSwitch()))) &&
            ((this.commandPosition==null && other.getCommandPosition()==null) || 
             (this.commandPosition!=null &&
              this.commandPosition.equals(other.getCommandPosition()))) &&
            this.stdio == other.isStdio() &&
            ((this.switchType==null && other.getSwitchType()==null) || 
             (this.switchType!=null &&
              this.switchType.equals(other.getSwitchType())));
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
        if (getCommandSwitch() != null) {
            _hashCode += getCommandSwitch().hashCode();
        }
        if (getCommandPosition() != null) {
            _hashCode += getCommandPosition().hashCode();
        }
        _hashCode += new Boolean(isStdio()).hashCode();
        if (getSwitchType() != null) {
            _hashCode += getSwitchType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CommandLineParameterDefinition.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "CommandLineParameterDefinition"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("commandSwitch");
        attrField.setXmlName(new javax.xml.namespace.QName("", "commandSwitch"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("commandPosition");
        attrField.setXmlName(new javax.xml.namespace.QName("", "commandPosition"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("stdio");
        attrField.setXmlName(new javax.xml.namespace.QName("", "stdio"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("switchType");
        attrField.setXmlName(new javax.xml.namespace.QName("", "switchType"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "switchTypes"));
        typeDesc.addFieldDesc(attrField);
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
