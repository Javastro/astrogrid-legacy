/**
 * Credentials.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.community.beans.v1.axis;

public class Credentials  implements java.io.Serializable {
    private org.astrogrid.community.beans.v1.axis._Account account;
    private org.astrogrid.community.beans.v1.axis._Group group;
    private org.astrogrid.community.beans.v1.axis._SecurityToken securityToken;

    public Credentials() {
    }

    public org.astrogrid.community.beans.v1.axis._Account getAccount() {
        return account;
    }

    public void setAccount(org.astrogrid.community.beans.v1.axis._Account account) {
        this.account = account;
    }

    public org.astrogrid.community.beans.v1.axis._Group getGroup() {
        return group;
    }

    public void setGroup(org.astrogrid.community.beans.v1.axis._Group group) {
        this.group = group;
    }

    public org.astrogrid.community.beans.v1.axis._SecurityToken getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(org.astrogrid.community.beans.v1.axis._SecurityToken securityToken) {
        this.securityToken = securityToken;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Credentials)) return false;
        Credentials other = (Credentials) obj;
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
            ((this.group==null && other.getGroup()==null) || 
             (this.group!=null &&
              this.group.equals(other.getGroup()))) &&
            ((this.securityToken==null && other.getSecurityToken()==null) || 
             (this.securityToken!=null &&
              this.securityToken.equals(other.getSecurityToken())));
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
        if (getGroup() != null) {
            _hashCode += getGroup().hashCode();
        }
        if (getSecurityToken() != null) {
            _hashCode += getSecurityToken().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Credentials.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Credentials"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("account");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Account"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Account"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("group");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Group"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "Group"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityToken");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "SecurityToken"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/Credentials/v1", "SecurityToken"));
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
