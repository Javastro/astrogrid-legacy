/**
 * ObservingProgram.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.aladinimage;

public class ObservingProgram  extends org.astrogrid.datacenter.cdsdelegate.aladinimage.ObservingProgramDescription  implements java.io.Serializable {
    private org.astrogrid.datacenter.cdsdelegate.aladinimage.ObservationGroup[] observationGroups;
    private int observationGroupsCount;

    public ObservingProgram() {
    }

    public org.astrogrid.datacenter.cdsdelegate.aladinimage.ObservationGroup[] getObservationGroups() {
        return observationGroups;
    }

    public void setObservationGroups(org.astrogrid.datacenter.cdsdelegate.aladinimage.ObservationGroup[] observationGroups) {
        this.observationGroups = observationGroups;
    }

    public int getObservationGroupsCount() {
        return observationGroupsCount;
    }

    public void setObservationGroupsCount(int observationGroupsCount) {
        this.observationGroupsCount = observationGroupsCount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObservingProgram)) return false;
        ObservingProgram other = (ObservingProgram) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.observationGroups==null && other.getObservationGroups()==null) || 
             (this.observationGroups!=null &&
              java.util.Arrays.equals(this.observationGroups, other.getObservationGroups()))) &&
            this.observationGroupsCount == other.getObservationGroupsCount();
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
        if (getObservationGroups() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObservationGroups());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObservationGroups(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getObservationGroupsCount();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObservingProgram.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:AladinImage", "ObservingProgram"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observationGroups");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observationGroups"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:AladinImage", "ObservationGroup"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observationGroupsCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observationGroupsCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
