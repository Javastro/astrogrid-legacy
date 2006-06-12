/*
 * An XML document type.
 * Localname: SizeRef
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.SizeRefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one SizeRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class SizeRefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CSizeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.SizeRefDocument
{
    
    public SizeRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SIZEREF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "SizeRef");
    
    
    /**
     * Gets the "SizeRef" element
     */
    public java.lang.String getSizeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZEREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "SizeRef" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetSizeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(SIZEREF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "SizeRef" element
     */
    public void setSizeRef(java.lang.String sizeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SIZEREF$0);
            }
            target.setStringValue(sizeRef);
        }
    }
    
    /**
     * Sets (as xml) the "SizeRef" element
     */
    public void xsetSizeRef(org.apache.xmlbeans.XmlIDREF sizeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(SIZEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(SIZEREF$0);
            }
            target.set(sizeRef);
        }
    }
}
