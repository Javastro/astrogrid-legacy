/*
 * An XML document type.
 * Localname: PixelCoordinate
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one PixelCoordinate(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class PixelCoordinateDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordinateDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument
{
    
    public PixelCoordinateDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXELCOORDINATE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixelCoordinate");
    
    
    /**
     * Gets the "PixelCoordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate getPixelCoordinate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate)get_store().find_element_user(PIXELCOORDINATE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PixelCoordinate" element
     */
    public void setPixelCoordinate(org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate pixelCoordinate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate)get_store().find_element_user(PIXELCOORDINATE$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate)get_store().add_element_user(PIXELCOORDINATE$0);
            }
            target.set(pixelCoordinate);
        }
    }
    
    /**
     * Appends and returns a new empty "PixelCoordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate addNewPixelCoordinate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate)get_store().add_element_user(PIXELCOORDINATE$0);
            return target;
        }
    }
    /**
     * An XML PixelCoordinate(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public static class PixelCoordinateImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate
    {
        
        public PixelCoordinateImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName VALUE$0 = 
            new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value");
        
        
        /**
         * Gets the "Value" element
         */
        public double getValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUE$0, 0);
                if (target == null)
                {
                    return 0.0;
                }
                return target.getDoubleValue();
            }
        }
        
        /**
         * Gets (as xml) the "Value" element
         */
        public org.apache.xmlbeans.XmlDouble xgetValue()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(VALUE$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "Value" element
         */
        public void setValue(double value)
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
                target.setDoubleValue(value);
            }
        }
        
        /**
         * Sets (as xml) the "Value" element
         */
        public void xsetValue(org.apache.xmlbeans.XmlDouble value)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlDouble target = null;
                target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(VALUE$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(VALUE$0);
                }
                target.set(value);
            }
        }
    }
}
