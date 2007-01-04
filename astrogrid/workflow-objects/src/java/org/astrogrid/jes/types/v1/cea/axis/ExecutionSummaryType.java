/**
 * ExecutionSummaryType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.jes.types.v1.cea.axis;

public class ExecutionSummaryType  implements java.io.Serializable {
    private java.lang.String applicationName;
    private java.lang.String executionId;
    private org.astrogrid.jes.types.v1.cea.axis.InputListType inputList;
    private org.astrogrid.jes.types.v1.cea.axis.ResultListType resultList;
    private org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase status;

    public ExecutionSummaryType() {
    }

    public java.lang.String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(java.lang.String applicationName) {
        this.applicationName = applicationName;
    }

    public java.lang.String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(java.lang.String executionId) {
        this.executionId = executionId;
    }

    public org.astrogrid.jes.types.v1.cea.axis.InputListType getInputList() {
        return inputList;
    }

    public void setInputList(org.astrogrid.jes.types.v1.cea.axis.InputListType inputList) {
        this.inputList = inputList;
    }

    public org.astrogrid.jes.types.v1.cea.axis.ResultListType getResultList() {
        return resultList;
    }

    public void setResultList(org.astrogrid.jes.types.v1.cea.axis.ResultListType resultList) {
        this.resultList = resultList;
    }

    public org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase getStatus() {
        return status;
    }

    public void setStatus(org.astrogrid.jes.types.v1.cea.axis.ExecutionPhase status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExecutionSummaryType)) return false;
        ExecutionSummaryType other = (ExecutionSummaryType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.applicationName==null && other.getApplicationName()==null) || 
             (this.applicationName!=null &&
              this.applicationName.equals(other.getApplicationName()))) &&
            ((this.executionId==null && other.getExecutionId()==null) || 
             (this.executionId!=null &&
              this.executionId.equals(other.getExecutionId()))) &&
            ((this.inputList==null && other.getInputList()==null) || 
             (this.inputList!=null &&
              this.inputList.equals(other.getInputList()))) &&
            ((this.resultList==null && other.getResultList()==null) || 
             (this.resultList!=null &&
              this.resultList.equals(other.getResultList()))) &&
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
        if (getApplicationName() != null) {
            _hashCode += getApplicationName().hashCode();
        }
        if (getExecutionId() != null) {
            _hashCode += getExecutionId().hashCode();
        }
        if (getInputList() != null) {
            _hashCode += getInputList().hashCode();
        }
        if (getResultList() != null) {
            _hashCode += getResultList().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecutionSummaryType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "execution-summary-type"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("applicationName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "application-name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("executionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "execution-id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "input-list"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "input-list-type"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "result-list"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "result-list-type"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CEATypes/v1", "execution-phase"));
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
