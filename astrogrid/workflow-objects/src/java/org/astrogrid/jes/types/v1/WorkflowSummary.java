/**
 * WorkflowSummary.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.types.v1;

public class WorkflowSummary  implements java.io.Serializable {
    private java.lang.String workflowName;
    private org.astrogrid.jes.types.v1.JobURN jobUrn;

    public WorkflowSummary() {
    }

    public java.lang.String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(java.lang.String workflowName) {
        this.workflowName = workflowName;
    }

    public org.astrogrid.jes.types.v1.JobURN getJobUrn() {
        return jobUrn;
    }

    public void setJobUrn(org.astrogrid.jes.types.v1.JobURN jobUrn) {
        this.jobUrn = jobUrn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WorkflowSummary)) return false;
        WorkflowSummary other = (WorkflowSummary) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.workflowName==null && other.getWorkflowName()==null) || 
             (this.workflowName!=null &&
              this.workflowName.equals(other.getWorkflowName()))) &&
            ((this.jobUrn==null && other.getJobUrn()==null) || 
             (this.jobUrn!=null &&
              this.jobUrn.equals(other.getJobUrn())));
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
        if (getWorkflowName() != null) {
            _hashCode += getWorkflowName().hashCode();
        }
        if (getJobUrn() != null) {
            _hashCode += getJobUrn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WorkflowSummary.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:jes/types/v1", "workflow-summary"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflowName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "workflow-name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobUrn");
        elemField.setXmlName(new javax.xml.namespace.QName("", "job-urn"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:jes/types/v1", "jobURN"));
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
