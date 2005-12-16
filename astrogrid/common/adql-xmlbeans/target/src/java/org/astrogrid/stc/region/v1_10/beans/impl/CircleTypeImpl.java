/*
 * XML Type:  circleType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.CircleType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML circleType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class CircleTypeImpl extends org.astrogrid.stc.region.v1_10.beans.impl.ShapeTypeImpl implements org.astrogrid.stc.region.v1_10.beans.CircleType
{
    
    public CircleTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CENTER$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Center");
    private static final javax.xml.namespace.QName RADIUS$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Radius");
    
    
    /**
     * Gets the "Center" element
     */
    public java.util.List getCenter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CENTER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Center" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double2Type xgetCenter()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().find_element_user(CENTER$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Center" element
     */
    public void setCenter(java.util.List center)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CENTER$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CENTER$0);
            }
            target.setListValue(center);
        }
    }
    
    /**
     * Sets (as xml) the "Center" element
     */
    public void xsetCenter(org.astrogrid.stc.coords.v1_10.beans.Double2Type center)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().find_element_user(CENTER$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().add_element_user(CENTER$0);
            }
            target.set(center);
        }
    }
    
    /**
     * Gets the "Radius" element
     */
    public double getRadius()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RADIUS$2, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "Radius" element
     */
    public org.apache.xmlbeans.XmlDouble xgetRadius()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(RADIUS$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Radius" element
     */
    public void setRadius(double radius)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RADIUS$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RADIUS$2);
            }
            target.setDoubleValue(radius);
        }
    }
    
    /**
     * Sets (as xml) the "Radius" element
     */
    public void xsetRadius(org.apache.xmlbeans.XmlDouble radius)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(RADIUS$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(RADIUS$2);
            }
            target.set(radius);
        }
    }
}
