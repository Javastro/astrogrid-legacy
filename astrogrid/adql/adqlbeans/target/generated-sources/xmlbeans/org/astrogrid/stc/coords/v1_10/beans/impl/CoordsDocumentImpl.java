/*
 * An XML document type.
 * Localname: Coords
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.CoordsDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Coords(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class CoordsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.coords.v1_10.beans.CoordsDocument
{
    
    public CoordsDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COORDS$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Coords");
    private static final org.apache.xmlbeans.QNameSet COORDS$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "AstroCoords"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Coords"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixelCoords"),
    });
    
    
    /**
     * Gets the "Coords" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordsType getCoords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordsType)get_store().find_element_user(COORDS$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Coords" element
     */
    public void setCoords(org.astrogrid.stc.coords.v1_10.beans.CoordsType coords)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordsType)get_store().find_element_user(COORDS$1, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordsType)get_store().add_element_user(COORDS$0);
            }
            target.set(coords);
        }
    }
    
    /**
     * Appends and returns a new empty "Coords" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordsType addNewCoords()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordsType)get_store().add_element_user(COORDS$0);
            return target;
        }
    }
}
