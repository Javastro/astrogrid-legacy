/*
 * XML Type:  queryExpressionType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.QueryExpressionType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML queryExpressionType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class QueryExpressionTypeImpl extends org.astrogrid.adql.beans.impl.FromTableTypeImpl implements org.astrogrid.adql.beans.QueryExpressionType
{
    
    public QueryExpressionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SELECT$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Select");
    private static final javax.xml.namespace.QName JOINEDTABLE$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "JoinedTable");
    
    
    /**
     * Gets the "Select" element
     */
    public org.astrogrid.adql.beans.SelectType getSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectType target = null;
            target = (org.astrogrid.adql.beans.SelectType)get_store().find_element_user(SELECT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Select" element
     */
    public boolean isSetSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SELECT$0) != 0;
        }
    }
    
    /**
     * Sets the "Select" element
     */
    public void setSelect(org.astrogrid.adql.beans.SelectType select)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectType target = null;
            target = (org.astrogrid.adql.beans.SelectType)get_store().find_element_user(SELECT$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SelectType)get_store().add_element_user(SELECT$0);
            }
            target.set(select);
        }
    }
    
    /**
     * Appends and returns a new empty "Select" element
     */
    public org.astrogrid.adql.beans.SelectType addNewSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectType target = null;
            target = (org.astrogrid.adql.beans.SelectType)get_store().add_element_user(SELECT$0);
            return target;
        }
    }
    
    /**
     * Unsets the "Select" element
     */
    public void unsetSelect()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SELECT$0, 0);
        }
    }
    
    /**
     * Gets the "JoinedTable" element
     */
    public org.astrogrid.adql.beans.JoinTableType getJoinedTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.JoinTableType target = null;
            target = (org.astrogrid.adql.beans.JoinTableType)get_store().find_element_user(JOINEDTABLE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "JoinedTable" element
     */
    public boolean isSetJoinedTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(JOINEDTABLE$2) != 0;
        }
    }
    
    /**
     * Sets the "JoinedTable" element
     */
    public void setJoinedTable(org.astrogrid.adql.beans.JoinTableType joinedTable)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.JoinTableType target = null;
            target = (org.astrogrid.adql.beans.JoinTableType)get_store().find_element_user(JOINEDTABLE$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.JoinTableType)get_store().add_element_user(JOINEDTABLE$2);
            }
            target.set(joinedTable);
        }
    }
    
    /**
     * Appends and returns a new empty "JoinedTable" element
     */
    public org.astrogrid.adql.beans.JoinTableType addNewJoinedTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.JoinTableType target = null;
            target = (org.astrogrid.adql.beans.JoinTableType)get_store().add_element_user(JOINEDTABLE$2);
            return target;
        }
    }
    
    /**
     * Unsets the "JoinedTable" element
     */
    public void unsetJoinedTable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(JOINEDTABLE$2, 0);
        }
    }
}
