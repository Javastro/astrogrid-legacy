/*
 * XML Type:  constraintType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.ConstraintType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML constraintType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class ConstraintTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.region.v1_10.beans.ConstraintType
{
    
    public ConstraintTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VECTOR$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Vector");
    private static final javax.xml.namespace.QName OFFSET$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Offset");
    
    
    /**
     * Gets the "Vector" element
     */
    public java.util.List getVector()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VECTOR$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Vector" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double3Type xgetVector()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().find_element_user(VECTOR$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Vector" element
     */
    public void setVector(java.util.List vector)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VECTOR$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VECTOR$0);
            }
            target.setListValue(vector);
        }
    }
    
    /**
     * Sets (as xml) the "Vector" element
     */
    public void xsetVector(org.astrogrid.stc.coords.v1_10.beans.Double3Type vector)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().find_element_user(VECTOR$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().add_element_user(VECTOR$0);
            }
            target.set(vector);
        }
    }
    
    /**
     * Gets the "Offset" element
     */
    public double getOffset()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(OFFSET$2, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "Offset" element
     */
    public org.apache.xmlbeans.XmlDouble xgetOffset()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(OFFSET$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Offset" element
     */
    public void setOffset(double offset)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(OFFSET$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(OFFSET$2);
            }
            target.setDoubleValue(offset);
        }
    }
    
    /**
     * Sets (as xml) the "Offset" element
     */
    public void xsetOffset(org.apache.xmlbeans.XmlDouble offset)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(OFFSET$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(OFFSET$2);
            }
            target.set(offset);
        }
    }
}
