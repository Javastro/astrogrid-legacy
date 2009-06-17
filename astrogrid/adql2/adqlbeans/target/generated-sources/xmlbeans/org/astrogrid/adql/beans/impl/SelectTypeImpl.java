/*
 * XML Type:  selectType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.SelectType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML selectType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class SelectTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.adql.beans.SelectType
{
    
    public SelectTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ALLOW$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Allow");
    private static final javax.xml.namespace.QName RESTRICT$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Restrict");
    private static final javax.xml.namespace.QName SELECTIONLIST$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "SelectionList");
    private static final javax.xml.namespace.QName INTO$6 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "InTo");
    private static final javax.xml.namespace.QName FROM$8 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "From");
    private static final javax.xml.namespace.QName WHERE$10 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Where");
    private static final javax.xml.namespace.QName GROUPBY$12 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "GroupBy");
    private static final javax.xml.namespace.QName HAVING$14 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Having");
    private static final javax.xml.namespace.QName ORDERBY$16 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "OrderBy");
    
    
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
     * Gets the "Restrict" element
     */
    public org.astrogrid.adql.beans.SelectionLimitType getRestrict()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionLimitType target = null;
            target = (org.astrogrid.adql.beans.SelectionLimitType)get_store().find_element_user(RESTRICT$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Restrict" element
     */
    public boolean isSetRestrict()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RESTRICT$2) != 0;
        }
    }
    
    /**
     * Sets the "Restrict" element
     */
    public void setRestrict(org.astrogrid.adql.beans.SelectionLimitType restrict)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionLimitType target = null;
            target = (org.astrogrid.adql.beans.SelectionLimitType)get_store().find_element_user(RESTRICT$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SelectionLimitType)get_store().add_element_user(RESTRICT$2);
            }
            target.set(restrict);
        }
    }
    
    /**
     * Appends and returns a new empty "Restrict" element
     */
    public org.astrogrid.adql.beans.SelectionLimitType addNewRestrict()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionLimitType target = null;
            target = (org.astrogrid.adql.beans.SelectionLimitType)get_store().add_element_user(RESTRICT$2);
            return target;
        }
    }
    
    /**
     * Unsets the "Restrict" element
     */
    public void unsetRestrict()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RESTRICT$2, 0);
        }
    }
    
    /**
     * Gets the "SelectionList" element
     */
    public org.astrogrid.adql.beans.SelectionListType getSelectionList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionListType target = null;
            target = (org.astrogrid.adql.beans.SelectionListType)get_store().find_element_user(SELECTIONLIST$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "SelectionList" element
     */
    public void setSelectionList(org.astrogrid.adql.beans.SelectionListType selectionList)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionListType target = null;
            target = (org.astrogrid.adql.beans.SelectionListType)get_store().find_element_user(SELECTIONLIST$4, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.SelectionListType)get_store().add_element_user(SELECTIONLIST$4);
            }
            target.set(selectionList);
        }
    }
    
    /**
     * Appends and returns a new empty "SelectionList" element
     */
    public org.astrogrid.adql.beans.SelectionListType addNewSelectionList()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.SelectionListType target = null;
            target = (org.astrogrid.adql.beans.SelectionListType)get_store().add_element_user(SELECTIONLIST$4);
            return target;
        }
    }
    
    /**
     * Gets the "InTo" element
     */
    public org.astrogrid.adql.beans.IntoType getInTo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.IntoType target = null;
            target = (org.astrogrid.adql.beans.IntoType)get_store().find_element_user(INTO$6, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "InTo" element
     */
    public boolean isSetInTo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INTO$6) != 0;
        }
    }
    
    /**
     * Sets the "InTo" element
     */
    public void setInTo(org.astrogrid.adql.beans.IntoType inTo)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.IntoType target = null;
            target = (org.astrogrid.adql.beans.IntoType)get_store().find_element_user(INTO$6, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.IntoType)get_store().add_element_user(INTO$6);
            }
            target.set(inTo);
        }
    }
    
    /**
     * Appends and returns a new empty "InTo" element
     */
    public org.astrogrid.adql.beans.IntoType addNewInTo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.IntoType target = null;
            target = (org.astrogrid.adql.beans.IntoType)get_store().add_element_user(INTO$6);
            return target;
        }
    }
    
    /**
     * Unsets the "InTo" element
     */
    public void unsetInTo()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INTO$6, 0);
        }
    }
    
    /**
     * Gets the "From" element
     */
    public org.astrogrid.adql.beans.FromType getFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromType target = null;
            target = (org.astrogrid.adql.beans.FromType)get_store().find_element_user(FROM$8, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "From" element
     */
    public boolean isSetFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FROM$8) != 0;
        }
    }
    
    /**
     * Sets the "From" element
     */
    public void setFrom(org.astrogrid.adql.beans.FromType from)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromType target = null;
            target = (org.astrogrid.adql.beans.FromType)get_store().find_element_user(FROM$8, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.FromType)get_store().add_element_user(FROM$8);
            }
            target.set(from);
        }
    }
    
    /**
     * Appends and returns a new empty "From" element
     */
    public org.astrogrid.adql.beans.FromType addNewFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.FromType target = null;
            target = (org.astrogrid.adql.beans.FromType)get_store().add_element_user(FROM$8);
            return target;
        }
    }
    
    /**
     * Unsets the "From" element
     */
    public void unsetFrom()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FROM$8, 0);
        }
    }
    
    /**
     * Gets the "Where" element
     */
    public org.astrogrid.adql.beans.WhereType getWhere()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.WhereType target = null;
            target = (org.astrogrid.adql.beans.WhereType)get_store().find_element_user(WHERE$10, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Where" element
     */
    public boolean isSetWhere()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(WHERE$10) != 0;
        }
    }
    
    /**
     * Sets the "Where" element
     */
    public void setWhere(org.astrogrid.adql.beans.WhereType where)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.WhereType target = null;
            target = (org.astrogrid.adql.beans.WhereType)get_store().find_element_user(WHERE$10, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.WhereType)get_store().add_element_user(WHERE$10);
            }
            target.set(where);
        }
    }
    
    /**
     * Appends and returns a new empty "Where" element
     */
    public org.astrogrid.adql.beans.WhereType addNewWhere()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.WhereType target = null;
            target = (org.astrogrid.adql.beans.WhereType)get_store().add_element_user(WHERE$10);
            return target;
        }
    }
    
    /**
     * Unsets the "Where" element
     */
    public void unsetWhere()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(WHERE$10, 0);
        }
    }
    
    /**
     * Gets the "GroupBy" element
     */
    public org.astrogrid.adql.beans.GroupByType getGroupBy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.GroupByType target = null;
            target = (org.astrogrid.adql.beans.GroupByType)get_store().find_element_user(GROUPBY$12, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "GroupBy" element
     */
    public boolean isSetGroupBy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(GROUPBY$12) != 0;
        }
    }
    
    /**
     * Sets the "GroupBy" element
     */
    public void setGroupBy(org.astrogrid.adql.beans.GroupByType groupBy)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.GroupByType target = null;
            target = (org.astrogrid.adql.beans.GroupByType)get_store().find_element_user(GROUPBY$12, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.GroupByType)get_store().add_element_user(GROUPBY$12);
            }
            target.set(groupBy);
        }
    }
    
    /**
     * Appends and returns a new empty "GroupBy" element
     */
    public org.astrogrid.adql.beans.GroupByType addNewGroupBy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.GroupByType target = null;
            target = (org.astrogrid.adql.beans.GroupByType)get_store().add_element_user(GROUPBY$12);
            return target;
        }
    }
    
    /**
     * Unsets the "GroupBy" element
     */
    public void unsetGroupBy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(GROUPBY$12, 0);
        }
    }
    
    /**
     * Gets the "Having" element
     */
    public org.astrogrid.adql.beans.HavingType getHaving()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.HavingType target = null;
            target = (org.astrogrid.adql.beans.HavingType)get_store().find_element_user(HAVING$14, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "Having" element
     */
    public boolean isSetHaving()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(HAVING$14) != 0;
        }
    }
    
    /**
     * Sets the "Having" element
     */
    public void setHaving(org.astrogrid.adql.beans.HavingType having)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.HavingType target = null;
            target = (org.astrogrid.adql.beans.HavingType)get_store().find_element_user(HAVING$14, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.HavingType)get_store().add_element_user(HAVING$14);
            }
            target.set(having);
        }
    }
    
    /**
     * Appends and returns a new empty "Having" element
     */
    public org.astrogrid.adql.beans.HavingType addNewHaving()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.HavingType target = null;
            target = (org.astrogrid.adql.beans.HavingType)get_store().add_element_user(HAVING$14);
            return target;
        }
    }
    
    /**
     * Unsets the "Having" element
     */
    public void unsetHaving()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(HAVING$14, 0);
        }
    }
    
    /**
     * Gets the "OrderBy" element
     */
    public org.astrogrid.adql.beans.OrderExpressionType getOrderBy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.OrderExpressionType target = null;
            target = (org.astrogrid.adql.beans.OrderExpressionType)get_store().find_element_user(ORDERBY$16, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "OrderBy" element
     */
    public boolean isSetOrderBy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ORDERBY$16) != 0;
        }
    }
    
    /**
     * Sets the "OrderBy" element
     */
    public void setOrderBy(org.astrogrid.adql.beans.OrderExpressionType orderBy)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.OrderExpressionType target = null;
            target = (org.astrogrid.adql.beans.OrderExpressionType)get_store().find_element_user(ORDERBY$16, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.OrderExpressionType)get_store().add_element_user(ORDERBY$16);
            }
            target.set(orderBy);
        }
    }
    
    /**
     * Appends and returns a new empty "OrderBy" element
     */
    public org.astrogrid.adql.beans.OrderExpressionType addNewOrderBy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.OrderExpressionType target = null;
            target = (org.astrogrid.adql.beans.OrderExpressionType)get_store().add_element_user(ORDERBY$16);
            return target;
        }
    }
    
    /**
     * Unsets the "OrderBy" element
     */
    public void unsetOrderBy()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ORDERBY$16, 0);
        }
    }
}
