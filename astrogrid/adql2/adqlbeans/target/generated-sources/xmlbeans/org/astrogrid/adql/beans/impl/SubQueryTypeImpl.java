/*
 * XML Type:  subQueryType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.SubQueryType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML subQueryType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class SubQueryTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.SubQueryType
{
    
    public SubQueryTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName QUERYEXPRESSION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "QueryExpression");
    
    
    /**
     * Gets the "QueryExpression" element
     */
    public org.astrogrid.adql.beans.QueryExpressionType getQueryExpression()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.QueryExpressionType target = null;
            target = (org.astrogrid.adql.beans.QueryExpressionType)get_store().find_element_user(QUERYEXPRESSION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "QueryExpression" element
     */
    public void setQueryExpression(org.astrogrid.adql.beans.QueryExpressionType queryExpression)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.QueryExpressionType target = null;
            target = (org.astrogrid.adql.beans.QueryExpressionType)get_store().find_element_user(QUERYEXPRESSION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.QueryExpressionType)get_store().add_element_user(QUERYEXPRESSION$0);
            }
            target.set(queryExpression);
        }
    }
    
    /**
     * Appends and returns a new empty "QueryExpression" element
     */
    public org.astrogrid.adql.beans.QueryExpressionType addNewQueryExpression()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.QueryExpressionType target = null;
            target = (org.astrogrid.adql.beans.QueryExpressionType)get_store().add_element_user(QUERYEXPRESSION$0);
            return target;
        }
    }
}
