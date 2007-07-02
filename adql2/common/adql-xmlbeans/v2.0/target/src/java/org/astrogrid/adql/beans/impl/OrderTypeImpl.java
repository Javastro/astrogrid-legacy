/*
 * XML Type:  orderType
 * Namespace: http://www.ivoa.net/xml/ADQL/v2.0/adql
 * Java type: org.astrogrid.adql.beans.OrderType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML orderType(@http://www.ivoa.net/xml/ADQL/v2.0/adql).
 *
 * This is a complex type.
 */
public class OrderTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.OrderType
{
    
    public OrderTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXPRESSION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v2.0/adql", "Expression");
    private static final javax.xml.namespace.QName ORDER$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v2.0/adql", "Order");
    
    
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
     * Gets the "Order" element
     */
    public org.astrogrid.adql.beans.OrderOptionType getOrder()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.OrderOptionType target = null;
            target = (org.astrogrid.adql.beans.OrderOptionType)get_store().find_element_user(ORDER$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Order" element
     */
    public boolean isSetOrder()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ORDER$2) != 0;
        }
    }
    
    /**
     * Sets the "Order" element
     */
    public void setOrder(org.astrogrid.adql.beans.OrderOptionType order)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.OrderOptionType target = null;
            target = (org.astrogrid.adql.beans.OrderOptionType)get_store().find_element_user(ORDER$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.OrderOptionType)get_store().add_element_user(ORDER$2);
            }
            target.set(order);
        }
    }
    
    /**
     * Appends and returns a new empty "Order" element
     */
    public org.astrogrid.adql.beans.OrderOptionType addNewOrder()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.OrderOptionType target = null;
            target = (org.astrogrid.adql.beans.OrderOptionType)get_store().add_element_user(ORDER$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Order" element
     */
    public void unsetOrder()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ORDER$2, 0);
        }
    }
}
