/**
 * Download.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.microsoft.www;

public class Download  implements java.io.Serializable {
    private java.lang.String downloadID;
    private java.lang.String cultureID;
    private java.util.Calendar datePublished;
    private java.lang.String version;
    private java.lang.String name;
    private java.lang.String shortName;
    private java.lang.String subTitle;
    private java.lang.String keywords;
    private int minTotalSize;
    private int maxTotalSize;
    private java.lang.String requirements;
    private java.lang.String instructions;
    private java.lang.String description;
    private java.lang.String shortDescription;
    private java.lang.String additionalInfo;
    private com.microsoft.www.ArrayOfDownloadRelatedLink relatedLinks;
    private com.microsoft.www.ArrayOfDownloadDCCategory DCCategories;
    private com.microsoft.www.ArrayOfDownloadProductOrTechnology relatedProductsOrTechnologies;
    private com.microsoft.www.ArrayOfDownloadRelease releases;
    private com.microsoft.www.ArrayOfDownloadRelatedDownload relatedDownloads;

    public Download() {
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

    public java.util.Calendar getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(java.util.Calendar datePublished) {
        this.datePublished = datePublished;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getShortName() {
        return shortName;
    }

    public void setShortName(java.lang.String shortName) {
        this.shortName = shortName;
    }

    public java.lang.String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(java.lang.String subTitle) {
        this.subTitle = subTitle;
    }

    public java.lang.String getKeywords() {
        return keywords;
    }

    public void setKeywords(java.lang.String keywords) {
        this.keywords = keywords;
    }

    public int getMinTotalSize() {
        return minTotalSize;
    }

    public void setMinTotalSize(int minTotalSize) {
        this.minTotalSize = minTotalSize;
    }

    public int getMaxTotalSize() {
        return maxTotalSize;
    }

    public void setMaxTotalSize(int maxTotalSize) {
        this.maxTotalSize = maxTotalSize;
    }

    public java.lang.String getRequirements() {
        return requirements;
    }

    public void setRequirements(java.lang.String requirements) {
        this.requirements = requirements;
    }

    public java.lang.String getInstructions() {
        return instructions;
    }

    public void setInstructions(java.lang.String instructions) {
        this.instructions = instructions;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(java.lang.String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public java.lang.String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(java.lang.String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public com.microsoft.www.ArrayOfDownloadRelatedLink getRelatedLinks() {
        return relatedLinks;
    }

    public void setRelatedLinks(com.microsoft.www.ArrayOfDownloadRelatedLink relatedLinks) {
        this.relatedLinks = relatedLinks;
    }

    public com.microsoft.www.ArrayOfDownloadDCCategory getDCCategories() {
        return DCCategories;
    }

    public void setDCCategories(com.microsoft.www.ArrayOfDownloadDCCategory DCCategories) {
        this.DCCategories = DCCategories;
    }

    public com.microsoft.www.ArrayOfDownloadProductOrTechnology getRelatedProductsOrTechnologies() {
        return relatedProductsOrTechnologies;
    }

    public void setRelatedProductsOrTechnologies(com.microsoft.www.ArrayOfDownloadProductOrTechnology relatedProductsOrTechnologies) {
        this.relatedProductsOrTechnologies = relatedProductsOrTechnologies;
    }

    public com.microsoft.www.ArrayOfDownloadRelease getReleases() {
        return releases;
    }

    public void setReleases(com.microsoft.www.ArrayOfDownloadRelease releases) {
        this.releases = releases;
    }

    public com.microsoft.www.ArrayOfDownloadRelatedDownload getRelatedDownloads() {
        return relatedDownloads;
    }

    public void setRelatedDownloads(com.microsoft.www.ArrayOfDownloadRelatedDownload relatedDownloads) {
        this.relatedDownloads = relatedDownloads;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Download)) return false;
        Download other = (Download) obj;
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
            ((this.datePublished==null && other.getDatePublished()==null) || 
             (this.datePublished!=null &&
              this.datePublished.equals(other.getDatePublished()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.shortName==null && other.getShortName()==null) || 
             (this.shortName!=null &&
              this.shortName.equals(other.getShortName()))) &&
            ((this.subTitle==null && other.getSubTitle()==null) || 
             (this.subTitle!=null &&
              this.subTitle.equals(other.getSubTitle()))) &&
            ((this.keywords==null && other.getKeywords()==null) || 
             (this.keywords!=null &&
              this.keywords.equals(other.getKeywords()))) &&
            this.minTotalSize == other.getMinTotalSize() &&
            this.maxTotalSize == other.getMaxTotalSize() &&
            ((this.requirements==null && other.getRequirements()==null) || 
             (this.requirements!=null &&
              this.requirements.equals(other.getRequirements()))) &&
            ((this.instructions==null && other.getInstructions()==null) || 
             (this.instructions!=null &&
              this.instructions.equals(other.getInstructions()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.shortDescription==null && other.getShortDescription()==null) || 
             (this.shortDescription!=null &&
              this.shortDescription.equals(other.getShortDescription()))) &&
            ((this.additionalInfo==null && other.getAdditionalInfo()==null) || 
             (this.additionalInfo!=null &&
              this.additionalInfo.equals(other.getAdditionalInfo()))) &&
            ((this.relatedLinks==null && other.getRelatedLinks()==null) || 
             (this.relatedLinks!=null &&
              this.relatedLinks.equals(other.getRelatedLinks()))) &&
            ((this.DCCategories==null && other.getDCCategories()==null) || 
             (this.DCCategories!=null &&
              this.DCCategories.equals(other.getDCCategories()))) &&
            ((this.relatedProductsOrTechnologies==null && other.getRelatedProductsOrTechnologies()==null) || 
             (this.relatedProductsOrTechnologies!=null &&
              this.relatedProductsOrTechnologies.equals(other.getRelatedProductsOrTechnologies()))) &&
            ((this.releases==null && other.getReleases()==null) || 
             (this.releases!=null &&
              this.releases.equals(other.getReleases()))) &&
            ((this.relatedDownloads==null && other.getRelatedDownloads()==null) || 
             (this.relatedDownloads!=null &&
              this.relatedDownloads.equals(other.getRelatedDownloads())));
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
        if (getDatePublished() != null) {
            _hashCode += getDatePublished().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getShortName() != null) {
            _hashCode += getShortName().hashCode();
        }
        if (getSubTitle() != null) {
            _hashCode += getSubTitle().hashCode();
        }
        if (getKeywords() != null) {
            _hashCode += getKeywords().hashCode();
        }
        _hashCode += getMinTotalSize();
        _hashCode += getMaxTotalSize();
        if (getRequirements() != null) {
            _hashCode += getRequirements().hashCode();
        }
        if (getInstructions() != null) {
            _hashCode += getInstructions().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getShortDescription() != null) {
            _hashCode += getShortDescription().hashCode();
        }
        if (getAdditionalInfo() != null) {
            _hashCode += getAdditionalInfo().hashCode();
        }
        if (getRelatedLinks() != null) {
            _hashCode += getRelatedLinks().hashCode();
        }
        if (getDCCategories() != null) {
            _hashCode += getDCCategories().hashCode();
        }
        if (getRelatedProductsOrTechnologies() != null) {
            _hashCode += getRelatedProductsOrTechnologies().hashCode();
        }
        if (getReleases() != null) {
            _hashCode += getReleases().hashCode();
        }
        if (getRelatedDownloads() != null) {
            _hashCode += getRelatedDownloads().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Download.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "Download"));
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
        elemField.setFieldName("datePublished");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "DatePublished"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "Name"));
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
        elemField.setFieldName("subTitle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "SubTitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keywords");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "Keywords"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minTotalSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "MinTotalSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxTotalSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "MaxTotalSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requirements");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "Requirements"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instructions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "Instructions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "Description"));
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
        elemField.setFieldName("additionalInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "AdditionalInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relatedLinks");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "RelatedLinks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadRelatedLink"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DCCategories");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "DCCategories"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadDCCategory"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relatedProductsOrTechnologies");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "RelatedProductsOrTechnologies"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadProductOrTechnology"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("releases");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "Releases"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadRelease"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("relatedDownloads");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.microsoft.com", "RelatedDownloads"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.microsoft.com", "ArrayOfDownloadRelatedDownload"));
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
