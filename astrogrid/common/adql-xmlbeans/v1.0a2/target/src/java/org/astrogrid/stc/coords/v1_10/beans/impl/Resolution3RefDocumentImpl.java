/*
 * An XML document type.
 * Localname: Resolution3Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Resolution3RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Resolution3Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Resolution3RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CResolution3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Resolution3RefDocument
{
    
    public Resolution3RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESOLUTION3REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution3Ref");
    
    
    /**
     * Gets the "Resolution3Ref" element
     */
    public java.lang.String getResolution3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION3REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Resolution3Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetResolution3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(RESOLUTION3REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Resolution3Ref" element
     */
    public void setResolution3Ref(java.lang.String resolution3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RESOLUTION3REF$0);
            }
            target.setStringValue(resolution3Ref);
        }
    }
    
    /**
     * Sets (as xml) the "Resolution3Ref" element
     */
    public void xsetResolution3Ref(org.apache.xmlbeans.XmlIDREF resolution3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(RESOLUTION3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(RESOLUTION3REF$0);
            }
            target.set(resolution3Ref);
        }
    }
}
