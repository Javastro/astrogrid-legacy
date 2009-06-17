/*
 * An XML document type.
 * Localname: Polygon
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.PolygonDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one Polygon(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class PolygonDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.PolygonDocument
{
    
    public PolygonDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POLYGON$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Polygon");
    
    
    /**
     * Gets the "Polygon" element
     */
    public org.astrogrid.stc.region.v1_10.beans.PolygonType getPolygon()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.PolygonType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.PolygonType)get_store().find_element_user(POLYGON$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Polygon" element
     */
    public void setPolygon(org.astrogrid.stc.region.v1_10.beans.PolygonType polygon)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.PolygonType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.PolygonType)get_store().find_element_user(POLYGON$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.PolygonType)get_store().add_element_user(POLYGON$0);
            }
            target.set(polygon);
        }
    }
    
    /**
     * Appends and returns a new empty "Polygon" element
     */
    public org.astrogrid.stc.region.v1_10.beans.PolygonType addNewPolygon()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.PolygonType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.PolygonType)get_store().add_element_user(POLYGON$0);
            return target;
        }
    }
}
