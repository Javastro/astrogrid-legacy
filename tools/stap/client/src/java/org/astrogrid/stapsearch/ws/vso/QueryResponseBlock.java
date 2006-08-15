/**
 * QueryResponseBlock.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.astrogrid.stapsearch.ws.vso;

public class QueryResponseBlock  implements java.io.Serializable {
    private java.lang.String provider;

    private java.lang.String source;

    private java.lang.String instrument;

    private java.lang.String physobs;

    private org.astrogrid.stapsearch.ws.vso.Time time;

    private org.astrogrid.stapsearch.ws.vso.Wave wave;

    private org.astrogrid.stapsearch.ws.vso.Extent extent;

    private java.lang.String fileid;

    private java.lang.Float size;

    private org.astrogrid.stapsearch.ws.vso.Extra extra;

    private java.lang.String info;

    public QueryResponseBlock() {
    }

    public QueryResponseBlock(
           java.lang.String provider,
           java.lang.String source,
           java.lang.String instrument,
           java.lang.String physobs,
           org.astrogrid.stapsearch.ws.vso.Time time,
           org.astrogrid.stapsearch.ws.vso.Wave wave,
           org.astrogrid.stapsearch.ws.vso.Extent extent,
           java.lang.String fileid,
           java.lang.Float size,
           org.astrogrid.stapsearch.ws.vso.Extra extra,
           java.lang.String info) {
           this.provider = provider;
           this.source = source;
           this.instrument = instrument;
           this.physobs = physobs;
           this.time = time;
           this.wave = wave;
           this.extent = extent;
           this.fileid = fileid;
           this.size = size;
           this.extra = extra;
           this.info = info;
    }


    /**
     * Gets the provider value for this QueryResponseBlock.
     * 
     * @return provider
     */
    public java.lang.String getProvider() {
        return provider;
    }


    /**
     * Sets the provider value for this QueryResponseBlock.
     * 
     * @param provider
     */
    public void setProvider(java.lang.String provider) {
        this.provider = provider;
    }


    /**
     * Gets the source value for this QueryResponseBlock.
     * 
     * @return source
     */
    public java.lang.String getSource() {
        return source;
    }


    /**
     * Sets the source value for this QueryResponseBlock.
     * 
     * @param source
     */
    public void setSource(java.lang.String source) {
        this.source = source;
    }


    /**
     * Gets the instrument value for this QueryResponseBlock.
     * 
     * @return instrument
     */
    public java.lang.String getInstrument() {
        return instrument;
    }


    /**
     * Sets the instrument value for this QueryResponseBlock.
     * 
     * @param instrument
     */
    public void setInstrument(java.lang.String instrument) {
        this.instrument = instrument;
    }


    /**
     * Gets the physobs value for this QueryResponseBlock.
     * 
     * @return physobs
     */
    public java.lang.String getPhysobs() {
        return physobs;
    }


    /**
     * Sets the physobs value for this QueryResponseBlock.
     * 
     * @param physobs
     */
    public void setPhysobs(java.lang.String physobs) {
        this.physobs = physobs;
    }


    /**
     * Gets the time value for this QueryResponseBlock.
     * 
     * @return time
     */
    public org.astrogrid.stapsearch.ws.vso.Time getTime() {
        return time;
    }


    /**
     * Sets the time value for this QueryResponseBlock.
     * 
     * @param time
     */
    public void setTime(org.astrogrid.stapsearch.ws.vso.Time time) {
        this.time = time;
    }


    /**
     * Gets the wave value for this QueryResponseBlock.
     * 
     * @return wave
     */
    public org.astrogrid.stapsearch.ws.vso.Wave getWave() {
        return wave;
    }


    /**
     * Sets the wave value for this QueryResponseBlock.
     * 
     * @param wave
     */
    public void setWave(org.astrogrid.stapsearch.ws.vso.Wave wave) {
        this.wave = wave;
    }


    /**
     * Gets the extent value for this QueryResponseBlock.
     * 
     * @return extent
     */
    public org.astrogrid.stapsearch.ws.vso.Extent getExtent() {
        return extent;
    }


    /**
     * Sets the extent value for this QueryResponseBlock.
     * 
     * @param extent
     */
    public void setExtent(org.astrogrid.stapsearch.ws.vso.Extent extent) {
        this.extent = extent;
    }


    /**
     * Gets the fileid value for this QueryResponseBlock.
     * 
     * @return fileid
     */
    public java.lang.String getFileid() {
        return fileid;
    }


    /**
     * Sets the fileid value for this QueryResponseBlock.
     * 
     * @param fileid
     */
    public void setFileid(java.lang.String fileid) {
        this.fileid = fileid;
    }


    /**
     * Gets the size value for this QueryResponseBlock.
     * 
     * @return size
     */
    public java.lang.Float getSize() {
        return size;
    }


    /**
     * Sets the size value for this QueryResponseBlock.
     * 
     * @param size
     */
    public void setSize(java.lang.Float size) {
        this.size = size;
    }


    /**
     * Gets the extra value for this QueryResponseBlock.
     * 
     * @return extra
     */
    public org.astrogrid.stapsearch.ws.vso.Extra getExtra() {
        return extra;
    }


    /**
     * Sets the extra value for this QueryResponseBlock.
     * 
     * @param extra
     */
    public void setExtra(org.astrogrid.stapsearch.ws.vso.Extra extra) {
        this.extra = extra;
    }


    /**
     * Gets the info value for this QueryResponseBlock.
     * 
     * @return info
     */
    public java.lang.String getInfo() {
        return info;
    }


    /**
     * Sets the info value for this QueryResponseBlock.
     * 
     * @param info
     */
    public void setInfo(java.lang.String info) {
        this.info = info;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryResponseBlock)) return false;
        QueryResponseBlock other = (QueryResponseBlock) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.provider==null && other.getProvider()==null) || 
             (this.provider!=null &&
              this.provider.equals(other.getProvider()))) &&
            ((this.source==null && other.getSource()==null) || 
             (this.source!=null &&
              this.source.equals(other.getSource()))) &&
            ((this.instrument==null && other.getInstrument()==null) || 
             (this.instrument!=null &&
              this.instrument.equals(other.getInstrument()))) &&
            ((this.physobs==null && other.getPhysobs()==null) || 
             (this.physobs!=null &&
              this.physobs.equals(other.getPhysobs()))) &&
            ((this.time==null && other.getTime()==null) || 
             (this.time!=null &&
              this.time.equals(other.getTime()))) &&
            ((this.wave==null && other.getWave()==null) || 
             (this.wave!=null &&
              this.wave.equals(other.getWave()))) &&
            ((this.extent==null && other.getExtent()==null) || 
             (this.extent!=null &&
              this.extent.equals(other.getExtent()))) &&
            ((this.fileid==null && other.getFileid()==null) || 
             (this.fileid!=null &&
              this.fileid.equals(other.getFileid()))) &&
            ((this.size==null && other.getSize()==null) || 
             (this.size!=null &&
              this.size.equals(other.getSize()))) &&
            ((this.extra==null && other.getExtra()==null) || 
             (this.extra!=null &&
              this.extra.equals(other.getExtra()))) &&
            ((this.info==null && other.getInfo()==null) || 
             (this.info!=null &&
              this.info.equals(other.getInfo())));
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
        if (getProvider() != null) {
            _hashCode += getProvider().hashCode();
        }
        if (getSource() != null) {
            _hashCode += getSource().hashCode();
        }
        if (getInstrument() != null) {
            _hashCode += getInstrument().hashCode();
        }
        if (getPhysobs() != null) {
            _hashCode += getPhysobs().hashCode();
        }
        if (getTime() != null) {
            _hashCode += getTime().hashCode();
        }
        if (getWave() != null) {
            _hashCode += getWave().hashCode();
        }
        if (getExtent() != null) {
            _hashCode += getExtent().hashCode();
        }
        if (getFileid() != null) {
            _hashCode += getFileid().hashCode();
        }
        if (getSize() != null) {
            _hashCode += getSize().hashCode();
        }
        if (getExtra() != null) {
            _hashCode += getExtra().hashCode();
        }
        if (getInfo() != null) {
            _hashCode += getInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QueryResponseBlock.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "QueryResponseBlock"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provider");
        elemField.setXmlName(new javax.xml.namespace.QName("", "provider"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source");
        elemField.setXmlName(new javax.xml.namespace.QName("", "source"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instrument");
        elemField.setXmlName(new javax.xml.namespace.QName("", "instrument"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("physobs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "physobs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("time");
        elemField.setXmlName(new javax.xml.namespace.QName("", "time"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Time"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wave");
        elemField.setXmlName(new javax.xml.namespace.QName("", "wave"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Wave"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extent");
        elemField.setXmlName(new javax.xml.namespace.QName("", "extent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Extent"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fileid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fileid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "size"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extra");
        elemField.setXmlName(new javax.xml.namespace.QName("", "extra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://virtualsolar.org/VSO/VSOi_strict", "Extra"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("info");
        elemField.setXmlName(new javax.xml.namespace.QName("", "info"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
