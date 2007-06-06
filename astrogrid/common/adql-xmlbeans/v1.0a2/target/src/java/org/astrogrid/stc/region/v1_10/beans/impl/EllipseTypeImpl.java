/*
 * XML Type:  ellipseType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.EllipseType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML ellipseType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class EllipseTypeImpl extends org.astrogrid.stc.region.v1_10.beans.impl.CircleTypeImpl implements org.astrogrid.stc.region.v1_10.beans.EllipseType
{
    
    public EllipseTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MINORRADIUS$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "MinorRadius");
    private static final javax.xml.namespace.QName POSANGLE$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "PosAngle");
    private static final javax.xml.namespace.QName POSANGLEUNIT$4 = 
        new javax.xml.namespace.QName("", "pos_angle_unit");
    
    
    /**
     * Gets the "MinorRadius" element
     */
    public double getMinorRadius()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MINORRADIUS$0, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "MinorRadius" element
     */
    public org.apache.xmlbeans.XmlDouble xgetMinorRadius()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(MINORRADIUS$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "MinorRadius" element
     */
    public void setMinorRadius(double minorRadius)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MINORRADIUS$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MINORRADIUS$0);
            }
            target.setDoubleValue(minorRadius);
        }
    }
    
    /**
     * Sets (as xml) the "MinorRadius" element
     */
    public void xsetMinorRadius(org.apache.xmlbeans.XmlDouble minorRadius)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(MINORRADIUS$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(MINORRADIUS$0);
            }
            target.set(minorRadius);
        }
    }
    
    /**
     * Gets the "PosAngle" element
     */
    public double getPosAngle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSANGLE$2, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "PosAngle" element
     */
    public org.apache.xmlbeans.XmlDouble xgetPosAngle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(POSANGLE$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "PosAngle" element
     */
    public void setPosAngle(double posAngle)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSANGLE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(POSANGLE$2);
            }
            target.setDoubleValue(posAngle);
        }
    }
    
    /**
     * Sets (as xml) the "PosAngle" element
     */
    public void xsetPosAngle(org.apache.xmlbeans.XmlDouble posAngle)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(POSANGLE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(POSANGLE$2);
            }
            target.set(posAngle);
        }
    }
    
    /**
     * Gets the "pos_angle_unit" attribute
     */
    public org.astrogrid.stc.coords.v1_10.beans.AngleUnitType.Enum getPosAngleUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(POSANGLEUNIT$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(POSANGLEUNIT$4);
            }
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.stc.coords.v1_10.beans.AngleUnitType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "pos_angle_unit" attribute
     */
    public org.astrogrid.stc.coords.v1_10.beans.AngleUnitType xgetPosAngleUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AngleUnitType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AngleUnitType)get_store().find_attribute_user(POSANGLEUNIT$4);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.AngleUnitType)get_default_attribute_value(POSANGLEUNIT$4);
            }
            return target;
        }
    }
    
    /**
     * True if has "pos_angle_unit" attribute
     */
    public boolean isSetPosAngleUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(POSANGLEUNIT$4) != null;
        }
    }
    
    /**
     * Sets the "pos_angle_unit" attribute
     */
    public void setPosAngleUnit(org.astrogrid.stc.coords.v1_10.beans.AngleUnitType.Enum posAngleUnit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(POSANGLEUNIT$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(POSANGLEUNIT$4);
            }
            target.setEnumValue(posAngleUnit);
        }
    }
    
    /**
     * Sets (as xml) the "pos_angle_unit" attribute
     */
    public void xsetPosAngleUnit(org.astrogrid.stc.coords.v1_10.beans.AngleUnitType posAngleUnit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AngleUnitType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AngleUnitType)get_store().find_attribute_user(POSANGLEUNIT$4);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.AngleUnitType)get_store().add_attribute_user(POSANGLEUNIT$4);
            }
            target.set(posAngleUnit);
        }
    }
    
    /**
     * Unsets the "pos_angle_unit" attribute
     */
    public void unsetPosAngleUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(POSANGLEUNIT$4);
        }
    }
}
