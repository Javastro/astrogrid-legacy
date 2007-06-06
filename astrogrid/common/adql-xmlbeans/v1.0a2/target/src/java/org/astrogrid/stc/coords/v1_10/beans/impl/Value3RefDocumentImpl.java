/*
 * An XML document type.
 * Localname: Value3Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Value3RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Value3Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Value3RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CValue3DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Value3RefDocument
{
    
    public Value3RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VALUE3REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value3Ref");
    
    
    /**
     * Gets the "Value3Ref" element
     */
    public java.lang.String getValue3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE3REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Value3Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetValue3Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(VALUE3REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Value3Ref" element
     */
    public void setValue3Ref(java.lang.String value3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUE3REF$0);
            }
            target.setStringValue(value3Ref);
        }
    }
    
    /**
     * Sets (as xml) the "Value3Ref" element
     */
    public void xsetValue3Ref(org.apache.xmlbeans.XmlIDREF value3Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(VALUE3REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(VALUE3REF$0);
            }
            target.set(value3Ref);
        }
    }
}
