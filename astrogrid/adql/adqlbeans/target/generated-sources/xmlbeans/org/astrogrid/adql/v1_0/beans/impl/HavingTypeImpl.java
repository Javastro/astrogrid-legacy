/*
 * XML Type:  havingType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.HavingType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML havingType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class HavingTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.v1_0.beans.HavingType
{
    
    public HavingTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONDITION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Condition");
    
    
    /**
     * Gets the "Condition" element
     */
    public org.astrogrid.adql.v1_0.beans.SearchType getCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SearchType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SearchType)get_store().find_element_user(CONDITION$0, 0);
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
    public void setCondition(org.astrogrid.adql.v1_0.beans.SearchType condition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SearchType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SearchType)get_store().find_element_user(CONDITION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.SearchType)get_store().add_element_user(CONDITION$0);
            }
            target.set(condition);
        }
    }
    
    /**
     * Appends and returns a new empty "Condition" element
     */
    public org.astrogrid.adql.v1_0.beans.SearchType addNewCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SearchType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SearchType)get_store().add_element_user(CONDITION$0);
            return target;
        }
    }
}
