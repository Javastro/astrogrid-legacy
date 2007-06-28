/*
 * XML Type:  xMatchType
 * Namespace: urn:astrogrid:schema:ADQL:v2.0
 * Java type: org.astrogrid.adql.beans.XMatchType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML xMatchType(@urn:astrogrid:schema:ADQL:v2.0).
 *
 * This is a complex type.
 */
public class XMatchTypeImpl extends org.astrogrid.adql.beans.impl.SearchTypeImpl implements org.astrogrid.adql.beans.XMatchType
{
    
    public XMatchTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TABLE$0 = 
        new javax.xml.namespace.QName("urn:astrogrid:schema:ADQL:v2.0", "Table");
    private static final javax.xml.namespace.QName NATURE$2 = 
        new javax.xml.namespace.QName("urn:astrogrid:schema:ADQL:v2.0", "Nature");
    private static final javax.xml.namespace.QName SIGMA$4 = 
        new javax.xml.namespace.QName("urn:astrogrid:schema:ADQL:v2.0", "Sigma");
    
    
    /**
     * Gets array of all "Table" elements
     */
    public org.astrogrid.adql.beans.XMatchTableAliasType[] getTableArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(TABLE$0, targetList);
            org.astrogrid.adql.beans.XMatchTableAliasType[] result = new org.astrogrid.adql.beans.XMatchTableAliasType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Table" element
     */
    public org.astrogrid.adql.beans.XMatchTableAliasType getTableArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.XMatchTableAliasType target = null;
            target = (org.astrogrid.adql.beans.XMatchTableAliasType)get_store().find_element_user(TABLE$0, i);
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
    public void setTableArray(org.astrogrid.adql.beans.XMatchTableAliasType[] tableArray)
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
    public void setTableArray(int i, org.astrogrid.adql.beans.XMatchTableAliasType table)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.XMatchTableAliasType target = null;
            target = (org.astrogrid.adql.beans.XMatchTableAliasType)get_store().find_element_user(TABLE$0, i);
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
    public org.astrogrid.adql.beans.XMatchTableAliasType insertNewTable(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.XMatchTableAliasType target = null;
            target = (org.astrogrid.adql.beans.XMatchTableAliasType)get_store().insert_element_user(TABLE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Table" element
     */
    public org.astrogrid.adql.beans.XMatchTableAliasType addNewTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.XMatchTableAliasType target = null;
            target = (org.astrogrid.adql.beans.XMatchTableAliasType)get_store().add_element_user(TABLE$0);
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
    
    /**
     * Gets the "Nature" element
     */
    public org.astrogrid.adql.beans.ComparisonType.Enum getNature()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NATURE$2, 0);
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.adql.beans.ComparisonType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Nature" element
     */
    public org.astrogrid.adql.beans.ComparisonType xgetNature()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ComparisonType target = null;
            target = (org.astrogrid.adql.beans.ComparisonType)get_store().find_element_user(NATURE$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Nature" element
     */
    public void setNature(org.astrogrid.adql.beans.ComparisonType.Enum nature)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NATURE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NATURE$2);
            }
            target.setEnumValue(nature);
        }
    }
    
    /**
     * Sets (as xml) the "Nature" element
     */
    public void xsetNature(org.astrogrid.adql.beans.ComparisonType nature)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ComparisonType target = null;
            target = (org.astrogrid.adql.beans.ComparisonType)get_store().find_element_user(NATURE$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.ComparisonType)get_store().add_element_user(NATURE$2);
            }
            target.set(nature);
        }
    }
    
    /**
     * Gets the "Sigma" element
     */
    public org.astrogrid.adql.beans.NumberType getSigma()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.NumberType target = null;
            target = (org.astrogrid.adql.beans.NumberType)get_store().find_element_user(SIGMA$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Sigma" element
     */
    public void setSigma(org.astrogrid.adql.beans.NumberType sigma)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.NumberType target = null;
            target = (org.astrogrid.adql.beans.NumberType)get_store().find_element_user(SIGMA$4, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.NumberType)get_store().add_element_user(SIGMA$4);
            }
            target.set(sigma);
        }
    }
    
    /**
     * Appends and returns a new empty "Sigma" element
     */
    public org.astrogrid.adql.beans.NumberType addNewSigma()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.NumberType target = null;
            target = (org.astrogrid.adql.beans.NumberType)get_store().add_element_user(SIGMA$4);
            return target;
        }
    }
}
