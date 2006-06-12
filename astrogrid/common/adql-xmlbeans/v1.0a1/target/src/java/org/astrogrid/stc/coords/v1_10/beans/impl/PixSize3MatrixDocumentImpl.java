/*
 * An XML document type.
 * Localname: PixSize3Matrix
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixSize3MatrixDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixSize3Matrix(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixSize3MatrixDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CPixSize3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixSize3MatrixDocument
{
    
    public PixSize3MatrixDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXSIZE3MATRIX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize3Matrix");
    
    
    /**
     * Gets the "PixSize3Matrix" element
     */
    public java.util.List getPixSize3Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE3MATRIX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "PixSize3Matrix" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double9Type xgetPixSize3Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double9Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().find_element_user(PIXSIZE3MATRIX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "PixSize3Matrix" element
     */
    public void setPixSize3Matrix(java.util.List pixSize3Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PIXSIZE3MATRIX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PIXSIZE3MATRIX$0);
            }
            target.setListValue(pixSize3Matrix);
        }
    }
    
    /**
     * Sets (as xml) the "PixSize3Matrix" element
     */
    public void xsetPixSize3Matrix(org.astrogrid.stc.coords.v1_10.beans.Double9Type pixSize3Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double9Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().find_element_user(PIXSIZE3MATRIX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().add_element_user(PIXSIZE3MATRIX$0);
            }
            target.set(pixSize3Matrix);
        }
    }
}
