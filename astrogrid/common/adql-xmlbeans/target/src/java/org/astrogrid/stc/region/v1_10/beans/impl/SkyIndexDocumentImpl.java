/*
 * An XML document type.
 * Localname: SkyIndex
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.SkyIndexDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * A document containing one SkyIndex(@http://www.ivoa.net/xml/STC/STCregion/v1.10) element.
 *
 * This is a complex type.
 */
public class SkyIndexDocumentImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionDocumentImpl implements org.astrogrid.stc.region.v1_10.beans.SkyIndexDocument
{
    
    public SkyIndexDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SKYINDEX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "SkyIndex");
    
    
    /**
     * Gets the "SkyIndex" element
     */
    public org.astrogrid.stc.region.v1_10.beans.SkyIndexType getSkyIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SkyIndexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SkyIndexType)get_store().find_element_user(SKYINDEX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "SkyIndex" element
     */
    public void setSkyIndex(org.astrogrid.stc.region.v1_10.beans.SkyIndexType skyIndex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SkyIndexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SkyIndexType)get_store().find_element_user(SKYINDEX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.SkyIndexType)get_store().add_element_user(SKYINDEX$0);
            }
            target.set(skyIndex);
        }
    }
    
    /**
     * Appends and returns a new empty "SkyIndex" element
     */
    public org.astrogrid.stc.region.v1_10.beans.SkyIndexType addNewSkyIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SkyIndexType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SkyIndexType)get_store().add_element_user(SKYINDEX$0);
            return target;
        }
    }
}
