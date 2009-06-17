/*
 * An XML document type.
 * Localname: Circle
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.CircleDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one Circle(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class CircleDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.CircleDocument
{
    
    public CircleDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CIRCLE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Circle");
    
    
    /**
     * Gets the "Circle" element
     */
    public org.astrogrid.stc.region.v1_10.beans.CircleType getCircle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.CircleType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.CircleType)get_store().find_element_user(CIRCLE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Circle" element
     */
    public void setCircle(org.astrogrid.stc.region.v1_10.beans.CircleType circle)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.CircleType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.CircleType)get_store().find_element_user(CIRCLE$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.CircleType)get_store().add_element_user(CIRCLE$0);
            }
            target.set(circle);
        }
    }
    
    /**
     * Appends and returns a new empty "Circle" element
     */
    public org.astrogrid.stc.region.v1_10.beans.CircleType addNewCircle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.CircleType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.CircleType)get_store().add_element_user(CIRCLE$0);
            return target;
        }
    }
}
