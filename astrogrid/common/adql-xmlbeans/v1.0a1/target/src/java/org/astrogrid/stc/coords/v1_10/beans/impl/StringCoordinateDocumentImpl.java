/*
 * An XML document type.
 * Localname: StringCoordinate
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one StringCoordinate(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class StringCoordinateDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordinateDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument
{
    
    public StringCoordinateDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName STRINGCOORDINATE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "StringCoordinate");
    
    
    /**
     * Gets the "StringCoordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate getStringCoordinate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate)get_store().find_element_user(STRINGCOORDINATE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "StringCoordinate" element
     */
    public void setStringCoordinate(org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate stringCoordinate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate)get_store().find_element_user(STRINGCOORDINATE$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate)get_store().add_element_user(STRINGCOORDINATE$0);
            }
            target.set(stringCoordinate);
        }
    }
    
    /**
     * Appends and returns a new empty "StringCoordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate addNewStringCoordinate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate)get_store().add_element_user(STRINGCOORDINATE$0);
            return target;
        }
    }
    /**
     * An XML StringCoordinate(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public static class StringCoordinateImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.StringCoordinateDocument.StringCoordinate
    {
        
        public StringCoordinateImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName VALUE$0 = 
            new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value");
        private static final javax.xml.namespace.QName UNIT$2 = 
            new javax.xml.namespace.QName("", "unit");
        
        
        /**
         * Gets the "Value" element
         */
        public java.lang.String getValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "Value" element
         */
        public org.apache.xmlbeans.XmlString xgetValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "Value" element
         */
        public void setValue(java.lang.String value)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUE$0);
                }
                target.setStringValue(value);
            }
        }
        
        /**
         * Sets (as xml) the "Value" element
         */
        public void xsetValue(org.apache.xmlbeans.XmlString value)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUE$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(VALUE$0);
                }
                target.set(value);
            }
        }
        
        /**
         * Gets the "unit" attribute
         */
        public java.lang.String getUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$2);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        public org.apache.xmlbeans.XmlString xgetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(UNIT$2);
                return target;
            }
        }
        
        /**
         * True if has "unit" attribute
         */
        public boolean isSetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().find_attribute_user(UNIT$2) != null;
            }
        }
        
        /**
         * Sets the "unit" attribute
         */
        public void setUnit(java.lang.String unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(UNIT$2);
                }
                target.setStringValue(unit);
            }
        }
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        public void xsetUnit(org.apache.xmlbeans.XmlString unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(UNIT$2);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(UNIT$2);
                }
                target.set(unit);
            }
        }
        
        /**
         * Unsets the "unit" attribute
         */
        public void unsetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_attribute(UNIT$2);
            }
        }
    }
}
