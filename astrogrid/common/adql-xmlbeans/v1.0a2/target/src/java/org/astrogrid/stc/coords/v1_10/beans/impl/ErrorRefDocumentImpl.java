/*
 * An XML document type.
 * Localname: ErrorRef
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.ErrorRefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one ErrorRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class ErrorRefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CErrorDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.ErrorRefDocument
{
    
    public ErrorRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ERRORREF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ErrorRef");
    
    
    /**
     * Gets the "ErrorRef" element
     */
    public java.lang.String getErrorRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERRORREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "ErrorRef" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetErrorRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(ERRORREF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "ErrorRef" element
     */
    public void setErrorRef(java.lang.String errorRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERRORREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ERRORREF$0);
            }
            target.setStringValue(errorRef);
        }
    }
    
    /**
     * Sets (as xml) the "ErrorRef" element
     */
    public void xsetErrorRef(org.apache.xmlbeans.XmlIDREF errorRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(ERRORREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(ERRORREF$0);
            }
            target.set(errorRef);
        }
    }
}
