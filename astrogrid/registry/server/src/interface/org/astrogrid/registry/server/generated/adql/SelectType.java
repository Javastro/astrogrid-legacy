/**
 * SelectType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.registry.server.generated.adql;

public class SelectType  implements java.io.Serializable {
    private org.astrogrid.registry.server.generated.adql.SelectionOptionType allow;
    private org.astrogrid.registry.server.generated.adql.SelectionLimitType restrict;
    private org.astrogrid.registry.server.generated.adql.SelectionListType selectionList;
    private org.astrogrid.registry.server.generated.adql.FromType from;
    private org.astrogrid.registry.server.generated.adql.WhereType where;
    private org.astrogrid.registry.server.generated.adql.GroupByType groupBy;
    private org.astrogrid.registry.server.generated.adql.HavingType having;
    private org.astrogrid.registry.server.generated.adql.OrderExpressionType orderBy;

    public SelectType() {
    }

    public org.astrogrid.registry.server.generated.adql.SelectionOptionType getAllow() {
        return allow;
    }

    public void setAllow(org.astrogrid.registry.server.generated.adql.SelectionOptionType allow) {
        this.allow = allow;
    }

    public org.astrogrid.registry.server.generated.adql.SelectionLimitType getRestrict() {
        return restrict;
    }

    public void setRestrict(org.astrogrid.registry.server.generated.adql.SelectionLimitType restrict) {
        this.restrict = restrict;
    }

    public org.astrogrid.registry.server.generated.adql.SelectionListType getSelectionList() {
        return selectionList;
    }

    public void setSelectionList(org.astrogrid.registry.server.generated.adql.SelectionListType selectionList) {
        this.selectionList = selectionList;
    }

    public org.astrogrid.registry.server.generated.adql.FromType getFrom() {
        return from;
    }

    public void setFrom(org.astrogrid.registry.server.generated.adql.FromType from) {
        this.from = from;
    }

    public org.astrogrid.registry.server.generated.adql.WhereType getWhere() {
        return where;
    }

    public void setWhere(org.astrogrid.registry.server.generated.adql.WhereType where) {
        this.where = where;
    }

    public org.astrogrid.registry.server.generated.adql.GroupByType getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(org.astrogrid.registry.server.generated.adql.GroupByType groupBy) {
        this.groupBy = groupBy;
    }

    public org.astrogrid.registry.server.generated.adql.HavingType getHaving() {
        return having;
    }

    public void setHaving(org.astrogrid.registry.server.generated.adql.HavingType having) {
        this.having = having;
    }

    public org.astrogrid.registry.server.generated.adql.OrderExpressionType getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(org.astrogrid.registry.server.generated.adql.OrderExpressionType orderBy) {
        this.orderBy = orderBy;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SelectType)) return false;
        SelectType other = (SelectType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.allow==null && other.getAllow()==null) || 
             (this.allow!=null &&
              this.allow.equals(other.getAllow()))) &&
            ((this.restrict==null && other.getRestrict()==null) || 
             (this.restrict!=null &&
              this.restrict.equals(other.getRestrict()))) &&
            ((this.selectionList==null && other.getSelectionList()==null) || 
             (this.selectionList!=null &&
              this.selectionList.equals(other.getSelectionList()))) &&
            ((this.from==null && other.getFrom()==null) || 
             (this.from!=null &&
              this.from.equals(other.getFrom()))) &&
            ((this.where==null && other.getWhere()==null) || 
             (this.where!=null &&
              this.where.equals(other.getWhere()))) &&
            ((this.groupBy==null && other.getGroupBy()==null) || 
             (this.groupBy!=null &&
              this.groupBy.equals(other.getGroupBy()))) &&
            ((this.having==null && other.getHaving()==null) || 
             (this.having!=null &&
              this.having.equals(other.getHaving()))) &&
            ((this.orderBy==null && other.getOrderBy()==null) || 
             (this.orderBy!=null &&
              this.orderBy.equals(other.getOrderBy())));
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
        if (getAllow() != null) {
            _hashCode += getAllow().hashCode();
        }
        if (getRestrict() != null) {
            _hashCode += getRestrict().hashCode();
        }
        if (getSelectionList() != null) {
            _hashCode += getSelectionList().hashCode();
        }
        if (getFrom() != null) {
            _hashCode += getFrom().hashCode();
        }
        if (getWhere() != null) {
            _hashCode += getWhere().hashCode();
        }
        if (getGroupBy() != null) {
            _hashCode += getGroupBy().hashCode();
        }
        if (getHaving() != null) {
            _hashCode += getHaving().hashCode();
        }
        if (getOrderBy() != null) {
            _hashCode += getOrderBy().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SelectType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allow");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Allow"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionOptionType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("restrict");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Restrict"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionLimitType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectionList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "SelectionList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "selectionListType"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("from");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "From"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "fromType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("where");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Where"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "whereType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "GroupBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "groupByType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("having");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "Having"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "havingType"));
        elemField.setMinOccurs(0);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "OrderBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v0.7.4", "orderExpressionType"));
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
