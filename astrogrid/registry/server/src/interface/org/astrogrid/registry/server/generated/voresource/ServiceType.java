/**
 * ServiceType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.voresource;

public class ServiceType  extends org.astrogrid.registry.server.generated.voresource.ResourceType  implements java.io.Serializable {
    private org.astrogrid.registry.server.generated.voresource.CapabilityType capability;
    private org.astrogrid.registry.server.generated.voresource.InterfaceType _interface;

    public ServiceType() {
    }

    public org.astrogrid.registry.server.generated.voresource.CapabilityType getCapability() {
        return capability;
    }

    public void setCapability(org.astrogrid.registry.server.generated.voresource.CapabilityType capability) {
        this.capability = capability;
    }

    public org.astrogrid.registry.server.generated.voresource.InterfaceType get_interface() {
        return _interface;
    }

    public void set_interface(org.astrogrid.registry.server.generated.voresource.InterfaceType _interface) {
        this._interface = _interface;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceType)) return false;
        ServiceType other = (ServiceType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.capability==null && other.getCapability()==null) || 
             (this.capability!=null &&
              this.capability.equals(other.getCapability()))) &&
            ((this._interface==null && other.get_interface()==null) || 
             (this._interface!=null &&
              this._interface.equals(other.get_interface())));
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
        if (getCapability() != null) {
            _hashCode += getCapability().hashCode();
        }
        if (get_interface() != null) {
            _hashCode += get_interface().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ServiceType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("capability");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Capability"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "CapabilityType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_interface");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Interface"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "InterfaceType"));
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
