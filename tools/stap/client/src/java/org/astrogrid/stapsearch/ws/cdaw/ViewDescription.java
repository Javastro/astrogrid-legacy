/**
 * ViewDescription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.cdaw;

public class ViewDescription  implements java.io.Serializable {
    private java.lang.String endpointAddress;

    private java.lang.String id;

    private java.lang.String noticeUrl;

    private java.lang.String overview;

    private boolean publicAccess;

    private java.lang.String subtitle;

    private java.lang.String title;

    private boolean underConstruction;

    public ViewDescription() {
    }

    public ViewDescription(
           java.lang.String endpointAddress,
           java.lang.String id,
           java.lang.String noticeUrl,
           java.lang.String overview,
           boolean publicAccess,
           java.lang.String subtitle,
           java.lang.String title,
           boolean underConstruction) {
           this.endpointAddress = endpointAddress;
           this.id = id;
           this.noticeUrl = noticeUrl;
           this.overview = overview;
           this.publicAccess = publicAccess;
           this.subtitle = subtitle;
           this.title = title;
           this.underConstruction = underConstruction;
    }


    /**
     * Gets the endpointAddress value for this ViewDescription.
     * 
     * @return endpointAddress
     */
    public java.lang.String getEndpointAddress() {
        return endpointAddress;
    }


    /**
     * Sets the endpointAddress value for this ViewDescription.
     * 
     * @param endpointAddress
     */
    public void setEndpointAddress(java.lang.String endpointAddress) {
        this.endpointAddress = endpointAddress;
    }


    /**
     * Gets the id value for this ViewDescription.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this ViewDescription.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the noticeUrl value for this ViewDescription.
     * 
     * @return noticeUrl
     */
    public java.lang.String getNoticeUrl() {
        return noticeUrl;
    }


    /**
     * Sets the noticeUrl value for this ViewDescription.
     * 
     * @param noticeUrl
     */
    public void setNoticeUrl(java.lang.String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }


    /**
     * Gets the overview value for this ViewDescription.
     * 
     * @return overview
     */
    public java.lang.String getOverview() {
        return overview;
    }


    /**
     * Sets the overview value for this ViewDescription.
     * 
     * @param overview
     */
    public void setOverview(java.lang.String overview) {
        this.overview = overview;
    }


    /**
     * Gets the publicAccess value for this ViewDescription.
     * 
     * @return publicAccess
     */
    public boolean isPublicAccess() {
        return publicAccess;
    }


    /**
     * Sets the publicAccess value for this ViewDescription.
     * 
     * @param publicAccess
     */
    public void setPublicAccess(boolean publicAccess) {
        this.publicAccess = publicAccess;
    }


    /**
     * Gets the subtitle value for this ViewDescription.
     * 
     * @return subtitle
     */
    public java.lang.String getSubtitle() {
        return subtitle;
    }


    /**
     * Sets the subtitle value for this ViewDescription.
     * 
     * @param subtitle
     */
    public void setSubtitle(java.lang.String subtitle) {
        this.subtitle = subtitle;
    }


    /**
     * Gets the title value for this ViewDescription.
     * 
     * @return title
     */
    public java.lang.String getTitle() {
        return title;
    }


    /**
     * Sets the title value for this ViewDescription.
     * 
     * @param title
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }


    /**
     * Gets the underConstruction value for this ViewDescription.
     * 
     * @return underConstruction
     */
    public boolean isUnderConstruction() {
        return underConstruction;
    }


    /**
     * Sets the underConstruction value for this ViewDescription.
     * 
     * @param underConstruction
     */
    public void setUnderConstruction(boolean underConstruction) {
        this.underConstruction = underConstruction;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ViewDescription)) return false;
        ViewDescription other = (ViewDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.endpointAddress==null && other.getEndpointAddress()==null) || 
             (this.endpointAddress!=null &&
              this.endpointAddress.equals(other.getEndpointAddress()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.noticeUrl==null && other.getNoticeUrl()==null) || 
             (this.noticeUrl!=null &&
              this.noticeUrl.equals(other.getNoticeUrl()))) &&
            ((this.overview==null && other.getOverview()==null) || 
             (this.overview!=null &&
              this.overview.equals(other.getOverview()))) &&
            this.publicAccess == other.isPublicAccess() &&
            ((this.subtitle==null && other.getSubtitle()==null) || 
             (this.subtitle!=null &&
              this.subtitle.equals(other.getSubtitle()))) &&
            ((this.title==null && other.getTitle()==null) || 
             (this.title!=null &&
              this.title.equals(other.getTitle()))) &&
            this.underConstruction == other.isUnderConstruction();
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
        if (getEndpointAddress() != null) {
            _hashCode += getEndpointAddress().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getNoticeUrl() != null) {
            _hashCode += getNoticeUrl().hashCode();
        }
        if (getOverview() != null) {
            _hashCode += getOverview().hashCode();
        }
        _hashCode += (isPublicAccess() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getSubtitle() != null) {
            _hashCode += getSubtitle().hashCode();
        }
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        _hashCode += (isUnderConstruction() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ViewDescription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cdaweb.gsfc.nasa.gov/WS/types/CDASWS", "ViewDescription"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endpointAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endpointAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noticeUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "noticeUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("overview");
        elemField.setXmlName(new javax.xml.namespace.QName("", "overview"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publicAccess");
        elemField.setXmlName(new javax.xml.namespace.QName("", "publicAccess"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subtitle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subtitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("underConstruction");
        elemField.setXmlName(new javax.xml.namespace.QName("", "underConstruction"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
