/*
 * An XML document type.
 * Localname: Value2Ref
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Value2RefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Value2Ref(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Value2RefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CValue2DocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Value2RefDocument
{
    
    public Value2RefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VALUE2REF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value2Ref");
    
    
    /**
     * Gets the "Value2Ref" element
     */
    public java.lang.String getValue2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE2REF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Value2Ref" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetValue2Ref()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(VALUE2REF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Value2Ref" element
     */
    public void setValue2Ref(java.lang.String value2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUE2REF$0);
            }
            target.setStringValue(value2Ref);
        }
    }
    
    /**
     * Sets (as xml) the "Value2Ref" element
     */
    public void xsetValue2Ref(org.apache.xmlbeans.XmlIDREF value2Ref)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(VALUE2REF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(VALUE2REF$0);
            }
            target.set(value2Ref);
        }
    }
}
