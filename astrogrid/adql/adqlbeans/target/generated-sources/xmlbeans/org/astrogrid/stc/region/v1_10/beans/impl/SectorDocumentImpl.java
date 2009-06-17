/*
 * An XML document type.
 * Localname: Sector
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.SectorDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one Sector(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class SectorDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.SectorDocument
{
    
    public SectorDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SECTOR$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Sector");
    
    
    /**
     * Gets the "Sector" element
     */
    public org.astrogrid.stc.region.v1_10.beans.SectorType getSector()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SectorType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SectorType)get_store().find_element_user(SECTOR$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Sector" element
     */
    public void setSector(org.astrogrid.stc.region.v1_10.beans.SectorType sector)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SectorType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SectorType)get_store().find_element_user(SECTOR$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.SectorType)get_store().add_element_user(SECTOR$0);
            }
            target.set(sector);
        }
    }
    
    /**
     * Appends and returns a new empty "Sector" element
     */
    public org.astrogrid.stc.region.v1_10.beans.SectorType addNewSector()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SectorType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SectorType)get_store().add_element_user(SECTOR$0);
            return target;
        }
    }
}
