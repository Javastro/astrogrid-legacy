/*
 * XML Type:  columnReferenceType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.ColumnReferenceType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML columnReferenceType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class ColumnReferenceTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.v1_0.beans.ColumnReferenceType
{
    
    public ColumnReferenceTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ARCHIVE$0 = 
        new javax.xml.namespace.QName("", "Archive");
    private static final javax.xml.namespace.QName TABLE$2 = 
        new javax.xml.namespace.QName("", "Table");
    private static final javax.xml.namespace.QName NAME$4 = 
        new javax.xml.namespace.QName("", "Name");
    private static final javax.xml.namespace.QName XPATHNAME$6 = 
        new javax.xml.namespace.QName("", "xpathName");
    
    
    /**
     * Gets the "Archive" attribute
     */
    public java.lang.String getArchive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ARCHIVE$0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Archive" attribute
     */
    public org.apache.xmlbeans.XmlString xgetArchive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ARCHIVE$0);
            return target;
        }
    }
    
    /**
     * True if has "Archive" attribute
     */
    public boolean isSetArchive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ARCHIVE$0) != null;
        }
    }
    
    /**
     * Sets the "Archive" attribute
     */
    public void setArchive(java.lang.String archive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ARCHIVE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ARCHIVE$0);
            }
            target.setStringValue(archive);
        }
    }
    
    /**
     * Sets (as xml) the "Archive" attribute
     */
    public void xsetArchive(org.apache.xmlbeans.XmlString archive)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ARCHIVE$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(ARCHIVE$0);
            }
            target.set(archive);
        }
    }
    
    /**
     * Unsets the "Archive" attribute
     */
    public void unsetArchive()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ARCHIVE$0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TABLE$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TABLE$2);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TABLE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TABLE$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(TABLE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(TABLE$2);
            }
            target.set(table);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$4);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$4);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(XPATHNAME$6);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(XPATHNAME$6);
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
            return get_store().find_attribute_user(XPATHNAME$6) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(XPATHNAME$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(XPATHNAME$6);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(XPATHNAME$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(XPATHNAME$6);
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
            get_store().remove_attribute(XPATHNAME$6);
        }
    }
}
