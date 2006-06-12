/*
 * XML Type:  astroCoordsFileType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * An XML astroCoordsFileType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public class AstroCoordsFileTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.coords.v1_10.beans.AstroCoordsFileType
{
    
    public AstroCoordsFileTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FITSFILE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "FITSFile");
    private static final javax.xml.namespace.QName FITSTIME$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "FITSTime");
    private static final javax.xml.namespace.QName FITSPOSITION$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "FITSPosition");
    private static final javax.xml.namespace.QName FITSVELOCITY$6 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "FITSVelocity");
    private static final javax.xml.namespace.QName FITSSPECTRAL$8 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "FITSSpectral");
    private static final javax.xml.namespace.QName FITSREDSHIFT$10 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "FITSRedshift");
    
    
    /**
     * Gets the "FITSFile" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.FitsType getFITSFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.FitsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.FitsType)get_store().find_element_user(FITSFILE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "FITSFile" element
     */
    public void setFITSFile(org.astrogrid.stc.coords.v1_10.beans.FitsType fitsFile)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.FitsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.FitsType)get_store().find_element_user(FITSFILE$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.FitsType)get_store().add_element_user(FITSFILE$0);
            }
            target.set(fitsFile);
        }
    }
    
    /**
     * Appends and returns a new empty "FITSFile" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.FitsType addNewFITSFile()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.FitsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.FitsType)get_store().add_element_user(FITSFILE$0);
            return target;
        }
    }
    
    /**
     * Gets the "FITSTime" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSTIME$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "FITSTime" element
     */
    public boolean isSetFITSTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FITSTIME$2) != 0;
        }
    }
    
    /**
     * Sets the "FITSTime" element
     */
    public void setFITSTime(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsTime)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSTIME$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSTIME$2);
            }
            target.set(fitsTime);
        }
    }
    
    /**
     * Appends and returns a new empty "FITSTime" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSTIME$2);
            return target;
        }
    }
    
    /**
     * Unsets the "FITSTime" element
     */
    public void unsetFITSTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FITSTIME$2, 0);
        }
    }
    
    /**
     * Gets the "FITSPosition" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSPOSITION$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "FITSPosition" element
     */
    public boolean isSetFITSPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FITSPOSITION$4) != 0;
        }
    }
    
    /**
     * Sets the "FITSPosition" element
     */
    public void setFITSPosition(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsPosition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSPOSITION$4, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSPOSITION$4);
            }
            target.set(fitsPosition);
        }
    }
    
    /**
     * Appends and returns a new empty "FITSPosition" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSPOSITION$4);
            return target;
        }
    }
    
    /**
     * Unsets the "FITSPosition" element
     */
    public void unsetFITSPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FITSPOSITION$4, 0);
        }
    }
    
    /**
     * Gets the "FITSVelocity" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSVelocity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSVELOCITY$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "FITSVelocity" element
     */
    public boolean isSetFITSVelocity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FITSVELOCITY$6) != 0;
        }
    }
    
    /**
     * Sets the "FITSVelocity" element
     */
    public void setFITSVelocity(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsVelocity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSVELOCITY$6, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSVELOCITY$6);
            }
            target.set(fitsVelocity);
        }
    }
    
    /**
     * Appends and returns a new empty "FITSVelocity" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSVelocity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSVELOCITY$6);
            return target;
        }
    }
    
    /**
     * Unsets the "FITSVelocity" element
     */
    public void unsetFITSVelocity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FITSVELOCITY$6, 0);
        }
    }
    
    /**
     * Gets the "FITSSpectral" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSSpectral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSSPECTRAL$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "FITSSpectral" element
     */
    public boolean isSetFITSSpectral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FITSSPECTRAL$8) != 0;
        }
    }
    
    /**
     * Sets the "FITSSpectral" element
     */
    public void setFITSSpectral(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsSpectral)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSSPECTRAL$8, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSSPECTRAL$8);
            }
            target.set(fitsSpectral);
        }
    }
    
    /**
     * Appends and returns a new empty "FITSSpectral" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSSpectral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSSPECTRAL$8);
            return target;
        }
    }
    
    /**
     * Unsets the "FITSSpectral" element
     */
    public void unsetFITSSpectral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FITSSPECTRAL$8, 0);
        }
    }
    
    /**
     * Gets the "FITSRedshift" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType getFITSRedshift()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSREDSHIFT$10, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "FITSRedshift" element
     */
    public boolean isSetFITSRedshift()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FITSREDSHIFT$10) != 0;
        }
    }
    
    /**
     * Sets the "FITSRedshift" element
     */
    public void setFITSRedshift(org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType fitsRedshift)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().find_element_user(FITSREDSHIFT$10, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSREDSHIFT$10);
            }
            target.set(fitsRedshift);
        }
    }
    
    /**
     * Appends and returns a new empty "FITSRedshift" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType addNewFITSRedshift()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordFITSColumnsType)get_store().add_element_user(FITSREDSHIFT$10);
            return target;
        }
    }
    
    /**
     * Unsets the "FITSRedshift" element
     */
    public void unsetFITSRedshift()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FITSREDSHIFT$10, 0);
        }
    }
}
