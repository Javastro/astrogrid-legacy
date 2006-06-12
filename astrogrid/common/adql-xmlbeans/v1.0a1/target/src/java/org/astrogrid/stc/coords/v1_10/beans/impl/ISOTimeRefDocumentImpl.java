/*
 * An XML document type.
 * Localname: ISOTimeRef
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.ISOTimeRefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one ISOTimeRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class ISOTimeRefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.AbsoluteTimeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.ISOTimeRefDocument
{
    
    public ISOTimeRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ISOTIMEREF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ISOTimeRef");
    
    
    /**
     * Gets the "ISOTimeRef" element
     */
    public java.lang.String getISOTimeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ISOTIMEREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "ISOTimeRef" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetISOTimeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(ISOTIMEREF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "ISOTimeRef" element
     */
    public void setISOTimeRef(java.lang.String isoTimeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ISOTIMEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ISOTIMEREF$0);
            }
            target.setStringValue(isoTimeRef);
        }
    }
    
    /**
     * Sets (as xml) the "ISOTimeRef" element
     */
    public void xsetISOTimeRef(org.apache.xmlbeans.XmlIDREF isoTimeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(ISOTIMEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(ISOTIMEREF$0);
            }
            target.set(isoTimeRef);
        }
    }
}
