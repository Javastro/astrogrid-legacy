/*
 * An XML document type.
 * Localname: Velocity2D
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Velocity2D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Velocity2DDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.VelocityDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument
{
    
    public Velocity2DDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VELOCITY2D$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity2D");
    
    
    /**
     * Gets the "Velocity2D" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D getVelocity2D()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D)get_store().find_element_user(VELOCITY2D$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Velocity2D" element
     */
    public void setVelocity2D(org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D velocity2D)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D)get_store().find_element_user(VELOCITY2D$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D)get_store().add_element_user(VELOCITY2D$0);
            }
            target.set(velocity2D);
        }
    }
    
    /**
     * Appends and returns a new empty "Velocity2D" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D addNewVelocity2D()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D)get_store().add_element_user(VELOCITY2D$0);
            return target;
        }
    }
    /**
     * An XML Velocity2D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public static class Velocity2DImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.Vector2CoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.Velocity2DDocument.Velocity2D
    {
        
        public Velocity2DImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName UNIT$0 = 
            new javax.xml.namespace.QName("", "unit");
        private static final javax.xml.namespace.QName VELTIMEUNIT$2 = 
            new javax.xml.namespace.QName("", "vel_time_unit");
        
        
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
        
        /**
         * Gets the "vel_time_unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType.Enum getVelTimeUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VELTIMEUNIT$2);
                if (target == null)
                {
                    return null;
                }
                return (org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "vel_time_unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType xgetVelTimeUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType)get_store().find_attribute_user(VELTIMEUNIT$2);
                return target;
            }
        }
        
        /**
         * Sets the "vel_time_unit" attribute
         */
        public void setVelTimeUnit(org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType.Enum velTimeUnit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(VELTIMEUNIT$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(VELTIMEUNIT$2);
                }
                target.setEnumValue(velTimeUnit);
            }
        }
        
        /**
         * Sets (as xml) the "vel_time_unit" attribute
         */
        public void xsetVelTimeUnit(org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType velTimeUnit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType)get_store().find_attribute_user(VELTIMEUNIT$2);
                if (target == null)
                {
                    target = (org.astrogrid.stc.coords.v1_10.beans.VelTimeUnitType)get_store().add_attribute_user(VELTIMEUNIT$2);
                }
                target.set(velTimeUnit);
            }
        }
    }
}
