/**
 * ExecPlan.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package net.ivoa.SkyNode;

public class ExecPlan  implements java.io.Serializable {
    private long planId;
    private java.lang.String format;
    private java.lang.String portalURL;
    private java.lang.String uploadTableName;
    private java.lang.String uploadTableAlias;
    private net.ivoa.SkyNode.ArrayOfPlanElement planElements;

    public ExecPlan() {
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public java.lang.String getFormat() {
        return format;
    }

    public void setFormat(java.lang.String format) {
        this.format = format;
    }

    public java.lang.String getPortalURL() {
        return portalURL;
    }

    public void setPortalURL(java.lang.String portalURL) {
        this.portalURL = portalURL;
    }

    public java.lang.String getUploadTableName() {
        return uploadTableName;
    }

    public void setUploadTableName(java.lang.String uploadTableName) {
        this.uploadTableName = uploadTableName;
    }

    public java.lang.String getUploadTableAlias() {
        return uploadTableAlias;
    }

    public void setUploadTableAlias(java.lang.String uploadTableAlias) {
        this.uploadTableAlias = uploadTableAlias;
    }

    public net.ivoa.SkyNode.ArrayOfPlanElement getPlanElements() {
        return planElements;
    }

    public void setPlanElements(net.ivoa.SkyNode.ArrayOfPlanElement planElements) {
        this.planElements = planElements;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExecPlan)) return false;
        ExecPlan other = (ExecPlan) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.planId == other.getPlanId() &&
            ((this.format==null && other.getFormat()==null) || 
             (this.format!=null &&
              this.format.equals(other.getFormat()))) &&
            ((this.portalURL==null && other.getPortalURL()==null) || 
             (this.portalURL!=null &&
              this.portalURL.equals(other.getPortalURL()))) &&
            ((this.uploadTableName==null && other.getUploadTableName()==null) || 
             (this.uploadTableName!=null &&
              this.uploadTableName.equals(other.getUploadTableName()))) &&
            ((this.uploadTableAlias==null && other.getUploadTableAlias()==null) || 
             (this.uploadTableAlias!=null &&
              this.uploadTableAlias.equals(other.getUploadTableAlias()))) &&
            ((this.planElements==null && other.getPlanElements()==null) || 
             (this.planElements!=null &&
              this.planElements.equals(other.getPlanElements())));
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
        _hashCode += new Long(getPlanId()).hashCode();
        if (getFormat() != null) {
            _hashCode += getFormat().hashCode();
        }
        if (getPortalURL() != null) {
            _hashCode += getPortalURL().hashCode();
        }
        if (getUploadTableName() != null) {
            _hashCode += getUploadTableName().hashCode();
        }
        if (getUploadTableAlias() != null) {
            _hashCode += getUploadTableAlias().hashCode();
        }
        if (getPlanElements() != null) {
            _hashCode += getPlanElements().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExecPlan.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ExecPlan"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("planId");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "PlanId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("format");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "Format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("portalURL");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "PortalURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uploadTableName");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "UploadTableName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uploadTableAlias");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "UploadTableAlias"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("planElements");
        elemField.setXmlName(new javax.xml.namespace.QName("SkyNode.ivoa.net", "PlanElements"));
        elemField.setXmlType(new javax.xml.namespace.QName("SkyNode.ivoa.net", "ArrayOfPlanElement"));
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
