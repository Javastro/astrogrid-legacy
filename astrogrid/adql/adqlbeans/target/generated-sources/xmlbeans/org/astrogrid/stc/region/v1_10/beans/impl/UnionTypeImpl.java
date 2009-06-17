/*
 * XML Type:  unionType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.UnionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML unionType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class UnionTypeImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionTypeImpl implements org.astrogrid.stc.region.v1_10.beans.UnionType
{
    
    public UnionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REGION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Region");
    
    
    /**
     * Gets array of all "Region" elements
     */
    public org.astrogrid.stc.region.v1_10.beans.RegionType[] getRegionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(REGION$0, targetList);
            org.astrogrid.stc.region.v1_10.beans.RegionType[] result = new org.astrogrid.stc.region.v1_10.beans.RegionType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Region" element
     */
    public org.astrogrid.stc.region.v1_10.beans.RegionType getRegionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().find_element_user(REGION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Region" element
     */
    public int sizeOfRegionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(REGION$0);
        }
    }
    
    /**
     * Sets array of all "Region" element
     */
    public void setRegionArray(org.astrogrid.stc.region.v1_10.beans.RegionType[] regionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(regionArray, REGION$0);
        }
    }
    
    /**
     * Sets ith "Region" element
     */
    public void setRegionArray(int i, org.astrogrid.stc.region.v1_10.beans.RegionType region)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().find_element_user(REGION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(region);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Region" element
     */
    public org.astrogrid.stc.region.v1_10.beans.RegionType insertNewRegion(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType)get_store().insert_element_user(REGION$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Region" element
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
    
    /**
     * Removes the ith "Region" element
     */
    public void removeRegion(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(REGION$0, i);
        }
    }
}
