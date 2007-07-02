/*
 * XML Type:  subQuerySet
 * Namespace: http://www.ivoa.net/xml/ADQL/v2.0/adql
 * Java type: org.astrogrid.adql.beans.SubQuerySet
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML subQuerySet(@http://www.ivoa.net/xml/ADQL/v2.0/adql).
 *
 * This is a complex type.
 */
public class SubQuerySetImpl extends org.astrogrid.adql.beans.impl.InclusionSetTypeImpl implements org.astrogrid.adql.beans.SubQuerySet
{
    
    public SubQuerySetImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SELECTION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v2.0/adql", "selection");
    
    
    /**
     * Gets the "selection" element
     */
    public org.astrogrid.adql.beans.SelectType getSelection()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectType target = null;
            target = (org.astrogrid.adql.beans.SelectType)get_store().find_element_user(SELECTION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "selection" element
     */
    public void setSelection(org.astrogrid.adql.beans.SelectType selection)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectType target = null;
            target = (org.astrogrid.adql.beans.SelectType)get_store().find_element_user(SELECTION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SelectType)get_store().add_element_user(SELECTION$0);
            }
            target.set(selection);
        }
    }
    
    /**
     * Appends and returns a new empty "selection" element
     */
    public org.astrogrid.adql.beans.SelectType addNewSelection()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectType target = null;
            target = (org.astrogrid.adql.beans.SelectType)get_store().add_element_user(SELECTION$0);
            return target;
        }
    }
}
