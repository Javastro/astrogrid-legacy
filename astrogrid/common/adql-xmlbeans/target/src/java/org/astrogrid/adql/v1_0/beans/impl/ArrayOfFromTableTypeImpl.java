/*
 * XML Type:  ArrayOfFromTableType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML ArrayOfFromTableType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class ArrayOfFromTableTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType
{
    
    public ArrayOfFromTableTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FROMTABLETYPE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "fromTableType");
    
    
    /**
     * Gets array of all "fromTableType" elements
     */
    public org.astrogrid.adql.v1_0.beans.FromTableType[] getFromTableTypeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(FROMTABLETYPE$0, targetList);
            org.astrogrid.adql.v1_0.beans.FromTableType[] result = new org.astrogrid.adql.v1_0.beans.FromTableType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "fromTableType" element
     */
    public org.astrogrid.adql.v1_0.beans.FromTableType getFromTableTypeArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.FromTableType target = null;
            target = (org.astrogrid.adql.v1_0.beans.FromTableType)get_store().find_element_user(FROMTABLETYPE$0, i);
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
            org.astrogrid.adql.v1_0.beans.FromTableType target = null;
            target = (org.astrogrid.adql.v1_0.beans.FromTableType)get_store().find_element_user(FROMTABLETYPE$0, i);
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
    public void setFromTableTypeArray(org.astrogrid.adql.v1_0.beans.FromTableType[] fromTableTypeArray)
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
    public void setFromTableTypeArray(int i, org.astrogrid.adql.v1_0.beans.FromTableType fromTableType)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.FromTableType target = null;
            target = (org.astrogrid.adql.v1_0.beans.FromTableType)get_store().find_element_user(FROMTABLETYPE$0, i);
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
            org.astrogrid.adql.v1_0.beans.FromTableType target = null;
            target = (org.astrogrid.adql.v1_0.beans.FromTableType)get_store().find_element_user(FROMTABLETYPE$0, i);
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
    public org.astrogrid.adql.v1_0.beans.FromTableType insertNewFromTableType(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.FromTableType target = null;
            target = (org.astrogrid.adql.v1_0.beans.FromTableType)get_store().insert_element_user(FROMTABLETYPE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "fromTableType" element
     */
    public org.astrogrid.adql.v1_0.beans.FromTableType addNewFromTableType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.FromTableType target = null;
            target = (org.astrogrid.adql.v1_0.beans.FromTableType)get_store().add_element_user(FROMTABLETYPE$0);
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
