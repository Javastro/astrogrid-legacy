/*
 * An XML document type.
 * Localname: Size3Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Size3RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Size3Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Size3RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CSize3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Size3RefDocument
{
    
    public Size3RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SIZE3REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size3Ref");
    
    
    /**
     * Gets the "Size3Ref" element
     */
    public java.lang.String getSize3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE3REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Size3Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetSize3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(SIZE3REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Size3Ref" element
     */
    public void setSize3Ref(java.lang.String size3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SIZE3REF$0);
            }
            target.setStringValue(size3Ref);
        }
    }
    
    /**
     * Sets (as xml) the "Size3Ref" element
     */
    public void xsetSize3Ref(org.apache.xmlbeans.XmlIDREF size3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(SIZE3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(SIZE3REF$0);
            }
            target.set(size3Ref);
        }
    }
}
