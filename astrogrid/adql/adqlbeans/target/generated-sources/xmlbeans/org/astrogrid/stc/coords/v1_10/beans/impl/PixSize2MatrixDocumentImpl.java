/*
 * An XML document type.
 * Localname: PixSize2Matrix
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixSize2MatrixDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixSize2Matrix(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixSize2MatrixDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CPixSize2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixSize2MatrixDocument
{
    
    public PixSize2MatrixDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXSIZE2MATRIX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize2Matrix");
    
    
    /**
     * Gets the "PixSize2Matrix" element
     */
    public java.util.List getPixSize2Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE2MATRIX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "PixSize2Matrix" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double4Type xgetPixSize2Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double4Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().find_element_user(PIXSIZE2MATRIX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "PixSize2Matrix" element
     */
    public void setPixSize2Matrix(java.util.List pixSize2Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE2MATRIX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PIXSIZE2MATRIX$0);
            }
            target.setListValue(pixSize2Matrix);
        }
    }
    
    /**
     * Sets (as xml) the "PixSize2Matrix" element
     */
    public void xsetPixSize2Matrix(org.astrogrid.stc.coords.v1_10.beans.Double4Type pixSize2Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double4Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().find_element_user(PIXSIZE2MATRIX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().add_element_user(PIXSIZE2MATRIX$0);
            }
            target.set(pixSize2Matrix);
        }
    }
}
