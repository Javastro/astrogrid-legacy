/*
 * XML Type:  intersectionSearchType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.IntersectionSearchType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML intersectionSearchType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class IntersectionSearchTypeImpl extends org.astrogrid.adql.beans.impl.SearchTypeImpl implements org.astrogrid.adql.beans.IntersectionSearchType
{
    
    public IntersectionSearchTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONDITION$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Condition");
    
    
    /**
     * Gets array of all "Condition" elements
     */
    public org.astrogrid.adql.beans.SearchType[] getConditionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CONDITION$0, targetList);
            org.astrogrid.adql.beans.SearchType[] result = new org.astrogrid.adql.beans.SearchType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Condition" element
     */
    public org.astrogrid.adql.beans.SearchType getConditionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SearchType target = null;
            target = (org.astrogrid.adql.beans.SearchType)get_store().find_element_user(CONDITION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Condition" element
     */
    public int sizeOfConditionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONDITION$0);
        }
    }
    
    /**
     * Sets array of all "Condition" element
     */
    public void setConditionArray(org.astrogrid.adql.beans.SearchType[] conditionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(conditionArray, CONDITION$0);
        }
    }
    
    /**
     * Sets ith "Condition" element
     */
    public void setConditionArray(int i, org.astrogrid.adql.beans.SearchType condition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SearchType target = null;
            target = (org.astrogrid.adql.beans.SearchType)get_store().find_element_user(CONDITION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(condition);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Condition" element
     */
    public org.astrogrid.adql.beans.SearchType insertNewCondition(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SearchType target = null;
            target = (org.astrogrid.adql.beans.SearchType)get_store().insert_element_user(CONDITION$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Condition" element
     */
    public org.astrogrid.adql.beans.SearchType addNewCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SearchType target = null;
            target = (org.astrogrid.adql.beans.SearchType)get_store().add_element_user(CONDITION$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Condition" element
     */
    public void removeCondition(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONDITION$0, i);
        }
    }
}
