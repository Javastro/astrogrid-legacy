/*
 * XML Type:  functionType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.FunctionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML functionType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class FunctionTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.v1_0.beans.FunctionType
{
    
    public FunctionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ALLOW$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Allow");
    private static final javax.xml.namespace.QName ARG$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Arg");
    
    
    /**
     * Gets the "Allow" element
     */
    public org.astrogrid.adql.v1_0.beans.SelectionOptionType getAllow()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectionOptionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectionOptionType)get_store().find_element_user(ALLOW$0, 0);
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
    public void setAllow(org.astrogrid.adql.v1_0.beans.SelectionOptionType allow)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectionOptionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectionOptionType)get_store().find_element_user(ALLOW$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.SelectionOptionType)get_store().add_element_user(ALLOW$0);
            }
            target.set(allow);
        }
    }
    
    /**
     * Appends and returns a new empty "Allow" element
     */
    public org.astrogrid.adql.v1_0.beans.SelectionOptionType addNewAllow()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectionOptionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectionOptionType)get_store().add_element_user(ALLOW$0);
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
    public org.astrogrid.adql.v1_0.beans.SelectionItemType[] getArgArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ARG$2, targetList);
            org.astrogrid.adql.v1_0.beans.SelectionItemType[] result = new org.astrogrid.adql.v1_0.beans.SelectionItemType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Arg" element
     */
    public org.astrogrid.adql.v1_0.beans.SelectionItemType getArgArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectionItemType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectionItemType)get_store().find_element_user(ARG$2, i);
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
    public void setArgArray(org.astrogrid.adql.v1_0.beans.SelectionItemType[] argArray)
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
    public void setArgArray(int i, org.astrogrid.adql.v1_0.beans.SelectionItemType arg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectionItemType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectionItemType)get_store().find_element_user(ARG$2, i);
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
    public org.astrogrid.adql.v1_0.beans.SelectionItemType insertNewArg(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectionItemType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectionItemType)get_store().insert_element_user(ARG$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Arg" element
     */
    public org.astrogrid.adql.v1_0.beans.SelectionItemType addNewArg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.SelectionItemType target = null;
            target = (org.astrogrid.adql.v1_0.beans.SelectionItemType)get_store().add_element_user(ARG$2);
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
