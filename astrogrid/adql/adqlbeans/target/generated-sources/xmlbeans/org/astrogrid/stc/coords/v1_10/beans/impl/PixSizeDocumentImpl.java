/*
 * An XML document type.
 * Localname: PixSize
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixSizeDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixSize(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixSizeDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CPixSizeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixSizeDocument
{
    
    public PixSizeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXSIZE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize");
    
    
    /**
     * Gets the "PixSize" element
     */
    public double getPixSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE$0, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "PixSize" element
     */
    public org.apache.xmlbeans.XmlDouble xgetPixSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(PIXSIZE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "PixSize" element
     */
    public void setPixSize(double pixSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PIXSIZE$0);
            }
            target.setDoubleValue(pixSize);
        }
    }
    
    /**
     * Sets (as xml) the "PixSize" element
     */
    public void xsetPixSize(org.apache.xmlbeans.XmlDouble pixSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(PIXSIZE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(PIXSIZE$0);
            }
            target.set(pixSize);
        }
    }
}
