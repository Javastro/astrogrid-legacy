/*
 * XML Type:  astroCoordsType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * An XML astroCoordsType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public class AstroCoordsTypeImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordsTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType
{
    
    public AstroCoordsTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIME$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Time");
    private static final javax.xml.namespace.QName POSITION$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position");
    private static final org.apache.xmlbeans.QNameSet POSITION$3 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position3D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position1D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position2D"),
    });
    private static final javax.xml.namespace.QName VELOCITY$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity");
    private static final org.apache.xmlbeans.QNameSet VELOCITY$5 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity3D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity1D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity2D"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Velocity"),
    });
    private static final javax.xml.namespace.QName SPECTRAL$6 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Spectral");
    private static final javax.xml.namespace.QName REDSHIFT$8 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Redshift");
    private static final javax.xml.namespace.QName COORDFILE$10 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CoordFile");
    
    
    /**
     * Gets the "Time" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType getTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType)get_store().find_element_user(TIME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Time" element
     */
    public boolean isSetTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TIME$0) != 0;
        }
    }
    
    /**
     * Sets the "Time" element
     */
    public void setTime(org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType time)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType)get_store().find_element_user(TIME$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType)get_store().add_element_user(TIME$0);
            }
            target.set(time);
        }
    }
    
    /**
     * Appends and returns a new empty "Time" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType addNewTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType)get_store().add_element_user(TIME$0);
            return target;
        }
    }
    
    /**
     * Unsets the "Time" element
     */
    public void unsetTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TIME$0, 0);
        }
    }
    
    /**
     * Gets the "Position" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType getPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(POSITION$3, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Position" element
     */
    public boolean isSetPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(POSITION$3) != 0;
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
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(POSITION$3, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(POSITION$2);
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
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(POSITION$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Position" element
     */
    public void unsetPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(POSITION$3, 0);
        }
    }
    
    /**
     * Gets the "Velocity" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType getVelocity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(VELOCITY$5, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Velocity" element
     */
    public boolean isSetVelocity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(VELOCITY$5) != 0;
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
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(VELOCITY$5, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(VELOCITY$4);
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
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(VELOCITY$4);
            return target;
        }
    }
    
    /**
     * Unsets the "Velocity" element
     */
    public void unsetVelocity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(VELOCITY$5, 0);
        }
    }
    
    /**
     * Gets the "Spectral" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral getSpectral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral)get_store().find_element_user(SPECTRAL$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Spectral" element
     */
    public boolean isSetSpectral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SPECTRAL$6) != 0;
        }
    }
    
    /**
     * Sets the "Spectral" element
     */
    public void setSpectral(org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral spectral)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral)get_store().find_element_user(SPECTRAL$6, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral)get_store().add_element_user(SPECTRAL$6);
            }
            target.set(spectral);
        }
    }
    
    /**
     * Appends and returns a new empty "Spectral" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral addNewSpectral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral)get_store().add_element_user(SPECTRAL$6);
            return target;
        }
    }
    
    /**
     * Unsets the "Spectral" element
     */
    public void unsetSpectral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SPECTRAL$6, 0);
        }
    }
    
    /**
     * Gets the "Redshift" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift getRedshift()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift)get_store().find_element_user(REDSHIFT$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Redshift" element
     */
    public boolean isSetRedshift()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(REDSHIFT$8) != 0;
        }
    }
    
    /**
     * Sets the "Redshift" element
     */
    public void setRedshift(org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift redshift)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift)get_store().find_element_user(REDSHIFT$8, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift)get_store().add_element_user(REDSHIFT$8);
            }
            target.set(redshift);
        }
    }
    
    /**
     * Appends and returns a new empty "Redshift" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift addNewRedshift()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift)get_store().add_element_user(REDSHIFT$8);
            return target;
        }
    }
    
    /**
     * Unsets the "Redshift" element
     */
    public void unsetRedshift()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(REDSHIFT$8, 0);
        }
    }
    
    /**
     * Gets the "CoordFile" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType getCoordFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType)get_store().find_element_user(COORDFILE$10, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "CoordFile" element
     */
    public boolean isSetCoordFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COORDFILE$10) != 0;
        }
    }
    
    /**
     * Sets the "CoordFile" element
     */
    public void setCoordFile(org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType coordFile)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType)get_store().find_element_user(COORDFILE$10, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType)get_store().add_element_user(COORDFILE$10);
            }
            target.set(coordFile);
        }
    }
    
    /**
     * Appends and returns a new empty "CoordFile" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType addNewCoordFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType)get_store().add_element_user(COORDFILE$10);
            return target;
        }
    }
    
    /**
     * Unsets the "CoordFile" element
     */
    public void unsetCoordFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COORDFILE$10, 0);
        }
    }
    /**
     * An XML Spectral(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public static class SpectralImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.ScalarCoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Spectral
    {
        
        public SpectralImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName UNIT$0 = 
            new javax.xml.namespace.QName("", "unit");
        
        
        /**
         * Gets the "unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType.Enum getUnit()
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
                return (org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType xgetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType)get_store().find_attribute_user(UNIT$0);
                return target;
            }
        }
        
        /**
         * True if has "unit" attribute
         */
        public boolean isSetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(UNIT$0) != null;
            }
        }
        
        /**
         * Sets the "unit" attribute
         */
        public void setUnit(org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType.Enum unit)
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
        public void xsetUnit(org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.astrogrid.stc.coords.v1_10.beans.SpectralUnitType)get_store().add_attribute_user(UNIT$0);
                }
                target.set(unit);
            }
        }
        
        /**
         * Unsets the "unit" attribute
         */
        public void unsetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(UNIT$0);
            }
        }
    }
    /**
     * An XML Redshift(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public static class RedshiftImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.ScalarCoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.AstroCoordsType.Redshift
    {
        
        public RedshiftImpl(org.apache.xmlbeans.SchemaType sType)
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
         * True if has "unit" attribute
         */
        public boolean isSetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(UNIT$0) != null;
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
         * Unsets the "unit" attribute
         */
        public void unsetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(UNIT$0);
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
         * True if has "vel_time_unit" attribute
         */
        public boolean isSetVelTimeUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(VELTIMEUNIT$2) != null;
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
        
        /**
         * Unsets the "vel_time_unit" attribute
         */
        public void unsetVelTimeUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(VELTIMEUNIT$2);
            }
        }
    }
}
