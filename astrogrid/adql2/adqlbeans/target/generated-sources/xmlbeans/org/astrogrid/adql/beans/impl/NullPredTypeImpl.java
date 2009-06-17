/*
 * XML Type:  nullPredType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.NullPredType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML nullPredType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class NullPredTypeImpl extends org.astrogrid.adql.beans.impl.SearchTypeImpl implements org.astrogrid.adql.beans.NullPredType
{
    
    public NullPredTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COLUMN$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Column");
    
    
    /**
     * Gets the "Column" element
     */
    public org.astrogrid.adql.beans.ColumnReferenceType getColumn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ColumnReferenceType target = null;
            target = (org.astrogrid.adql.beans.ColumnReferenceType)get_store().find_element_user(COLUMN$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Column" element
     */
    public void setColumn(org.astrogrid.adql.beans.ColumnReferenceType column)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ColumnReferenceType target = null;
            target = (org.astrogrid.adql.beans.ColumnReferenceType)get_store().find_element_user(COLUMN$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.ColumnReferenceType)get_store().add_element_user(COLUMN$0);
            }
            target.set(column);
        }
    }
    
    /**
     * Appends and returns a new empty "Column" element
     */
    public org.astrogrid.adql.beans.ColumnReferenceType addNewColumn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ColumnReferenceType target = null;
            target = (org.astrogrid.adql.beans.ColumnReferenceType)get_store().add_element_user(COLUMN$0);
            return target;
        }
    }
}
