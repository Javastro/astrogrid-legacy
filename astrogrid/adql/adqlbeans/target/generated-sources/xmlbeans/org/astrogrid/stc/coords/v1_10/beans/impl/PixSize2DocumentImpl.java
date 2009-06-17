/*
 * An XML document type.
 * Localname: PixSize2
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixSize2Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixSize2(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixSize2DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CPixSize2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixSize2Document
{
    
    public PixSize2DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXSIZE2$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize2");
    
    
    /**
     * Gets the "PixSize2" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size2Type getPixSize2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().find_element_user(PIXSIZE2$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PixSize2" element
     */
    public void setPixSize2(org.astrogrid.stc.coords.v1_10.beans.Size2Type pixSize2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().find_element_user(PIXSIZE2$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().add_element_user(PIXSIZE2$0);
            }
            target.set(pixSize2);
        }
    }
    
    /**
     * Appends and returns a new empty "PixSize2" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size2Type addNewPixSize2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().add_element_user(PIXSIZE2$0);
            return target;
        }
    }
}
