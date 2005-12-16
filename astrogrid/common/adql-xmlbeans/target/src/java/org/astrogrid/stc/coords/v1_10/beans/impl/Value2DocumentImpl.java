/*
 * An XML document type.
 * Localname: Value2
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Value2Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Value2(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Value2DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CValue2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Value2Document
{
    
    public Value2DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VALUE2$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value2");
    
    
    /**
     * Gets the "Value2" element
     */
    public java.util.List getValue2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE2$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Value2" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double2Type xgetValue2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().find_element_user(VALUE2$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Value2" element
     */
    public void setValue2(java.util.List value2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE2$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUE2$0);
            }
            target.setListValue(value2);
        }
    }
    
    /**
     * Sets (as xml) the "Value2" element
     */
    public void xsetValue2(org.astrogrid.stc.coords.v1_10.beans.Double2Type value2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().find_element_user(VALUE2$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().add_element_user(VALUE2$0);
            }
            target.set(value2);
        }
    }
}
