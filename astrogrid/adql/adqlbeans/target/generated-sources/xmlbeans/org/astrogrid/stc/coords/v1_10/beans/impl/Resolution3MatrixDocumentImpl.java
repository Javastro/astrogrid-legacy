/*
 * An XML document type.
 * Localname: Resolution3Matrix
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Resolution3MatrixDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Resolution3Matrix(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Resolution3MatrixDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CResolution3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Resolution3MatrixDocument
{
    
    public Resolution3MatrixDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESOLUTION3MATRIX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution3Matrix");
    
    
    /**
     * Gets the "Resolution3Matrix" element
     */
    public java.util.List getResolution3Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION3MATRIX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Resolution3Matrix" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double9Type xgetResolution3Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double9Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().find_element_user(RESOLUTION3MATRIX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Resolution3Matrix" element
     */
    public void setResolution3Matrix(java.util.List resolution3Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESOLUTION3MATRIX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RESOLUTION3MATRIX$0);
            }
            target.setListValue(resolution3Matrix);
        }
    }
    
    /**
     * Sets (as xml) the "Resolution3Matrix" element
     */
    public void xsetResolution3Matrix(org.astrogrid.stc.coords.v1_10.beans.Double9Type resolution3Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double9Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().find_element_user(RESOLUTION3MATRIX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().add_element_user(RESOLUTION3MATRIX$0);
            }
            target.set(resolution3Matrix);
        }
    }
}
