/**
 * Observation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.aladinimage;

public class Observation  implements java.io.Serializable {
    private float angularPixelSize;
    private java.lang.String availableCodings;
    private float centralPointRA;
    private float centralPountDEC;
    private java.lang.String dateAndTime;
    private java.lang.String name;
    private java.lang.String origin;
    private java.lang.String originalCoding;
    private float positionAngle;
    private java.lang.String referenceNumber;
    private float sizeAlpha;
    private float sizeDelta;
    private org.astrogrid.datacenter.cdsdelegate.aladinimage.StorageMapping[] storageMappings;
    private int storageMappingsCount;
    private org.astrogrid.datacenter.cdsdelegate.aladinimage.StoredImage[] storedImages;
    private int storedImagesCount;

    public Observation() {
    }

    public float getAngularPixelSize() {
        return angularPixelSize;
    }

    public void setAngularPixelSize(float angularPixelSize) {
        this.angularPixelSize = angularPixelSize;
    }

    public java.lang.String getAvailableCodings() {
        return availableCodings;
    }

    public void setAvailableCodings(java.lang.String availableCodings) {
        this.availableCodings = availableCodings;
    }

    public float getCentralPointRA() {
        return centralPointRA;
    }

    public void setCentralPointRA(float centralPointRA) {
        this.centralPointRA = centralPointRA;
    }

    public float getCentralPountDEC() {
        return centralPountDEC;
    }

    public void setCentralPountDEC(float centralPountDEC) {
        this.centralPountDEC = centralPountDEC;
    }

    public java.lang.String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(java.lang.String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getOrigin() {
        return origin;
    }

    public void setOrigin(java.lang.String origin) {
        this.origin = origin;
    }

    public java.lang.String getOriginalCoding() {
        return originalCoding;
    }

    public void setOriginalCoding(java.lang.String originalCoding) {
        this.originalCoding = originalCoding;
    }

    public float getPositionAngle() {
        return positionAngle;
    }

    public void setPositionAngle(float positionAngle) {
        this.positionAngle = positionAngle;
    }

    public java.lang.String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(java.lang.String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public float getSizeAlpha() {
        return sizeAlpha;
    }

    public void setSizeAlpha(float sizeAlpha) {
        this.sizeAlpha = sizeAlpha;
    }

    public float getSizeDelta() {
        return sizeDelta;
    }

    public void setSizeDelta(float sizeDelta) {
        this.sizeDelta = sizeDelta;
    }

    public org.astrogrid.datacenter.cdsdelegate.aladinimage.StorageMapping[] getStorageMappings() {
        return storageMappings;
    }

    public void setStorageMappings(org.astrogrid.datacenter.cdsdelegate.aladinimage.StorageMapping[] storageMappings) {
        this.storageMappings = storageMappings;
    }

    public int getStorageMappingsCount() {
        return storageMappingsCount;
    }

    public void setStorageMappingsCount(int storageMappingsCount) {
        this.storageMappingsCount = storageMappingsCount;
    }

    public org.astrogrid.datacenter.cdsdelegate.aladinimage.StoredImage[] getStoredImages() {
        return storedImages;
    }

    public void setStoredImages(org.astrogrid.datacenter.cdsdelegate.aladinimage.StoredImage[] storedImages) {
        this.storedImages = storedImages;
    }

    public int getStoredImagesCount() {
        return storedImagesCount;
    }

    public void setStoredImagesCount(int storedImagesCount) {
        this.storedImagesCount = storedImagesCount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Observation)) return false;
        Observation other = (Observation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.angularPixelSize == other.getAngularPixelSize() &&
            ((this.availableCodings==null && other.getAvailableCodings()==null) || 
             (this.availableCodings!=null &&
              this.availableCodings.equals(other.getAvailableCodings()))) &&
            this.centralPointRA == other.getCentralPointRA() &&
            this.centralPountDEC == other.getCentralPountDEC() &&
            ((this.dateAndTime==null && other.getDateAndTime()==null) || 
             (this.dateAndTime!=null &&
              this.dateAndTime.equals(other.getDateAndTime()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.origin==null && other.getOrigin()==null) || 
             (this.origin!=null &&
              this.origin.equals(other.getOrigin()))) &&
            ((this.originalCoding==null && other.getOriginalCoding()==null) || 
             (this.originalCoding!=null &&
              this.originalCoding.equals(other.getOriginalCoding()))) &&
            this.positionAngle == other.getPositionAngle() &&
            ((this.referenceNumber==null && other.getReferenceNumber()==null) || 
             (this.referenceNumber!=null &&
              this.referenceNumber.equals(other.getReferenceNumber()))) &&
            this.sizeAlpha == other.getSizeAlpha() &&
            this.sizeDelta == other.getSizeDelta() &&
            ((this.storageMappings==null && other.getStorageMappings()==null) || 
             (this.storageMappings!=null &&
              java.util.Arrays.equals(this.storageMappings, other.getStorageMappings()))) &&
            this.storageMappingsCount == other.getStorageMappingsCount() &&
            ((this.storedImages==null && other.getStoredImages()==null) || 
             (this.storedImages!=null &&
              java.util.Arrays.equals(this.storedImages, other.getStoredImages()))) &&
            this.storedImagesCount == other.getStoredImagesCount();
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
        _hashCode += new Float(getAngularPixelSize()).hashCode();
        if (getAvailableCodings() != null) {
            _hashCode += getAvailableCodings().hashCode();
        }
        _hashCode += new Float(getCentralPointRA()).hashCode();
        _hashCode += new Float(getCentralPountDEC()).hashCode();
        if (getDateAndTime() != null) {
            _hashCode += getDateAndTime().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getOrigin() != null) {
            _hashCode += getOrigin().hashCode();
        }
        if (getOriginalCoding() != null) {
            _hashCode += getOriginalCoding().hashCode();
        }
        _hashCode += new Float(getPositionAngle()).hashCode();
        if (getReferenceNumber() != null) {
            _hashCode += getReferenceNumber().hashCode();
        }
        _hashCode += new Float(getSizeAlpha()).hashCode();
        _hashCode += new Float(getSizeDelta()).hashCode();
        if (getStorageMappings() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStorageMappings());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStorageMappings(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getStorageMappingsCount();
        if (getStoredImages() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStoredImages());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStoredImages(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getStoredImagesCount();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Observation.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:AladinImage", "Observation"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("angularPixelSize");
        elemField.setXmlName(new javax.xml.namespace.QName("", "angularPixelSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("availableCodings");
        elemField.setXmlName(new javax.xml.namespace.QName("", "availableCodings"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("centralPointRA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "centralPointRA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("centralPountDEC");
        elemField.setXmlName(new javax.xml.namespace.QName("", "centralPountDEC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateAndTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateAndTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("origin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "origin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originalCoding");
        elemField.setXmlName(new javax.xml.namespace.QName("", "originalCoding"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positionAngle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "positionAngle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sizeAlpha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sizeAlpha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sizeDelta");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sizeDelta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storageMappings");
        elemField.setXmlName(new javax.xml.namespace.QName("", "storageMappings"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:AladinImage", "StorageMapping"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storageMappingsCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "storageMappingsCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storedImages");
        elemField.setXmlName(new javax.xml.namespace.QName("", "storedImages"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:AladinImage", "StoredImage"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storedImagesCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "storedImagesCount"));
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
