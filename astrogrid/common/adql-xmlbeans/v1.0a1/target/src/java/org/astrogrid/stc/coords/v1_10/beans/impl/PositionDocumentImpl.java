/*
 * An XML document type.
 * Localname: Position
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PositionDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Position(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PositionDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.coords.v1_10.beans.PositionDocument
{
    
    public PositionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POSITION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position");
    private static final org.apache.xmlbeans.QNameSet POSITION$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position3D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position1D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position2D"),
    });
    
    
    /**
     * Gets the "Position" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType getPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(POSITION$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Position" element
     */
    public void setPosition(org.astrogrid.stc.coords.v1_10.beans.CoordinateType position)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(POSITION$1, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(POSITION$0);
            }
            target.set(position);
        }
    }
    
    /**
     * Appends and returns a new empty "Position" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType addNewPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(POSITION$0);
            return target;
        }
    }
}
