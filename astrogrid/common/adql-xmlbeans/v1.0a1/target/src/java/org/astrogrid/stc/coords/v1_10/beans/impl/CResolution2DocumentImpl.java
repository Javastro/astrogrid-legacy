/*
 * An XML document type.
 * Localname: CResolution2
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.CResolution2Document
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one CResolution2(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class CResolution2DocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.coords.v1_10.beans.CResolution2Document
{
    
    public CResolution2DocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CRESOLUTION2$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CResolution2");
    private static final org.apache.xmlbeans.QNameSet CRESOLUTION2$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution2Ref"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CResolution2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution2Matrix"),
    });
    
    
    /**
     * Gets the "CResolution2" element
     */
    public org.apache.xmlbeans.XmlObject getCResolution2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CRESOLUTION2$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CResolution2" element
     */
    public void setCResolution2(org.apache.xmlbeans.XmlObject cResolution2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CRESOLUTION2$1, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CRESOLUTION2$0);
            }
            target.set(cResolution2);
        }
    }
    
    /**
     * Appends and returns a new empty "CResolution2" element
     */
    public org.apache.xmlbeans.XmlObject addNewCResolution2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CRESOLUTION2$0);
            return target;
        }
    }
}
