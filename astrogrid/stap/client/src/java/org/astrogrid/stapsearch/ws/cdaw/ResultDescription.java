/**
 * ResultDescription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.cdaw;

public class ResultDescription  implements java.io.Serializable {
    private java.lang.String[] errors;

    private java.lang.String[] messages;

    private java.lang.String[] statuses;

    private org.astrogrid.stapsearch.ws.cdaw.ThumbnailDescription thumbnailDescription;

    private java.lang.String[] urls;

    private java.lang.String[] warnings;

    public ResultDescription() {
    }

    public ResultDescription(
           java.lang.String[] errors,
           java.lang.String[] messages,
           java.lang.String[] statuses,
           org.astrogrid.stapsearch.ws.cdaw.ThumbnailDescription thumbnailDescription,
           java.lang.String[] urls,
           java.lang.String[] warnings) {
           this.errors = errors;
           this.messages = messages;
           this.statuses = statuses;
           this.thumbnailDescription = thumbnailDescription;
           this.urls = urls;
           this.warnings = warnings;
    }


    /**
     * Gets the errors value for this ResultDescription.
     * 
     * @return errors
     */
    public java.lang.String[] getErrors() {
        return errors;
    }


    /**
     * Sets the errors value for this ResultDescription.
     * 
     * @param errors
     */
    public void setErrors(java.lang.String[] errors) {
        this.errors = errors;
    }


    /**
     * Gets the messages value for this ResultDescription.
     * 
     * @return messages
     */
    public java.lang.String[] getMessages() {
        return messages;
    }


    /**
     * Sets the messages value for this ResultDescription.
     * 
     * @param messages
     */
    public void setMessages(java.lang.String[] messages) {
        this.messages = messages;
    }


    /**
     * Gets the statuses value for this ResultDescription.
     * 
     * @return statuses
     */
    public java.lang.String[] getStatuses() {
        return statuses;
    }


    /**
     * Sets the statuses value for this ResultDescription.
     * 
     * @param statuses
     */
    public void setStatuses(java.lang.String[] statuses) {
        this.statuses = statuses;
    }


    /**
     * Gets the thumbnailDescription value for this ResultDescription.
     * 
     * @return thumbnailDescription
     */
    public org.astrogrid.stapsearch.ws.cdaw.ThumbnailDescription getThumbnailDescription() {
        return thumbnailDescription;
    }


    /**
     * Sets the thumbnailDescription value for this ResultDescription.
     * 
     * @param thumbnailDescription
     */
    public void setThumbnailDescription(org.astrogrid.stapsearch.ws.cdaw.ThumbnailDescription thumbnailDescription) {
        this.thumbnailDescription = thumbnailDescription;
    }


    /**
     * Gets the urls value for this ResultDescription.
     * 
     * @return urls
     */
    public java.lang.String[] getUrls() {
        return urls;
    }


    /**
     * Sets the urls value for this ResultDescription.
     * 
     * @param urls
     */
    public void setUrls(java.lang.String[] urls) {
        this.urls = urls;
    }


    /**
     * Gets the warnings value for this ResultDescription.
     * 
     * @return warnings
     */
    public java.lang.String[] getWarnings() {
        return warnings;
    }


    /**
     * Sets the warnings value for this ResultDescription.
     * 
     * @param warnings
     */
    public void setWarnings(java.lang.String[] warnings) {
        this.warnings = warnings;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResultDescription)) return false;
        ResultDescription other = (ResultDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.errors==null && other.getErrors()==null) || 
             (this.errors!=null &&
              java.util.Arrays.equals(this.errors, other.getErrors()))) &&
            ((this.messages==null && other.getMessages()==null) || 
             (this.messages!=null &&
              java.util.Arrays.equals(this.messages, other.getMessages()))) &&
            ((this.statuses==null && other.getStatuses()==null) || 
             (this.statuses!=null &&
              java.util.Arrays.equals(this.statuses, other.getStatuses()))) &&
            ((this.thumbnailDescription==null && other.getThumbnailDescription()==null) || 
             (this.thumbnailDescription!=null &&
              this.thumbnailDescription.equals(other.getThumbnailDescription()))) &&
            ((this.urls==null && other.getUrls()==null) || 
             (this.urls!=null &&
              java.util.Arrays.equals(this.urls, other.getUrls()))) &&
            ((this.warnings==null && other.getWarnings()==null) || 
             (this.warnings!=null &&
              java.util.Arrays.equals(this.warnings, other.getWarnings())));
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
        if (getErrors() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getErrors());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getErrors(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMessages() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMessages());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMessages(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStatuses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStatuses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStatuses(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getThumbnailDescription() != null) {
            _hashCode += getThumbnailDescription().hashCode();
        }
        if (getUrls() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUrls());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUrls(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWarnings() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWarnings());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWarnings(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResultDescription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cdaweb.gsfc.nasa.gov/WS/types/CDASWS", "ResultDescription"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errors");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errors"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messages");
        elemField.setXmlName(new javax.xml.namespace.QName("", "messages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statuses");
        elemField.setXmlName(new javax.xml.namespace.QName("", "statuses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("thumbnailDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "thumbnailDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://cdaweb.gsfc.nasa.gov/WS/types/CDASWS", "ThumbnailDescription"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urls");
        elemField.setXmlName(new javax.xml.namespace.QName("", "urls"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("warnings");
        elemField.setXmlName(new javax.xml.namespace.QName("", "warnings"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
