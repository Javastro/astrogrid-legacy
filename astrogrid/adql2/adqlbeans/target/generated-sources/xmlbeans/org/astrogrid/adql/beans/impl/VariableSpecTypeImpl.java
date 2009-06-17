/*
 * XML Type:  variableSpecType
 * Namespace: http://www.ivoa.net/xml/v2.0/adql
 * Java type: org.astrogrid.adql.beans.VariableSpecType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.adql.beans.impl;
/**
 * An XML variableSpecType(@http://www.ivoa.net/xml/v2.0/adql).
 *
 * This is a complex type.
 */
public class VariableSpecTypeImpl extends org.astrogrid.adql.beans.impl.ScalarExpressionTypeImpl implements org.astrogrid.adql.beans.VariableSpecType
{
    
    public VariableSpecTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VARIABLE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/v2.0/adql", "Variable");
    
    
    /**
     * Gets the "Variable" element
     */
    public org.astrogrid.adql.beans.AtomType getVariable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.AtomType target = null;
            target = (org.astrogrid.adql.beans.AtomType)get_store().find_element_user(VARIABLE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Variable" element
     */
    public void setVariable(org.astrogrid.adql.beans.AtomType variable)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.AtomType target = null;
            target = (org.astrogrid.adql.beans.AtomType)get_store().find_element_user(VARIABLE$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.adql.beans.AtomType)get_store().add_element_user(VARIABLE$0);
            }
            target.set(variable);
        }
    }
    
    /**
     * Appends and returns a new empty "Variable" element
     */
    public org.astrogrid.adql.beans.AtomType addNewVariable()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.adql.beans.AtomType target = null;
            target = (org.astrogrid.adql.beans.AtomType)get_store().add_element_user(VARIABLE$0);
            return target;
        }
    }
}
