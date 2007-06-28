/*
 * XML Type:  ConstantListSet
 * Namespace: urn:astrogrid:schema:ADQL:v2.0
 * Java type: org.astrogrid.adql.beans.ConstantListSet
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML ConstantListSet(@urn:astrogrid:schema:ADQL:v2.0).
 *
 * This is a complex type.
 */
public class ConstantListSetImpl extends org.astrogrid.adql.beans.impl.InclusionSetTypeImpl implements org.astrogrid.adql.beans.ConstantListSet
{
    
    public ConstantListSetImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ITEM$0 = 
        new javax.xml.namespace.QName("urn:astrogrid:schema:ADQL:v2.0", "Item");
    
    
    /**
     * Gets array of all "Item" elements
     */
    public org.astrogrid.adql.beans.LiteralType[] getItemArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ITEM$0, targetList);
            org.astrogrid.adql.beans.LiteralType[] result = new org.astrogrid.adql.beans.LiteralType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Item" element
     */
    public org.astrogrid.adql.beans.LiteralType getItemArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.LiteralType target = null;
            target = (org.astrogrid.adql.beans.LiteralType)get_store().find_element_user(ITEM$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Item" element
     */
    public int sizeOfItemArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ITEM$0);
        }
    }
    
    /**
     * Sets array of all "Item" element
     */
    public void setItemArray(org.astrogrid.adql.beans.LiteralType[] itemArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(itemArray, ITEM$0);
        }
    }
    
    /**
     * Sets ith "Item" element
     */
    public void setItemArray(int i, org.astrogrid.adql.beans.LiteralType item)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.LiteralType target = null;
            target = (org.astrogrid.adql.beans.LiteralType)get_store().find_element_user(ITEM$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(item);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Item" element
     */
    public org.astrogrid.adql.beans.LiteralType insertNewItem(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.LiteralType target = null;
            target = (org.astrogrid.adql.beans.LiteralType)get_store().insert_element_user(ITEM$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Item" element
     */
    public org.astrogrid.adql.beans.LiteralType addNewItem()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.LiteralType target = null;
            target = (org.astrogrid.adql.beans.LiteralType)get_store().add_element_user(ITEM$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Item" element
     */
    public void removeItem(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ITEM$0, i);
        }
    }
}
