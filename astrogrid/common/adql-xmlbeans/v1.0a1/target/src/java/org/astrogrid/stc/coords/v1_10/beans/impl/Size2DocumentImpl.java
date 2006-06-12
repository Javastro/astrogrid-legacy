/*
 * An XML document type.
 * Localname: Size2
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Size2Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Size2(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Size2DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CSize2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Size2Document
{
    
    public Size2DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SIZE2$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size2");
    
    
    /**
     * Gets the "Size2" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size2Type getSize2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().find_element_user(SIZE2$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Size2" element
     */
    public void setSize2(org.astrogrid.stc.coords.v1_10.beans.Size2Type size2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().find_element_user(SIZE2$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().add_element_user(SIZE2$0);
            }
            target.set(size2);
        }
    }
    
    /**
     * Appends and returns a new empty "Size2" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size2Type addNewSize2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().add_element_user(SIZE2$0);
            return target;
        }
    }
}
