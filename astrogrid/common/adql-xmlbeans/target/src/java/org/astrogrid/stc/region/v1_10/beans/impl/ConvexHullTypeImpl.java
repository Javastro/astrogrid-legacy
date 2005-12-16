/*
 * XML Type:  convexHullType
 * Namespace: http://www.ivoa.net/xml/STC/STCregion/v1.10
 * Java type: org.astrogrid.stc.region.v1_10.beans.ConvexHullType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.region.v1_10.beans.impl;
/**
 * An XML convexHullType(@http://www.ivoa.net/xml/STC/STCregion/v1.10).
 *
 * This is a complex type.
 */
public class ConvexHullTypeImpl extends org.astrogrid.stc.region.v1_10.beans.impl.ShapeTypeImpl implements org.astrogrid.stc.region.v1_10.beans.ConvexHullType
{
    
    public ConvexHullTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POINT$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCregion/v1.10", "Point");
    
    
    /**
     * Gets array of all "Point" elements
     */
    public java.util.List[] getPointArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(POINT$0, targetList);
            java.util.List[] result = new java.util.List[targetList.size()];
            for (int i = 0, len = targetList.size() ; i < len ; i++)
                result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getListValue();
            return result;
        }
    }
    
    /**
     * Gets ith "Point" element
     */
    public java.util.List getPointArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POINT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.getListValue();
        }
    }
    
    /**
     * Gets (as xml) array of all "Point" elements
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double3Type[] xgetPointArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(POINT$0, targetList);
            org.astrogrid.stc.coords.v1_10.beans.Double3Type[] result = new org.astrogrid.stc.coords.v1_10.beans.Double3Type[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets (as xml) ith "Point" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double3Type xgetPointArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().find_element_user(POINT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return (org.astrogrid.stc.coords.v1_10.beans.Double3Type)target;
        }
    }
    
    /**
     * Returns number of "Point" element
     */
    public int sizeOfPointArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(POINT$0);
        }
    }
    
    /**
     * Sets array of all "Point" element
     */
    public void setPointArray(java.util.List[] pointArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(pointArray, POINT$0);
        }
    }
    
    /**
     * Sets ith "Point" element
     */
    public void setPointArray(int i, java.util.List point)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POINT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setListValue(point);
        }
    }
    
    /**
     * Sets (as xml) array of all "Point" element
     */
    public void xsetPointArray(org.astrogrid.stc.coords.v1_10.beans.Double3Type[]pointArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(pointArray, POINT$0);
        }
    }
    
    /**
     * Sets (as xml) ith "Point" element
     */
    public void xsetPointArray(int i, org.astrogrid.stc.coords.v1_10.beans.Double3Type point)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().find_element_user(POINT$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(point);
        }
    }
    
    /**
     * Inserts the value as the ith "Point" element
     */
    public void insertPoint(int i, java.util.List point)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = 
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(POINT$0, i);
            target.setListValue(point);
        }
    }
    
    /**
     * Appends the value as the last "Point" element
     */
    public void addPoint(java.util.List point)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(POINT$0);
            target.setListValue(point);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Point" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double3Type insertNewPoint(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().insert_element_user(POINT$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Point" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Double3Type addNewPoint()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Double3Type target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Double3Type)get_store().add_element_user(POINT$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Point" element
     */
    public void removePoint(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(POINT$0, i);
        }
    }
}
