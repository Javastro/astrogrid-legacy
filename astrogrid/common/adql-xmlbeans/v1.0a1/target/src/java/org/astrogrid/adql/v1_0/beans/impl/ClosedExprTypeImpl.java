/*
 * XML Type:  closedExprType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.ClosedExprType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML closedExprType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class ClosedExprTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.v1_0.beans.ClosedExprType
{
    
    public ClosedExprTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ARG$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Arg");
    
    
    /**
     * Gets the "Arg" element
     */
    public org.astrogrid.adql.v1_0.beans.ScalarExpressionType getArg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().find_element_user(ARG$0, 0);
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
    public void setArg(org.astrogrid.adql.v1_0.beans.ScalarExpressionType arg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().find_element_user(ARG$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().add_element_user(ARG$0);
            }
            target.set(arg);
        }
    }
    
    /**
     * Appends and returns a new empty "Arg" element
     */
    public org.astrogrid.adql.v1_0.beans.ScalarExpressionType addNewArg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().add_element_user(ARG$0);
            return target;
        }
    }
}
