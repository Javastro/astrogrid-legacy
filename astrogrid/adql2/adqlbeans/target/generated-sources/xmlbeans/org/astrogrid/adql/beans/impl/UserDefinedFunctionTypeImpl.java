/*
 * XML Type:  userDefinedFunctionType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.UserDefinedFunctionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML userDefinedFunctionType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class UserDefinedFunctionTypeImpl extends org.astrogrid.adql.beans.impl.FunctionTypeImpl implements org.astrogrid.adql.beans.UserDefinedFunctionType
{
    
    public UserDefinedFunctionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ARG$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Arg");
    private static final javax.xml.namespace.QName NAME$2 = 
        new javax.xml.namespace.QName("", "Name");
    private static final javax.xml.namespace.QName CATALOG$4 = 
        new javax.xml.namespace.QName("", "Catalog");
    private static final javax.xml.namespace.QName SCHEMA$6 = 
        new javax.xml.namespace.QName("", "Schema");
    
    
    /**
     * Gets array of all "Arg" elements
     */
    public org.astrogrid.adql.beans.SelectionItemType[] getArgArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ARG$0, targetList);
            org.astrogrid.adql.beans.SelectionItemType[] result = new org.astrogrid.adql.beans.SelectionItemType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Arg" element
     */
    public org.astrogrid.adql.beans.SelectionItemType getArgArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionItemType target = null;
            target = (org.astrogrid.adql.beans.SelectionItemType)get_store().find_element_user(ARG$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Arg" element
     */
    public int sizeOfArgArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ARG$0);
        }
    }
    
    /**
     * Sets array of all "Arg" element
     */
    public void setArgArray(org.astrogrid.adql.beans.SelectionItemType[] argArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(argArray, ARG$0);
        }
    }
    
    /**
     * Sets ith "Arg" element
     */
    public void setArgArray(int i, org.astrogrid.adql.beans.SelectionItemType arg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionItemType target = null;
            target = (org.astrogrid.adql.beans.SelectionItemType)get_store().find_element_user(ARG$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(arg);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Arg" element
     */
    public org.astrogrid.adql.beans.SelectionItemType insertNewArg(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionItemType target = null;
            target = (org.astrogrid.adql.beans.SelectionItemType)get_store().insert_element_user(ARG$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Arg" element
     */
    public org.astrogrid.adql.beans.SelectionItemType addNewArg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionItemType target = null;
            target = (org.astrogrid.adql.beans.SelectionItemType)get_store().add_element_user(ARG$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Arg" element
     */
    public void removeArg(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ARG$0, i);
        }
    }
    
    /**
     * Gets the "Name" attribute
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Name" attribute
     */
    public org.apache.xmlbeans.XmlString xgetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$2);
            return target;
        }
    }
    
    /**
     * Sets the "Name" attribute
     */
    public void setName(java.lang.String name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NAME$2);
            }
            target.setStringValue(name);
        }
    }
    
    /**
     * Sets (as xml) the "Name" attribute
     */
    public void xsetName(org.apache.xmlbeans.XmlString name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(NAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(NAME$2);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "Catalog" attribute
     */
    public java.lang.String getCatalog()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CATALOG$4);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Catalog" attribute
     */
    public org.apache.xmlbeans.XmlString xgetCatalog()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CATALOG$4);
            return target;
        }
    }
    
    /**
     * True if has "Catalog" attribute
     */
    public boolean isSetCatalog()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(CATALOG$4) != null;
        }
    }
    
    /**
     * Sets the "Catalog" attribute
     */
    public void setCatalog(java.lang.String catalog)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(CATALOG$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(CATALOG$4);
            }
            target.setStringValue(catalog);
        }
    }
    
    /**
     * Sets (as xml) the "Catalog" attribute
     */
    public void xsetCatalog(org.apache.xmlbeans.XmlString catalog)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(CATALOG$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(CATALOG$4);
            }
            target.set(catalog);
        }
    }
    
    /**
     * Unsets the "Catalog" attribute
     */
    public void unsetCatalog()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(CATALOG$4);
        }
    }
    
    /**
     * Gets the "Schema" attribute
     */
    public java.lang.String getSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SCHEMA$6);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Schema" attribute
     */
    public org.apache.xmlbeans.XmlString xgetSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(SCHEMA$6);
            return target;
        }
    }
    
    /**
     * True if has "Schema" attribute
     */
    public boolean isSetSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(SCHEMA$6) != null;
        }
    }
    
    /**
     * Sets the "Schema" attribute
     */
    public void setSchema(java.lang.String schema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SCHEMA$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SCHEMA$6);
            }
            target.setStringValue(schema);
        }
    }
    
    /**
     * Sets (as xml) the "Schema" attribute
     */
    public void xsetSchema(org.apache.xmlbeans.XmlString schema)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(SCHEMA$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(SCHEMA$6);
            }
            target.set(schema);
        }
    }
    
    /**
     * Unsets the "Schema" attribute
     */
    public void unsetSchema()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(SCHEMA$6);
        }
    }
}
