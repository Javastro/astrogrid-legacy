/**
 * _CommandLineExecutionControllerConfig.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceabase;

public class _CommandLineExecutionControllerConfig  extends org.astrogrid.applications.beans.v1.axis.ceabase.CommonExecutionConnectorConfigType  implements java.io.Serializable {
    private org.astrogrid.applications.beans.v1.axis.ceabase.CommandLineApplication[] application;

    public _CommandLineExecutionControllerConfig() {
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.CommandLineApplication[] getApplication() {
        return application;
    }

    public void setApplication(org.astrogrid.applications.beans.v1.axis.ceabase.CommandLineApplication[] application) {
        this.application = application;
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.CommandLineApplication getApplication(int i) {
        return application[i];
    }

    public void setApplication(int i, org.astrogrid.applications.beans.v1.axis.ceabase.CommandLineApplication value) {
        this.application[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _CommandLineExecutionControllerConfig)) return false;
        _CommandLineExecutionControllerConfig other = (_CommandLineExecutionControllerConfig) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.application==null && other.getApplication()==null) || 
             (this.application!=null &&
              java.util.Arrays.equals(this.application, other.getApplication())));
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
        if (getApplication() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getApplication());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getApplication(), i);
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
        new org.apache.axis.description.TypeDesc(_CommandLineExecutionControllerConfig.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "CommandLineExecutionControllerConfig"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("application");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "Application"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "CommandLineApplication"));
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
