/*
 * An XML document type.
 * Localname: Size3
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Size3Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Size3(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Size3DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CSize3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Size3Document
{
    
    public Size3DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SIZE3$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size3");
    
    
    /**
     * Gets the "Size3" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size3Type getSize3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().find_element_user(SIZE3$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Size3" element
     */
    public void setSize3(org.astrogrid.stc.coords.v1_10.beans.Size3Type size3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().find_element_user(SIZE3$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().add_element_user(SIZE3$0);
            }
            target.set(size3);
        }
    }
    
    /**
     * Appends and returns a new empty "Size3" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size3Type addNewSize3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().add_element_user(SIZE3$0);
            return target;
        }
    }
}
