/*
 * An XML document type.
 * Localname: Value3
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Value3Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Value3(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Value3DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CValue3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Value3Document
{
    
    public Value3DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VALUE3$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value3");
    
    
    /**
     * Gets the "Value3" element
     */
    public java.util.List getValue3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE3$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Value3" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double3Type xgetValue3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().find_element_user(VALUE3$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Value3" element
     */
    public void setValue3(java.util.List value3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE3$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUE3$0);
            }
            target.setListValue(value3);
        }
    }
    
    /**
     * Sets (as xml) the "Value3" element
     */
    public void xsetValue3(org.astrogrid.stc.coords.v1_10.beans.Double3Type value3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().find_element_user(VALUE3$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().add_element_user(VALUE3$0);
            }
            target.set(value3);
        }
    }
}
