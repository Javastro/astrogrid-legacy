/*
 * XML Type:  regionType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.RegionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML regionType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class RegionTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.region.v1_10.beans.RegionType
{
    
    public RegionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FILLFACTOR$0 = 
        new javax.xml.namespace.QName("", "fill_factor");
    private static final javax.xml.namespace.QName NOTE$2 = 
        new javax.xml.namespace.QName("", "note");
    
    
    /**
     * Gets the "fill_factor" attribute
     */
    public double getFillFactor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FILLFACTOR$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(FILLFACTOR$0);
            }
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "fill_factor" attribute
     */
    public org.astrogrid.stc.region.v1_10.beans.RegionType.FillFactor xgetFillFactor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType.FillFactor target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType.FillFactor)get_store().find_attribute_user(FILLFACTOR$0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.RegionType.FillFactor)get_default_attribute_value(FILLFACTOR$0);
            }
            return target;
        }
    }
    
    /**
     * True if has "fill_factor" attribute
     */
    public boolean isSetFillFactor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(FILLFACTOR$0) != null;
        }
    }
    
    /**
     * Sets the "fill_factor" attribute
     */
    public void setFillFactor(double fillFactor)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(FILLFACTOR$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(FILLFACTOR$0);
            }
            target.setDoubleValue(fillFactor);
        }
    }
    
    /**
     * Sets (as xml) the "fill_factor" attribute
     */
    public void xsetFillFactor(org.astrogrid.stc.region.v1_10.beans.RegionType.FillFactor fillFactor)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.RegionType.FillFactor target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.RegionType.FillFactor)get_store().find_attribute_user(FILLFACTOR$0);
            if (target == null)
            {
                target = (org.astrogrid.stc.region.v1_10.beans.RegionType.FillFactor)get_store().add_attribute_user(FILLFACTOR$0);
            }
            target.set(fillFactor);
        }
    }
    
    /**
     * Unsets the "fill_factor" attribute
     */
    public void unsetFillFactor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(FILLFACTOR$0);
        }
    }
    
    /**
     * Gets the "note" attribute
     */
    public java.lang.String getNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NOTE$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "note" attribute
     */
    public org.apache.xmlbeans.XmlString xgetNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NOTE$2);
            return target;
        }
    }
    
    /**
     * True if has "note" attribute
     */
    public boolean isSetNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(NOTE$2) != null;
        }
    }
    
    /**
     * Sets the "note" attribute
     */
    public void setNote(java.lang.String note)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NOTE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NOTE$2);
            }
            target.setStringValue(note);
        }
    }
    
    /**
     * Sets (as xml) the "note" attribute
     */
    public void xsetNote(org.apache.xmlbeans.XmlString note)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NOTE$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NOTE$2);
            }
            target.set(note);
        }
    }
    
    /**
     * Unsets the "note" attribute
     */
    public void unsetNote()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(NOTE$2);
        }
    }
    /**
     * An XML fill_factor(@).
     *
     * This is an atomic type that is a restriction of org.astrogrid.stc.region.v1_10.beans.RegionType$FillFactor.
     */
    public static class FillFactorImpl extends org.apache.xmlbeans.impl.values.JavaDoubleHolderEx implements org.astrogrid.stc.region.v1_10.beans.RegionType.FillFactor
    {
        
        public FillFactorImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected FillFactorImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
