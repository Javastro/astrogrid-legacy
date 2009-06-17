/*
 * XML Type:  binaryExprType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.BinaryExprType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML binaryExprType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class BinaryExprTypeImpl extends org.astrogrid.adql.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.beans.BinaryExprType
{
    
    public BinaryExprTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ARG$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Arg");
    private static final javax.xml.namespace.QName OPER$2 = 
        new javax.xml.namespace.QName("", "Oper");
    
    
    /**
     * Gets array of all "Arg" elements
     */
    public org.astrogrid.adql.beans.ScalarExpressionType[] getArgArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ARG$0, targetList);
            org.astrogrid.adql.beans.ScalarExpressionType[] result = new org.astrogrid.adql.beans.ScalarExpressionType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Arg" element
     */
    public org.astrogrid.adql.beans.ScalarExpressionType getArgArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().find_element_user(ARG$0, i);
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
            return get_store().count_elements(ARG$0);
        }
    }
    
    /**
     * Sets array of all "Arg" element
     */
    public void setArgArray(org.astrogrid.adql.beans.ScalarExpressionType[] argArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(argArray, ARG$0);
        }
    }
    
    /**
     * Sets ith "Arg" element
     */
    public void setArgArray(int i, org.astrogrid.adql.beans.ScalarExpressionType arg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().find_element_user(ARG$0, i);
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
    public org.astrogrid.adql.beans.ScalarExpressionType insertNewArg(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().insert_element_user(ARG$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Arg" element
     */
    public org.astrogrid.adql.beans.ScalarExpressionType addNewArg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().add_element_user(ARG$0);
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
            get_store().remove_element(ARG$0, i);
        }
    }
    
    /**
     * Gets the "Oper" attribute
     */
    public org.astrogrid.adql.beans.BinaryOperatorType.Enum getOper()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPER$2);
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.adql.beans.BinaryOperatorType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Oper" attribute
     */
    public org.astrogrid.adql.beans.BinaryOperatorType xgetOper()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.BinaryOperatorType target = null;
            target = (org.astrogrid.adql.beans.BinaryOperatorType)get_store().find_attribute_user(OPER$2);
            return target;
        }
    }
    
    /**
     * Sets the "Oper" attribute
     */
    public void setOper(org.astrogrid.adql.beans.BinaryOperatorType.Enum oper)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(OPER$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(OPER$2);
            }
            target.setEnumValue(oper);
        }
    }
    
    /**
     * Sets (as xml) the "Oper" attribute
     */
    public void xsetOper(org.astrogrid.adql.beans.BinaryOperatorType oper)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.BinaryOperatorType target = null;
            target = (org.astrogrid.adql.beans.BinaryOperatorType)get_store().find_attribute_user(OPER$2);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.BinaryOperatorType)get_store().add_attribute_user(OPER$2);
            }
            target.set(oper);
        }
    }
}
