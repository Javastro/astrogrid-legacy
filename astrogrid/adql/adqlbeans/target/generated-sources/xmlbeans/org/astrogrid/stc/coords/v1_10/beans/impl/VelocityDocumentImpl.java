/*
 * An XML document type.
 * Localname: Velocity
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.VelocityDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Velocity(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class VelocityDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.coords.v1_10.beans.VelocityDocument
{
    
    public VelocityDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VELOCITY$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity");
    private static final org.apache.xmlbeans.QNameSet VELOCITY$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity3D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity1D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity2D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity"),
    });
    
    
    /**
     * Gets the "Velocity" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType getVelocity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(VELOCITY$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Velocity" element
     */
    public void setVelocity(org.astrogrid.stc.coords.v1_10.beans.CoordinateType velocity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(VELOCITY$1, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(VELOCITY$0);
            }
            target.set(velocity);
        }
    }
    
    /**
     * Appends and returns a new empty "Velocity" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType addNewVelocity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(VELOCITY$0);
            return target;
        }
    }
}
