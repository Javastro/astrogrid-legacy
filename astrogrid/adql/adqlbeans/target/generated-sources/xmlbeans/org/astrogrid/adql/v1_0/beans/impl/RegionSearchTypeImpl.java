/*
 * XML Type:  regionSearchType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.RegionSearchType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML regionSearchType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class RegionSearchTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.SearchTypeImpl implements org.astrogrid.adql.v1_0.beans.RegionSearchType
{
    
    public RegionSearchTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REGION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Region");
    
    
    /**
     * Gets the "Region" element
     */
    public org.astrogrid.stc.region.v1_10.beans.RegionType getRegion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().find_element_user(REGION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Region" element
     */
    public void setRegion(org.astrogrid.stc.region.v1_10.beans.RegionType region)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().find_element_user(REGION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().add_element_user(REGION$0);
            }
            target.set(region);
        }
    }
    
    /**
     * Appends and returns a new empty "Region" element
     */
    public org.astrogrid.stc.region.v1_10.beans.RegionType addNewRegion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().add_element_user(REGION$0);
            return target;
        }
    }
}
