/*
 * An XML document type.
 * Localname: Size2Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Size2RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Size2Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Size2RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CSize2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Size2RefDocument
{
    
    public Size2RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SIZE2REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size2Ref");
    
    
    /**
     * Gets the "Size2Ref" element
     */
    public java.lang.String getSize2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE2REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Size2Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetSize2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(SIZE2REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Size2Ref" element
     */
    public void setSize2Ref(java.lang.String size2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SIZE2REF$0);
            }
            target.setStringValue(size2Ref);
        }
    }
    
    /**
     * Sets (as xml) the "Size2Ref" element
     */
    public void xsetSize2Ref(org.apache.xmlbeans.XmlIDREF size2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(SIZE2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(SIZE2REF$0);
            }
            target.set(size2Ref);
        }
    }
}
