/*
 * XML Type:  aggregateFunctionType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.AggregateFunctionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML aggregateFunctionType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class AggregateFunctionTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.FunctionTypeImpl implements org.astrogrid.adql.v1_0.beans.AggregateFunctionType
{
    
    public AggregateFunctionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("", "Name");
    
    
    /**
     * Gets the "Name" attribute
     */
    public org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType.Enum getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$0);
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Name" attribute
     */
    public org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType xgetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType target = null;
            target = (org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType)get_store().find_attribute_user(NAME$0);
            return target;
        }
    }
    
    /**
     * Sets the "Name" attribute
     */
    public void setName(org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType.Enum name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$0);
            }
            target.setEnumValue(name);
        }
    }
    
    /**
     * Sets (as xml) the "Name" attribute
     */
    public void xsetName(org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType target = null;
            target = (org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType)get_store().find_attribute_user(NAME$0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.AggregateFunctionNameType)get_store().add_attribute_user(NAME$0);
            }
            target.set(name);
        }
    }
}
