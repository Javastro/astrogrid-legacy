/*
 * An XML document type.
 * Localname: Resolution2Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Resolution2RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Resolution2Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Resolution2RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CResolution2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Resolution2RefDocument
{
    
    public Resolution2RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESOLUTION2REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution2Ref");
    
    
    /**
     * Gets the "Resolution2Ref" element
     */
    public java.lang.String getResolution2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION2REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Resolution2Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetResolution2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(RESOLUTION2REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Resolution2Ref" element
     */
    public void setResolution2Ref(java.lang.String resolution2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RESOLUTION2REF$0);
            }
            target.setStringValue(resolution2Ref);
        }
    }
    
    /**
     * Sets (as xml) the "Resolution2Ref" element
     */
    public void xsetResolution2Ref(org.apache.xmlbeans.XmlIDREF resolution2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(RESOLUTION2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(RESOLUTION2REF$0);
            }
            target.set(resolution2Ref);
        }
    }
}
