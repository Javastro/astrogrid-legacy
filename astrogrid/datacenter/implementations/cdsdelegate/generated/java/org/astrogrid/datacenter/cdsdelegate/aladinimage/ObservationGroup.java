/**
 * ObservationGroup.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.datacenter.cdsdelegate.aladinimage;

public class ObservationGroup  implements java.io.Serializable {
    private org.astrogrid.datacenter.cdsdelegate.aladinimage.Filter[] filters;
    private int filtersCount;
    private org.astrogrid.datacenter.cdsdelegate.aladinimage.Observation[] observations;
    private int observationsCount;
    private java.lang.String[] selectionCriterions;
    private java.lang.String[] selectionRanges;

    public ObservationGroup() {
    }

    public org.astrogrid.datacenter.cdsdelegate.aladinimage.Filter[] getFilters() {
        return filters;
    }

    public void setFilters(org.astrogrid.datacenter.cdsdelegate.aladinimage.Filter[] filters) {
        this.filters = filters;
    }

    public int getFiltersCount() {
        return filtersCount;
    }

    public void setFiltersCount(int filtersCount) {
        this.filtersCount = filtersCount;
    }

    public org.astrogrid.datacenter.cdsdelegate.aladinimage.Observation[] getObservations() {
        return observations;
    }

    public void setObservations(org.astrogrid.datacenter.cdsdelegate.aladinimage.Observation[] observations) {
        this.observations = observations;
    }

    public int getObservationsCount() {
        return observationsCount;
    }

    public void setObservationsCount(int observationsCount) {
        this.observationsCount = observationsCount;
    }

    public java.lang.String[] getSelectionCriterions() {
        return selectionCriterions;
    }

    public void setSelectionCriterions(java.lang.String[] selectionCriterions) {
        this.selectionCriterions = selectionCriterions;
    }

    public java.lang.String[] getSelectionRanges() {
        return selectionRanges;
    }

    public void setSelectionRanges(java.lang.String[] selectionRanges) {
        this.selectionRanges = selectionRanges;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObservationGroup)) return false;
        ObservationGroup other = (ObservationGroup) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.filters==null && other.getFilters()==null) || 
             (this.filters!=null &&
              java.util.Arrays.equals(this.filters, other.getFilters()))) &&
            this.filtersCount == other.getFiltersCount() &&
            ((this.observations==null && other.getObservations()==null) || 
             (this.observations!=null &&
              java.util.Arrays.equals(this.observations, other.getObservations()))) &&
            this.observationsCount == other.getObservationsCount() &&
            ((this.selectionCriterions==null && other.getSelectionCriterions()==null) || 
             (this.selectionCriterions!=null &&
              java.util.Arrays.equals(this.selectionCriterions, other.getSelectionCriterions()))) &&
            ((this.selectionRanges==null && other.getSelectionRanges()==null) || 
             (this.selectionRanges!=null &&
              java.util.Arrays.equals(this.selectionRanges, other.getSelectionRanges())));
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
        if (getFilters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFilters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFilters(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getFiltersCount();
        if (getObservations() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObservations());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObservations(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getObservationsCount();
        if (getSelectionCriterions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSelectionCriterions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSelectionCriterions(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSelectionRanges() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSelectionRanges());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSelectionRanges(), i);
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
        new org.apache.axis.description.TypeDesc(ObservationGroup.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:AladinImage", "ObservationGroup"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filters");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filters"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:AladinImage", "Filter"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filtersCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filtersCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observations"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:AladinImage", "Observation"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observationsCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observationsCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectionCriterions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "selectionCriterions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectionRanges");
        elemField.setXmlName(new javax.xml.namespace.QName("", "selectionRanges"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
