/*
 * An XML document type.
 * Localname: TimeOffset
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one TimeOffset(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class TimeOffsetDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.RelativeTimeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument
{
    
    public TimeOffsetDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIMEOFFSET$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "TimeOffset");
    
    
    /**
     * Gets the "TimeOffset" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset getTimeOffset()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset)get_store().find_element_user(TIMEOFFSET$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "TimeOffset" element
     */
    public void setTimeOffset(org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset timeOffset)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset)get_store().find_element_user(TIMEOFFSET$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset)get_store().add_element_user(TIMEOFFSET$0);
            }
            target.set(timeOffset);
        }
    }
    
    /**
     * Appends and returns a new empty "TimeOffset" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset addNewTimeOffset()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset)get_store().add_element_user(TIMEOFFSET$0);
            return target;
        }
    }
    /**
     * An XML TimeOffset(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is an atomic type that is a restriction of org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument$TimeOffset.
     */
    public static class TimeOffsetImpl extends org.apache.xmlbeans.impl.values.JavaDecimalHolderEx implements org.astrogrid.stc.coords.v1_10.beans.TimeOffsetDocument.TimeOffset
    {
        
        public TimeOffsetImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, true);
        }
        
        protected TimeOffsetImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
        
        private static final javax.xml.namespace.QName UNIT$0 = 
            new javax.xml.namespace.QName("", "unit");
        
        
        /**
         * Gets the "unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.TimeUnitType.Enum getUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(UNIT$0);
                }
                if (target == null)
                {
                    return null;
                }
                return (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.TimeUnitType xgetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.TimeUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType)get_default_attribute_value(UNIT$0);
                }
                return target;
            }
        }
        
        /**
         * True if has "unit" attribute
         */
        public boolean isSetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(UNIT$0) != null;
            }
        }
        
        /**
         * Sets the "unit" attribute
         */
        public void setUnit(org.astrogrid.stc.coords.v1_10.beans.TimeUnitType.Enum unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(UNIT$0);
                }
                target.setEnumValue(unit);
            }
        }
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        public void xsetUnit(org.astrogrid.stc.coords.v1_10.beans.TimeUnitType unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.TimeUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType)get_store().add_attribute_user(UNIT$0);
                }
                target.set(unit);
            }
        }
        
        /**
         * Unsets the "unit" attribute
         */
        public void unsetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(UNIT$0);
            }
        }
    }
}
