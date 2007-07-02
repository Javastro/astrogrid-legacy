/*
 * XML Type:  existsPredType
 * Namespace: http://www.ivoa.net/xml/ADQL/v2.0/adql
 * Java type: org.astrogrid.adql.beans.ExistsPredType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML existsPredType(@http://www.ivoa.net/xml/ADQL/v2.0/adql).
 *
 * This is a complex type.
 */
public class ExistsPredTypeImpl extends org.astrogrid.adql.beans.impl.SearchTypeImpl implements org.astrogrid.adql.beans.ExistsPredType
{
    
    public ExistsPredTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SET$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v2.0/adql", "Set");
    
    
    /**
     * Gets the "Set" element
     */
    public org.astrogrid.adql.beans.SubQuerySet getSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SubQuerySet target = null;
            target = (org.astrogrid.adql.beans.SubQuerySet)get_store().find_element_user(SET$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Set" element
     */
    public void setSet(org.astrogrid.adql.beans.SubQuerySet set)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SubQuerySet target = null;
            target = (org.astrogrid.adql.beans.SubQuerySet)get_store().find_element_user(SET$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SubQuerySet)get_store().add_element_user(SET$0);
            }
            target.set(set);
        }
    }
    
    /**
     * Appends and returns a new empty "Set" element
     */
    public org.astrogrid.adql.beans.SubQuerySet addNewSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SubQuerySet target = null;
            target = (org.astrogrid.adql.beans.SubQuerySet)get_store().add_element_user(SET$0);
            return target;
        }
    }
}
