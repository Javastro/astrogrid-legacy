/**
 * MessageType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.types.v1.cea.axis;

public class MessageType  implements java.io.Serializable, org.apache.axis.encoding.SimpleType {
    private java.lang.String value;
    private java.lang.String source;  // attribute
    private java.util.Calendar timestamp;  // attribute
    private org.astrogrid.jes.types.v1.cea.axis.LogLevel level;  // attribute
    private org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase phase;  // attribute

    public MessageType() {
    }

    // Simple Types must have a String constructor
    public MessageType(java.lang.String value) {
        this.value = new java.lang.String(value);
    }

    // Simple Types must have a toString for serializing the value
    public java.lang.String toString() {
        return value == null ? null : value.toString();
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }

    public java.lang.String getSource() {
        return source;
    }

    public void setSource(java.lang.String source) {
        this.source = source;
    }

    public java.util.Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.util.Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public org.astrogrid.jes.types.v1.cea.axis.LogLevel getLevel() {
        return level;
    }

    public void setLevel(org.astrogrid.jes.types.v1.cea.axis.LogLevel level) {
        this.level = level;
    }

    public org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase getPhase() {
        return phase;
    }

    public void setPhase(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase phase) {
        this.phase = phase;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MessageType)) return false;
        MessageType other = (MessageType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue()))) &&
            ((this.source==null && other.getSource()==null) || 
             (this.source!=null &&
              this.source.equals(other.getSource()))) &&
            ((this.timestamp==null && other.getTimestamp()==null) || 
             (this.timestamp!=null &&
              this.timestamp.equals(other.getTimestamp()))) &&
            ((this.level==null && other.getLevel()==null) || 
             (this.level!=null &&
              this.level.equals(other.getLevel()))) &&
            ((this.phase==null && other.getPhase()==null) || 
             (this.phase!=null &&
              this.phase.equals(other.getPhase())));
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
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        if (getSource() != null) {
            _hashCode += getSource().hashCode();
        }
        if (getTimestamp() != null) {
            _hashCode += getTimestamp().hashCode();
        }
        if (getLevel() != null) {
            _hashCode += getLevel().hashCode();
        }
        if (getPhase() != null) {
            _hashCode += getPhase().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MessageType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "message-type"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("source");
        attrField.setXmlName(new javax.xml.namespace.QName("", "source"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("timestamp");
        attrField.setXmlName(new javax.xml.namespace.QName("", "timestamp"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("level");
        attrField.setXmlName(new javax.xml.namespace.QName("", "level"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "log-level"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("phase");
        attrField.setXmlName(new javax.xml.namespace.QName("", "phase"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "execution-phase"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
          new  org.apache.axis.encoding.ser.SimpleSerializer(
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
          new  org.apache.axis.encoding.ser.SimpleDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
