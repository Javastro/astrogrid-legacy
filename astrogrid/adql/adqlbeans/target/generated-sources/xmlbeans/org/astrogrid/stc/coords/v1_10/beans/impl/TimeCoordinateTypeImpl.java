/*
 * XML Type:  timeCoordinateType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * An XML timeCoordinateType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public class TimeCoordinateTypeImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.TimeCoordinateType
{
    
    public TimeCoordinateTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TIMEINSTANT$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "TimeInstant");
    private static final javax.xml.namespace.QName CERROR$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CError");
    private static final org.apache.xmlbeans.QNameSet CERROR$3 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CError"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ErrorRef"),
    });
    private static final javax.xml.namespace.QName CRESOLUTION$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CResolution");
    private static final org.apache.xmlbeans.QNameSet CRESOLUTION$5 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CResolution"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "ResolutionRef"),
    });
    private static final javax.xml.namespace.QName CSIZE$6 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CSize");
    private static final org.apache.xmlbeans.QNameSet CSIZE$7 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CSize"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "SizeRef"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size"),
    });
    private static final javax.xml.namespace.QName CPIXSIZE$8 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CPixSize");
    private static final org.apache.xmlbeans.QNameSet CPIXSIZE$9 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CPixSize"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSizeRef"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize"),
    });
    private static final javax.xml.namespace.QName UNIT$10 = 
        new javax.xml.namespace.QName("", "unit");
    
    
    /**
     * Gets the "TimeInstant" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstronTimeType getTimeInstant()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstronTimeType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstronTimeType)get_store().find_element_user(TIMEINSTANT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "TimeInstant" element
     */
    public boolean isSetTimeInstant()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(TIMEINSTANT$0) != 0;
        }
    }
    
    /**
     * Sets the "TimeInstant" element
     */
    public void setTimeInstant(org.astrogrid.stc.coords.v1_10.beans.AstronTimeType timeInstant)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstronTimeType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstronTimeType)get_store().find_element_user(TIMEINSTANT$0, 0);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.AstronTimeType)get_store().add_element_user(TIMEINSTANT$0);
            }
            target.set(timeInstant);
        }
    }
    
    /**
     * Appends and returns a new empty "TimeInstant" element
     */
    public org.astrogrid.stc.coords.v1_10.beans.AstronTimeType addNewTimeInstant()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.AstronTimeType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.AstronTimeType)get_store().add_element_user(TIMEINSTANT$0);
            return target;
        }
    }
    
    /**
     * Unsets the "TimeInstant" element
     */
    public void unsetTimeInstant()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(TIMEINSTANT$0, 0);
        }
    }
    
    /**
     * Gets array of all "CError" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCErrorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CERROR$3, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CError" element
     */
    public org.apache.xmlbeans.XmlObject getCErrorArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CERROR$3, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CError" element
     */
    public int sizeOfCErrorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CERROR$3);
        }
    }
    
    /**
     * Sets array of all "CError" element
     */
    public void setCErrorArray(org.apache.xmlbeans.XmlObject[] cErrorArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cErrorArray, CERROR$2, CERROR$3);
        }
    }
    
    /**
     * Sets ith "CError" element
     */
    public void setCErrorArray(int i, org.apache.xmlbeans.XmlObject cError)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CERROR$3, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cError);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CError" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCError(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CERROR$3, CERROR$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CError" element
     */
    public org.apache.xmlbeans.XmlObject addNewCError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CERROR$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "CError" element
     */
    public void removeCError(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CERROR$3, i);
        }
    }
    
    /**
     * Gets array of all "CResolution" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCResolutionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CRESOLUTION$5, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CResolution" element
     */
    public org.apache.xmlbeans.XmlObject getCResolutionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CRESOLUTION$5, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CResolution" element
     */
    public int sizeOfCResolutionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CRESOLUTION$5);
        }
    }
    
    /**
     * Sets array of all "CResolution" element
     */
    public void setCResolutionArray(org.apache.xmlbeans.XmlObject[] cResolutionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cResolutionArray, CRESOLUTION$4, CRESOLUTION$5);
        }
    }
    
    /**
     * Sets ith "CResolution" element
     */
    public void setCResolutionArray(int i, org.apache.xmlbeans.XmlObject cResolution)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CRESOLUTION$5, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cResolution);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CResolution" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCResolution(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CRESOLUTION$5, CRESOLUTION$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CResolution" element
     */
    public org.apache.xmlbeans.XmlObject addNewCResolution()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CRESOLUTION$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "CResolution" element
     */
    public void removeCResolution(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CRESOLUTION$5, i);
        }
    }
    
    /**
     * Gets array of all "CSize" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCSizeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CSIZE$7, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CSize" element
     */
    public org.apache.xmlbeans.XmlObject getCSizeArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CSIZE$7, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CSize" element
     */
    public int sizeOfCSizeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CSIZE$7);
        }
    }
    
    /**
     * Sets array of all "CSize" element
     */
    public void setCSizeArray(org.apache.xmlbeans.XmlObject[] cSizeArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cSizeArray, CSIZE$6, CSIZE$7);
        }
    }
    
    /**
     * Sets ith "CSize" element
     */
    public void setCSizeArray(int i, org.apache.xmlbeans.XmlObject cSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CSIZE$7, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cSize);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CSize" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCSize(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CSIZE$7, CSIZE$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CSize" element
     */
    public org.apache.xmlbeans.XmlObject addNewCSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CSIZE$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "CSize" element
     */
    public void removeCSize(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CSIZE$7, i);
        }
    }
    
    /**
     * Gets array of all "CPixSize" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCPixSizeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CPIXSIZE$9, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CPixSize" element
     */
    public org.apache.xmlbeans.XmlObject getCPixSizeArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CPIXSIZE$9, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CPixSize" element
     */
    public int sizeOfCPixSizeArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CPIXSIZE$9);
        }
    }
    
    /**
     * Sets array of all "CPixSize" element
     */
    public void setCPixSizeArray(org.apache.xmlbeans.XmlObject[] cPixSizeArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cPixSizeArray, CPIXSIZE$8, CPIXSIZE$9);
        }
    }
    
    /**
     * Sets ith "CPixSize" element
     */
    public void setCPixSizeArray(int i, org.apache.xmlbeans.XmlObject cPixSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CPIXSIZE$9, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cPixSize);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CPixSize" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCPixSize(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CPIXSIZE$9, CPIXSIZE$8, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CPixSize" element
     */
    public org.apache.xmlbeans.XmlObject addNewCPixSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CPIXSIZE$8);
            return target;
        }
    }
    
    /**
     * Removes the ith "CPixSize" element
     */
    public void removeCPixSize(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CPIXSIZE$9, i);
        }
    }
    
    /**
     * Gets the "unit" attribute
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeUnitType.Enum getUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(UNIT$10);
            }
            if (target == null)
            {
                return null;
            }
            return (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "unit" attribute
     */
    public org.astrogrid.stc.coords.v1_10.beans.TimeUnitType xgetUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeUnitType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType)get_store().find_attribute_user(UNIT$10);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType)get_default_attribute_value(UNIT$10);
            }
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
            return get_store().find_attribute_user(UNIT$10) != null;
        }
    }
    
    /**
     * Sets the "unit" attribute
     */
    public void setUnit(org.astrogrid.stc.coords.v1_10.beans.TimeUnitType.Enum unit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(UNIT$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(UNIT$10);
            }
            target.setEnumValue(unit);
        }
    }
    
    /**
     * Sets (as xml) the "unit" attribute
     */
    public void xsetUnit(org.astrogrid.stc.coords.v1_10.beans.TimeUnitType unit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.astrogrid.stc.coords.v1_10.beans.TimeUnitType target = null;
            target = (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType)get_store().find_attribute_user(UNIT$10);
            if (target == null)
            {
                target = (org.astrogrid.stc.coords.v1_10.beans.TimeUnitType)get_store().add_attribute_user(UNIT$10);
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
            get_store().remove_attribute(UNIT$10);
        }
    }
}
