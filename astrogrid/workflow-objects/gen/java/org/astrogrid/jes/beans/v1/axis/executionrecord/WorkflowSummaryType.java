/**
 * WorkflowSummaryType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.beans.v1.axis.executionrecord;

public class WorkflowSummaryType  implements java.io.Serializable {
    private org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN jobId;
    private org.astrogrid.jes.beans.v1.axis.executionrecord._extension[] extension;
    private org.astrogrid.jes.types.v1.cea.axis.MessageType[] message;
    private java.lang.String workflowName;
    private java.lang.String description;
    private java.util.Calendar startTime;  // attribute
    private java.util.Calendar finishTime;  // attribute
    private org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase status;  // attribute

    public WorkflowSummaryType() {
    }

    public org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN getJobId() {
        return jobId;
    }

    public void setJobId(org.astrogrid.jes.beans.v1.axis.executionrecord.JobURN jobId) {
        this.jobId = jobId;
    }

    public org.astrogrid.jes.beans.v1.axis.executionrecord._extension[] getExtension() {
        return extension;
    }

    public void setExtension(org.astrogrid.jes.beans.v1.axis.executionrecord._extension[] extension) {
        this.extension = extension;
    }

    public org.astrogrid.jes.beans.v1.axis.executionrecord._extension getExtension(int i) {
        return extension[i];
    }

    public void setExtension(int i, org.astrogrid.jes.beans.v1.axis.executionrecord._extension value) {
        this.extension[i] = value;
    }

    public org.astrogrid.jes.types.v1.cea.axis.MessageType[] getMessage() {
        return message;
    }

    public void setMessage(org.astrogrid.jes.types.v1.cea.axis.MessageType[] message) {
        this.message = message;
    }

    public org.astrogrid.jes.types.v1.cea.axis.MessageType getMessage(int i) {
        return message[i];
    }

    public void setMessage(int i, org.astrogrid.jes.types.v1.cea.axis.MessageType value) {
        this.message[i] = value;
    }

    public java.lang.String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(java.lang.String workflowName) {
        this.workflowName = workflowName;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.util.Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(java.util.Calendar startTime) {
        this.startTime = startTime;
    }

    public java.util.Calendar getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(java.util.Calendar finishTime) {
        this.finishTime = finishTime;
    }

    public org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase getStatus() {
        return status;
    }

    public void setStatus(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WorkflowSummaryType)) return false;
        WorkflowSummaryType other = (WorkflowSummaryType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId()))) &&
            ((this.extension==null && other.getExtension()==null) || 
             (this.extension!=null &&
              java.util.Arrays.equals(this.extension, other.getExtension()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              java.util.Arrays.equals(this.message, other.getMessage()))) &&
            ((this.workflowName==null && other.getWorkflowName()==null) || 
             (this.workflowName!=null &&
              this.workflowName.equals(other.getWorkflowName()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime()))) &&
            ((this.finishTime==null && other.getFinishTime()==null) || 
             (this.finishTime!=null &&
              this.finishTime.equals(other.getFinishTime()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        if (getExtension() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExtension());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExtension(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMessage() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMessage());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMessage(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWorkflowName() != null) {
            _hashCode += getWorkflowName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
        }
        if (getFinishTime() != null) {
            _hashCode += getFinishTime().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WorkflowSummaryType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "workflow-summary-type"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("startTime");
        attrField.setXmlName(new javax.xml.namespace.QName("", "startTime"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("finishTime");
        attrField.setXmlName(new javax.xml.namespace.QName("", "finishTime"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("status");
        attrField.setXmlName(new javax.xml.namespace.QName("", "status"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "execution-phase"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "jobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "jobURN"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extension");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "extension"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "extension"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "message-type"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflowName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "workflow-name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/ExecutionRecord/v1", "description"));
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
