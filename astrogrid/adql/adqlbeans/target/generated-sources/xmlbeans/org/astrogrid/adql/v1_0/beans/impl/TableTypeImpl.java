/*
 * XML Type:  tableType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.TableType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML tableType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class TableTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.FromTableTypeImpl implements org.astrogrid.adql.v1_0.beans.TableType
{
    
    public TableTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ARCHIVE$0 = 
        new javax.xml.namespace.QName("", "Archive");
    private static final javax.xml.namespace.QName NAME$2 = 
        new javax.xml.namespace.QName("", "Name");
    private static final javax.xml.namespace.QName ALIAS$4 = 
        new javax.xml.namespace.QName("", "Alias");
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
     * Gets the "Name" attribute
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$2);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$2);
            }
            target.set(name);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ALIAS$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ALIAS$4);
            return target;
        }
    }
    
    /**
     * True if has "Alias" attribute
     */
    public boolean isSetAlias()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(ALIAS$4) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(ALIAS$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(ALIAS$4);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(ALIAS$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(ALIAS$4);
            }
            target.set(alias);
        }
    }
    
    /**
     * Unsets the "Alias" attribute
     */
    public void unsetAlias()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(ALIAS$4);
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
