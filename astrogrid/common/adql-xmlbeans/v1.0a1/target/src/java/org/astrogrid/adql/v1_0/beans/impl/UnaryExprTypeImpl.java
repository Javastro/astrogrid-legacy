/*
 * XML Type:  unaryExprType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.UnaryExprType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML unaryExprType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class UnaryExprTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.v1_0.beans.UnaryExprType
{
    
    public UnaryExprTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ARG$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Arg");
    private static final javax.xml.namespace.QName OPER$2 = 
        new javax.xml.namespace.QName("", "Oper");
    
    
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
    
    /**
     * Gets the "Oper" attribute
     */
    public org.astrogrid.adql.v1_0.beans.UnaryOperatorType.Enum getOper()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPER$2);
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.adql.v1_0.beans.UnaryOperatorType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Oper" attribute
     */
    public org.astrogrid.adql.v1_0.beans.UnaryOperatorType xgetOper()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.UnaryOperatorType target = null;
            target = (org.astrogrid.adql.v1_0.beans.UnaryOperatorType)get_store().find_attribute_user(OPER$2);
            return target;
        }
    }
    
    /**
     * Sets the "Oper" attribute
     */
    public void setOper(org.astrogrid.adql.v1_0.beans.UnaryOperatorType.Enum oper)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPER$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OPER$2);
            }
            target.setEnumValue(oper);
        }
    }
    
    /**
     * Sets (as xml) the "Oper" attribute
     */
    public void xsetOper(org.astrogrid.adql.v1_0.beans.UnaryOperatorType oper)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.UnaryOperatorType target = null;
            target = (org.astrogrid.adql.v1_0.beans.UnaryOperatorType)get_store().find_attribute_user(OPER$2);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.UnaryOperatorType)get_store().add_attribute_user(OPER$2);
            }
            target.set(oper);
        }
    }
}
