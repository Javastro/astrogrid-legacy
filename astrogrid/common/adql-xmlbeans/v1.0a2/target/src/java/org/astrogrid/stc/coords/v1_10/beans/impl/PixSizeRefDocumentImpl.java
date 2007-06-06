/*
 * An XML document type.
 * Localname: PixSizeRef
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixSizeRefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixSizeRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixSizeRefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CPixSizeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixSizeRefDocument
{
    
    public PixSizeRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXSIZEREF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSizeRef");
    
    
    /**
     * Gets the "PixSizeRef" element
     */
    public java.lang.String getPixSizeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZEREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "PixSizeRef" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetPixSizeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(PIXSIZEREF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "PixSizeRef" element
     */
    public void setPixSizeRef(java.lang.String pixSizeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PIXSIZEREF$0);
            }
            target.setStringValue(pixSizeRef);
        }
    }
    
    /**
     * Sets (as xml) the "PixSizeRef" element
     */
    public void xsetPixSizeRef(org.apache.xmlbeans.XmlIDREF pixSizeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(PIXSIZEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(PIXSIZEREF$0);
            }
            target.set(pixSizeRef);
        }
    }
}
