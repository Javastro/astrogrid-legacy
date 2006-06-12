/*
 * An XML document type.
 * Localname: Error3Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Error3RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Error3Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Error3RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CError3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Error3RefDocument
{
    
    public Error3RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ERROR3REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error3Ref");
    
    
    /**
     * Gets the "Error3Ref" element
     */
    public java.lang.String getError3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR3REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Error3Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetError3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(ERROR3REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Error3Ref" element
     */
    public void setError3Ref(java.lang.String error3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ERROR3REF$0);
            }
            target.setStringValue(error3Ref);
        }
    }
    
    /**
     * Sets (as xml) the "Error3Ref" element
     */
    public void xsetError3Ref(org.apache.xmlbeans.XmlIDREF error3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(ERROR3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(ERROR3REF$0);
            }
            target.set(error3Ref);
        }
    }
}
