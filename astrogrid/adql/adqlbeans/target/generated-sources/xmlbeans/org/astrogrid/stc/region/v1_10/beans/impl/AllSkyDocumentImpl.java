/*
 * An XML document type.
 * Localname: AllSky
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.AllSkyDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one AllSky(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class AllSkyDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.AllSkyDocument
{
    
    public AllSkyDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ALLSKY$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "AllSky");
    
    
    /**
     * Gets the "AllSky" element
     */
    public org.astrogrid.stc.region.v1_10.beans.RegionType getAllSky()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().find_element_user(ALLSKY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AllSky" element
     */
    public void setAllSky(org.astrogrid.stc.region.v1_10.beans.RegionType allSky)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().find_element_user(ALLSKY$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().add_element_user(ALLSKY$0);
            }
            target.set(allSky);
        }
    }
    
    /**
     * Appends and returns a new empty "AllSky" element
     */
    public org.astrogrid.stc.region.v1_10.beans.RegionType addNewAllSky()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().add_element_user(ALLSKY$0);
            return target;
        }
    }
}
