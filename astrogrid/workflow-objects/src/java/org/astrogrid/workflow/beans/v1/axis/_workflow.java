/**
 * _workflow.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.workflow.beans.v1.axis;

public class _workflow  implements java.io.Serializable {
    private org.astrogrid.workflow.beans.v1.axis._sequence sequence;
    private java.lang.String description;
    private org.astrogrid.community.beans.v1.axis.Credentials credentials;
    private org.astrogrid.jes.beans.vi.axis.executionrecord._jobExecutionRecord jobExecutionRecord;
    private java.lang.String name;  // attribute

    public _workflow() {
    }

    public org.astrogrid.workflow.beans.v1.axis._sequence getSequence() {
        return sequence;
    }

    public void setSequence(org.astrogrid.workflow.beans.v1.axis._sequence sequence) {
        this.sequence = sequence;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public org.astrogrid.community.beans.v1.axis.Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(org.astrogrid.community.beans.v1.axis.Credentials credentials) {
        this.credentials = credentials;
    }

    public org.astrogrid.jes.beans.vi.axis.executionrecord._jobExecutionRecord getJobExecutionRecord() {
        return jobExecutionRecord;
    }

    public void setJobExecutionRecord(org.astrogrid.jes.beans.vi.axis.executionrecord._jobExecutionRecord jobExecutionRecord) {
        this.jobExecutionRecord = jobExecutionRecord;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _workflow)) return false;
        _workflow other = (_workflow) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sequence==null && other.getSequence()==null) || 
             (this.sequence!=null &&
              this.sequence.equals(other.getSequence()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.credentials==null && other.getCredentials()==null) || 
             (this.credentials!=null &&
              this.credentials.equals(other.getCredentials()))) &&
            ((this.jobExecutionRecord==null && other.getJobExecutionRecord()==null) || 
             (this.jobExecutionRecord!=null &&
              this.jobExecutionRecord.equals(other.getJobExecutionRecord()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName())));
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
        if (getSequence() != null) {
            _hashCode += getSequence().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getCredentials() != null) {
            _hashCode += getCredentials().hashCode();
        }
        if (getJobExecutionRecord() != null) {
            _hashCode += getJobExecutionRecord().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(_workflow.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "workflow"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequence");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "sequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "sequence"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("credentials");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "Credentials"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Credentials"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobExecutionRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "job-execution-record"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "job-execution-record"));
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
