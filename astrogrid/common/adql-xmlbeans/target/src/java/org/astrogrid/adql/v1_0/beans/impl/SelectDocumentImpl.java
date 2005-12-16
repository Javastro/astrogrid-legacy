/*
 * An XML document type.
 * Localname: Select
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.SelectDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * A document containing one Select(@http://www.ivoa.net/xml/ADQL/v1.0) element.
 *
 * This is a complex type.
 */
public class SelectDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.v1_0.beans.SelectDocument
{
    
    public SelectDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SELECT$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Select");
    
    
    /**
     * Gets the "Select" element
     */
    public org.astrogrid.adql.v1_0.beans.SelectType getSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectType)get_store().find_element_user(SELECT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Select" element
     */
    public void setSelect(org.astrogrid.adql.v1_0.beans.SelectType select)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectType)get_store().find_element_user(SELECT$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.SelectType)get_store().add_element_user(SELECT$0);
            }
            target.set(select);
        }
    }
    
    /**
     * Appends and returns a new empty "Select" element
     */
    public org.astrogrid.adql.v1_0.beans.SelectType addNewSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectType)get_store().add_element_user(SELECT$0);
            return target;
        }
    }
}
