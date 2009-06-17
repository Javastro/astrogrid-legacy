/*
 * An XML document type.
 * Localname: PixSize3
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixSize3Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixSize3(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixSize3DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CPixSize3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixSize3Document
{
    
    public PixSize3DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXSIZE3$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize3");
    
    
    /**
     * Gets the "PixSize3" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size3Type getPixSize3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().find_element_user(PIXSIZE3$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PixSize3" element
     */
    public void setPixSize3(org.astrogrid.stc.coords.v1_10.beans.Size3Type pixSize3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().find_element_user(PIXSIZE3$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().add_element_user(PIXSIZE3$0);
            }
            target.set(pixSize3);
        }
    }
    
    /**
     * Appends and returns a new empty "PixSize3" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size3Type addNewPixSize3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().add_element_user(PIXSIZE3$0);
            return target;
        }
    }
}
