/*
 * An XML document type.
 * Localname: AstroCoords
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.AstroCoordsDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one AstroCoords(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class AstroCoordsDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordsDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.AstroCoordsDocument
{
    
    public AstroCoordsDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ASTROCOORDS$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "AstroCoords");
    
    
    /**
     * Gets the "AstroCoords" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType getAstroCoords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType)get_store().find_element_user(ASTROCOORDS$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AstroCoords" element
     */
    public void setAstroCoords(org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType astroCoords)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType)get_store().find_element_user(ASTROCOORDS$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType)get_store().add_element_user(ASTROCOORDS$0);
            }
            target.set(astroCoords);
        }
    }
    
    /**
     * Appends and returns a new empty "AstroCoords" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType addNewAstroCoords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType)get_store().add_element_user(ASTROCOORDS$0);
            return target;
        }
    }
}
