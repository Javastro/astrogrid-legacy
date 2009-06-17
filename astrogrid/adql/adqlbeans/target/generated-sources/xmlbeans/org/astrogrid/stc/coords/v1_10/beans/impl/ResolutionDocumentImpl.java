/*
 * An XML document type.
 * Localname: Resolution
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.ResolutionDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Resolution(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class ResolutionDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CResolutionDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.ResolutionDocument
{
    
    public ResolutionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESOLUTION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution");
    
    
    /**
     * Gets the "Resolution" element
     */
    public double getResolution()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION$0, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "Resolution" element
     */
    public org.apache.xmlbeans.XmlDouble xgetResolution()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(RESOLUTION$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Resolution" element
     */
    public void setResolution(double resolution)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RESOLUTION$0);
            }
            target.setDoubleValue(resolution);
        }
    }
    
    /**
     * Sets (as xml) the "Resolution" element
     */
    public void xsetResolution(org.apache.xmlbeans.XmlDouble resolution)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(RESOLUTION$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(RESOLUTION$0);
            }
            target.set(resolution);
        }
    }
}
