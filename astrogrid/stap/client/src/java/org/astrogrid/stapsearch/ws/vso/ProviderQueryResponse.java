/**
 * ProviderQueryResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public class ProviderQueryResponse  implements java.io.Serializable {
    private float version;

    private java.lang.String provider;

    private java.lang.Integer no_of_records_found;

    private java.lang.Integer no_of_records_returned;

    private org.astrogrid.stapsearch.ws.vso.QueryResponseBlock[] record;

    private java.lang.String error;

    private java.lang.String debug;

    private java.lang.String status;

    public ProviderQueryResponse() {
    }

    public ProviderQueryResponse(
           float version,
           java.lang.String provider,
           java.lang.Integer no_of_records_found,
           java.lang.Integer no_of_records_returned,
           org.astrogrid.stapsearch.ws.vso.QueryResponseBlock[] record,
           java.lang.String error,
           java.lang.String debug,
           java.lang.String status) {
           this.version = version;
           this.provider = provider;
           this.no_of_records_found = no_of_records_found;
           this.no_of_records_returned = no_of_records_returned;
           this.record = record;
           this.error = error;
           this.debug = debug;
           this.status = status;
    }


    /**
     * Gets the version value for this ProviderQueryResponse.
     * 
     * @return version
     */
    public float getVersion() {
        return version;
    }


    /**
     * Sets the version value for this ProviderQueryResponse.
     * 
     * @param version
     */
    public void setVersion(float version) {
        this.version = version;
    }


    /**
     * Gets the provider value for this ProviderQueryResponse.
     * 
     * @return provider
     */
    public java.lang.String getProvider() {
        return provider;
    }


    /**
     * Sets the provider value for this ProviderQueryResponse.
     * 
     * @param provider
     */
    public void setProvider(java.lang.String provider) {
        this.provider = provider;
    }


    /**
     * Gets the no_of_records_found value for this ProviderQueryResponse.
     * 
     * @return no_of_records_found
     */
    public java.lang.Integer getNo_of_records_found() {
        return no_of_records_found;
    }


    /**
     * Sets the no_of_records_found value for this ProviderQueryResponse.
     * 
     * @param no_of_records_found
     */
    public void setNo_of_records_found(java.lang.Integer no_of_records_found) {
        this.no_of_records_found = no_of_records_found;
    }


    /**
     * Gets the no_of_records_returned value for this ProviderQueryResponse.
     * 
     * @return no_of_records_returned
     */
    public java.lang.Integer getNo_of_records_returned() {
        return no_of_records_returned;
    }


    /**
     * Sets the no_of_records_returned value for this ProviderQueryResponse.
     * 
     * @param no_of_records_returned
     */
    public void setNo_of_records_returned(java.lang.Integer no_of_records_returned) {
        this.no_of_records_returned = no_of_records_returned;
    }


    /**
     * Gets the record value for this ProviderQueryResponse.
     * 
     * @return record
     */
    public org.astrogrid.stapsearch.ws.vso.QueryResponseBlock[] getRecord() {
        return record;
    }


    /**
     * Sets the record value for this ProviderQueryResponse.
     * 
     * @param record
     */
    public void setRecord(org.astrogrid.stapsearch.ws.vso.QueryResponseBlock[] record) {
        this.record = record;
    }


    /**
     * Gets the error value for this ProviderQueryResponse.
     * 
     * @return error
     */
    public java.lang.String getError() {
        return error;
    }


    /**
     * Sets the error value for this ProviderQueryResponse.
     * 
     * @param error
     */
    public void setError(java.lang.String error) {
        this.error = error;
    }


    /**
     * Gets the debug value for this ProviderQueryResponse.
     * 
     * @return debug
     */
    public java.lang.String getDebug() {
        return debug;
    }


    /**
     * Sets the debug value for this ProviderQueryResponse.
     * 
     * @param debug
     */
    public void setDebug(java.lang.String debug) {
        this.debug = debug;
    }


    /**
     * Gets the status value for this ProviderQueryResponse.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ProviderQueryResponse.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProviderQueryResponse)) return false;
        ProviderQueryResponse other = (ProviderQueryResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.version == other.getVersion() &&
            ((this.provider==null && other.getProvider()==null) || 
             (this.provider!=null &&
              this.provider.equals(other.getProvider()))) &&
            ((this.no_of_records_found==null && other.getNo_of_records_found()==null) || 
             (this.no_of_records_found!=null &&
              this.no_of_records_found.equals(other.getNo_of_records_found()))) &&
            ((this.no_of_records_returned==null && other.getNo_of_records_returned()==null) || 
             (this.no_of_records_returned!=null &&
              this.no_of_records_returned.equals(other.getNo_of_records_returned()))) &&
            ((this.record==null && other.getRecord()==null) || 
             (this.record!=null &&
              java.util.Arrays.equals(this.record, other.getRecord()))) &&
            ((this.error==null && other.getError()==null) || 
             (this.error!=null &&
              this.error.equals(other.getError()))) &&
            ((this.debug==null && other.getDebug()==null) || 
             (this.debug!=null &&
              this.debug.equals(other.getDebug()))) &&
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
        _hashCode += new Float(getVersion()).hashCode();
        if (getProvider() != null) {
            _hashCode += getProvider().hashCode();
        }
        if (getNo_of_records_found() != null) {
            _hashCode += getNo_of_records_found().hashCode();
        }
        if (getNo_of_records_returned() != null) {
            _hashCode += getNo_of_records_returned().hashCode();
        }
        if (getRecord() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRecord());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRecord(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getError() != null) {
            _hashCode += getError().hashCode();
        }
        if (getDebug() != null) {
            _hashCode += getDebug().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProviderQueryResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "ProviderQueryResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provider");
        elemField.setXmlName(new javax.xml.namespace.QName("", "provider"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("no_of_records_found");
        elemField.setXmlName(new javax.xml.namespace.QName("", "no_of_records_found"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("no_of_records_returned");
        elemField.setXmlName(new javax.xml.namespace.QName("", "no_of_records_returned"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("record");
        elemField.setXmlName(new javax.xml.namespace.QName("", "record"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "QueryResponseBlock"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("error");
        elemField.setXmlName(new javax.xml.namespace.QName("", "error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("debug");
        elemField.setXmlName(new javax.xml.namespace.QName("", "debug"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
