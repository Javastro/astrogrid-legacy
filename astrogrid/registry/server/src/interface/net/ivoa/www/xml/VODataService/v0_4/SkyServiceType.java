/**
 * SkyServiceType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.www.xml.VODataService.v0_4;

public class SkyServiceType  extends org.astrogrid.registry.server.generated.voresource.ServiceType  implements java.io.Serializable {
    private org.astrogrid.registry.server.generated.voresource.ResourceReferenceType[] facility;
    private org.astrogrid.registry.server.generated.voresource.ResourceReferenceType[] instrument;
    private net.ivoa.www.xml.VODataService.v0_4.CoverageType coverage;

    public SkyServiceType() {
    }

    public org.astrogrid.registry.server.generated.voresource.ResourceReferenceType[] getFacility() {
        return facility;
    }

    public void setFacility(org.astrogrid.registry.server.generated.voresource.ResourceReferenceType[] facility) {
        this.facility = facility;
    }

    public org.astrogrid.registry.server.generated.voresource.ResourceReferenceType getFacility(int i) {
        return facility[i];
    }

    public void setFacility(int i, org.astrogrid.registry.server.generated.voresource.ResourceReferenceType value) {
        this.facility[i] = value;
    }

    public org.astrogrid.registry.server.generated.voresource.ResourceReferenceType[] getInstrument() {
        return instrument;
    }

    public void setInstrument(org.astrogrid.registry.server.generated.voresource.ResourceReferenceType[] instrument) {
        this.instrument = instrument;
    }

    public org.astrogrid.registry.server.generated.voresource.ResourceReferenceType getInstrument(int i) {
        return instrument[i];
    }

    public void setInstrument(int i, org.astrogrid.registry.server.generated.voresource.ResourceReferenceType value) {
        this.instrument[i] = value;
    }

    public net.ivoa.www.xml.VODataService.v0_4.CoverageType getCoverage() {
        return coverage;
    }

    public void setCoverage(net.ivoa.www.xml.VODataService.v0_4.CoverageType coverage) {
        this.coverage = coverage;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SkyServiceType)) return false;
        SkyServiceType other = (SkyServiceType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.facility==null && other.getFacility()==null) || 
             (this.facility!=null &&
              java.util.Arrays.equals(this.facility, other.getFacility()))) &&
            ((this.instrument==null && other.getInstrument()==null) || 
             (this.instrument!=null &&
              java.util.Arrays.equals(this.instrument, other.getInstrument()))) &&
            ((this.coverage==null && other.getCoverage()==null) || 
             (this.coverage!=null &&
              this.coverage.equals(other.getCoverage())));
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
        if (getFacility() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFacility());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFacility(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getInstrument() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInstrument());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInstrument(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCoverage() != null) {
            _hashCode += getCoverage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SkyServiceType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "SkyServiceType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("facility");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Facility"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceReferenceType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instrument");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "Instrument"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VOResource/v0.9", "ResourceReferenceType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("coverage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "Coverage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/VODataService/v0.4", "CoverageType"));
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
