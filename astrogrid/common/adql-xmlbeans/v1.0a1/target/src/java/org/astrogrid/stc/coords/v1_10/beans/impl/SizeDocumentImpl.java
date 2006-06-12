/*
 * An XML document type.
 * Localname: Size
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.SizeDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Size(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class SizeDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CSizeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.SizeDocument
{
    
    public SizeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SIZE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size");
    
    
    /**
     * Gets the "Size" element
     */
    public double getSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE$0, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "Size" element
     */
    public org.apache.xmlbeans.XmlDouble xgetSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(SIZE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Size" element
     */
    public void setSize(double size)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SIZE$0);
            }
            target.setDoubleValue(size);
        }
    }
    
    /**
     * Sets (as xml) the "Size" element
     */
    public void xsetSize(org.apache.xmlbeans.XmlDouble size)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(SIZE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(SIZE$0);
            }
            target.set(size);
        }
    }
}
