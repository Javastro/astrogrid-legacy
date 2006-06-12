/*
 * An XML document type.
 * Localname: PixSize2Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixSize2RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixSize2Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixSize2RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CPixSize2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixSize2RefDocument
{
    
    public PixSize2RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXSIZE2REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize2Ref");
    
    
    /**
     * Gets the "PixSize2Ref" element
     */
    public java.lang.String getPixSize2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE2REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "PixSize2Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetPixSize2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(PIXSIZE2REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "PixSize2Ref" element
     */
    public void setPixSize2Ref(java.lang.String pixSize2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PIXSIZE2REF$0);
            }
            target.setStringValue(pixSize2Ref);
        }
    }
    
    /**
     * Sets (as xml) the "PixSize2Ref" element
     */
    public void xsetPixSize2Ref(org.apache.xmlbeans.XmlIDREF pixSize2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(PIXSIZE2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(PIXSIZE2REF$0);
            }
            target.set(pixSize2Ref);
        }
    }
}
