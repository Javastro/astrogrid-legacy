/**
 * VOTABLE.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package fr.u_strasbg.vizier.xml.VOTable_1_1_xsd;

public class VOTABLE  implements java.io.Serializable {
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.AnyTEXT DESCRIPTION;
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.ArrayOfChoice1 DEFINITIONS;
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.INFO[] INFO;
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.RESOURCE[] RESOURCE;
    private org.apache.axis.types.Id ID;  // attribute
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.VOTABLEVersion version;  // attribute

    public VOTABLE() {
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.AnyTEXT getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.AnyTEXT DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.ArrayOfChoice1 getDEFINITIONS() {
        return DEFINITIONS;
    }

    public void setDEFINITIONS(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.ArrayOfChoice1 DEFINITIONS) {
        this.DEFINITIONS = DEFINITIONS;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.INFO[] getINFO() {
        return INFO;
    }

    public void setINFO(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.INFO[] INFO) {
        this.INFO = INFO;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.INFO getINFO(int i) {
        return INFO[i];
    }

    public void setINFO(int i, fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.INFO value) {
        this.INFO[i] = value;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.RESOURCE[] getRESOURCE() {
        return RESOURCE;
    }

    public void setRESOURCE(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.RESOURCE[] RESOURCE) {
        this.RESOURCE = RESOURCE;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.RESOURCE getRESOURCE(int i) {
        return RESOURCE[i];
    }

    public void setRESOURCE(int i, fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.RESOURCE value) {
        this.RESOURCE[i] = value;
    }

    public org.apache.axis.types.Id getID() {
        return ID;
    }

    public void setID(org.apache.axis.types.Id ID) {
        this.ID = ID;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.VOTABLEVersion getVersion() {
        return version;
    }

    public void setVersion(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.VOTABLEVersion version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VOTABLE)) return false;
        VOTABLE other = (VOTABLE) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.DESCRIPTION==null && other.getDESCRIPTION()==null) || 
             (this.DESCRIPTION!=null &&
              this.DESCRIPTION.equals(other.getDESCRIPTION()))) &&
            ((this.DEFINITIONS==null && other.getDEFINITIONS()==null) || 
             (this.DEFINITIONS!=null &&
              this.DEFINITIONS.equals(other.getDEFINITIONS()))) &&
            ((this.INFO==null && other.getINFO()==null) || 
             (this.INFO!=null &&
              java.util.Arrays.equals(this.INFO, other.getINFO()))) &&
            ((this.RESOURCE==null && other.getRESOURCE()==null) || 
             (this.RESOURCE!=null &&
              java.util.Arrays.equals(this.RESOURCE, other.getRESOURCE()))) &&
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion())));
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
        if (getDESCRIPTION() != null) {
            _hashCode += getDESCRIPTION().hashCode();
        }
        if (getDEFINITIONS() != null) {
            _hashCode += getDEFINITIONS().hashCode();
        }
        if (getINFO() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getINFO());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getINFO(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRESOURCE() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRESOURCE());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRESOURCE(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VOTABLE.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "VOTABLE"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("version");
        attrField.setXmlName(new javax.xml.namespace.QName("", "version"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "VOTABLEVersion"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DESCRIPTION");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "DESCRIPTION"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "anyTEXT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DEFINITIONS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "DEFINITIONS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "ArrayOfChoice1"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("INFO");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "INFO"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "INFO"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RESOURCE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "RESOURCE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "RESOURCE"));
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
