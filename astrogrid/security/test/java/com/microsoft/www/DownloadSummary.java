/**
 * DownloadSummary.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public class DownloadSummary  implements java.io.Serializable {
    private java.lang.String downloadID;
    private java.lang.String cultureID;
    private java.lang.String shortName;
    private java.lang.String shortDescription;
    private java.util.Calendar datePublished;
    private java.lang.String rankOverall;
    private java.lang.String rankInThisCulture;
    private java.lang.String detailsUrl;

    public DownloadSummary() {
    }

    public java.lang.String getDownloadID() {
        return downloadID;
    }

    public void setDownloadID(java.lang.String downloadID) {
        this.downloadID = downloadID;
    }

    public java.lang.String getCultureID() {
        return cultureID;
    }

    public void setCultureID(java.lang.String cultureID) {
        this.cultureID = cultureID;
    }

    public java.lang.String getShortName() {
        return shortName;
    }

    public void setShortName(java.lang.String shortName) {
        this.shortName = shortName;
    }

    public java.lang.String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(java.lang.String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public java.util.Calendar getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(java.util.Calendar datePublished) {
        this.datePublished = datePublished;
    }

    public java.lang.String getRankOverall() {
        return rankOverall;
    }

    public void setRankOverall(java.lang.String rankOverall) {
        this.rankOverall = rankOverall;
    }

    public java.lang.String getRankInThisCulture() {
        return rankInThisCulture;
    }

    public void setRankInThisCulture(java.lang.String rankInThisCulture) {
        this.rankInThisCulture = rankInThisCulture;
    }

    public java.lang.String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(java.lang.String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DownloadSummary)) return false;
        DownloadSummary other = (DownloadSummary) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.downloadID==null && other.getDownloadID()==null) || 
             (this.downloadID!=null &&
              this.downloadID.equals(other.getDownloadID()))) &&
            ((this.cultureID==null && other.getCultureID()==null) || 
             (this.cultureID!=null &&
              this.cultureID.equals(other.getCultureID()))) &&
            ((this.shortName==null && other.getShortName()==null) || 
             (this.shortName!=null &&
              this.shortName.equals(other.getShortName()))) &&
            ((this.shortDescription==null && other.getShortDescription()==null) || 
             (this.shortDescription!=null &&
              this.shortDescription.equals(other.getShortDescription()))) &&
            ((this.datePublished==null && other.getDatePublished()==null) || 
             (this.datePublished!=null &&
              this.datePublished.equals(other.getDatePublished()))) &&
            ((this.rankOverall==null && other.getRankOverall()==null) || 
             (this.rankOverall!=null &&
              this.rankOverall.equals(other.getRankOverall()))) &&
            ((this.rankInThisCulture==null && other.getRankInThisCulture()==null) || 
             (this.rankInThisCulture!=null &&
              this.rankInThisCulture.equals(other.getRankInThisCulture()))) &&
            ((this.detailsUrl==null && other.getDetailsUrl()==null) || 
             (this.detailsUrl!=null &&
              this.detailsUrl.equals(other.getDetailsUrl())));
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
        if (getDownloadID() != null) {
            _hashCode += getDownloadID().hashCode();
        }
        if (getCultureID() != null) {
            _hashCode += getCultureID().hashCode();
        }
        if (getShortName() != null) {
            _hashCode += getShortName().hashCode();
        }
        if (getShortDescription() != null) {
            _hashCode += getShortDescription().hashCode();
        }
        if (getDatePublished() != null) {
            _hashCode += getDatePublished().hashCode();
        }
        if (getRankOverall() != null) {
            _hashCode += getRankOverall().hashCode();
        }
        if (getRankInThisCulture() != null) {
            _hashCode += getRankInThisCulture().hashCode();
        }
        if (getDetailsUrl() != null) {
            _hashCode += getDetailsUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DownloadSummary.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "DownloadSummary"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("downloadID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "DownloadID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cultureID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "CultureID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shortName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "ShortName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shortDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "ShortDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datePublished");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "DatePublished"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rankOverall");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "RankOverall"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rankInThisCulture");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "RankInThisCulture"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detailsUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "DetailsUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
