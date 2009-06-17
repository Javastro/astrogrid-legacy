/*
 * An XML document type.
 * Localname: Position3D
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Position3DDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Position3D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Position3DDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.PositionDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Position3DDocument
{
    
    public Position3DDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POSITION3D$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position3D");
    
    
    /**
     * Gets the "Position3D" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D getPosition3D()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D)get_store().find_element_user(POSITION3D$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Position3D" element
     */
    public void setPosition3D(org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D position3D)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D)get_store().find_element_user(POSITION3D$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D)get_store().add_element_user(POSITION3D$0);
            }
            target.set(position3D);
        }
    }
    
    /**
     * Appends and returns a new empty "Position3D" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D addNewPosition3D()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D)get_store().add_element_user(POSITION3D$0);
            return target;
        }
    }
    /**
     * An XML Position3D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public static class Position3DImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.Vector3CoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.Position3DDocument.Position3D
    {
        
        public Position3DImpl(org.apache.xmlbeans.SchemaType sType)
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
