/*
 * An XML document type.
 * Localname: TimeOffsetRef
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one TimeOffsetRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class TimeOffsetRefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.RelativeTimeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument
{
    
    public TimeOffsetRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIMEOFFSETREF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "TimeOffsetRef");
    
    
    /**
     * Gets the "TimeOffsetRef" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef getTimeOffsetRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef)get_store().find_element_user(TIMEOFFSETREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "TimeOffsetRef" element
     */
    public void setTimeOffsetRef(org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef timeOffsetRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef)get_store().find_element_user(TIMEOFFSETREF$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef)get_store().add_element_user(TIMEOFFSETREF$0);
            }
            target.set(timeOffsetRef);
        }
    }
    
    /**
     * Appends and returns a new empty "TimeOffsetRef" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef addNewTimeOffsetRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef)get_store().add_element_user(TIMEOFFSETREF$0);
            return target;
        }
    }
    /**
     * An XML TimeOffsetRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is an atomic type that is a restriction of org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument$TimeOffsetRef.
     */
    public static class TimeOffsetRefImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements org.astrogrid.stc.coords.v1_10.beans.TimeOffsetRefDocument.TimeOffsetRef
    {
        
        public TimeOffsetRefImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, true);
        }
        
        protected TimeOffsetRefImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
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
