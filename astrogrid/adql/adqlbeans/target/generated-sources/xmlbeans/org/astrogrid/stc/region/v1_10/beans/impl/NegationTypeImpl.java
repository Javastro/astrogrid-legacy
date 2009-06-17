/*
 * XML Type:  negationType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.NegationType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML negationType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class NegationTypeImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionTypeImpl implements org.astrogrid.stc.region.v1_10.beans.NegationType
{
    
    public NegationTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REGION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Region");
    
    
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
