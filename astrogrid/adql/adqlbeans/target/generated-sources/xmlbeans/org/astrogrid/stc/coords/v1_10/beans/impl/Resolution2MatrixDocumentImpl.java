/*
 * An XML document type.
 * Localname: Resolution2Matrix
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Resolution2MatrixDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Resolution2Matrix(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Resolution2MatrixDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CResolution2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Resolution2MatrixDocument
{
    
    public Resolution2MatrixDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESOLUTION2MATRIX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution2Matrix");
    
    
    /**
     * Gets the "Resolution2Matrix" element
     */
    public java.util.List getResolution2Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION2MATRIX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Resolution2Matrix" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double4Type xgetResolution2Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double4Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().find_element_user(RESOLUTION2MATRIX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Resolution2Matrix" element
     */
    public void setResolution2Matrix(java.util.List resolution2Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION2MATRIX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RESOLUTION2MATRIX$0);
            }
            target.setListValue(resolution2Matrix);
        }
    }
    
    /**
     * Sets (as xml) the "Resolution2Matrix" element
     */
    public void xsetResolution2Matrix(org.astrogrid.stc.coords.v1_10.beans.Double4Type resolution2Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double4Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().find_element_user(RESOLUTION2MATRIX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().add_element_user(RESOLUTION2MATRIX$0);
            }
            target.set(resolution2Matrix);
        }
    }
}
