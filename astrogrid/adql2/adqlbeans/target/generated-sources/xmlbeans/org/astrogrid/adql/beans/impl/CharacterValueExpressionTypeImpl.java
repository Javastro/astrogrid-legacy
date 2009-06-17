/*
 * XML Type:  characterValueExpressionType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.CharacterValueExpressionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML characterValueExpressionType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class CharacterValueExpressionTypeImpl extends org.astrogrid.adql.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.beans.CharacterValueExpressionType
{
    
    public CharacterValueExpressionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CHARACTERFACTOR$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "CharacterFactor");
    
    
    /**
     * Gets array of all "CharacterFactor" elements
     */
    public org.astrogrid.adql.beans.ScalarExpressionType[] getCharacterFactorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CHARACTERFACTOR$0, targetList);
            org.astrogrid.adql.beans.ScalarExpressionType[] result = new org.astrogrid.adql.beans.ScalarExpressionType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CharacterFactor" element
     */
    public org.astrogrid.adql.beans.ScalarExpressionType getCharacterFactorArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().find_element_user(CHARACTERFACTOR$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CharacterFactor" element
     */
    public int sizeOfCharacterFactorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CHARACTERFACTOR$0);
        }
    }
    
    /**
     * Sets array of all "CharacterFactor" element
     */
    public void setCharacterFactorArray(org.astrogrid.adql.beans.ScalarExpressionType[] characterFactorArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(characterFactorArray, CHARACTERFACTOR$0);
        }
    }
    
    /**
     * Sets ith "CharacterFactor" element
     */
    public void setCharacterFactorArray(int i, org.astrogrid.adql.beans.ScalarExpressionType characterFactor)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().find_element_user(CHARACTERFACTOR$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(characterFactor);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CharacterFactor" element
     */
    public org.astrogrid.adql.beans.ScalarExpressionType insertNewCharacterFactor(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().insert_element_user(CHARACTERFACTOR$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CharacterFactor" element
     */
    public org.astrogrid.adql.beans.ScalarExpressionType addNewCharacterFactor()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ScalarExpressionType target = null;
            target = (org.astrogrid.adql.beans.ScalarExpressionType)get_store().add_element_user(CHARACTERFACTOR$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "CharacterFactor" element
     */
    public void removeCharacterFactor(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CHARACTERFACTOR$0, i);
        }
    }
}
