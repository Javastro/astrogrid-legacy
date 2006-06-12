/*
 * An XML document type.
 * Localname: ScalarCoordinate
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one ScalarCoordinate(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class ScalarCoordinateDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordinateDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument
{
    
    public ScalarCoordinateDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SCALARCOORDINATE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ScalarCoordinate");
    
    
    /**
     * Gets the "ScalarCoordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate getScalarCoordinate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate)get_store().find_element_user(SCALARCOORDINATE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ScalarCoordinate" element
     */
    public void setScalarCoordinate(org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate scalarCoordinate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate)get_store().find_element_user(SCALARCOORDINATE$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate)get_store().add_element_user(SCALARCOORDINATE$0);
            }
            target.set(scalarCoordinate);
        }
    }
    
    /**
     * Appends and returns a new empty "ScalarCoordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate addNewScalarCoordinate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate)get_store().add_element_user(SCALARCOORDINATE$0);
            return target;
        }
    }
    /**
     * An XML ScalarCoordinate(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public static class ScalarCoordinateImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.ScalarCoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.ScalarCoordinateDocument.ScalarCoordinate
    {
        
        public ScalarCoordinateImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName UNIT$0 = 
            new javax.xml.namespace.QName("", "unit");
        
        
        /**
         * Gets the "unit" attribute
         */
        public java.lang.String getUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(UNIT$0);
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
                return get_store().find_attribute_user(UNIT$0) != null;
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(UNIT$0);
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
                target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(UNIT$0);
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
                get_store().remove_attribute(UNIT$0);
            }
        }
    }
}
