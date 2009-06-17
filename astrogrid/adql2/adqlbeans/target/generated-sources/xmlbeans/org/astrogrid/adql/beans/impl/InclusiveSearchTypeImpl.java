/*
 * XML Type:  inclusiveSearchType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.InclusiveSearchType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML inclusiveSearchType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class InclusiveSearchTypeImpl extends org.astrogrid.adql.beans.impl.SearchTypeImpl implements org.astrogrid.adql.beans.InclusiveSearchType
{
    
    public InclusiveSearchTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXPRESSION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Expression");
    private static final javax.xml.namespace.QName INPREDICATEVALUE$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "InPredicateValue");
    
    
    /**
     * Gets the "Expression" element
     */
    public org.astrogrid.adql.beans.ScalarExpressionType getExpression()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().find_element_user(EXPRESSION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Expression" element
     */
    public void setExpression(org.astrogrid.adql.beans.ScalarExpressionType expression)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().find_element_user(EXPRESSION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().add_element_user(EXPRESSION$0);
            }
            target.set(expression);
        }
    }
    
    /**
     * Appends and returns a new empty "Expression" element
     */
    public org.astrogrid.adql.beans.ScalarExpressionType addNewExpression()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().add_element_user(EXPRESSION$0);
            return target;
        }
    }
    
    /**
     * Gets the "InPredicateValue" element
     */
    public org.astrogrid.adql.beans.InPredicateValueType getInPredicateValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.InPredicateValueType target = null;
            target = (org.astrogrid.adql.beans.InPredicateValueType)get_store().find_element_user(INPREDICATEVALUE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "InPredicateValue" element
     */
    public void setInPredicateValue(org.astrogrid.adql.beans.InPredicateValueType inPredicateValue)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.InPredicateValueType target = null;
            target = (org.astrogrid.adql.beans.InPredicateValueType)get_store().find_element_user(INPREDICATEVALUE$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.InPredicateValueType)get_store().add_element_user(INPREDICATEVALUE$2);
            }
            target.set(inPredicateValue);
        }
    }
    
    /**
     * Appends and returns a new empty "InPredicateValue" element
     */
    public org.astrogrid.adql.beans.InPredicateValueType addNewInPredicateValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.InPredicateValueType target = null;
            target = (org.astrogrid.adql.beans.InPredicateValueType)get_store().add_element_user(INPREDICATEVALUE$2);
            return target;
        }
    }
}
