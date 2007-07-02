/*
 * XML Type:  trigonometricFunctionType
 * Namespace: http://www.ivoa.net/xml/ADQL/v2.0/adql
 * Java type: org.astrogrid.adql.beans.TrigonometricFunctionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML trigonometricFunctionType(@http://www.ivoa.net/xml/ADQL/v2.0/adql).
 *
 * This is a complex type.
 */
public class TrigonometricFunctionTypeImpl extends org.astrogrid.adql.beans.impl.FunctionTypeImpl implements org.astrogrid.adql.beans.TrigonometricFunctionType
{
    
    public TrigonometricFunctionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("", "Name");
    
    
    /**
     * Gets the "Name" attribute
     */
    public org.astrogrid.adql.beans.TrigonometricFunctionNameType.Enum getName()
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
            return (org.astrogrid.adql.beans.TrigonometricFunctionNameType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Name" attribute
     */
    public org.astrogrid.adql.beans.TrigonometricFunctionNameType xgetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.TrigonometricFunctionNameType target = null;
            target = (org.astrogrid.adql.beans.TrigonometricFunctionNameType)get_store().find_attribute_user(NAME$0);
            return target;
        }
    }
    
    /**
     * Sets the "Name" attribute
     */
    public void setName(org.astrogrid.adql.beans.TrigonometricFunctionNameType.Enum name)
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
    public void xsetName(org.astrogrid.adql.beans.TrigonometricFunctionNameType name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.TrigonometricFunctionNameType target = null;
            target = (org.astrogrid.adql.beans.TrigonometricFunctionNameType)get_store().find_attribute_user(NAME$0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.TrigonometricFunctionNameType)get_store().add_attribute_user(NAME$0);
            }
            target.set(name);
        }
    }
}
