/*
 * XML Type:  orderOptionType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.OrderOptionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML orderOptionType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class OrderOptionTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.v1_0.beans.OrderOptionType
{
    
    public OrderOptionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DIRECTION$0 = 
        new javax.xml.namespace.QName("", "Direction");
    
    
    /**
     * Gets the "Direction" attribute
     */
    public org.astrogrid.adql.v1_0.beans.OrderDirectionType.Enum getDirection()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DIRECTION$0);
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.adql.v1_0.beans.OrderDirectionType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Direction" attribute
     */
    public org.astrogrid.adql.v1_0.beans.OrderDirectionType xgetDirection()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.OrderDirectionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.OrderDirectionType)get_store().find_attribute_user(DIRECTION$0);
            return target;
        }
    }
    
    /**
     * Sets the "Direction" attribute
     */
    public void setDirection(org.astrogrid.adql.v1_0.beans.OrderDirectionType.Enum direction)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(DIRECTION$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(DIRECTION$0);
            }
            target.setEnumValue(direction);
        }
    }
    
    /**
     * Sets (as xml) the "Direction" attribute
     */
    public void xsetDirection(org.astrogrid.adql.v1_0.beans.OrderDirectionType direction)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.OrderDirectionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.OrderDirectionType)get_store().find_attribute_user(DIRECTION$0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.OrderDirectionType)get_store().add_attribute_user(DIRECTION$0);
            }
            target.set(direction);
        }
    }
}
