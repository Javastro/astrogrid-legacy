/*
 * XML Type:  columnReferenceType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.ColumnReferenceType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML columnReferenceType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class ColumnReferenceTypeImpl extends org.astrogrid.adql.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.beans.ColumnReferenceType
{
    
    public ColumnReferenceTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CATALOG$0 = 
        new javax.xml.namespace.QName("", "Catalog");
    private static final javax.xml.namespace.QName SCHEMA$2 = 
        new javax.xml.namespace.QName("", "Schema");
    private static final javax.xml.namespace.QName TABLE$4 = 
        new javax.xml.namespace.QName("", "Table");
    private static final javax.xml.namespace.QName NAME$6 = 
        new javax.xml.namespace.QName("", "Name");
    private static final javax.xml.namespace.QName XPATHNAME$8 = 
        new javax.xml.namespace.QName("", "xpathName");
    
    
    /**
     * Gets the "Catalog" attribute
     */
    public java.lang.String getCatalog()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CATALOG$0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Catalog" attribute
     */
    public org.apache.xmlbeans.XmlString xgetCatalog()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CATALOG$0);
            return target;
        }
    }
    
    /**
     * True if has "Catalog" attribute
     */
    public boolean isSetCatalog()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CATALOG$0) != null;
        }
    }
    
    /**
     * Sets the "Catalog" attribute
     */
    public void setCatalog(java.lang.String catalog)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CATALOG$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CATALOG$0);
            }
            target.setStringValue(catalog);
        }
    }
    
    /**
     * Sets (as xml) the "Catalog" attribute
     */
    public void xsetCatalog(org.apache.xmlbeans.XmlString catalog)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CATALOG$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(CATALOG$0);
            }
            target.set(catalog);
        }
    }
    
    /**
     * Unsets the "Catalog" attribute
     */
    public void unsetCatalog()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CATALOG$0);
        }
    }
    
    /**
     * Gets the "Schema" attribute
     */
    public java.lang.String getSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SCHEMA$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Schema" attribute
     */
    public org.apache.xmlbeans.XmlString xgetSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(SCHEMA$2);
            return target;
        }
    }
    
    /**
     * True if has "Schema" attribute
     */
    public boolean isSetSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(SCHEMA$2) != null;
        }
    }
    
    /**
     * Sets the "Schema" attribute
     */
    public void setSchema(java.lang.String schema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SCHEMA$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SCHEMA$2);
            }
            target.setStringValue(schema);
        }
    }
    
    /**
     * Sets (as xml) the "Schema" attribute
     */
    public void xsetSchema(org.apache.xmlbeans.XmlString schema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(SCHEMA$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(SCHEMA$2);
            }
            target.set(schema);
        }
    }
    
    /**
     * Unsets the "Schema" attribute
     */
    public void unsetSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(SCHEMA$2);
        }
    }
    
    /**
     * Gets the "Table" attribute
     */
    public java.lang.String getTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TABLE$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Table" attribute
     */
    public org.apache.xmlbeans.XmlString xgetTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TABLE$4);
            return target;
        }
    }
    
    /**
     * True if has "Table" attribute
     */
    public boolean isSetTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TABLE$4) != null;
        }
    }
    
    /**
     * Sets the "Table" attribute
     */
    public void setTable(java.lang.String table)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TABLE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TABLE$4);
            }
            target.setStringValue(table);
        }
    }
    
    /**
     * Sets (as xml) the "Table" attribute
     */
    public void xsetTable(org.apache.xmlbeans.XmlString table)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TABLE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(TABLE$4);
            }
            target.set(table);
        }
    }
    
    /**
     * Unsets the "Table" attribute
     */
    public void unsetTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TABLE$4);
        }
    }
    
    /**
     * Gets the "Name" attribute
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Name" attribute
     */
    public org.apache.xmlbeans.XmlString xgetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
            return target;
        }
    }
    
    /**
     * Sets the "Name" attribute
     */
    public void setName(java.lang.String name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$6);
            }
            target.setStringValue(name);
        }
    }
    
    /**
     * Sets (as xml) the "Name" attribute
     */
    public void xsetName(org.apache.xmlbeans.XmlString name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$6);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "xpathName" attribute
     */
    public java.lang.String getXpathName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(XPATHNAME$8);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "xpathName" attribute
     */
    public org.apache.xmlbeans.XmlString xgetXpathName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(XPATHNAME$8);
            return target;
        }
    }
    
    /**
     * True if has "xpathName" attribute
     */
    public boolean isSetXpathName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(XPATHNAME$8) != null;
        }
    }
    
    /**
     * Sets the "xpathName" attribute
     */
    public void setXpathName(java.lang.String xpathName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(XPATHNAME$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(XPATHNAME$8);
            }
            target.setStringValue(xpathName);
        }
    }
    
    /**
     * Sets (as xml) the "xpathName" attribute
     */
    public void xsetXpathName(org.apache.xmlbeans.XmlString xpathName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(XPATHNAME$8);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(XPATHNAME$8);
            }
            target.set(xpathName);
        }
    }
    
    /**
     * Unsets the "xpathName" attribute
     */
    public void unsetXpathName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(XPATHNAME$8);
        }
    }
}
