/**
 * GROUP.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package fr.u_strasbg.vizier.xml.VOTable_1_1_xsd;

public class GROUP  implements java.io.Serializable {
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.AnyTEXT DESCRIPTION;
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.PARAMref PARAMref;
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.FIELD FIELD;
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.PARAM PARAM;
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.FIELDref FIELDref;
    private fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.GROUP GROUP;
    private org.apache.axis.types.Id ID;  // attribute
    private org.apache.axis.types.Token name;  // attribute
    private org.apache.axis.types.IDRef ref;  // attribute
    private org.apache.axis.types.Token ucd;  // attribute
    private java.lang.String utype;  // attribute

    public GROUP() {
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.AnyTEXT getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.AnyTEXT DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.PARAMref getPARAMref() {
        return PARAMref;
    }

    public void setPARAMref(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.PARAMref PARAMref) {
        this.PARAMref = PARAMref;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.FIELD getFIELD() {
        return FIELD;
    }

    public void setFIELD(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.FIELD FIELD) {
        this.FIELD = FIELD;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.PARAM getPARAM() {
        return PARAM;
    }

    public void setPARAM(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.PARAM PARAM) {
        this.PARAM = PARAM;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.FIELDref getFIELDref() {
        return FIELDref;
    }

    public void setFIELDref(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.FIELDref FIELDref) {
        this.FIELDref = FIELDref;
    }

    public fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.GROUP getGROUP() {
        return GROUP;
    }

    public void setGROUP(fr.u_strasbg.vizier.xml.VOTable_1_1_xsd.GROUP GROUP) {
        this.GROUP = GROUP;
    }

    public org.apache.axis.types.Id getID() {
        return ID;
    }

    public void setID(org.apache.axis.types.Id ID) {
        this.ID = ID;
    }

    public org.apache.axis.types.Token getName() {
        return name;
    }

    public void setName(org.apache.axis.types.Token name) {
        this.name = name;
    }

    public org.apache.axis.types.IDRef getRef() {
        return ref;
    }

    public void setRef(org.apache.axis.types.IDRef ref) {
        this.ref = ref;
    }

    public org.apache.axis.types.Token getUcd() {
        return ucd;
    }

    public void setUcd(org.apache.axis.types.Token ucd) {
        this.ucd = ucd;
    }

    public java.lang.String getUtype() {
        return utype;
    }

    public void setUtype(java.lang.String utype) {
        this.utype = utype;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GROUP)) return false;
        GROUP other = (GROUP) obj;
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
            ((this.PARAMref==null && other.getPARAMref()==null) || 
             (this.PARAMref!=null &&
              this.PARAMref.equals(other.getPARAMref()))) &&
            ((this.FIELD==null && other.getFIELD()==null) || 
             (this.FIELD!=null &&
              this.FIELD.equals(other.getFIELD()))) &&
            ((this.PARAM==null && other.getPARAM()==null) || 
             (this.PARAM!=null &&
              this.PARAM.equals(other.getPARAM()))) &&
            ((this.FIELDref==null && other.getFIELDref()==null) || 
             (this.FIELDref!=null &&
              this.FIELDref.equals(other.getFIELDref()))) &&
            ((this.GROUP==null && other.getGROUP()==null) || 
             (this.GROUP!=null &&
              this.GROUP.equals(other.getGROUP()))) &&
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.ref==null && other.getRef()==null) || 
             (this.ref!=null &&
              this.ref.equals(other.getRef()))) &&
            ((this.ucd==null && other.getUcd()==null) || 
             (this.ucd!=null &&
              this.ucd.equals(other.getUcd()))) &&
            ((this.utype==null && other.getUtype()==null) || 
             (this.utype!=null &&
              this.utype.equals(other.getUtype())));
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
        if (getPARAMref() != null) {
            _hashCode += getPARAMref().hashCode();
        }
        if (getFIELD() != null) {
            _hashCode += getFIELD().hashCode();
        }
        if (getPARAM() != null) {
            _hashCode += getPARAM().hashCode();
        }
        if (getFIELDref() != null) {
            _hashCode += getFIELDref().hashCode();
        }
        if (getGROUP() != null) {
            _hashCode += getGROUP().hashCode();
        }
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getRef() != null) {
            _hashCode += getRef().hashCode();
        }
        if (getUcd() != null) {
            _hashCode += getUcd().hashCode();
        }
        if (getUtype() != null) {
            _hashCode += getUtype().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GROUP.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "GROUP"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ref");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ref"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "IDREF"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ucd");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ucd"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "token"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("utype");
        attrField.setXmlName(new javax.xml.namespace.QName("", "utype"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DESCRIPTION");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "DESCRIPTION"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "anyTEXT"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PARAMref");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "PARAMref"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "PARAMref"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FIELD");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "FIELD"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "FIELD"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PARAM");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "PARAM"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "PARAM"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FIELDref");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "FIELDref"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "FIELDref"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("GROUP");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "GROUP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://vizier.u-strasbg.fr/xml/VOTable-1.1.xsd", "GROUP"));
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
