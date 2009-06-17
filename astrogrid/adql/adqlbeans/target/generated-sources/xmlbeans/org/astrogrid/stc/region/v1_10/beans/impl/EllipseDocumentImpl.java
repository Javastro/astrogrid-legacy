/*
 * An XML document type.
 * Localname: Ellipse
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.EllipseDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one Ellipse(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class EllipseDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.EllipseDocument
{
    
    public EllipseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ELLIPSE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Ellipse");
    
    
    /**
     * Gets the "Ellipse" element
     */
    public org.astrogrid.stc.region.v1_10.beans.EllipseType getEllipse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.EllipseType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.EllipseType)get_store().find_element_user(ELLIPSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Ellipse" element
     */
    public void setEllipse(org.astrogrid.stc.region.v1_10.beans.EllipseType ellipse)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.EllipseType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.EllipseType)get_store().find_element_user(ELLIPSE$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.EllipseType)get_store().add_element_user(ELLIPSE$0);
            }
            target.set(ellipse);
        }
    }
    
    /**
     * Appends and returns a new empty "Ellipse" element
     */
    public org.astrogrid.stc.region.v1_10.beans.EllipseType addNewEllipse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.EllipseType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.EllipseType)get_store().add_element_user(ELLIPSE$0);
            return target;
        }
    }
}
