/*
 * An XML document type.
 * Localname: PixSize3Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixSize3RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixSize3Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixSize3RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CPixSize3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixSize3RefDocument
{
    
    public PixSize3RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXSIZE3REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize3Ref");
    
    
    /**
     * Gets the "PixSize3Ref" element
     */
    public java.lang.String getPixSize3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE3REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "PixSize3Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetPixSize3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(PIXSIZE3REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "PixSize3Ref" element
     */
    public void setPixSize3Ref(java.lang.String pixSize3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PIXSIZE3REF$0);
            }
            target.setStringValue(pixSize3Ref);
        }
    }
    
    /**
     * Sets (as xml) the "PixSize3Ref" element
     */
    public void xsetPixSize3Ref(org.apache.xmlbeans.XmlIDREF pixSize3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(PIXSIZE3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(PIXSIZE3REF$0);
            }
            target.set(pixSize3Ref);
        }
    }
}
