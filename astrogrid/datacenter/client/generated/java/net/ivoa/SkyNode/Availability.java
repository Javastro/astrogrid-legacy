/**
 * Availability.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class Availability  implements java.io.Serializable {
    private java.util.Calendar timeOnServer;
    private java.util.Calendar upTime;
    private java.util.Calendar validTo;
    private java.lang.String servername;
    private java.lang.String message;
    private java.lang.String location;

    public Availability() {
    }

    public java.util.Calendar getTimeOnServer() {
        return timeOnServer;
    }

    public void setTimeOnServer(java.util.Calendar timeOnServer) {
        this.timeOnServer = timeOnServer;
    }

    public java.util.Calendar getUpTime() {
        return upTime;
    }

    public void setUpTime(java.util.Calendar upTime) {
        this.upTime = upTime;
    }

    public java.util.Calendar getValidTo() {
        return validTo;
    }

    public void setValidTo(java.util.Calendar validTo) {
        this.validTo = validTo;
    }

    public java.lang.String getServername() {
        return servername;
    }

    public void setServername(java.lang.String servername) {
        this.servername = servername;
    }

    public java.lang.String getMessage() {
        return message;
    }

    public void setMessage(java.lang.String message) {
        this.message = message;
    }

    public java.lang.String getLocation() {
        return location;
    }

    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Availability)) return false;
        Availability other = (Availability) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.timeOnServer==null && other.getTimeOnServer()==null) || 
             (this.timeOnServer!=null &&
              this.timeOnServer.equals(other.getTimeOnServer()))) &&
            ((this.upTime==null && other.getUpTime()==null) || 
             (this.upTime!=null &&
              this.upTime.equals(other.getUpTime()))) &&
            ((this.validTo==null && other.getValidTo()==null) || 
             (this.validTo!=null &&
              this.validTo.equals(other.getValidTo()))) &&
            ((this.servername==null && other.getServername()==null) || 
             (this.servername!=null &&
              this.servername.equals(other.getServername()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.location==null && other.getLocation()==null) || 
             (this.location!=null &&
              this.location.equals(other.getLocation())));
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
        if (getTimeOnServer() != null) {
            _hashCode += getTimeOnServer().hashCode();
        }
        if (getUpTime() != null) {
            _hashCode += getUpTime().hashCode();
        }
        if (getValidTo() != null) {
            _hashCode += getValidTo().hashCode();
        }
        if (getServername() != null) {
            _hashCode += getServername().hashCode();
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getLocation() != null) {
            _hashCode += getLocation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Availability.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Availability"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeOnServer");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "timeOnServer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("upTime");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "upTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("validTo");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "validTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servername");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Servername"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("location");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "location"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
