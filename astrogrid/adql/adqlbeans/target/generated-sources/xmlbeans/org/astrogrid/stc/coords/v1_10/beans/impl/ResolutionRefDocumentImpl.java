/*
 * An XML document type.
 * Localname: ResolutionRef
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.ResolutionRefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one ResolutionRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class ResolutionRefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CResolutionDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.ResolutionRefDocument
{
    
    public ResolutionRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESOLUTIONREF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ResolutionRef");
    
    
    /**
     * Gets the "ResolutionRef" element
     */
    public java.lang.String getResolutionRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTIONREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "ResolutionRef" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetResolutionRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(RESOLUTIONREF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "ResolutionRef" element
     */
    public void setResolutionRef(java.lang.String resolutionRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTIONREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RESOLUTIONREF$0);
            }
            target.setStringValue(resolutionRef);
        }
    }
    
    /**
     * Sets (as xml) the "ResolutionRef" element
     */
    public void xsetResolutionRef(org.apache.xmlbeans.XmlIDREF resolutionRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(RESOLUTIONREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(RESOLUTIONREF$0);
            }
            target.set(resolutionRef);
        }
    }
}
