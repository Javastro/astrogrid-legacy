/**
 * _step.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.workflow.beans.v1.axis;

public class _step  extends org.astrogrid.workflow.beans.v1.axis.AbstractActivity  implements java.io.Serializable {
    private java.lang.String description;
    private org.astrogrid.workflow.beans.v1.axis._tool tool;
    private org.astrogrid.jes.beans.v1.axis.executionrecord._stepExecutionRecord[] stepExecutionRecord;
    private java.lang.String name;  // attribute
    private org.astrogrid.workflow.beans.v1.axis.JoinType joinCondition;  // attribute
    private int stepNumber;  // attribute
    private int sequenceNumber;  // attribute

    public _step() {
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public org.astrogrid.workflow.beans.v1.axis._tool getTool() {
        return tool;
    }

    public void setTool(org.astrogrid.workflow.beans.v1.axis._tool tool) {
        this.tool = tool;
    }

    public org.astrogrid.jes.beans.v1.axis.executionrecord._stepExecutionRecord[] getStepExecutionRecord() {
        return stepExecutionRecord;
    }

    public void setStepExecutionRecord(org.astrogrid.jes.beans.v1.axis.executionrecord._stepExecutionRecord[] stepExecutionRecord) {
        this.stepExecutionRecord = stepExecutionRecord;
    }

    public org.astrogrid.jes.beans.v1.axis.executionrecord._stepExecutionRecord getStepExecutionRecord(int i) {
        return stepExecutionRecord[i];
    }

    public void setStepExecutionRecord(int i, org.astrogrid.jes.beans.v1.axis.executionrecord._stepExecutionRecord value) {
        this.stepExecutionRecord[i] = value;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public org.astrogrid.workflow.beans.v1.axis.JoinType getJoinCondition() {
        return joinCondition;
    }

    public void setJoinCondition(org.astrogrid.workflow.beans.v1.axis.JoinType joinCondition) {
        this.joinCondition = joinCondition;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _step)) return false;
        _step other = (_step) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.tool==null && other.getTool()==null) || 
             (this.tool!=null &&
              this.tool.equals(other.getTool()))) &&
            ((this.stepExecutionRecord==null && other.getStepExecutionRecord()==null) || 
             (this.stepExecutionRecord!=null &&
              java.util.Arrays.equals(this.stepExecutionRecord, other.getStepExecutionRecord()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.joinCondition==null && other.getJoinCondition()==null) || 
             (this.joinCondition!=null &&
              this.joinCondition.equals(other.getJoinCondition()))) &&
            this.stepNumber == other.getStepNumber() &&
            this.sequenceNumber == other.getSequenceNumber();
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
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getTool() != null) {
            _hashCode += getTool().hashCode();
        }
        if (getStepExecutionRecord() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStepExecutionRecord());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStepExecutionRecord(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getJoinCondition() != null) {
            _hashCode += getJoinCondition().hashCode();
        }
        _hashCode += getStepNumber();
        _hashCode += getSequenceNumber();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_step.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "step"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("joinCondition");
        attrField.setXmlName(new javax.xml.namespace.QName("", "joinCondition"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "join-type"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("stepNumber");
        attrField.setXmlName(new javax.xml.namespace.QName("", "stepNumber"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("sequenceNumber");
        attrField.setXmlName(new javax.xml.namespace.QName("", "sequenceNumber"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tool");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "tool"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "tool"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stepExecutionRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "step-execution-record"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "step-execution-record"));
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
