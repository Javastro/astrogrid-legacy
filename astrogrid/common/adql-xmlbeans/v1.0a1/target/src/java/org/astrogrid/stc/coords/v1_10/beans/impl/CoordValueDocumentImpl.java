/*
 * An XML document type.
 * Localname: CoordValue
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.CoordValueDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one CoordValue(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class CoordValueDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.coords.v1_10.beans.CoordValueDocument
{
    
    public CoordValueDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COORDVALUE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CoordValue");
    private static final org.apache.xmlbeans.QNameSet COORDVALUE$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CoordValue"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CValue2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CValue3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value3Ref"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ValueRef"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CValue"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value2Ref"),
    });
    
    
    /**
     * Gets the "CoordValue" element
     */
    public org.apache.xmlbeans.XmlObject getCoordValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(COORDVALUE$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CoordValue" element
     */
    public void setCoordValue(org.apache.xmlbeans.XmlObject coordValue)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(COORDVALUE$1, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(COORDVALUE$0);
            }
            target.set(coordValue);
        }
    }
    
    /**
     * Appends and returns a new empty "CoordValue" element
     */
    public org.apache.xmlbeans.XmlObject addNewCoordValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(COORDVALUE$0);
            return target;
        }
    }
}
