/**
 * _DataCentreControllerConfig.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.beans.v1.axis.ceabase;

public class _DataCentreControllerConfig  extends org.astrogrid.applications.beans.v1.axis.ceabase.CommonExecutionConnectorConfigType  implements java.io.Serializable {
    private org.astrogrid.applications.beans.v1.axis.ceabase.DataCentreApplication[] dataCentre;

    public _DataCentreControllerConfig() {
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.DataCentreApplication[] getDataCentre() {
        return dataCentre;
    }

    public void setDataCentre(org.astrogrid.applications.beans.v1.axis.ceabase.DataCentreApplication[] dataCentre) {
        this.dataCentre = dataCentre;
    }

    public org.astrogrid.applications.beans.v1.axis.ceabase.DataCentreApplication getDataCentre(int i) {
        return dataCentre[i];
    }

    public void setDataCentre(int i, org.astrogrid.applications.beans.v1.axis.ceabase.DataCentreApplication value) {
        this.dataCentre[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof _DataCentreControllerConfig)) return false;
        _DataCentreControllerConfig other = (_DataCentreControllerConfig) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.dataCentre==null && other.getDataCentre()==null) || 
             (this.dataCentre!=null &&
              java.util.Arrays.equals(this.dataCentre, other.getDataCentre())));
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
        if (getDataCentre() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDataCentre());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDataCentre(), i);
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
        new org.apache.axis.description.TypeDesc(_DataCentreControllerConfig.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "DataCentreControllerConfig"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCentre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "DataCentre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1", "DataCentreApplication"));
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
