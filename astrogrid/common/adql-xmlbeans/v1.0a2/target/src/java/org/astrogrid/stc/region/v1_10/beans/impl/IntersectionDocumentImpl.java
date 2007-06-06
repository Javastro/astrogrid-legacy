/*
 * An XML document type.
 * Localname: Intersection
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.IntersectionDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one Intersection(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class IntersectionDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.IntersectionDocument
{
    
    public IntersectionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INTERSECTION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Intersection");
    
    
    /**
     * Gets the "Intersection" element
     */
    public org.astrogrid.stc.region.v1_10.beans.IntersectionType getIntersection()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.IntersectionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.IntersectionType)get_store().find_element_user(INTERSECTION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Intersection" element
     */
    public void setIntersection(org.astrogrid.stc.region.v1_10.beans.IntersectionType intersection)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.IntersectionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.IntersectionType)get_store().find_element_user(INTERSECTION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.IntersectionType)get_store().add_element_user(INTERSECTION$0);
            }
            target.set(intersection);
        }
    }
    
    /**
     * Appends and returns a new empty "Intersection" element
     */
    public org.astrogrid.stc.region.v1_10.beans.IntersectionType addNewIntersection()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.IntersectionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.IntersectionType)get_store().add_element_user(INTERSECTION$0);
            return target;
        }
    }
}
