/*
 * XML Type:  columnNameListType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.ColumnNameListType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML columnNameListType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class ColumnNameListTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.ColumnNameListType
{
    
    public ColumnNameListTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COLUMNNAME$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "ColumnName");
    
    
    /**
     * Gets array of all "ColumnName" elements
     */
    public java.lang.String[] getColumnNameArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(COLUMNNAME$0, targetList);
            java.lang.String[] result = new java.lang.String[targetList.size()];
            for (int i = 0, len = targetList.size() ; i < len ; i++)
                result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();
            return result;
        }
    }
    
    /**
     * Gets ith "ColumnName" element
     */
    public java.lang.String getColumnNameArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COLUMNNAME$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) array of all "ColumnName" elements
     */
    public org.apache.xmlbeans.XmlString[] xgetColumnNameArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(COLUMNNAME$0, targetList);
            org.apache.xmlbeans.XmlString[] result = new org.apache.xmlbeans.XmlString[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets (as xml) ith "ColumnName" element
     */
    public org.apache.xmlbeans.XmlString xgetColumnNameArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COLUMNNAME$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return (org.apache.xmlbeans.XmlString)target;
        }
    }
    
    /**
     * Returns number of "ColumnName" element
     */
    public int sizeOfColumnNameArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COLUMNNAME$0);
        }
    }
    
    /**
     * Sets array of all "ColumnName" element
     */
    public void setColumnNameArray(java.lang.String[] columnNameArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(columnNameArray, COLUMNNAME$0);
        }
    }
    
    /**
     * Sets ith "ColumnName" element
     */
    public void setColumnNameArray(int i, java.lang.String columnName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COLUMNNAME$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(columnName);
        }
    }
    
    /**
     * Sets (as xml) array of all "ColumnName" element
     */
    public void xsetColumnNameArray(org.apache.xmlbeans.XmlString[]columnNameArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(columnNameArray, COLUMNNAME$0);
        }
    }
    
    /**
     * Sets (as xml) ith "ColumnName" element
     */
    public void xsetColumnNameArray(int i, org.apache.xmlbeans.XmlString columnName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(COLUMNNAME$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(columnName);
        }
    }
    
    /**
     * Inserts the value as the ith "ColumnName" element
     */
    public void insertColumnName(int i, java.lang.String columnName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = 
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(COLUMNNAME$0, i);
            target.setStringValue(columnName);
        }
    }
    
    /**
     * Appends the value as the last "ColumnName" element
     */
    public void addColumnName(java.lang.String columnName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(COLUMNNAME$0);
            target.setStringValue(columnName);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "ColumnName" element
     */
    public org.apache.xmlbeans.XmlString insertNewColumnName(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(COLUMNNAME$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "ColumnName" element
     */
    public org.apache.xmlbeans.XmlString addNewColumnName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(COLUMNNAME$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "ColumnName" element
     */
    public void removeColumnName(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COLUMNNAME$0, i);
        }
    }
}
