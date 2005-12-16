/*
 * XML Type:  atomType
 * Namespace: http://www.ivoa.net/xml/ADQL/v1.0
 * Java type: org.astrogrid.adql.v1_0.beans.AtomType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.v1_0.beans.impl;
/**
 * An XML atomType(@http://www.ivoa.net/xml/ADQL/v1.0).
 *
 * This is a complex type.
 */
public class AtomTypeImpl extends org.astrogrid.adql.v1_0.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.v1_0.beans.AtomType
{
    
    public AtomTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName LITERAL$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Literal");
    private static final javax.xml.namespace.QName UNIT$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/ADQL/v1.0", "Unit");
    
    
    /**
     * Gets the "Literal" element
     */
    public org.astrogrid.adql.v1_0.beans.LiteralType getLiteral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.LiteralType target = null;
            target = (org.astrogrid.adql.v1_0.beans.LiteralType)get_store().find_element_user(LITERAL$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Literal" element
     */
    public void setLiteral(org.astrogrid.adql.v1_0.beans.LiteralType literal)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.LiteralType target = null;
            target = (org.astrogrid.adql.v1_0.beans.LiteralType)get_store().find_element_user(LITERAL$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.v1_0.beans.LiteralType)get_store().add_element_user(LITERAL$0);
            }
            target.set(literal);
        }
    }
    
    /**
     * Appends and returns a new empty "Literal" element
     */
    public org.astrogrid.adql.v1_0.beans.LiteralType addNewLiteral()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.v1_0.beans.LiteralType target = null;
            target = (org.astrogrid.adql.v1_0.beans.LiteralType)get_store().add_element_user(LITERAL$0);
            return target;
        }
    }
    
    /**
     * Gets the "Unit" element
     */
    public java.lang.String getUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UNIT$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Unit" element
     */
    public org.apache.xmlbeans.XmlString xgetUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UNIT$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "Unit" element
     */
    public boolean isSetUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(UNIT$2) != 0;
        }
    }
    
    /**
     * Sets the "Unit" element
     */
    public void setUnit(java.lang.String unit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(UNIT$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(UNIT$2);
            }
            target.setStringValue(unit);
        }
    }
    
    /**
     * Sets (as xml) the "Unit" element
     */
    public void xsetUnit(org.apache.xmlbeans.XmlString unit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(UNIT$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(UNIT$2);
            }
            target.set(unit);
        }
    }
    
    /**
     * Unsets the "Unit" element
     */
    public void unsetUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(UNIT$2, 0);
        }
    }
}
