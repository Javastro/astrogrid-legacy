/*
 * XML Type:  whereType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.WhereType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML whereType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class WhereTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.WhereType
{
    
    public WhereTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONDITION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Condition");
    
    
    /**
     * Gets the "Condition" element
     */
    public org.astrogrid.adql.beans.SearchType getCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SearchType target = null;
            target = (org.astrogrid.adql.beans.SearchType)get_store().find_element_user(CONDITION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Condition" element
     */
    public void setCondition(org.astrogrid.adql.beans.SearchType condition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SearchType target = null;
            target = (org.astrogrid.adql.beans.SearchType)get_store().find_element_user(CONDITION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SearchType)get_store().add_element_user(CONDITION$0);
            }
            target.set(condition);
        }
    }
    
    /**
     * Appends and returns a new empty "Condition" element
     */
    public org.astrogrid.adql.beans.SearchType addNewCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SearchType target = null;
            target = (org.astrogrid.adql.beans.SearchType)get_store().add_element_user(CONDITION$0);
            return target;
        }
    }
}
