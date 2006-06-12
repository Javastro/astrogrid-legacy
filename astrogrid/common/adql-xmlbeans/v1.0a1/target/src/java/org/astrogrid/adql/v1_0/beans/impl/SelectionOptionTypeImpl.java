/*
 * XML Type:  selectionOptionType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.SelectionOptionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML selectionOptionType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class SelectionOptionTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.v1_0.beans.SelectionOptionType
{
    
    public SelectionOptionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName OPTION$0 = 
        new javax.xml.namespace.QName("", "Option");
    
    
    /**
     * Gets the "Option" attribute
     */
    public org.astrogrid.adql.v1_0.beans.AllOrDistinctType.Enum getOption()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPTION$0);
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.adql.v1_0.beans.AllOrDistinctType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Option" attribute
     */
    public org.astrogrid.adql.v1_0.beans.AllOrDistinctType xgetOption()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.AllOrDistinctType target = null;
            target = (org.astrogrid.adql.v1_0.beans.AllOrDistinctType)get_store().find_attribute_user(OPTION$0);
            return target;
        }
    }
    
    /**
     * Sets the "Option" attribute
     */
    public void setOption(org.astrogrid.adql.v1_0.beans.AllOrDistinctType.Enum option)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPTION$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OPTION$0);
            }
            target.setEnumValue(option);
        }
    }
    
    /**
     * Sets (as xml) the "Option" attribute
     */
    public void xsetOption(org.astrogrid.adql.v1_0.beans.AllOrDistinctType option)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.AllOrDistinctType target = null;
            target = (org.astrogrid.adql.v1_0.beans.AllOrDistinctType)get_store().find_attribute_user(OPTION$0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.AllOrDistinctType)get_store().add_attribute_user(OPTION$0);
            }
            target.set(option);
        }
    }
}
