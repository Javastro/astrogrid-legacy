/*
 * An XML document type.
 * Localname: Resolution3
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Resolution3Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Resolution3(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Resolution3DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CResolution3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Resolution3Document
{
    
    public Resolution3DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESOLUTION3$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution3");
    
    
    /**
     * Gets the "Resolution3" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size3Type getResolution3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().find_element_user(RESOLUTION3$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Resolution3" element
     */
    public void setResolution3(org.astrogrid.stc.coords.v1_10.beans.Size3Type resolution3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().find_element_user(RESOLUTION3$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().add_element_user(RESOLUTION3$0);
            }
            target.set(resolution3);
        }
    }
    
    /**
     * Appends and returns a new empty "Resolution3" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size3Type addNewResolution3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().add_element_user(RESOLUTION3$0);
            return target;
        }
    }
}
