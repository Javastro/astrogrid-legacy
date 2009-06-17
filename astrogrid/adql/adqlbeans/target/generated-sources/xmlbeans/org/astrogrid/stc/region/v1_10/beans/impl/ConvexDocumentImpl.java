/*
 * An XML document type.
 * Localname: Convex
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.ConvexDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one Convex(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class ConvexDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.ConvexDocument
{
    
    public ConvexDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONVEX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Convex");
    
    
    /**
     * Gets the "Convex" element
     */
    public org.astrogrid.stc.region.v1_10.beans.ConvexType getConvex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConvexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConvexType)get_store().find_element_user(CONVEX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Convex" element
     */
    public void setConvex(org.astrogrid.stc.region.v1_10.beans.ConvexType convex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConvexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConvexType)get_store().find_element_user(CONVEX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.ConvexType)get_store().add_element_user(CONVEX$0);
            }
            target.set(convex);
        }
    }
    
    /**
     * Appends and returns a new empty "Convex" element
     */
    public org.astrogrid.stc.region.v1_10.beans.ConvexType addNewConvex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConvexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConvexType)get_store().add_element_user(CONVEX$0);
            return target;
        }
    }
}
