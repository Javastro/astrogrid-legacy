/**
 * KernelResults.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.myspace.delegate;

public class KernelResults  implements java.io.Serializable {
    private byte[] contentsBytes;
    private java.lang.String contentsString;
    private java.lang.Object[] entries;
    private org.astrogrid.myspace.delegate.EntryResults entryResults;
    private java.lang.Object[] statusList;
    private org.astrogrid.myspace.delegate.StatusResults statusResults;

    public KernelResults() {
    }

    public byte[] getContentsBytes() {
        return contentsBytes;
    }

    public void setContentsBytes(byte[] contentsBytes) {
        this.contentsBytes = contentsBytes;
    }

    public java.lang.String getContentsString() {
        return contentsString;
    }

    public void setContentsString(java.lang.String contentsString) {
        this.contentsString = contentsString;
    }

    public java.lang.Object[] getEntries() {
        return entries;
    }

    public void setEntries(java.lang.Object[] entries) {
        this.entries = entries;
    }

    public org.astrogrid.myspace.delegate.EntryResults getEntryResults() {
        return entryResults;
    }

    public void setEntryResults(org.astrogrid.myspace.delegate.EntryResults entryResults) {
        this.entryResults = entryResults;
    }

    public java.lang.Object[] getStatusList() {
        return statusList;
    }

    public void setStatusList(java.lang.Object[] statusList) {
        this.statusList = statusList;
    }

    public org.astrogrid.myspace.delegate.StatusResults getStatusResults() {
        return statusResults;
    }

    public void setStatusResults(org.astrogrid.myspace.delegate.StatusResults statusResults) {
        this.statusResults = statusResults;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof KernelResults)) return false;
        KernelResults other = (KernelResults) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.contentsBytes==null && other.getContentsBytes()==null) || 
             (this.contentsBytes!=null &&
              java.util.Arrays.equals(this.contentsBytes, other.getContentsBytes()))) &&
            ((this.contentsString==null && other.getContentsString()==null) || 
             (this.contentsString!=null &&
              this.contentsString.equals(other.getContentsString()))) &&
            ((this.entries==null && other.getEntries()==null) || 
             (this.entries!=null &&
              java.util.Arrays.equals(this.entries, other.getEntries()))) &&
            ((this.entryResults==null && other.getEntryResults()==null) || 
             (this.entryResults!=null &&
              this.entryResults.equals(other.getEntryResults()))) &&
            ((this.statusList==null && other.getStatusList()==null) || 
             (this.statusList!=null &&
              java.util.Arrays.equals(this.statusList, other.getStatusList()))) &&
            ((this.statusResults==null && other.getStatusResults()==null) || 
             (this.statusResults!=null &&
              this.statusResults.equals(other.getStatusResults())));
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
        if (getContentsBytes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContentsBytes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContentsBytes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getContentsString() != null) {
            _hashCode += getContentsString().hashCode();
        }
        if (getEntries() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEntries());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEntries(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getEntryResults() != null) {
            _hashCode += getEntryResults().hashCode();
        }
        if (getStatusList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStatusList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStatusList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStatusResults() != null) {
            _hashCode += getStatusResults().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(KernelResults.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Kernel", "KernelResults"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentsBytes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contentsBytes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contentsString");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contentsString"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entries");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entries"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entryResults");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entryResults"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Kernel", "EntryResults"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "statusList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusResults");
        elemField.setXmlName(new javax.xml.namespace.QName("", "statusResults"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Kernel", "StatusResults"));
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
