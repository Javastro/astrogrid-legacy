/*
 * An XML document type.
 * Localname: Error2
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Error2Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Error2(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Error2DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CError2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Error2Document
{
    
    public Error2DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ERROR2$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error2");
    
    
    /**
     * Gets the "Error2" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size2Type getError2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().find_element_user(ERROR2$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Error2" element
     */
    public void setError2(org.astrogrid.stc.coords.v1_10.beans.Size2Type error2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().find_element_user(ERROR2$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().add_element_user(ERROR2$0);
            }
            target.set(error2);
        }
    }
    
    /**
     * Appends and returns a new empty "Error2" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size2Type addNewError2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size2Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size2Type)get_store().add_element_user(ERROR2$0);
            return target;
        }
    }
}
