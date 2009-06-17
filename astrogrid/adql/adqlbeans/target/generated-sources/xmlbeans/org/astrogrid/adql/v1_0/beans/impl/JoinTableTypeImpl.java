/*
 * XML Type:  joinTableType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.JoinTableType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML joinTableType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class JoinTableTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.FromTableTypeImpl implements org.astrogrid.adql.v1_0.beans.JoinTableType
{
    
    public JoinTableTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName QUALIFIER$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Qualifier");
    private static final javax.xml.namespace.QName TABLES$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Tables");
    private static final javax.xml.namespace.QName CONDITION$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Condition");
    
    
    /**
     * Gets the "Qualifier" element
     */
    public org.astrogrid.adql.v1_0.beans.JointTableQualifierType.Enum getQualifier()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(QUALIFIER$0, 0);
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.adql.v1_0.beans.JointTableQualifierType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Qualifier" element
     */
    public org.astrogrid.adql.v1_0.beans.JointTableQualifierType xgetQualifier()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.JointTableQualifierType target = null;
            target = (org.astrogrid.adql.v1_0.beans.JointTableQualifierType)get_store().find_element_user(QUALIFIER$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Qualifier" element
     */
    public void setQualifier(org.astrogrid.adql.v1_0.beans.JointTableQualifierType.Enum qualifier)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(QUALIFIER$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(QUALIFIER$0);
            }
            target.setEnumValue(qualifier);
        }
    }
    
    /**
     * Sets (as xml) the "Qualifier" element
     */
    public void xsetQualifier(org.astrogrid.adql.v1_0.beans.JointTableQualifierType qualifier)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.JointTableQualifierType target = null;
            target = (org.astrogrid.adql.v1_0.beans.JointTableQualifierType)get_store().find_element_user(QUALIFIER$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.JointTableQualifierType)get_store().add_element_user(QUALIFIER$0);
            }
            target.set(qualifier);
        }
    }
    
    /**
     * Gets the "Tables" element
     */
    public org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType getTables()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType)get_store().find_element_user(TABLES$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Tables" element
     */
    public void setTables(org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType tables)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType)get_store().find_element_user(TABLES$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType)get_store().add_element_user(TABLES$2);
            }
            target.set(tables);
        }
    }
    
    /**
     * Appends and returns a new empty "Tables" element
     */
    public org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType addNewTables()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType)get_store().add_element_user(TABLES$2);
            return target;
        }
    }
    
    /**
     * Gets the "Condition" element
     */
    public org.astrogrid.adql.v1_0.beans.ComparisonPredType getCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ComparisonPredType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ComparisonPredType)get_store().find_element_user(CONDITION$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Condition" element
     */
    public void setCondition(org.astrogrid.adql.v1_0.beans.ComparisonPredType condition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ComparisonPredType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ComparisonPredType)get_store().find_element_user(CONDITION$4, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.ComparisonPredType)get_store().add_element_user(CONDITION$4);
            }
            target.set(condition);
        }
    }
    
    /**
     * Appends and returns a new empty "Condition" element
     */
    public org.astrogrid.adql.v1_0.beans.ComparisonPredType addNewCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.ComparisonPredType target = null;
            target = (org.astrogrid.adql.v1_0.beans.ComparisonPredType)get_store().add_element_user(CONDITION$4);
            return target;
        }
    }
}
