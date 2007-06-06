/*
 * An XML document type.
 * Localname: JDTimeRef
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.JDTimeRefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one JDTimeRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class JDTimeRefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.AbsoluteTimeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.JDTimeRefDocument
{
    
    public JDTimeRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName JDTIMEREF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "JDTimeRef");
    
    
    /**
     * Gets the "JDTimeRef" element
     */
    public java.lang.String getJDTimeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(JDTIMEREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "JDTimeRef" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetJDTimeRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(JDTIMEREF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "JDTimeRef" element
     */
    public void setJDTimeRef(java.lang.String jdTimeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(JDTIMEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(JDTIMEREF$0);
            }
            target.setStringValue(jdTimeRef);
        }
    }
    
    /**
     * Sets (as xml) the "JDTimeRef" element
     */
    public void xsetJDTimeRef(org.apache.xmlbeans.XmlIDREF jdTimeRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(JDTIMEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(JDTIMEREF$0);
            }
            target.set(jdTimeRef);
        }
    }
}
