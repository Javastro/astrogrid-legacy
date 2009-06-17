/*
 * An XML document type.
 * Localname: Coordinate
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.CoordinateDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Coordinate(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class CoordinateDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.coords.v1_10.beans.CoordinateDocument
{
    
    public CoordinateDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COORDINATE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Coordinate");
    private static final org.apache.xmlbeans.QNameSet COORDINATE$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ScalarCoordinate"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixelCoordinate"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Coordinate"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "StringCoordinate"),
    });
    
    
    /**
     * Gets the "Coordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType getCoordinate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(COORDINATE$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Coordinate" element
     */
    public void setCoordinate(org.astrogrid.stc.coords.v1_10.beans.CoordinateType coordinate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(COORDINATE$1, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(COORDINATE$0);
            }
            target.set(coordinate);
        }
    }
    
    /**
     * Appends and returns a new empty "Coordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType addNewCoordinate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(COORDINATE$0);
            return target;
        }
    }
}
