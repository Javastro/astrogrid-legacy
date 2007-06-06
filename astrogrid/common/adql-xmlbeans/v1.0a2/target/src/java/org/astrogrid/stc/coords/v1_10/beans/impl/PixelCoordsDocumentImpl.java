/*
 * An XML document type.
 * Localname: PixelCoords
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixelCoordsDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixelCoords(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixelCoordsDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordsDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixelCoordsDocument
{
    
    public PixelCoordsDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXELCOORDS$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixelCoords");
    
    
    /**
     * Gets the "PixelCoords" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType getPixelCoords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType)get_store().find_element_user(PIXELCOORDS$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PixelCoords" element
     */
    public void setPixelCoords(org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType pixelCoords)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType)get_store().find_element_user(PIXELCOORDS$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType)get_store().add_element_user(PIXELCOORDS$0);
            }
            target.set(pixelCoords);
        }
    }
    
    /**
     * Appends and returns a new empty "PixelCoords" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType addNewPixelCoords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType)get_store().add_element_user(PIXELCOORDS$0);
            return target;
        }
    }
}
