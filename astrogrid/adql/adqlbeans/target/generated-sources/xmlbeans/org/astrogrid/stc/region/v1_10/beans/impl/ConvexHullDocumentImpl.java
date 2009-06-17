/*
 * An XML document type.
 * Localname: ConvexHull
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.ConvexHullDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one ConvexHull(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class ConvexHullDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.ConvexHullDocument
{
    
    public ConvexHullDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONVEXHULL$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "ConvexHull");
    
    
    /**
     * Gets the "ConvexHull" element
     */
    public org.astrogrid.stc.region.v1_10.beans.ConvexHullType getConvexHull()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConvexHullType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConvexHullType)get_store().find_element_user(CONVEXHULL$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ConvexHull" element
     */
    public void setConvexHull(org.astrogrid.stc.region.v1_10.beans.ConvexHullType convexHull)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConvexHullType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConvexHullType)get_store().find_element_user(CONVEXHULL$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.ConvexHullType)get_store().add_element_user(CONVEXHULL$0);
            }
            target.set(convexHull);
        }
    }
    
    /**
     * Appends and returns a new empty "ConvexHull" element
     */
    public org.astrogrid.stc.region.v1_10.beans.ConvexHullType addNewConvexHull()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConvexHullType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConvexHullType)get_store().add_element_user(CONVEXHULL$0);
            return target;
        }
    }
}
