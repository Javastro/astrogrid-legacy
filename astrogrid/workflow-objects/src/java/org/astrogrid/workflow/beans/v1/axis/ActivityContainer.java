/**
 * ActivityContainer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.workflow.beans.v1.axis;

public abstract class ActivityContainer  extends org.astrogrid.workflow.beans.v1.axis.AbstractActivity  implements java.io.Serializable {
    private org.astrogrid.workflow.beans.v1.axis.AbstractActivity[] activity;

    public ActivityContainer() {
    }

    public org.astrogrid.workflow.beans.v1.axis.AbstractActivity[] getActivity() {
        return activity;
    }

    public void setActivity(org.astrogrid.workflow.beans.v1.axis.AbstractActivity[] activity) {
        this.activity = activity;
    }

    public org.astrogrid.workflow.beans.v1.axis.AbstractActivity getActivity(int i) {
        return activity[i];
    }

    public void setActivity(int i, org.astrogrid.workflow.beans.v1.axis.AbstractActivity value) {
        this.activity[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActivityContainer)) return false;
        ActivityContainer other = (ActivityContainer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.activity==null && other.getActivity()==null) || 
             (this.activity!=null &&
              java.util.Arrays.equals(this.activity, other.getActivity())));
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
        if (getActivity() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getActivity());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActivity(), i);
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
        new org.apache.axis.description.TypeDesc(ActivityContainer.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "ActivityContainer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "Activity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.astrogrid.org/schema/AGWorkflow/v1", "AbstractActivity"));
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
