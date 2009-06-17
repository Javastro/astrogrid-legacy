/*
 * XML Type:  selectionLimitType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.SelectionLimitType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML selectionLimitType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class SelectionLimitTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.SelectionLimitType
{
    
    public SelectionLimitTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TOP$0 = 
        new javax.xml.namespace.QName("", "Top");
    
    
    /**
     * Gets the "Top" attribute
     */
    public long getTop()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TOP$0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "Top" attribute
     */
    public org.apache.xmlbeans.XmlUnsignedInt xgetTop()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(TOP$0);
            return target;
        }
    }
    
    /**
     * True if has "Top" attribute
     */
    public boolean isSetTop()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(TOP$0) != null;
        }
    }
    
    /**
     * Sets the "Top" attribute
     */
    public void setTop(long top)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TOP$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TOP$0);
            }
            target.setLongValue(top);
        }
    }
    
    /**
     * Sets (as xml) the "Top" attribute
     */
    public void xsetTop(org.apache.xmlbeans.XmlUnsignedInt top)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(TOP$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(TOP$0);
            }
            target.set(top);
        }
    }
    
    /**
     * Unsets the "Top" attribute
     */
    public void unsetTop()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(TOP$0);
        }
    }
}
