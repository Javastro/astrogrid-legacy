/*
 * An XML document type.
 * Localname: Error2Matrix
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Error2MatrixDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Error2Matrix(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Error2MatrixDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CError2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Error2MatrixDocument
{
    
    public Error2MatrixDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ERROR2MATRIX$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error2Matrix");
    
    
    /**
     * Gets the "Error2Matrix" element
     */
    public java.util.List getError2Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR2MATRIX$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) the "Error2Matrix" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double4Type xgetError2Matrix()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double4Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().find_element_user(ERROR2MATRIX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Error2Matrix" element
     */
    public void setError2Matrix(java.util.List error2Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR2MATRIX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ERROR2MATRIX$0);
            }
            target.setListValue(error2Matrix);
        }
    }
    
    /**
     * Sets (as xml) the "Error2Matrix" element
     */
    public void xsetError2Matrix(org.astrogrid.stc.coords.v1_10.beans.Double4Type error2Matrix)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double4Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().find_element_user(ERROR2MATRIX$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Double4Type)get_store().add_element_user(ERROR2MATRIX$0);
            }
            target.set(error2Matrix);
        }
    }
}
