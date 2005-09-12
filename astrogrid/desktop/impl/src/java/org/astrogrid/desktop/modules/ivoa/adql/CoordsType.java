/**
 * CoordsType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.desktop.modules.ivoa.adql;

public class CoordsType  implements java.io.Serializable {
    private org.astrogrid.desktop.modules.ivoa.adql.VelScalarType velScalar;
    private org.astrogrid.desktop.modules.ivoa.adql.Vel3VectorType vel3Vector;
    private org.astrogrid.desktop.modules.ivoa.adql.Pos2VectorType pos2Vector;
    private org.apache.axis.types.URI coordFile;
    private org.astrogrid.desktop.modules.ivoa.adql.CoordTimeType time;
    private org.astrogrid.desktop.modules.ivoa.adql.Vel2VectorType vel2Vector;
    private org.astrogrid.desktop.modules.ivoa.adql.PosScalarType posScalar;
    private org.astrogrid.desktop.modules.ivoa.adql.Pos3VectorType pos3Vector;
    private org.astrogrid.desktop.modules.ivoa.adql.CoordSpectralType spectrum;
    private org.astrogrid.desktop.modules.ivoa.adql.VelScalarType redshift;
    private org.apache.axis.types.Id ID;  // attribute
    private org.apache.axis.types.IDRef coord_system_id;  // attribute

    public CoordsType() {
    }

    public org.astrogrid.desktop.modules.ivoa.adql.VelScalarType getVelScalar() {
        return velScalar;
    }

    public void setVelScalar(org.astrogrid.desktop.modules.ivoa.adql.VelScalarType velScalar) {
        this.velScalar = velScalar;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.Vel3VectorType getVel3Vector() {
        return vel3Vector;
    }

    public void setVel3Vector(org.astrogrid.desktop.modules.ivoa.adql.Vel3VectorType vel3Vector) {
        this.vel3Vector = vel3Vector;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.Pos2VectorType getPos2Vector() {
        return pos2Vector;
    }

    public void setPos2Vector(org.astrogrid.desktop.modules.ivoa.adql.Pos2VectorType pos2Vector) {
        this.pos2Vector = pos2Vector;
    }

    public org.apache.axis.types.URI getCoordFile() {
        return coordFile;
    }

    public void setCoordFile(org.apache.axis.types.URI coordFile) {
        this.coordFile = coordFile;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordTimeType getTime() {
        return time;
    }

    public void setTime(org.astrogrid.desktop.modules.ivoa.adql.CoordTimeType time) {
        this.time = time;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.Vel2VectorType getVel2Vector() {
        return vel2Vector;
    }

    public void setVel2Vector(org.astrogrid.desktop.modules.ivoa.adql.Vel2VectorType vel2Vector) {
        this.vel2Vector = vel2Vector;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.PosScalarType getPosScalar() {
        return posScalar;
    }

    public void setPosScalar(org.astrogrid.desktop.modules.ivoa.adql.PosScalarType posScalar) {
        this.posScalar = posScalar;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.Pos3VectorType getPos3Vector() {
        return pos3Vector;
    }

    public void setPos3Vector(org.astrogrid.desktop.modules.ivoa.adql.Pos3VectorType pos3Vector) {
        this.pos3Vector = pos3Vector;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.CoordSpectralType getSpectrum() {
        return spectrum;
    }

    public void setSpectrum(org.astrogrid.desktop.modules.ivoa.adql.CoordSpectralType spectrum) {
        this.spectrum = spectrum;
    }

    public org.astrogrid.desktop.modules.ivoa.adql.VelScalarType getRedshift() {
        return redshift;
    }

    public void setRedshift(org.astrogrid.desktop.modules.ivoa.adql.VelScalarType redshift) {
        this.redshift = redshift;
    }

    public org.apache.axis.types.Id getID() {
        return ID;
    }

    public void setID(org.apache.axis.types.Id ID) {
        this.ID = ID;
    }

    public org.apache.axis.types.IDRef getCoord_system_id() {
        return coord_system_id;
    }

    public void setCoord_system_id(org.apache.axis.types.IDRef coord_system_id) {
        this.coord_system_id = coord_system_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CoordsType)) return false;
        CoordsType other = (CoordsType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.velScalar==null && other.getVelScalar()==null) || 
             (this.velScalar!=null &&
              this.velScalar.equals(other.getVelScalar()))) &&
            ((this.vel3Vector==null && other.getVel3Vector()==null) || 
             (this.vel3Vector!=null &&
              this.vel3Vector.equals(other.getVel3Vector()))) &&
            ((this.pos2Vector==null && other.getPos2Vector()==null) || 
             (this.pos2Vector!=null &&
              this.pos2Vector.equals(other.getPos2Vector()))) &&
            ((this.coordFile==null && other.getCoordFile()==null) || 
             (this.coordFile!=null &&
              this.coordFile.equals(other.getCoordFile()))) &&
            ((this.time==null && other.getTime()==null) || 
             (this.time!=null &&
              this.time.equals(other.getTime()))) &&
            ((this.vel2Vector==null && other.getVel2Vector()==null) || 
             (this.vel2Vector!=null &&
              this.vel2Vector.equals(other.getVel2Vector()))) &&
            ((this.posScalar==null && other.getPosScalar()==null) || 
             (this.posScalar!=null &&
              this.posScalar.equals(other.getPosScalar()))) &&
            ((this.pos3Vector==null && other.getPos3Vector()==null) || 
             (this.pos3Vector!=null &&
              this.pos3Vector.equals(other.getPos3Vector()))) &&
            ((this.spectrum==null && other.getSpectrum()==null) || 
             (this.spectrum!=null &&
              this.spectrum.equals(other.getSpectrum()))) &&
            ((this.redshift==null && other.getRedshift()==null) || 
             (this.redshift!=null &&
              this.redshift.equals(other.getRedshift()))) &&
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.coord_system_id==null && other.getCoord_system_id()==null) || 
             (this.coord_system_id!=null &&
              this.coord_system_id.equals(other.getCoord_system_id())));
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
        if (getVelScalar() != null) {
            _hashCode += getVelScalar().hashCode();
        }
        if (getVel3Vector() != null) {
            _hashCode += getVel3Vector().hashCode();
        }
        if (getPos2Vector() != null) {
            _hashCode += getPos2Vector().hashCode();
        }
        if (getCoordFile() != null) {
            _hashCode += getCoordFile().hashCode();
        }
        if (getTime() != null) {
            _hashCode += getTime().hashCode();
        }
        if (getVel2Vector() != null) {
            _hashCode += getVel2Vector().hashCode();
        }
        if (getPosScalar() != null) {
            _hashCode += getPosScalar().hashCode();
        }
        if (getPos3Vector() != null) {
            _hashCode += getPos3Vector().hashCode();
        }
        if (getSpectrum() != null) {
            _hashCode += getSpectrum().hashCode();
        }
        if (getRedshift() != null) {
            _hashCode += getRedshift().hashCode();
        }
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getCoord_system_id() != null) {
            _hashCode += getCoord_system_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CoordsType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordsType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("coord_system_id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "coord_system_id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "IDREF"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("velScalar");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "VelScalar"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "velScalarType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vel3Vector");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Vel3Vector"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "vel3VectorType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pos2Vector");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Pos2Vector"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "pos2VectorType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coordFile");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "CoordFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("time");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Time"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordTimeType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vel2Vector");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Vel2Vector"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "vel2VectorType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("posScalar");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "PosScalar"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "posScalarType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pos3Vector");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Pos3Vector"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "pos3VectorType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spectrum");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Spectrum"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "coordSpectralType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("redshift");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:nvo-coords", "Redshift"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:nvo-coords", "velScalarType"));
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
