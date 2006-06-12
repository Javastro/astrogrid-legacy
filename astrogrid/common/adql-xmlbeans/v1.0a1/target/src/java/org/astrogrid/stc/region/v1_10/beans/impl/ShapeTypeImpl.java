/*
 * XML Type:  shapeType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.ShapeType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML shapeType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class ShapeTypeImpl extends org.astrogrid.stc.region.v1_10.beans.impl.RegionTypeImpl implements org.astrogrid.stc.region.v1_10.beans.ShapeType
{
    
    public ShapeTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COORDSYSTEMID$0 = 
        new javax.xml.namespace.QName("", "coord_system_id");
    private static final javax.xml.namespace.QName UNIT$2 = 
        new javax.xml.namespace.QName("", "unit");
    
    
    /**
     * Gets the "coord_system_id" attribute
     */
    public java.lang.String getCoordSystemId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COORDSYSTEMID$0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "coord_system_id" attribute
     */
    public org.apache.xmlbeans.XmlIDREF xgetCoordSystemId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(COORDSYSTEMID$0);
            return target;
        }
    }
    
    /**
     * Sets the "coord_system_id" attribute
     */
    public void setCoordSystemId(java.lang.String coordSystemId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COORDSYSTEMID$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(COORDSYSTEMID$0);
            }
            target.setStringValue(coordSystemId);
        }
    }
    
    /**
     * Sets (as xml) the "coord_system_id" attribute
     */
    public void xsetCoordSystemId(org.apache.xmlbeans.XmlIDREF coordSystemId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(COORDSYSTEMID$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_attribute_user(COORDSYSTEMID$0);
            }
            target.set(coordSystemId);
        }
    }
    
    /**
     * Gets the "unit" attribute
     */
    public org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum getUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$2);
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
            target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().find_attribute_user(UNIT$2);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(UNIT$2);
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
            target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().find_attribute_user(UNIT$2);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().add_attribute_user(UNIT$2);
            }
            target.set(unit);
        }
    }
}
