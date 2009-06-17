/*
 * XML Type:  existsPredType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.ExistsPredType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML existsPredType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class ExistsPredTypeImpl extends org.astrogrid.adql.beans.impl.SearchTypeImpl implements org.astrogrid.adql.beans.ExistsPredType
{
    
    public ExistsPredTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SUBQUERY$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "SubQuery");
    
    
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
}
