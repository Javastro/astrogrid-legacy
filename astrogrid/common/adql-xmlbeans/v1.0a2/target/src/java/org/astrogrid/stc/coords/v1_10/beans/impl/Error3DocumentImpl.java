/*
 * An XML document type.
 * Localname: Error3
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Error3Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Error3(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Error3DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CError3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Error3Document
{
    
    public Error3DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ERROR3$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error3");
    
    
    /**
     * Gets the "Error3" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size3Type getError3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().find_element_user(ERROR3$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Error3" element
     */
    public void setError3(org.astrogrid.stc.coords.v1_10.beans.Size3Type error3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().find_element_user(ERROR3$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().add_element_user(ERROR3$0);
            }
            target.set(error3);
        }
    }
    
    /**
     * Appends and returns a new empty "Error3" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Size3Type addNewError3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Size3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Size3Type)get_store().add_element_user(ERROR3$0);
            return target;
        }
    }
}
