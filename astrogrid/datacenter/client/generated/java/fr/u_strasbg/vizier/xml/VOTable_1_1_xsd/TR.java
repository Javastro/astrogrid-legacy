/**
 * TR.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package fr.u_strasbg.vizier.xml.VOTable_1_1_xsd;

public class TR  implements java.io.Serializable {
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.TD[] TD;

    public TR() {
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.TD[] getTD() {
        return TD;
    }

    public void setTD(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.TD[] TD) {
        this.TD = TD;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.TD getTD(int i) {
        return TD[i];
    }

    public void setTD(int i, fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.TD value) {
        this.TD[i] = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TR)) return false;
        TR other = (TR) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.TD==null && other.getTD()==null) || 
             (this.TD!=null &&
              java.util.Arrays.equals(this.TD, other.getTD())));
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
        if (getTD() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTD());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTD(), i);
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
        new org.apache.axis.description.TypeDesc(TR.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "TR"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TD");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "TD"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "TD"));
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
