/**
 * DatasetDescription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.cdaw;

public class DatasetDescription  implements java.io.Serializable {
    private java.util.Calendar endTime;

    private java.lang.String id;

    private java.lang.String label;

    private org.astrogrid.stapsearch.ws.cdaw.DatasetLink[] links;

    private java.lang.String notesUrl;

    private java.lang.String piAffiliation;

    private java.lang.String piName;

    private java.util.Calendar startTime;

    public DatasetDescription() {
    }

    public DatasetDescription(
           java.util.Calendar endTime,
           java.lang.String id,
           java.lang.String label,
           org.astrogrid.stapsearch.ws.cdaw.DatasetLink[] links,
           java.lang.String notesUrl,
           java.lang.String piAffiliation,
           java.lang.String piName,
           java.util.Calendar startTime) {
           this.endTime = endTime;
           this.id = id;
           this.label = label;
           this.links = links;
           this.notesUrl = notesUrl;
           this.piAffiliation = piAffiliation;
           this.piName = piName;
           this.startTime = startTime;
    }


    /**
     * Gets the endTime value for this DatasetDescription.
     * 
     * @return endTime
     */
    public java.util.Calendar getEndTime() {
        return endTime;
    }


    /**
     * Sets the endTime value for this DatasetDescription.
     * 
     * @param endTime
     */
    public void setEndTime(java.util.Calendar endTime) {
        this.endTime = endTime;
    }


    /**
     * Gets the id value for this DatasetDescription.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this DatasetDescription.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the label value for this DatasetDescription.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this DatasetDescription.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }


    /**
     * Gets the links value for this DatasetDescription.
     * 
     * @return links
     */
    public org.astrogrid.stapsearch.ws.cdaw.DatasetLink[] getLinks() {
        return links;
    }


    /**
     * Sets the links value for this DatasetDescription.
     * 
     * @param links
     */
    public void setLinks(org.astrogrid.stapsearch.ws.cdaw.DatasetLink[] links) {
        this.links = links;
    }


    /**
     * Gets the notesUrl value for this DatasetDescription.
     * 
     * @return notesUrl
     */
    public java.lang.String getNotesUrl() {
        return notesUrl;
    }


    /**
     * Sets the notesUrl value for this DatasetDescription.
     * 
     * @param notesUrl
     */
    public void setNotesUrl(java.lang.String notesUrl) {
        this.notesUrl = notesUrl;
    }


    /**
     * Gets the piAffiliation value for this DatasetDescription.
     * 
     * @return piAffiliation
     */
    public java.lang.String getPiAffiliation() {
        return piAffiliation;
    }


    /**
     * Sets the piAffiliation value for this DatasetDescription.
     * 
     * @param piAffiliation
     */
    public void setPiAffiliation(java.lang.String piAffiliation) {
        this.piAffiliation = piAffiliation;
    }


    /**
     * Gets the piName value for this DatasetDescription.
     * 
     * @return piName
     */
    public java.lang.String getPiName() {
        return piName;
    }


    /**
     * Sets the piName value for this DatasetDescription.
     * 
     * @param piName
     */
    public void setPiName(java.lang.String piName) {
        this.piName = piName;
    }


    /**
     * Gets the startTime value for this DatasetDescription.
     * 
     * @return startTime
     */
    public java.util.Calendar getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this DatasetDescription.
     * 
     * @param startTime
     */
    public void setStartTime(java.util.Calendar startTime) {
        this.startTime = startTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatasetDescription)) return false;
        DatasetDescription other = (DatasetDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.endTime==null && other.getEndTime()==null) || 
             (this.endTime!=null &&
              this.endTime.equals(other.getEndTime()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.label==null && other.getLabel()==null) || 
             (this.label!=null &&
              this.label.equals(other.getLabel()))) &&
            ((this.links==null && other.getLinks()==null) || 
             (this.links!=null &&
              java.util.Arrays.equals(this.links, other.getLinks()))) &&
            ((this.notesUrl==null && other.getNotesUrl()==null) || 
             (this.notesUrl!=null &&
              this.notesUrl.equals(other.getNotesUrl()))) &&
            ((this.piAffiliation==null && other.getPiAffiliation()==null) || 
             (this.piAffiliation!=null &&
              this.piAffiliation.equals(other.getPiAffiliation()))) &&
            ((this.piName==null && other.getPiName()==null) || 
             (this.piName!=null &&
              this.piName.equals(other.getPiName()))) &&
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime())));
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
        if (getEndTime() != null) {
            _hashCode += getEndTime().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getLinks() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLinks());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLinks(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNotesUrl() != null) {
            _hashCode += getNotesUrl().hashCode();
        }
        if (getPiAffiliation() != null) {
            _hashCode += getPiAffiliation().hashCode();
        }
        if (getPiName() != null) {
            _hashCode += getPiName().hashCode();
        }
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatasetDescription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cdaweb.gsfc.nasa.gov/WS/types/CDASWS", "DatasetDescription"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("", "label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("links");
        elemField.setXmlName(new javax.xml.namespace.QName("", "links"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://cdaweb.gsfc.nasa.gov/WS/types/CDASWS", "DatasetLink"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notesUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "notesUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piAffiliation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piAffiliation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
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
