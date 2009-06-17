/*
 * An XML document type.
 * Localname: TimeOrigin
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one TimeOrigin(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class TimeOriginDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.AbsoluteTimeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument
{
    
    public TimeOriginDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIMEORIGIN$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "TimeOrigin");
    
    
    /**
     * Gets the "TimeOrigin" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin.Enum getTimeOrigin()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIMEORIGIN$0, 0);
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "TimeOrigin" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin xgetTimeOrigin()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin)get_store().find_element_user(TIMEORIGIN$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "TimeOrigin" element
     */
    public void setTimeOrigin(org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin.Enum timeOrigin)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TIMEORIGIN$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TIMEORIGIN$0);
            }
            target.setEnumValue(timeOrigin);
        }
    }
    
    /**
     * Sets (as xml) the "TimeOrigin" element
     */
    public void xsetTimeOrigin(org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin timeOrigin)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin)get_store().find_element_user(TIMEORIGIN$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin)get_store().add_element_user(TIMEORIGIN$0);
            }
            target.set(timeOrigin);
        }
    }
    /**
     * An XML TimeOrigin(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is an atomic type that is a restriction of org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument$TimeOrigin.
     */
    public static class TimeOriginImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements org.astrogrid.stc.coords.v1_10.beans.TimeOriginDocument.TimeOrigin
    {
        
        public TimeOriginImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected TimeOriginImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
