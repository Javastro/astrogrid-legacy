/**
 * ArrayOfPlanElement.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class ArrayOfPlanElement  implements java.io.Serializable {
    private net.ivoa.SkyNode.PlanElement[] planElement;

    public ArrayOfPlanElement() {
    }

    public net.ivoa.SkyNode.PlanElement[] getPlanElement() {
        return planElement;
    }

    public void setPlanElement(net.ivoa.SkyNode.PlanElement[] planElement) {
        this.planElement = planElement;
    }

    public net.ivoa.SkyNode.PlanElement getPlanElement(int i) {
        return planElement[i];
    }

    public void setPlanElement(int i, net.ivoa.SkyNode.PlanElement value) {
        this.planElement[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfPlanElement)) return false;
        ArrayOfPlanElement other = (ArrayOfPlanElement) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.planElement==null && other.getPlanElement()==null) || 
             (this.planElement!=null &&
              java.util.Arrays.equals(this.planElement, other.getPlanElement())));
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
        if (getPlanElement() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPlanElement());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPlanElement(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfPlanElement.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfPlanElement"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("planElement");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "PlanElement"));
        elemField.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "PlanElement"));
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
