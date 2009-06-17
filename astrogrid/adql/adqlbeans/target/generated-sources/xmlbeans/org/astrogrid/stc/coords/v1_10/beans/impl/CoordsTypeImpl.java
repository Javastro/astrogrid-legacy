/*
 * XML Type:  coordsType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.CoordsType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * An XML coordsType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public class CoordsTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.astrogrid.stc.coords.v1_10.beans.CoordsType
{
    
    public CoordsTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COORDINATE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Coordinate");
    private static final org.apache.xmlbeans.QNameSet COORDINATE$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ScalarCoordinate"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixelCoordinate"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Coordinate"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "StringCoordinate"),
    });
    private static final javax.xml.namespace.QName COORDSYSTEMID$2 = 
        new javax.xml.namespace.QName("", "coord_system_id");
    
    
    /**
     * Gets array of all "Coordinate" elements
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType[] getCoordinateArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(COORDINATE$1, targetList);
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType[] result = new org.astrogrid.stc.coords.v1_10.beans.CoordinateType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Coordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType getCoordinateArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(COORDINATE$1, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Coordinate" element
     */
    public int sizeOfCoordinateArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COORDINATE$1);
        }
    }
    
    /**
     * Sets array of all "Coordinate" element
     */
    public void setCoordinateArray(org.astrogrid.stc.coords.v1_10.beans.CoordinateType[] coordinateArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(coordinateArray, COORDINATE$0, COORDINATE$1);
        }
    }
    
    /**
     * Sets ith "Coordinate" element
     */
    public void setCoordinateArray(int i, org.astrogrid.stc.coords.v1_10.beans.CoordinateType coordinate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().find_element_user(COORDINATE$1, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(coordinate);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Coordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType insertNewCoordinate(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().insert_element_user(COORDINATE$1, COORDINATE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Coordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.CoordinateType addNewCoordinate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.CoordinateType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.CoordinateType)get_store().add_element_user(COORDINATE$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Coordinate" element
     */
    public void removeCoordinate(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COORDINATE$1, i);
        }
    }
    
    /**
     * Gets the "coord_system_id" attribute
     */
    public java.lang.String getCoordSystemId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COORDSYSTEMID$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "coord_system_id" attribute
     */
    public org.apache.xmlbeans.XmlIDREF xgetCoordSystemId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(COORDSYSTEMID$2);
            return target;
        }
    }
    
    /**
     * Sets the "coord_system_id" attribute
     */
    public void setCoordSystemId(java.lang.String coordSystemId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COORDSYSTEMID$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(COORDSYSTEMID$2);
            }
            target.setStringValue(coordSystemId);
        }
    }
    
    /**
     * Sets (as xml) the "coord_system_id" attribute
     */
    public void xsetCoordSystemId(org.apache.xmlbeans.XmlIDREF coordSystemId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlIDREF target = null;
            target = (org.apache.xmlbeans.XmlIDREF)get_store().find_attribute_user(COORDSYSTEMID$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlIDREF)get_store().add_attribute_user(COORDSYSTEMID$2);
            }
            target.set(coordSystemId);
        }
    }
}
