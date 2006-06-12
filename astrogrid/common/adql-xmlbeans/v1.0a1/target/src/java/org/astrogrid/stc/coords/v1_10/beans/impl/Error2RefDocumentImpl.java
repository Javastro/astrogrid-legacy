/*
 * An XML document type.
 * Localname: Error2Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Error2RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Error2Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Error2RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CError2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Error2RefDocument
{
    
    public Error2RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ERROR2REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error2Ref");
    
    
    /**
     * Gets the "Error2Ref" element
     */
    public java.lang.String getError2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR2REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Error2Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetError2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(ERROR2REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Error2Ref" element
     */
    public void setError2Ref(java.lang.String error2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ERROR2REF$0);
            }
            target.setStringValue(error2Ref);
        }
    }
    
    /**
     * Sets (as xml) the "Error2Ref" element
     */
    public void xsetError2Ref(org.apache.xmlbeans.XmlIDREF error2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(ERROR2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(ERROR2REF$0);
            }
            target.set(error2Ref);
        }
    }
}
