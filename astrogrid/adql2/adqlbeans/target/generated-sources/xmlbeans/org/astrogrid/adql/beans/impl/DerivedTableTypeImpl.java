/*
 * XML Type:  derivedTableType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.DerivedTableType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML derivedTableType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class DerivedTableTypeImpl extends org.astrogrid.adql.beans.impl.FromTableTypeImpl implements org.astrogrid.adql.beans.DerivedTableType
{
    
    public DerivedTableTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SUBQUERY$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "SubQuery");
    private static final javax.xml.namespace.QName ALIAS$2 = 
        new javax.xml.namespace.QName("", "Alias");
    
    
    /**
     * Gets the "SubQuery" element
     */
    public org.astrogrid.adql.beans.SubQueryType getSubQuery()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SubQueryType target = null;
            target = (org.astrogrid.adql.beans.SubQueryType)get_store().find_element_user(SUBQUERY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "SubQuery" element
     */
    public void setSubQuery(org.astrogrid.adql.beans.SubQueryType subQuery)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SubQueryType target = null;
            target = (org.astrogrid.adql.beans.SubQueryType)get_store().find_element_user(SUBQUERY$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SubQueryType)get_store().add_element_user(SUBQUERY$0);
            }
            target.set(subQuery);
        }
    }
    
    /**
     * Appends and returns a new empty "SubQuery" element
     */
    public org.astrogrid.adql.beans.SubQueryType addNewSubQuery()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SubQueryType target = null;
            target = (org.astrogrid.adql.beans.SubQueryType)get_store().add_element_user(SUBQUERY$0);
            return target;
        }
    }
    
    /**
     * Gets the "Alias" attribute
     */
    public java.lang.String getAlias()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ALIAS$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Alias" attribute
     */
    public org.apache.xmlbeans.XmlString xgetAlias()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ALIAS$2);
            return target;
        }
    }
    
    /**
     * Sets the "Alias" attribute
     */
    public void setAlias(java.lang.String alias)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ALIAS$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ALIAS$2);
            }
            target.setStringValue(alias);
        }
    }
    
    /**
     * Sets (as xml) the "Alias" attribute
     */
    public void xsetAlias(org.apache.xmlbeans.XmlString alias)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ALIAS$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(ALIAS$2);
            }
            target.set(alias);
        }
    }
}
