/*
 * XML Type:  closedExprType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.ClosedExprType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML closedExprType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class ClosedExprTypeImpl extends org.astrogrid.adql.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.beans.ClosedExprType
{
    
    public ClosedExprTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ARG$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Arg");
    
    
    /**
     * Gets the "Arg" element
     */
    public org.astrogrid.adql.beans.ScalarExpressionType getArg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().find_element_user(ARG$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Arg" element
     */
    public void setArg(org.astrogrid.adql.beans.ScalarExpressionType arg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().find_element_user(ARG$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().add_element_user(ARG$0);
            }
            target.set(arg);
        }
    }
    
    /**
     * Appends and returns a new empty "Arg" element
     */
    public org.astrogrid.adql.beans.ScalarExpressionType addNewArg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().add_element_user(ARG$0);
            return target;
        }
    }
}
