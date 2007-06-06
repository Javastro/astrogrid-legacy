/*
 * An XML document type.
 * Localname: Position1D
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Position1DDocument
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * A document containing one Position1D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10) element.
 *
 * This is a complex type.
 */
public class Position1DDocumentImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.PositionDocumentImpl implements org.astrogrid.stc.coords.v1_10.beans.Position1DDocument
{
    
    public Position1DDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POSITION1D$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Position1D");
    
    
    /**
     * Gets the "Position1D" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D getPosition1D()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D)get_store().find_element_user(POSITION1D$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Position1D" element
     */
    public void setPosition1D(org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D position1D)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D)get_store().find_element_user(POSITION1D$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D)get_store().add_element_user(POSITION1D$0);
            }
            target.set(position1D);
        }
    }
    
    /**
     * Appends and returns a new empty "Position1D" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D addNewPosition1D()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D)get_store().add_element_user(POSITION1D$0);
            return target;
        }
    }
    /**
     * An XML Position1D(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
     *
     * This is a complex type.
     */
    public static class Position1DImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.ScalarCoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.Position1DDocument.Position1D
    {
        
        public Position1DImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName UNIT$0 = 
            new javax.xml.namespace.QName("", "unit");
        
        
        /**
         * Gets the "unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum getUnit()
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
                return (org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum)target.getEnumValue();
            }
        }
        
        /**
         * Gets (as xml) the "unit" attribute
         */
        public org.astrogrid.stc.coords.v1_10.beans.PosUnitType xgetUnit()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.PosUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().find_attribute_user(UNIT$0);
                return target;
            }
        }
        
        /**
         * Sets the "unit" attribute
         */
        public void setUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType.Enum unit)
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
                target.setEnumValue(unit);
            }
        }
        
        /**
         * Sets (as xml) the "unit" attribute
         */
        public void xsetUnit(org.astrogrid.stc.coords.v1_10.beans.PosUnitType unit)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.astrogrid.stc.coords.v1_10.beans.PosUnitType target = null;
                target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().find_attribute_user(UNIT$0);
                if (target == null)
                {
                    target = (org.astrogrid.stc.coords.v1_10.beans.PosUnitType)get_store().add_attribute_user(UNIT$0);
                }
                target.set(unit);
            }
        }
    }
}
