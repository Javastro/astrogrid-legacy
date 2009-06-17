/*
 * An XML document type.
 * Localname: CValue3
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.CValue3Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one CValue3(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class CValue3DocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordValueDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.CValue3Document
{
    
    public CValue3DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CVALUE3$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CValue3");
    private static final org.apache.xmlbeans.QNameSet CVALUE3$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CValue3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value3Ref"),
    });
    
    
    /**
     * Gets the "CValue3" element
     */
    public org.apache.xmlbeans.XmlObject getCValue3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CVALUE3$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CValue3" element
     */
    public void setCValue3(org.apache.xmlbeans.XmlObject cValue3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CVALUE3$1, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CVALUE3$0);
            }
            target.set(cValue3);
        }
    }
    
    /**
     * Appends and returns a new empty "CValue3" element
     */
    public org.apache.xmlbeans.XmlObject addNewCValue3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CVALUE3$0);
            return target;
        }
    }
}
