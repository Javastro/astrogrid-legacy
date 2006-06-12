/*
 * An XML document type.
 * Localname: Error3Matrix
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Error3MatrixDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Error3Matrix(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Error3MatrixDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CError3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Error3MatrixDocument
{
    
    public Error3MatrixDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ERROR3MATRIX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error3Matrix");
    
    
    /**
     * Gets the "Error3Matrix" element
     */
    public java.util.List getError3Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR3MATRIX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Error3Matrix" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double9Type xgetError3Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double9Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().find_element_user(ERROR3MATRIX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Error3Matrix" element
     */
    public void setError3Matrix(java.util.List error3Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR3MATRIX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ERROR3MATRIX$0);
            }
            target.setListValue(error3Matrix);
        }
    }
    
    /**
     * Sets (as xml) the "Error3Matrix" element
     */
    public void xsetError3Matrix(org.astrogrid.stc.coords.v1_10.beans.Double9Type error3Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double9Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().find_element_user(ERROR3MATRIX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double9Type)get_store().add_element_user(ERROR3MATRIX$0);
            }
            target.set(error3Matrix);
        }
    }
}
