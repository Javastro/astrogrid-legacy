/*
 * XML Type:  astronTimeType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.AstronTimeType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * An XML astronTimeType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public class AstronTimeTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.coords.v1_10.beans.AstronTimeType
{
    
    public AstronTimeTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIMESCALE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Timescale");
    private static final javax.xml.namespace.QName RELATIVETIME$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "RelativeTime");
    private static final org.apache.xmlbeans.QNameSet RELATIVETIME$3 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "TimeOffset"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "TimeOffsetRef"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "RelativeTime"),
    });
    private static final javax.xml.namespace.QName ABSOLUTETIME$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "AbsoluteTime");
    private static final org.apache.xmlbeans.QNameSet ABSOLUTETIME$5 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "AbsoluteTime"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "TimeOrigin"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "JDTime"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ISOTime"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "MJDTime"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ISOTimeRef"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "MJDTimeRef"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "JDTimeRef"),
    });
    
    
    /**
     * Gets the "Timescale" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeScaleType.Enum getTimescale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIMESCALE$0, 0);
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.stc.coords.v1_10.beans.TimeScaleType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Timescale" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeScaleType xgetTimescale()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeScaleType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeScaleType)get_store().find_element_user(TIMESCALE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Timescale" element
     */
    public void setTimescale(org.astrogrid.stc.coords.v1_10.beans.TimeScaleType.Enum timescale)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIMESCALE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TIMESCALE$0);
            }
            target.setEnumValue(timescale);
        }
    }
    
    /**
     * Sets (as xml) the "Timescale" element
     */
    public void xsetTimescale(org.astrogrid.stc.coords.v1_10.beans.TimeScaleType timescale)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeScaleType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeScaleType)get_store().find_element_user(TIMESCALE$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.TimeScaleType)get_store().add_element_user(TIMESCALE$0);
            }
            target.set(timescale);
        }
    }
    
    /**
     * Gets the "RelativeTime" element
     */
    public org.apache.xmlbeans.XmlObject getRelativeTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(RELATIVETIME$3, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "RelativeTime" element
     */
    public boolean isSetRelativeTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RELATIVETIME$3) != 0;
        }
    }
    
    /**
     * Sets the "RelativeTime" element
     */
    public void setRelativeTime(org.apache.xmlbeans.XmlObject relativeTime)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(RELATIVETIME$3, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(RELATIVETIME$2);
            }
            target.set(relativeTime);
        }
    }
    
    /**
     * Appends and returns a new empty "RelativeTime" element
     */
    public org.apache.xmlbeans.XmlObject addNewRelativeTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(RELATIVETIME$2);
            return target;
        }
    }
    
    /**
     * Unsets the "RelativeTime" element
     */
    public void unsetRelativeTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RELATIVETIME$3, 0);
        }
    }
    
    /**
     * Gets the "AbsoluteTime" element
     */
    public org.apache.xmlbeans.XmlObject getAbsoluteTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(ABSOLUTETIME$5, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "AbsoluteTime" element
     */
    public void setAbsoluteTime(org.apache.xmlbeans.XmlObject absoluteTime)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(ABSOLUTETIME$5, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(ABSOLUTETIME$4);
            }
            target.set(absoluteTime);
        }
    }
    
    /**
     * Appends and returns a new empty "AbsoluteTime" element
     */
    public org.apache.xmlbeans.XmlObject addNewAbsoluteTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(ABSOLUTETIME$4);
            return target;
        }
    }
}
