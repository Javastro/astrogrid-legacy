/*
 * XML Type:  groupByType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.GroupByType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML groupByType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class GroupByTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.v1_0.beans.GroupByType
{
    
    public GroupByTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COLUMN$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Column");
    
    
    /**
     * Gets array of all "Column" elements
     */
    public org.astrogrid.adql.v1_0.beans.ColumnReferenceType[] getColumnArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(COLUMN$0, targetList);
            org.astrogrid.adql.v1_0.beans.ColumnReferenceType[] result = new org.astrogrid.adql.v1_0.beans.ColumnReferenceType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Column" element
     */
    public org.astrogrid.adql.v1_0.beans.ColumnReferenceType getColumnArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ColumnReferenceType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ColumnReferenceType)get_store().find_element_user(COLUMN$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Column" element
     */
    public int sizeOfColumnArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COLUMN$0);
        }
    }
    
    /**
     * Sets array of all "Column" element
     */
    public void setColumnArray(org.astrogrid.adql.v1_0.beans.ColumnReferenceType[] columnArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(columnArray, COLUMN$0);
        }
    }
    
    /**
     * Sets ith "Column" element
     */
    public void setColumnArray(int i, org.astrogrid.adql.v1_0.beans.ColumnReferenceType column)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ColumnReferenceType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ColumnReferenceType)get_store().find_element_user(COLUMN$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(column);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Column" element
     */
    public org.astrogrid.adql.v1_0.beans.ColumnReferenceType insertNewColumn(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ColumnReferenceType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ColumnReferenceType)get_store().insert_element_user(COLUMN$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Column" element
     */
    public org.astrogrid.adql.v1_0.beans.ColumnReferenceType addNewColumn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ColumnReferenceType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ColumnReferenceType)get_store().add_element_user(COLUMN$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Column" element
     */
    public void removeColumn(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COLUMN$0, i);
        }
    }
}
