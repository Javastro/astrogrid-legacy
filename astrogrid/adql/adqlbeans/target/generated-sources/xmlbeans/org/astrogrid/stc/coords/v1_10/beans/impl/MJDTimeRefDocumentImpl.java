/*
 * An XML document type.
 * Localname: MJDTimeRef
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.MJDTimeRefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one MJDTimeRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class MJDTimeRefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.AbsoluteTimeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.MJDTimeRefDocument
{
    
    public MJDTimeRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MJDTIMEREF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "MJDTimeRef");
    
    
    /**
     * Gets the "MJDTimeRef" element
     */
    public java.lang.String getMJDTimeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MJDTIMEREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "MJDTimeRef" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetMJDTimeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(MJDTIMEREF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "MJDTimeRef" element
     */
    public void setMJDTimeRef(java.lang.String mjdTimeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MJDTIMEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MJDTIMEREF$0);
            }
            target.setStringValue(mjdTimeRef);
        }
    }
    
    /**
     * Sets (as xml) the "MJDTimeRef" element
     */
    public void xsetMJDTimeRef(org.apache.xmlbeans.XmlIDREF mjdTimeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(MJDTIMEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(MJDTIMEREF$0);
            }
            target.set(mjdTimeRef);
        }
    }
}
