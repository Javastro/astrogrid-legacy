/**
 * RegistryType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource.voregistry;

public class RegistryType  extends org.astrogrid.registry.server.generated.voresource.ServiceType  implements java.io.Serializable {
    private org.astrogrid.registry.server.generated.voresource.AuthorityIDType[] managedAuthority;

    public RegistryType() {
    }

    public org.astrogrid.registry.server.generated.voresource.AuthorityIDType[] getManagedAuthority() {
        return managedAuthority;
    }

    public void setManagedAuthority(org.astrogrid.registry.server.generated.voresource.AuthorityIDType[] managedAuthority) {
        this.managedAuthority = managedAuthority;
    }

    public org.astrogrid.registry.server.generated.voresource.AuthorityIDType getManagedAuthority(int i) {
        return managedAuthority[i];
    }

    public void setManagedAuthority(int i, org.astrogrid.registry.server.generated.voresource.AuthorityIDType value) {
        this.managedAuthority[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistryType)) return false;
        RegistryType other = (RegistryType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.managedAuthority==null && other.getManagedAuthority()==null) || 
             (this.managedAuthority!=null &&
              java.util.Arrays.equals(this.managedAuthority, other.getManagedAuthority())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getManagedAuthority() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getManagedAuthority());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getManagedAuthority(), i);
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
        new org.apache.axis.description.TypeDesc(RegistryType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VORegistry/v0.2", "RegistryType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("managedAuthority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VORegistry/v0.2", "ManagedAuthority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "AuthorityIDType"));
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
