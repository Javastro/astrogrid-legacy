/**
 * ThumbnailDescription.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.cdaw;

public class ThumbnailDescription  implements java.io.Serializable {
    private java.lang.String[] dataCdfs;

    private java.lang.String dataset;

    private java.util.Calendar endTime;

    private java.lang.String masterCdf;

    private java.lang.String name;

    private int numCols;

    private int numFrames;

    private int numRows;

    private long options;

    private int startRecord;

    private java.util.Calendar startTime;

    private int thumbnailHeight;

    private int thumbnailWidth;

    private int titleHeight;

    private java.lang.String type;

    private java.lang.String varName;

    public ThumbnailDescription() {
    }

    public ThumbnailDescription(
           java.lang.String[] dataCdfs,
           java.lang.String dataset,
           java.util.Calendar endTime,
           java.lang.String masterCdf,
           java.lang.String name,
           int numCols,
           int numFrames,
           int numRows,
           long options,
           int startRecord,
           java.util.Calendar startTime,
           int thumbnailHeight,
           int thumbnailWidth,
           int titleHeight,
           java.lang.String type,
           java.lang.String varName) {
           this.dataCdfs = dataCdfs;
           this.dataset = dataset;
           this.endTime = endTime;
           this.masterCdf = masterCdf;
           this.name = name;
           this.numCols = numCols;
           this.numFrames = numFrames;
           this.numRows = numRows;
           this.options = options;
           this.startRecord = startRecord;
           this.startTime = startTime;
           this.thumbnailHeight = thumbnailHeight;
           this.thumbnailWidth = thumbnailWidth;
           this.titleHeight = titleHeight;
           this.type = type;
           this.varName = varName;
    }


    /**
     * Gets the dataCdfs value for this ThumbnailDescription.
     * 
     * @return dataCdfs
     */
    public java.lang.String[] getDataCdfs() {
        return dataCdfs;
    }


    /**
     * Sets the dataCdfs value for this ThumbnailDescription.
     * 
     * @param dataCdfs
     */
    public void setDataCdfs(java.lang.String[] dataCdfs) {
        this.dataCdfs = dataCdfs;
    }


    /**
     * Gets the dataset value for this ThumbnailDescription.
     * 
     * @return dataset
     */
    public java.lang.String getDataset() {
        return dataset;
    }


    /**
     * Sets the dataset value for this ThumbnailDescription.
     * 
     * @param dataset
     */
    public void setDataset(java.lang.String dataset) {
        this.dataset = dataset;
    }


    /**
     * Gets the endTime value for this ThumbnailDescription.
     * 
     * @return endTime
     */
    public java.util.Calendar getEndTime() {
        return endTime;
    }


    /**
     * Sets the endTime value for this ThumbnailDescription.
     * 
     * @param endTime
     */
    public void setEndTime(java.util.Calendar endTime) {
        this.endTime = endTime;
    }


    /**
     * Gets the masterCdf value for this ThumbnailDescription.
     * 
     * @return masterCdf
     */
    public java.lang.String getMasterCdf() {
        return masterCdf;
    }


    /**
     * Sets the masterCdf value for this ThumbnailDescription.
     * 
     * @param masterCdf
     */
    public void setMasterCdf(java.lang.String masterCdf) {
        this.masterCdf = masterCdf;
    }


    /**
     * Gets the name value for this ThumbnailDescription.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ThumbnailDescription.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the numCols value for this ThumbnailDescription.
     * 
     * @return numCols
     */
    public int getNumCols() {
        return numCols;
    }


    /**
     * Sets the numCols value for this ThumbnailDescription.
     * 
     * @param numCols
     */
    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }


    /**
     * Gets the numFrames value for this ThumbnailDescription.
     * 
     * @return numFrames
     */
    public int getNumFrames() {
        return numFrames;
    }


    /**
     * Sets the numFrames value for this ThumbnailDescription.
     * 
     * @param numFrames
     */
    public void setNumFrames(int numFrames) {
        this.numFrames = numFrames;
    }


    /**
     * Gets the numRows value for this ThumbnailDescription.
     * 
     * @return numRows
     */
    public int getNumRows() {
        return numRows;
    }


    /**
     * Sets the numRows value for this ThumbnailDescription.
     * 
     * @param numRows
     */
    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }


    /**
     * Gets the options value for this ThumbnailDescription.
     * 
     * @return options
     */
    public long getOptions() {
        return options;
    }


    /**
     * Sets the options value for this ThumbnailDescription.
     * 
     * @param options
     */
    public void setOptions(long options) {
        this.options = options;
    }


    /**
     * Gets the startRecord value for this ThumbnailDescription.
     * 
     * @return startRecord
     */
    public int getStartRecord() {
        return startRecord;
    }


    /**
     * Sets the startRecord value for this ThumbnailDescription.
     * 
     * @param startRecord
     */
    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }


    /**
     * Gets the startTime value for this ThumbnailDescription.
     * 
     * @return startTime
     */
    public java.util.Calendar getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this ThumbnailDescription.
     * 
     * @param startTime
     */
    public void setStartTime(java.util.Calendar startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the thumbnailHeight value for this ThumbnailDescription.
     * 
     * @return thumbnailHeight
     */
    public int getThumbnailHeight() {
        return thumbnailHeight;
    }


    /**
     * Sets the thumbnailHeight value for this ThumbnailDescription.
     * 
     * @param thumbnailHeight
     */
    public void setThumbnailHeight(int thumbnailHeight) {
        this.thumbnailHeight = thumbnailHeight;
    }


    /**
     * Gets the thumbnailWidth value for this ThumbnailDescription.
     * 
     * @return thumbnailWidth
     */
    public int getThumbnailWidth() {
        return thumbnailWidth;
    }


    /**
     * Sets the thumbnailWidth value for this ThumbnailDescription.
     * 
     * @param thumbnailWidth
     */
    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }


    /**
     * Gets the titleHeight value for this ThumbnailDescription.
     * 
     * @return titleHeight
     */
    public int getTitleHeight() {
        return titleHeight;
    }


    /**
     * Sets the titleHeight value for this ThumbnailDescription.
     * 
     * @param titleHeight
     */
    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }


    /**
     * Gets the type value for this ThumbnailDescription.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this ThumbnailDescription.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the varName value for this ThumbnailDescription.
     * 
     * @return varName
     */
    public java.lang.String getVarName() {
        return varName;
    }


    /**
     * Sets the varName value for this ThumbnailDescription.
     * 
     * @param varName
     */
    public void setVarName(java.lang.String varName) {
        this.varName = varName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ThumbnailDescription)) return false;
        ThumbnailDescription other = (ThumbnailDescription) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataCdfs==null && other.getDataCdfs()==null) || 
             (this.dataCdfs!=null &&
              java.util.Arrays.equals(this.dataCdfs, other.getDataCdfs()))) &&
            ((this.dataset==null && other.getDataset()==null) || 
             (this.dataset!=null &&
              this.dataset.equals(other.getDataset()))) &&
            ((this.endTime==null && other.getEndTime()==null) || 
             (this.endTime!=null &&
              this.endTime.equals(other.getEndTime()))) &&
            ((this.masterCdf==null && other.getMasterCdf()==null) || 
             (this.masterCdf!=null &&
              this.masterCdf.equals(other.getMasterCdf()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            this.numCols == other.getNumCols() &&
            this.numFrames == other.getNumFrames() &&
            this.numRows == other.getNumRows() &&
            this.options == other.getOptions() &&
            this.startRecord == other.getStartRecord() &&
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime()))) &&
            this.thumbnailHeight == other.getThumbnailHeight() &&
            this.thumbnailWidth == other.getThumbnailWidth() &&
            this.titleHeight == other.getTitleHeight() &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.varName==null && other.getVarName()==null) || 
             (this.varName!=null &&
              this.varName.equals(other.getVarName())));
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
        if (getDataCdfs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDataCdfs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDataCdfs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDataset() != null) {
            _hashCode += getDataset().hashCode();
        }
        if (getEndTime() != null) {
            _hashCode += getEndTime().hashCode();
        }
        if (getMasterCdf() != null) {
            _hashCode += getMasterCdf().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        _hashCode += getNumCols();
        _hashCode += getNumFrames();
        _hashCode += getNumRows();
        _hashCode += new Long(getOptions()).hashCode();
        _hashCode += getStartRecord();
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
        }
        _hashCode += getThumbnailHeight();
        _hashCode += getThumbnailWidth();
        _hashCode += getTitleHeight();
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getVarName() != null) {
            _hashCode += getVarName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ThumbnailDescription.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cdaweb.gsfc.nasa.gov/WS/types/CDASWS", "ThumbnailDescription"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataCdfs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataCdfs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataset");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataset"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("masterCdf");
        elemField.setXmlName(new javax.xml.namespace.QName("", "masterCdf"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numCols");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numCols"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numFrames");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numFrames"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numRows");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numRows"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("options");
        elemField.setXmlName(new javax.xml.namespace.QName("", "options"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startRecord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startRecord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("thumbnailHeight");
        elemField.setXmlName(new javax.xml.namespace.QName("", "thumbnailHeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("thumbnailWidth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "thumbnailWidth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titleHeight");
        elemField.setXmlName(new javax.xml.namespace.QName("", "titleHeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("varName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "varName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
