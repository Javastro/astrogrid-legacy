/*
 * XML Type:  namedColumnsJoinType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.NamedColumnsJoinType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML namedColumnsJoinType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class NamedColumnsJoinTypeImpl extends org.astrogrid.adql.beans.impl.JoinSpecTypeImpl implements org.astrogrid.adql.beans.NamedColumnsJoinType
{
    
    public NamedColumnsJoinTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COLUMNLIST$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "ColumnList");
    
    
    /**
     * Gets the "ColumnList" element
     */
    public org.astrogrid.adql.beans.ColumnNameListType getColumnList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ColumnNameListType target = null;
            target = (org.astrogrid.adql.beans.ColumnNameListType)get_store().find_element_user(COLUMNLIST$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ColumnList" element
     */
    public void setColumnList(org.astrogrid.adql.beans.ColumnNameListType columnList)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ColumnNameListType target = null;
            target = (org.astrogrid.adql.beans.ColumnNameListType)get_store().find_element_user(COLUMNLIST$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.ColumnNameListType)get_store().add_element_user(COLUMNLIST$0);
            }
            target.set(columnList);
        }
    }
    
    /**
     * Appends and returns a new empty "ColumnList" element
     */
    public org.astrogrid.adql.beans.ColumnNameListType addNewColumnList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ColumnNameListType target = null;
            target = (org.astrogrid.adql.beans.ColumnNameListType)get_store().add_element_user(COLUMNLIST$0);
            return target;
        }
    }
}
