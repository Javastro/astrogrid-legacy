/*
 * XML Type:  exclusiveSearchType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.ExclusiveSearchType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML exclusiveSearchType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class ExclusiveSearchTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.SearchTypeImpl implements org.astrogrid.adql.v1_0.beans.ExclusiveSearchType
{
    
    public ExclusiveSearchTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXPRESSION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Expression");
    private static final javax.xml.namespace.QName SET$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Set");
    
    
    /**
     * Gets the "Expression" element
     */
    public org.astrogrid.adql.v1_0.beans.ScalarExpressionType getExpression()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().find_element_user(EXPRESSION$0, 0);
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
    public void setExpression(org.astrogrid.adql.v1_0.beans.ScalarExpressionType expression)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().find_element_user(EXPRESSION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().add_element_user(EXPRESSION$0);
            }
            target.set(expression);
        }
    }
    
    /**
     * Appends and returns a new empty "Expression" element
     */
    public org.astrogrid.adql.v1_0.beans.ScalarExpressionType addNewExpression()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().add_element_user(EXPRESSION$0);
            return target;
        }
    }
    
    /**
     * Gets the "Set" element
     */
    public org.astrogrid.adql.v1_0.beans.InclusionSetType getSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.InclusionSetType target = null;
            target = (org.astrogrid.adql.v1_0.beans.InclusionSetType)get_store().find_element_user(SET$2, 0);
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
    public void setSet(org.astrogrid.adql.v1_0.beans.InclusionSetType set)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.InclusionSetType target = null;
            target = (org.astrogrid.adql.v1_0.beans.InclusionSetType)get_store().find_element_user(SET$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.InclusionSetType)get_store().add_element_user(SET$2);
            }
            target.set(set);
        }
    }
    
    /**
     * Appends and returns a new empty "Set" element
     */
    public org.astrogrid.adql.v1_0.beans.InclusionSetType addNewSet()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.InclusionSetType target = null;
            target = (org.astrogrid.adql.v1_0.beans.InclusionSetType)get_store().add_element_user(SET$2);
            return target;
        }
    }
}
