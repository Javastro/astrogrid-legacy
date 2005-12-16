/*
 * An XML document type.
 * Localname: Size3Matrix
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Size3MatrixDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Size3Matrix(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Size3MatrixDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CSize3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Size3MatrixDocument
{
    
    public Size3MatrixDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SIZE3MATRIX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size3Matrix");
    
    
    /**
     * Gets the "Size3Matrix" element
     */
    public java.util.List getSize3Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE3MATRIX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Size3Matrix" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double9Type xgetSize3Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double9Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().find_element_user(SIZE3MATRIX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Size3Matrix" element
     */
    public void setSize3Matrix(java.util.List size3Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE3MATRIX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SIZE3MATRIX$0);
            }
            target.setListValue(size3Matrix);
        }
    }
    
    /**
     * Sets (as xml) the "Size3Matrix" element
     */
    public void xsetSize3Matrix(org.astrogrid.stc.coords.v1_10.beans.Double9Type size3Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double9Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().find_element_user(SIZE3MATRIX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().add_element_user(SIZE3MATRIX$0);
            }
            target.set(size3Matrix);
        }
    }
}
