/*
 * XML Type:  aliasSelectionItemType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.AliasSelectionItemType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML aliasSelectionItemType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class AliasSelectionItemTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.SelectionItemTypeImpl implements org.astrogrid.adql.v1_0.beans.AliasSelectionItemType
{
    
    public AliasSelectionItemTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXPRESSION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Expression");
    private static final javax.xml.namespace.QName AS$2 = 
        new javax.xml.namespace.QName("", "As");
    
    
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
     * Gets the "As" attribute
     */
    public java.lang.String getAs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AS$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "As" attribute
     */
    public org.apache.xmlbeans.XmlString xgetAs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(AS$2);
            return target;
        }
    }
    
    /**
     * True if has "As" attribute
     */
    public boolean isSetAs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(AS$2) != null;
        }
    }
    
    /**
     * Sets the "As" attribute
     */
    public void setAs(java.lang.String as)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(AS$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(AS$2);
            }
            target.setStringValue(as);
        }
    }
    
    /**
     * Sets (as xml) the "As" attribute
     */
    public void xsetAs(org.apache.xmlbeans.XmlString as)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(AS$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(AS$2);
            }
            target.set(as);
        }
    }
    
    /**
     * Unsets the "As" attribute
     */
    public void unsetAs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(AS$2);
        }
    }
}
