/*
 * XML Type:  ArrayOfFromTableType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.ArrayOfFromTableType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML ArrayOfFromTableType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class ArrayOfFromTableTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.ArrayOfFromTableType
{
    
    public ArrayOfFromTableTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FROMTABLETYPE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "fromTableType");
    
    
    /**
     * Gets array of all "fromTableType" elements
     */
    public org.astrogrid.adql.beans.FromTableType[] getFromTableTypeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(FROMTABLETYPE$0, targetList);
            org.astrogrid.adql.beans.FromTableType[] result = new org.astrogrid.adql.beans.FromTableType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "fromTableType" element
     */
    public org.astrogrid.adql.beans.FromTableType getFromTableTypeArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().find_element_user(FROMTABLETYPE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Tests for nil ith "fromTableType" element
     */
    public boolean isNilFromTableTypeArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().find_element_user(FROMTABLETYPE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.isNil();
        }
    }
    
    /**
     * Returns number of "fromTableType" element
     */
    public int sizeOfFromTableTypeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FROMTABLETYPE$0);
        }
    }
    
    /**
     * Sets array of all "fromTableType" element
     */
    public void setFromTableTypeArray(org.astrogrid.adql.beans.FromTableType[] fromTableTypeArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(fromTableTypeArray, FROMTABLETYPE$0);
        }
    }
    
    /**
     * Sets ith "fromTableType" element
     */
    public void setFromTableTypeArray(int i, org.astrogrid.adql.beans.FromTableType fromTableType)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().find_element_user(FROMTABLETYPE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(fromTableType);
        }
    }
    
    /**
     * Nils the ith "fromTableType" element
     */
    public void setNilFromTableTypeArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().find_element_user(FROMTABLETYPE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setNil();
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "fromTableType" element
     */
    public org.astrogrid.adql.beans.FromTableType insertNewFromTableType(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().insert_element_user(FROMTABLETYPE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "fromTableType" element
     */
    public org.astrogrid.adql.beans.FromTableType addNewFromTableType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().add_element_user(FROMTABLETYPE$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "fromTableType" element
     */
    public void removeFromTableType(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FROMTABLETYPE$0, i);
        }
    }
}
