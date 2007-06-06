/*
 * XML Type:  binaryExprType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.BinaryExprType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML binaryExprType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class BinaryExprTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.v1_0.beans.BinaryExprType
{
    
    public BinaryExprTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ARG$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Arg");
    private static final javax.xml.namespace.QName OPER$2 = 
        new javax.xml.namespace.QName("", "Oper");
    
    
    /**
     * Gets array of all "Arg" elements
     */
    public org.astrogrid.adql.v1_0.beans.ScalarExpressionType[] getArgArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(ARG$0, targetList);
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType[] result = new org.astrogrid.adql.v1_0.beans.ScalarExpressionType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Arg" element
     */
    public org.astrogrid.adql.v1_0.beans.ScalarExpressionType getArgArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().find_element_user(ARG$0, i);
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
    public void setArgArray(org.astrogrid.adql.v1_0.beans.ScalarExpressionType[] argArray)
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
    public void setArgArray(int i, org.astrogrid.adql.v1_0.beans.ScalarExpressionType arg)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().find_element_user(ARG$0, i);
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
    public org.astrogrid.adql.v1_0.beans.ScalarExpressionType insertNewArg(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().insert_element_user(ARG$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Arg" element
     */
    public org.astrogrid.adql.v1_0.beans.ScalarExpressionType addNewArg()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ScalarExpressionType)get_store().add_element_user(ARG$0);
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
    public org.astrogrid.adql.v1_0.beans.BinaryOperatorType.Enum getOper()
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
            return (org.astrogrid.adql.v1_0.beans.BinaryOperatorType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Oper" attribute
     */
    public org.astrogrid.adql.v1_0.beans.BinaryOperatorType xgetOper()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.BinaryOperatorType target = null;
            target = (org.astrogrid.adql.v1_0.beans.BinaryOperatorType)get_store().find_attribute_user(OPER$2);
            return target;
        }
    }
    
    /**
     * Sets the "Oper" attribute
     */
    public void setOper(org.astrogrid.adql.v1_0.beans.BinaryOperatorType.Enum oper)
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
    public void xsetOper(org.astrogrid.adql.v1_0.beans.BinaryOperatorType oper)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.BinaryOperatorType target = null;
            target = (org.astrogrid.adql.v1_0.beans.BinaryOperatorType)get_store().find_attribute_user(OPER$2);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.BinaryOperatorType)get_store().add_attribute_user(OPER$2);
            }
            target.set(oper);
        }
    }
}
