/*
 * XML Type:  smallCircleType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.SmallCircleType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML smallCircleType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class SmallCircleTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.region.v1_10.beans.SmallCircleType
{
    
    public SmallCircleTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POLE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Pole");
    
    
    /**
     * Gets the "Pole" element
     */
    public java.util.List getPole()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POLE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Pole" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double2Type xgetPole()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().find_element_user(POLE$0, 0);
            return target;
        }
    }
    
    /**
     * True if has "Pole" element
     */
    public boolean isSetPole()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(POLE$0) != 0;
        }
    }
    
    /**
     * Sets the "Pole" element
     */
    public void setPole(java.util.List pole)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POLE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(POLE$0);
            }
            target.setListValue(pole);
        }
    }
    
    /**
     * Sets (as xml) the "Pole" element
     */
    public void xsetPole(org.astrogrid.stc.coords.v1_10.beans.Double2Type pole)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().find_element_user(POLE$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double2Type)get_store().add_element_user(POLE$0);
            }
            target.set(pole);
        }
    }
    
    /**
     * Unsets the "Pole" element
     */
    public void unsetPole()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(POLE$0, 0);
        }
    }
}
