/*
 * XML Type:  joinTableType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.JoinTableType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML joinTableType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class JoinTableTypeImpl extends org.astrogrid.adql.beans.impl.FromTableTypeImpl implements org.astrogrid.adql.beans.JoinTableType
{
    
    public JoinTableTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName QUALIFIER$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Qualifier");
    private static final javax.xml.namespace.QName TABLES$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Tables");
    private static final javax.xml.namespace.QName JOINSPECIFICATION$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "JoinSpecification");
    private static final javax.xml.namespace.QName NATURALJOIN$6 = 
        new javax.xml.namespace.QName("", "NaturalJoin");
    
    
    /**
     * Gets the "Qualifier" element
     */
    public org.astrogrid.adql.beans.JoinTableQualifierType.Enum getQualifier()
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
            return (org.astrogrid.adql.beans.JoinTableQualifierType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "Qualifier" element
     */
    public org.astrogrid.adql.beans.JoinTableQualifierType xgetQualifier()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.JoinTableQualifierType target = null;
            target = (org.astrogrid.adql.beans.JoinTableQualifierType)get_store().find_element_user(QUALIFIER$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Qualifier" element
     */
    public void setQualifier(org.astrogrid.adql.beans.JoinTableQualifierType.Enum qualifier)
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
    public void xsetQualifier(org.astrogrid.adql.beans.JoinTableQualifierType qualifier)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.JoinTableQualifierType target = null;
            target = (org.astrogrid.adql.beans.JoinTableQualifierType)get_store().find_element_user(QUALIFIER$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.JoinTableQualifierType)get_store().add_element_user(QUALIFIER$0);
            }
            target.set(qualifier);
        }
    }
    
    /**
     * Gets the "Tables" element
     */
    public org.astrogrid.adql.beans.ArrayOfFromTableType getTables()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ArrayOfFromTableType target = null;
            target = (org.astrogrid.adql.beans.ArrayOfFromTableType)get_store().find_element_user(TABLES$2, 0);
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
    public void setTables(org.astrogrid.adql.beans.ArrayOfFromTableType tables)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ArrayOfFromTableType target = null;
            target = (org.astrogrid.adql.beans.ArrayOfFromTableType)get_store().find_element_user(TABLES$2, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.ArrayOfFromTableType)get_store().add_element_user(TABLES$2);
            }
            target.set(tables);
        }
    }
    
    /**
     * Appends and returns a new empty "Tables" element
     */
    public org.astrogrid.adql.beans.ArrayOfFromTableType addNewTables()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.ArrayOfFromTableType target = null;
            target = (org.astrogrid.adql.beans.ArrayOfFromTableType)get_store().add_element_user(TABLES$2);
            return target;
        }
    }
    
    /**
     * Gets the "JoinSpecification" element
     */
    public org.astrogrid.adql.beans.JoinSpecType getJoinSpecification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.JoinSpecType target = null;
            target = (org.astrogrid.adql.beans.JoinSpecType)get_store().find_element_user(JOINSPECIFICATION$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "JoinSpecification" element
     */
    public boolean isSetJoinSpecification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(JOINSPECIFICATION$4) != 0;
        }
    }
    
    /**
     * Sets the "JoinSpecification" element
     */
    public void setJoinSpecification(org.astrogrid.adql.beans.JoinSpecType joinSpecification)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.JoinSpecType target = null;
            target = (org.astrogrid.adql.beans.JoinSpecType)get_store().find_element_user(JOINSPECIFICATION$4, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.JoinSpecType)get_store().add_element_user(JOINSPECIFICATION$4);
            }
            target.set(joinSpecification);
        }
    }
    
    /**
     * Appends and returns a new empty "JoinSpecification" element
     */
    public org.astrogrid.adql.beans.JoinSpecType addNewJoinSpecification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.JoinSpecType target = null;
            target = (org.astrogrid.adql.beans.JoinSpecType)get_store().add_element_user(JOINSPECIFICATION$4);
            return target;
        }
    }
    
    /**
     * Unsets the "JoinSpecification" element
     */
    public void unsetJoinSpecification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(JOINSPECIFICATION$4, 0);
        }
    }
    
    /**
     * Gets the "NaturalJoin" attribute
     */
    public boolean getNaturalJoin()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NATURALJOIN$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(NATURALJOIN$6);
            }
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "NaturalJoin" attribute
     */
    public org.apache.xmlbeans.XmlBoolean xgetNaturalJoin()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NATURALJOIN$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(NATURALJOIN$6);
            }
            return target;
        }
    }
    
    /**
     * True if has "NaturalJoin" attribute
     */
    public boolean isSetNaturalJoin()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(NATURALJOIN$6) != null;
        }
    }
    
    /**
     * Sets the "NaturalJoin" attribute
     */
    public void setNaturalJoin(boolean naturalJoin)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(NATURALJOIN$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(NATURALJOIN$6);
            }
            target.setBooleanValue(naturalJoin);
        }
    }
    
    /**
     * Sets (as xml) the "NaturalJoin" attribute
     */
    public void xsetNaturalJoin(org.apache.xmlbeans.XmlBoolean naturalJoin)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(NATURALJOIN$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(NATURALJOIN$6);
            }
            target.set(naturalJoin);
        }
    }
    
    /**
     * Unsets the "NaturalJoin" attribute
     */
    public void unsetNaturalJoin()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(NATURALJOIN$6);
        }
    }
}
