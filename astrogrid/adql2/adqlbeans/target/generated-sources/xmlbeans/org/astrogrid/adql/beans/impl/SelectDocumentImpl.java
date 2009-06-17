/*
 * An XML document type.
 * Localname: Select
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.SelectDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * A document containing one Select(@http://www.ivoa.net/xml/v2.0/adql) element.
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
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Select");
    
    
    /**
     * Gets the "Select" element
     */
    public org.astrogrid.adql.beans.SelectDocument.Select getSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectDocument.Select target = null;
            target = (org.astrogrid.adql.beans.SelectDocument.Select)get_store().find_element_user(SELECT$0, 0);
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
    public void setSelect(org.astrogrid.adql.beans.SelectDocument.Select select)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectDocument.Select target = null;
            target = (org.astrogrid.adql.beans.SelectDocument.Select)get_store().find_element_user(SELECT$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SelectDocument.Select)get_store().add_element_user(SELECT$0);
            }
            target.set(select);
        }
    }
    
    /**
     * Appends and returns a new empty "Select" element
     */
    public org.astrogrid.adql.beans.SelectDocument.Select addNewSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectDocument.Select target = null;
            target = (org.astrogrid.adql.beans.SelectDocument.Select)get_store().add_element_user(SELECT$0);
            return target;
        }
    }
    /**
     * An XML Select(@http://www.ivoa.net/xml/v2.0/adql).
     *
     * This is a complex type.
     */
    public static class SelectImpl extends org.astrogrid.adql.beans.impl.SelectTypeImpl implements org.astrogrid.adql.beans.SelectDocument.Select
    {
        
        public SelectImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName SCHEMAVERSION$0 = 
            new javax.xml.namespace.QName("", "schemaVersion");
        
        
        /**
         * Gets the "schemaVersion" attribute
         */
        public java.math.BigDecimal getSchemaVersion()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SCHEMAVERSION$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(SCHEMAVERSION$0);
                }
                if (target == null)
                {
                    return null;
                }
                return target.getBigDecimalValue();
            }
        }
        
        /**
         * Gets (as xml) the "schemaVersion" attribute
         */
        public org.astrogrid.adql.beans.SchemaVersionType xgetSchemaVersion()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.adql.beans.SchemaVersionType target = null;
                target = (org.astrogrid.adql.beans.SchemaVersionType)get_store().find_attribute_user(SCHEMAVERSION$0);
                if (target == null)
                {
                    target = (org.astrogrid.adql.beans.SchemaVersionType)get_default_attribute_value(SCHEMAVERSION$0);
                }
                return target;
            }
        }
        
        /**
         * True if has "schemaVersion" attribute
         */
        public boolean isSetSchemaVersion()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(SCHEMAVERSION$0) != null;
            }
        }
        
        /**
         * Sets the "schemaVersion" attribute
         */
        public void setSchemaVersion(java.math.BigDecimal schemaVersion)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SCHEMAVERSION$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SCHEMAVERSION$0);
                }
                target.setBigDecimalValue(schemaVersion);
            }
        }
        
        /**
         * Sets (as xml) the "schemaVersion" attribute
         */
        public void xsetSchemaVersion(org.astrogrid.adql.beans.SchemaVersionType schemaVersion)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.adql.beans.SchemaVersionType target = null;
                target = (org.astrogrid.adql.beans.SchemaVersionType)get_store().find_attribute_user(SCHEMAVERSION$0);
                if (target == null)
                {
                    target = (org.astrogrid.adql.beans.SchemaVersionType)get_store().add_attribute_user(SCHEMAVERSION$0);
                }
                target.set(schemaVersion);
            }
        }
        
        /**
         * Unsets the "schemaVersion" attribute
         */
        public void unsetSchemaVersion()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(SCHEMAVERSION$0);
            }
        }
    }
}
