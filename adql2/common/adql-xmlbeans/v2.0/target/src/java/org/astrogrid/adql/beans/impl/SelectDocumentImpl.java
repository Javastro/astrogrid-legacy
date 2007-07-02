/*
 * An XML document type.
 * Localname: Select
 * Namespace: http://www.ivoa.net/xml/ADQL/v2.0/adql
 * Java type: org.astrogrid.adql.beans.SelectDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * A document containing one Select(@http://www.ivoa.net/xml/ADQL/v2.0/adql) element.
 *
 * This is a complex type.
 */
public class SelectDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.SelectDocument
{
    
    public SelectDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SELECT$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v2.0/adql", "Select");
    
    
    /**
     * Gets the "Select" element
     */
    public org.astrogrid.adql.beans.SelectType getSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectType target = null;
            target = (org.astrogrid.adql.beans.SelectType)get_store().find_element_user(SELECT$0, 0);
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
    public void setSelect(org.astrogrid.adql.beans.SelectType select)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectType target = null;
            target = (org.astrogrid.adql.beans.SelectType)get_store().find_element_user(SELECT$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SelectType)get_store().add_element_user(SELECT$0);
            }
            target.set(select);
        }
    }
    
    /**
     * Appends and returns a new empty "Select" element
     */
    public org.astrogrid.adql.beans.SelectType addNewSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectType target = null;
            target = (org.astrogrid.adql.beans.SelectType)get_store().add_element_user(SELECT$0);
            return target;
        }
    }
}
