/*
 * XML Type:  convexType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.ConvexType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML convexType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class ConvexTypeImpl extends org.astrogrid.stc.region.v1_10.beans.impl.ShapeTypeImpl implements org.astrogrid.stc.region.v1_10.beans.ConvexType
{
    
    public ConvexTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CONSTRAINT$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Constraint");
    
    
    /**
     * Gets array of all "Constraint" elements
     */
    public org.astrogrid.stc.region.v1_10.beans.ConstraintType[] getConstraintArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CONSTRAINT$0, targetList);
            org.astrogrid.stc.region.v1_10.beans.ConstraintType[] result = new org.astrogrid.stc.region.v1_10.beans.ConstraintType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Constraint" element
     */
    public org.astrogrid.stc.region.v1_10.beans.ConstraintType getConstraintArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConstraintType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConstraintType)get_store().find_element_user(CONSTRAINT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Constraint" element
     */
    public int sizeOfConstraintArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CONSTRAINT$0);
        }
    }
    
    /**
     * Sets array of all "Constraint" element
     */
    public void setConstraintArray(org.astrogrid.stc.region.v1_10.beans.ConstraintType[] constraintArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(constraintArray, CONSTRAINT$0);
        }
    }
    
    /**
     * Sets ith "Constraint" element
     */
    public void setConstraintArray(int i, org.astrogrid.stc.region.v1_10.beans.ConstraintType constraint)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConstraintType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConstraintType)get_store().find_element_user(CONSTRAINT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(constraint);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Constraint" element
     */
    public org.astrogrid.stc.region.v1_10.beans.ConstraintType insertNewConstraint(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConstraintType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConstraintType)get_store().insert_element_user(CONSTRAINT$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Constraint" element
     */
    public org.astrogrid.stc.region.v1_10.beans.ConstraintType addNewConstraint()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.region.v1_10.beans.ConstraintType target = null;
            target = (org.astrogrid.stc.region.v1_10.beans.ConstraintType)get_store().add_element_user(CONSTRAINT$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Constraint" element
     */
    public void removeConstraint(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CONSTRAINT$0, i);
        }
    }
}
