/*
 * XML Type:  sectorType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.SectorType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML sectorType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class SectorTypeImpl extends org.astrogrid.stc.region.v1_10.beans.impl.ShapeTypeImpl implements org.astrogrid.stc.region.v1_10.beans.SectorType
{
    
    public SectorTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POSITION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Position");
    private static final javax.xml.namespace.QName POSANGLE1$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "PosAngle1");
    private static final javax.xml.namespace.QName POSANGLE2$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "PosAngle2");
    private static final javax.xml.namespace.QName POSANGLEUNIT$6 = 
        new javax.xml.namespace.QName("", "pos_angle_unit");
    
    
    /**
     * Gets the "Position" element
     */
    public java.util.List getPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSITION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Position" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double2Type xgetPosition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().find_element_user(POSITION$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Position" element
     */
    public void setPosition(java.util.List position)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSITION$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(POSITION$0);
            }
            target.setListValue(position);
        }
    }
    
    /**
     * Sets (as xml) the "Position" element
     */
    public void xsetPosition(org.astrogrid.stc.coords.v1_10.beans.Double2Type position)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().find_element_user(POSITION$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().add_element_user(POSITION$0);
            }
            target.set(position);
        }
    }
    
    /**
     * Gets the "PosAngle1" element
     */
    public double getPosAngle1()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSANGLE1$2, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "PosAngle1" element
     */
    public org.apache.xmlbeans.XmlDouble xgetPosAngle1()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(POSANGLE1$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "PosAngle1" element
     */
    public void setPosAngle1(double posAngle1)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSANGLE1$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(POSANGLE1$2);
            }
            target.setDoubleValue(posAngle1);
        }
    }
    
    /**
     * Sets (as xml) the "PosAngle1" element
     */
    public void xsetPosAngle1(org.apache.xmlbeans.XmlDouble posAngle1)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(POSANGLE1$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(POSANGLE1$2);
            }
            target.set(posAngle1);
        }
    }
    
    /**
     * Gets the "PosAngle2" element
     */
    public double getPosAngle2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSANGLE2$4, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "PosAngle2" element
     */
    public org.apache.xmlbeans.XmlDouble xgetPosAngle2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(POSANGLE2$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "PosAngle2" element
     */
    public void setPosAngle2(double posAngle2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSANGLE2$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(POSANGLE2$4);
            }
            target.setDoubleValue(posAngle2);
        }
    }
    
    /**
     * Sets (as xml) the "PosAngle2" element
     */
    public void xsetPosAngle2(org.apache.xmlbeans.XmlDouble posAngle2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(POSANGLE2$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(POSANGLE2$4);
            }
            target.set(posAngle2);
        }
    }
    
    /**
     * Gets the "pos_angle_unit" attribute
     */
    public org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum getPosAngleUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(POSANGLEUNIT$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(POSANGLEUNIT$6);
            }
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "pos_angle_unit" attribute
     */
    public org.astrogrid.stc.coords.v1_10.beans.PosUnitType xgetPosAngleUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PosUnitType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().find_attribute_user(POSANGLEUNIT$6);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_default_attribute_value(POSANGLEUNIT$6);
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
            return get_store().find_attribute_user(POSANGLEUNIT$6) != null;
        }
    }
    
    /**
     * Sets the "pos_angle_unit" attribute
     */
    public void setPosAngleUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum posAngleUnit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(POSANGLEUNIT$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(POSANGLEUNIT$6);
            }
            target.setEnumValue(posAngleUnit);
        }
    }
    
    /**
     * Sets (as xml) the "pos_angle_unit" attribute
     */
    public void xsetPosAngleUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType posAngleUnit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PosUnitType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().find_attribute_user(POSANGLEUNIT$6);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().add_attribute_user(POSANGLEUNIT$6);
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
            get_store().remove_attribute(POSANGLEUNIT$6);
        }
    }
}
