/*
 * An XML document type.
 * Localname: Position2D
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Position2DDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Position2D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Position2DDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.PositionDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Position2DDocument
{
    
    public Position2DDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POSITION2D$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position2D");
    
    
    /**
     * Gets the "Position2D" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D getPosition2D()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D)get_store().find_element_user(POSITION2D$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Position2D" element
     */
    public void setPosition2D(org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D position2D)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D)get_store().find_element_user(POSITION2D$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D)get_store().add_element_user(POSITION2D$0);
            }
            target.set(position2D);
        }
    }
    
    /**
     * Appends and returns a new empty "Position2D" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D addNewPosition2D()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D)get_store().add_element_user(POSITION2D$0);
            return target;
        }
    }
    /**
     * An XML Position2D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public static class Position2DImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.Vector2CoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.Position2DDocument.Position2D
    {
        
        public Position2DImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName UNIT$0 = 
            new javax.xml.namespace.QName("", "unit");
        
        
        /**
         * Gets the "unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum getUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    return null;
                }
                return (org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.PosUnitType xgetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.PosUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().find_attribute_user(UNIT$0);
                return target;
            }
        }
        
        /**
         * Sets the "unit" attribute
         */
        public void setUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(UNIT$0);
                }
                target.setEnumValue(unit);
            }
        }
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        public void xsetUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.PosUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().add_attribute_user(UNIT$0);
                }
                target.set(unit);
            }
        }
    }
}
