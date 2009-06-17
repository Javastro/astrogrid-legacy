/*
 * An XML document type.
 * Localname: MJDTime
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.MJDTimeDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one MJDTime(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class MJDTimeDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.AbsoluteTimeDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.MJDTimeDocument
{
    
    public MJDTimeDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MJDTIME$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "MJDTime");
    
    
    /**
     * Gets the "MJDTime" element
     */
    public java.math.BigDecimal getMJDTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MJDTIME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getBigDecimalValue();
        }
    }
    
    /**
     * Gets (as xml) the "MJDTime" element
     */
    public org.apache.xmlbeans.XmlDecimal xgetMJDTime()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_element_user(MJDTIME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "MJDTime" element
     */
    public void setMJDTime(java.math.BigDecimal mjdTime)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MJDTIME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MJDTIME$0);
            }
            target.setBigDecimalValue(mjdTime);
        }
    }
    
    /**
     * Sets (as xml) the "MJDTime" element
     */
    public void xsetMJDTime(org.apache.xmlbeans.XmlDecimal mjdTime)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_element_user(MJDTIME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDecimal)get_store().add_element_user(MJDTIME$0);
            }
            target.set(mjdTime);
        }
    }
}
