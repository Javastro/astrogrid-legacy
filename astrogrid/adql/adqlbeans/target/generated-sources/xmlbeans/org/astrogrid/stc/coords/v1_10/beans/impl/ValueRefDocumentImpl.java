/*
 * An XML document type.
 * Localname: ValueRef
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.ValueRefDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one ValueRef(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class ValueRefDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CValueDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.ValueRefDocument
{
    
    public ValueRefDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VALUEREF$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ValueRef");
    
    
    /**
     * Gets the "ValueRef" element
     */
    public java.lang.String getValueRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUEREF$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "ValueRef" element
     */
    public org.apache.xmlbeans.XmlIDREF xgetValueRef()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(VALUEREF$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "ValueRef" element
     */
    public void setValueRef(java.lang.String valueRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUEREF$0);
            }
            target.setStringValue(valueRef);
        }
    }
    
    /**
     * Sets (as xml) the "ValueRef" element
     */
    public void xsetValueRef(org.apache.xmlbeans.XmlIDREF valueRef)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_element_user(VALUEREF$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_element_user(VALUEREF$0);
            }
            target.set(valueRef);
        }
    }
}
