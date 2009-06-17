/*
 * An XML document type.
 * Localname: Resolution2
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Resolution2Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Resolution2(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Resolution2DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CResolution2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Resolution2Document
{
    
    public Resolution2DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESOLUTION2$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution2");
    
    
    /**
     * Gets the "Resolution2" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size2Type getResolution2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().find_element_user(RESOLUTION2$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Resolution2" element
     */
    public void setResolution2(org.astrogrid.stc.coords.v1_10.beans.Size2Type resolution2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().find_element_user(RESOLUTION2$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().add_element_user(RESOLUTION2$0);
            }
            target.set(resolution2);
        }
    }
    
    /**
     * Appends and returns a new empty "Resolution2" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size2Type addNewResolution2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().add_element_user(RESOLUTION2$0);
            return target;
        }
    }
}
