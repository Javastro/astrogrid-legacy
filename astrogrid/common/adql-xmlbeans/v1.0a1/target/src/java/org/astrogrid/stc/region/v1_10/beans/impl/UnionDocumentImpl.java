/*
 * An XML document type.
 * Localname: Union
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.UnionDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one Union(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class UnionDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.UnionDocument
{
    
    public UnionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName UNION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Union");
    
    
    /**
     * Gets the "Union" element
     */
    public org.astrogrid.stc.region.v1_10.beans.UnionType getUnion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.UnionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.UnionType)get_store().find_element_user(UNION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Union" element
     */
    public void setUnion(org.astrogrid.stc.region.v1_10.beans.UnionType union)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.UnionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.UnionType)get_store().find_element_user(UNION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.UnionType)get_store().add_element_user(UNION$0);
            }
            target.set(union);
        }
    }
    
    /**
     * Appends and returns a new empty "Union" element
     */
    public org.astrogrid.stc.region.v1_10.beans.UnionType addNewUnion()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.UnionType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.UnionType)get_store().add_element_user(UNION$0);
            return target;
        }
    }
}
