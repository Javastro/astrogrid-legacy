/**
 * SecurityToken.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.service.authentication.data;

public class SecurityToken  implements java.io.Serializable {
    private java.lang.String account;
    private java.util.Calendar expirationDate;
    private java.lang.String ident;
    private java.lang.String target;
    private java.util.Calendar todaysDate;
    private java.lang.String token;
    private java.lang.Boolean used;

    public SecurityToken() {
    }

    public java.lang.String getAccount() {
        return account;
    }

    public void setAccount(java.lang.String account) {
        this.account = account;
    }

    public java.util.Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(java.util.Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    public java.lang.String getIdent() {
        return ident;
    }

    public void setIdent(java.lang.String ident) {
        this.ident = ident;
    }

    public java.lang.String getTarget() {
        return target;
    }

    public void setTarget(java.lang.String target) {
        this.target = target;
    }

    public java.util.Calendar getTodaysDate() {
        return todaysDate;
    }

    public void setTodaysDate(java.util.Calendar todaysDate) {
        this.todaysDate = todaysDate;
    }

    public java.lang.String getToken() {
        return token;
    }

    public void setToken(java.lang.String token) {
        this.token = token;
    }

    public java.lang.Boolean getUsed() {
        return used;
    }

    public void setUsed(java.lang.Boolean used) {
        this.used = used;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SecurityToken)) return false;
        SecurityToken other = (SecurityToken) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.account==null && other.getAccount()==null) || 
             (this.account!=null &&
              this.account.equals(other.getAccount()))) &&
            ((this.expirationDate==null && other.getExpirationDate()==null) || 
             (this.expirationDate!=null &&
              this.expirationDate.equals(other.getExpirationDate()))) &&
            ((this.ident==null && other.getIdent()==null) || 
             (this.ident!=null &&
              this.ident.equals(other.getIdent()))) &&
            ((this.target==null && other.getTarget()==null) || 
             (this.target!=null &&
              this.target.equals(other.getTarget()))) &&
            ((this.todaysDate==null && other.getTodaysDate()==null) || 
             (this.todaysDate!=null &&
              this.todaysDate.equals(other.getTodaysDate()))) &&
            ((this.token==null && other.getToken()==null) || 
             (this.token!=null &&
              this.token.equals(other.getToken()))) &&
            ((this.used==null && other.getUsed()==null) || 
             (this.used!=null &&
              this.used.equals(other.getUsed())));
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
        if (getAccount() != null) {
            _hashCode += getAccount().hashCode();
        }
        if (getExpirationDate() != null) {
            _hashCode += getExpirationDate().hashCode();
        }
        if (getIdent() != null) {
            _hashCode += getIdent().hashCode();
        }
        if (getTarget() != null) {
            _hashCode += getTarget().hashCode();
        }
        if (getTodaysDate() != null) {
            _hashCode += getTodaysDate().hashCode();
        }
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
        }
        if (getUsed() != null) {
            _hashCode += getUsed().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SecurityToken.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:data.authentication.community.astrogrid.org", "SecurityToken"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("account");
        elemField.setXmlName(new javax.xml.namespace.QName("", "account"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "expirationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ident");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ident"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("target");
        elemField.setXmlName(new javax.xml.namespace.QName("", "target"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("todaysDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "todaysDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("", "token"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("used");
        elemField.setXmlName(new javax.xml.namespace.QName("", "used"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "boolean"));
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
