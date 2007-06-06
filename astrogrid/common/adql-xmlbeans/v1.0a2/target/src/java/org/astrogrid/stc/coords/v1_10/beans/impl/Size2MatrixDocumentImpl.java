/*
 * An XML document type.
 * Localname: Size2Matrix
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Size2MatrixDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Size2Matrix(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Size2MatrixDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CSize2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Size2MatrixDocument
{
    
    public Size2MatrixDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SIZE2MATRIX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size2Matrix");
    
    
    /**
     * Gets the "Size2Matrix" element
     */
    public java.util.List getSize2Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE2MATRIX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Size2Matrix" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double4Type xgetSize2Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double4Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().find_element_user(SIZE2MATRIX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Size2Matrix" element
     */
    public void setSize2Matrix(java.util.List size2Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIZE2MATRIX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SIZE2MATRIX$0);
            }
            target.setListValue(size2Matrix);
        }
    }
    
    /**
     * Sets (as xml) the "Size2Matrix" element
     */
    public void xsetSize2Matrix(org.astrogrid.stc.coords.v1_10.beans.Double4Type size2Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double4Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().find_element_user(SIZE2MATRIX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().add_element_user(SIZE2MATRIX$0);
            }
            target.set(size2Matrix);
        }
    }
}
