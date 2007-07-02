/*
 * XML Type:  exclusiveSearchType
 * Namespace: http://www.ivoa.net/xml/ADQL/v2.0/adql
 * Java type: org.astrogrid.adql.beans.ExclusiveSearchType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML exclusiveSearchType(@http://www.ivoa.net/xml/ADQL/v2.0/adql).
 *
 * This is a complex type.
 */
public class ExclusiveSearchTypeImpl extends org.astrogrid.adql.beans.impl.SearchTypeImpl implements org.astrogrid.adql.beans.ExclusiveSearchType
{
    
    public ExclusiveSearchTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXPRESSION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v2.0/adql", "Expression");
    private static final javax.xml.namespace.QName SET$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v2.0/adql", "Set");
    
    
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
     * Gets the "Set" element
     */
    public org.astrogrid.adql.beans.InclusionSetType getSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.InclusionSetType target = null;
            target = (org.astrogrid.adql.beans.InclusionSetType)get_store().find_element_user(SET$2, 0);
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
    public void setSet(org.astrogrid.adql.beans.InclusionSetType set)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.InclusionSetType target = null;
            target = (org.astrogrid.adql.beans.InclusionSetType)get_store().find_element_user(SET$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.InclusionSetType)get_store().add_element_user(SET$2);
            }
            target.set(set);
        }
    }
    
    /**
     * Appends and returns a new empty "Set" element
     */
    public org.astrogrid.adql.beans.InclusionSetType addNewSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.InclusionSetType target = null;
            target = (org.astrogrid.adql.beans.InclusionSetType)get_store().add_element_user(SET$2);
            return target;
        }
    }
}
