/*
 * XML Type:  intoType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.IntoType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML intoType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class IntoTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.IntoType
{
    
    public IntoTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TABLENAME$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "TableName");
    
    
    /**
     * Gets the "TableName" element
     */
    public java.lang.String getTableName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TABLENAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "TableName" element
     */
    public org.apache.xmlbeans.XmlString xgetTableName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TABLENAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "TableName" element
     */
    public void setTableName(java.lang.String tableName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TABLENAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TABLENAME$0);
            }
            target.setStringValue(tableName);
        }
    }
    
    /**
     * Sets (as xml) the "TableName" element
     */
    public void xsetTableName(org.apache.xmlbeans.XmlString tableName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TABLENAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TABLENAME$0);
            }
            target.set(tableName);
        }
    }
}
