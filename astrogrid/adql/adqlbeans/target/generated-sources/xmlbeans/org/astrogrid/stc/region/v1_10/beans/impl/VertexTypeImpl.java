/*
 * XML Type:  vertexType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.VertexType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML vertexType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class VertexTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.region.v1_10.beans.VertexType
{
    
    public VertexTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POSITION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Position");
    private static final javax.xml.namespace.QName SMALLCIRCLE$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "SmallCircle");
    
    
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
     * Gets the "SmallCircle" element
     */
    public org.astrogrid.stc.region.v1_10.beans.SmallCircleType getSmallCircle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SmallCircleType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SmallCircleType)get_store().find_element_user(SMALLCIRCLE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Tests for nil "SmallCircle" element
     */
    public boolean isNilSmallCircle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SmallCircleType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SmallCircleType)get_store().find_element_user(SMALLCIRCLE$2, 0);
            if (target == null) return false;
            return target.isNil();
        }
    }
    
    /**
     * True if has "SmallCircle" element
     */
    public boolean isSetSmallCircle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SMALLCIRCLE$2) != 0;
        }
    }
    
    /**
     * Sets the "SmallCircle" element
     */
    public void setSmallCircle(org.astrogrid.stc.region.v1_10.beans.SmallCircleType smallCircle)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SmallCircleType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SmallCircleType)get_store().find_element_user(SMALLCIRCLE$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.SmallCircleType)get_store().add_element_user(SMALLCIRCLE$2);
            }
            target.set(smallCircle);
        }
    }
    
    /**
     * Appends and returns a new empty "SmallCircle" element
     */
    public org.astrogrid.stc.region.v1_10.beans.SmallCircleType addNewSmallCircle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SmallCircleType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SmallCircleType)get_store().add_element_user(SMALLCIRCLE$2);
            return target;
        }
    }
    
    /**
     * Nils the "SmallCircle" element
     */
    public void setNilSmallCircle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.SmallCircleType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.SmallCircleType)get_store().find_element_user(SMALLCIRCLE$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.SmallCircleType)get_store().add_element_user(SMALLCIRCLE$2);
            }
            target.setNil();
        }
    }
    
    /**
     * Unsets the "SmallCircle" element
     */
    public void unsetSmallCircle()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SMALLCIRCLE$2, 0);
        }
    }
}
