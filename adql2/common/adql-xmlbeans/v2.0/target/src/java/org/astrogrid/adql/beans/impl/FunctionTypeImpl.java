/*
 * XML Type:  functionType
 * Namespace: urn:astrogrid:schema:ADQL:v2.0
 * Java type: org.astrogrid.adql.beans.FunctionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML functionType(@urn:astrogrid:schema:ADQL:v2.0).
 *
 * This is a complex type.
 */
public class FunctionTypeImpl extends org.astrogrid.adql.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.beans.FunctionType
{
    
    public FunctionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ALLOW$0 = 
        new javax.xml.namespace.QName("urn:astrogrid:schema:ADQL:v2.0", "Allow");
    private static final javax.xml.namespace.QName ARG$2 = 
        new javax.xml.namespace.QName("urn:astrogrid:schema:ADQL:v2.0", "Arg");
    
    
    /**
     * Gets the "Allow" element
     */
    public org.astrogrid.adql.beans.SelectionOptionType getAllow()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionOptionType target = null;
            target = (org.astrogrid.adql.beans.SelectionOptionType)get_store().find_element_user(ALLOW$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Allow" element
     */
    public boolean isSetAllow()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ALLOW$0) != 0;
        }
    }
    
    /**
     * Sets the "Allow" element
     */
    public void setAllow(org.astrogrid.adql.beans.SelectionOptionType allow)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionOptionType target = null;
            target = (org.astrogrid.adql.beans.SelectionOptionType)get_store().find_element_user(ALLOW$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SelectionOptionType)get_store().add_element_user(ALLOW$0);
            }
            target.set(allow);
        }
    }
    
    /**
     * Appends and returns a new empty "Allow" element
     */
    public org.astrogrid.adql.beans.SelectionOptionType addNewAllow()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionOptionType target = null;
            target = (org.astrogrid.adql.beans.SelectionOptionType)get_store().add_element_user(ALLOW$0);
            return target;
        }
    }
    
    /**
     * Unsets the "Allow" element
     */
    public void unsetAllow()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ALLOW$0, 0);
        }
    }
    
    /**
     * Gets array of all "Arg" elements
     */
    public org.astrogrid.adql.beans.SelectionItemType[] getArgArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ARG$2, targetList);
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
            target = (org.astrogrid.adql.beans.SelectionItemType)get_store().find_element_user(ARG$2, i);
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
            return get_store().count_elements(ARG$2);
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
            arraySetterHelper(argArray, ARG$2);
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
            target = (org.astrogrid.adql.beans.SelectionItemType)get_store().find_element_user(ARG$2, i);
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
            target = (org.astrogrid.adql.beans.SelectionItemType)get_store().insert_element_user(ARG$2, i);
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
            target = (org.astrogrid.adql.beans.SelectionItemType)get_store().add_element_user(ARG$2);
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
            get_store().remove_element(ARG$2, i);
        }
    }
}
