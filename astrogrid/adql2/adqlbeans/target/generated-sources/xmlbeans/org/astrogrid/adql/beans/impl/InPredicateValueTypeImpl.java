/*
 * XML Type:  inPredicateValueType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.InPredicateValueType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML inPredicateValueType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class InPredicateValueTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.InPredicateValueType
{
    
    public InPredicateValueTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SUBQUERY$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "SubQuery");
    private static final javax.xml.namespace.QName INVALUELIST$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "InValueList");
    
    
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
     * True if has "SubQuery" element
     */
    public boolean isSetSubQuery()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SUBQUERY$0) != 0;
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
     * Unsets the "SubQuery" element
     */
    public void unsetSubQuery()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SUBQUERY$0, 0);
        }
    }
    
    /**
     * Gets the "InValueList" element
     */
    public org.astrogrid.adql.beans.InValueListType getInValueList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.InValueListType target = null;
            target = (org.astrogrid.adql.beans.InValueListType)get_store().find_element_user(INVALUELIST$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "InValueList" element
     */
    public boolean isSetInValueList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INVALUELIST$2) != 0;
        }
    }
    
    /**
     * Sets the "InValueList" element
     */
    public void setInValueList(org.astrogrid.adql.beans.InValueListType inValueList)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.InValueListType target = null;
            target = (org.astrogrid.adql.beans.InValueListType)get_store().find_element_user(INVALUELIST$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.InValueListType)get_store().add_element_user(INVALUELIST$2);
            }
            target.set(inValueList);
        }
    }
    
    /**
     * Appends and returns a new empty "InValueList" element
     */
    public org.astrogrid.adql.beans.InValueListType addNewInValueList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.InValueListType target = null;
            target = (org.astrogrid.adql.beans.InValueListType)get_store().add_element_user(INVALUELIST$2);
            return target;
        }
    }
    
    /**
     * Unsets the "InValueList" element
     */
    public void unsetInValueList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INVALUELIST$2, 0);
        }
    }
}
