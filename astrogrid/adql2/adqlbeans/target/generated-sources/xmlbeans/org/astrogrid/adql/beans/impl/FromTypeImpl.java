/*
 * XML Type:  fromType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.FromType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML fromType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class FromTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.FromType
{
    
    public FromTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TABLE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Table");
    
    
    /**
     * Gets array of all "Table" elements
     */
    public org.astrogrid.adql.beans.FromTableType[] getTableArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(TABLE$0, targetList);
            org.astrogrid.adql.beans.FromTableType[] result = new org.astrogrid.adql.beans.FromTableType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Table" element
     */
    public org.astrogrid.adql.beans.FromTableType getTableArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().find_element_user(TABLE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Table" element
     */
    public int sizeOfTableArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TABLE$0);
        }
    }
    
    /**
     * Sets array of all "Table" element
     */
    public void setTableArray(org.astrogrid.adql.beans.FromTableType[] tableArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(tableArray, TABLE$0);
        }
    }
    
    /**
     * Sets ith "Table" element
     */
    public void setTableArray(int i, org.astrogrid.adql.beans.FromTableType table)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().find_element_user(TABLE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(table);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Table" element
     */
    public org.astrogrid.adql.beans.FromTableType insertNewTable(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().insert_element_user(TABLE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Table" element
     */
    public org.astrogrid.adql.beans.FromTableType addNewTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromTableType target = null;
            target = (org.astrogrid.adql.beans.FromTableType)get_store().add_element_user(TABLE$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Table" element
     */
    public void removeTable(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TABLE$0, i);
        }
    }
}
