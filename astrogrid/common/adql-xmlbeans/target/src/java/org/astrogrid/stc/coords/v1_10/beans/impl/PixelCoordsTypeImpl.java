/*
 * XML Type:  pixelCoordsType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * An XML pixelCoordsType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public class PixelCoordsTypeImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordsTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.PixelCoordsType
{
    
    public PixelCoordsTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PIXELCOORDINATE$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixelCoordinate");
    
    
    /**
     * Gets array of all "PixelCoordinate" elements
     */
    public org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate[] getPixelCoordinateArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(PIXELCOORDINATE$0, targetList);
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate[] result = new org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "PixelCoordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate getPixelCoordinateArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate)get_store().find_element_user(PIXELCOORDINATE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "PixelCoordinate" element
     */
    public int sizeOfPixelCoordinateArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PIXELCOORDINATE$0);
        }
    }
    
    /**
     * Sets array of all "PixelCoordinate" element
     */
    public void setPixelCoordinateArray(org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate[] pixelCoordinateArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(pixelCoordinateArray, PIXELCOORDINATE$0);
        }
    }
    
    /**
     * Sets ith "PixelCoordinate" element
     */
    public void setPixelCoordinateArray(int i, org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate pixelCoordinate)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate)get_store().find_element_user(PIXELCOORDINATE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(pixelCoordinate);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "PixelCoordinate" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate insertNewPixelCoordinate(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.PixelCoordinateDocument.PixelCoordinate)get_store().insert_element_user(PIXELCOORDINATE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "PixelCoordinate" element
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
     * Removes the ith "PixelCoordinate" element
     */
    public void removePixelCoordinate(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PIXELCOORDINATE$0, i);
        }
    }
}
