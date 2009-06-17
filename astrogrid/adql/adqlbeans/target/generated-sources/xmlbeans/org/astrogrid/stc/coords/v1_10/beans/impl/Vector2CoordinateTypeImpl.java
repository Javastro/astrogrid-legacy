/*
 * XML Type:  vector2CoordinateType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * An XML vector2CoordinateType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public class Vector2CoordinateTypeImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.Vector2CoordinateType
{
    
    public Vector2CoordinateTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CVALUE2$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CValue2");
    private static final org.apache.xmlbeans.QNameSet CVALUE2$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CValue2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value2Ref"),
    });
    private static final javax.xml.namespace.QName CERROR2$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CError2");
    private static final org.apache.xmlbeans.QNameSet CERROR2$3 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error2Matrix"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CError2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error2Ref"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error2"),
    });
    private static final javax.xml.namespace.QName CRESOLUTION2$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CResolution2");
    private static final org.apache.xmlbeans.QNameSet CRESOLUTION2$5 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution2Ref"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CResolution2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution2Matrix"),
    });
    private static final javax.xml.namespace.QName CSIZE2$6 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CSize2");
    private static final org.apache.xmlbeans.QNameSet CSIZE2$7 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CSize2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size2Ref"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size2Matrix"),
    });
    private static final javax.xml.namespace.QName CPIXSIZE2$8 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CPixSize2");
    private static final org.apache.xmlbeans.QNameSet CPIXSIZE2$9 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize2Matrix"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize2Ref"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize2"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CPixSize2"),
    });
    
    
    /**
     * Gets the "CValue2" element
     */
    public org.apache.xmlbeans.XmlObject getCValue2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CVALUE2$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "CValue2" element
     */
    public boolean isSetCValue2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CVALUE2$1) != 0;
        }
    }
    
    /**
     * Sets the "CValue2" element
     */
    public void setCValue2(org.apache.xmlbeans.XmlObject cValue2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CVALUE2$1, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CVALUE2$0);
            }
            target.set(cValue2);
        }
    }
    
    /**
     * Appends and returns a new empty "CValue2" element
     */
    public org.apache.xmlbeans.XmlObject addNewCValue2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CVALUE2$0);
            return target;
        }
    }
    
    /**
     * Unsets the "CValue2" element
     */
    public void unsetCValue2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CVALUE2$1, 0);
        }
    }
    
    /**
     * Gets array of all "CError2" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCError2Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CERROR2$3, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CError2" element
     */
    public org.apache.xmlbeans.XmlObject getCError2Array(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CERROR2$3, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CError2" element
     */
    public int sizeOfCError2Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CERROR2$3);
        }
    }
    
    /**
     * Sets array of all "CError2" element
     */
    public void setCError2Array(org.apache.xmlbeans.XmlObject[] cError2Array)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cError2Array, CERROR2$2, CERROR2$3);
        }
    }
    
    /**
     * Sets ith "CError2" element
     */
    public void setCError2Array(int i, org.apache.xmlbeans.XmlObject cError2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CERROR2$3, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cError2);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CError2" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCError2(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CERROR2$3, CERROR2$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CError2" element
     */
    public org.apache.xmlbeans.XmlObject addNewCError2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CERROR2$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "CError2" element
     */
    public void removeCError2(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CERROR2$3, i);
        }
    }
    
    /**
     * Gets array of all "CResolution2" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCResolution2Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CRESOLUTION2$5, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CResolution2" element
     */
    public org.apache.xmlbeans.XmlObject getCResolution2Array(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CRESOLUTION2$5, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CResolution2" element
     */
    public int sizeOfCResolution2Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CRESOLUTION2$5);
        }
    }
    
    /**
     * Sets array of all "CResolution2" element
     */
    public void setCResolution2Array(org.apache.xmlbeans.XmlObject[] cResolution2Array)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cResolution2Array, CRESOLUTION2$4, CRESOLUTION2$5);
        }
    }
    
    /**
     * Sets ith "CResolution2" element
     */
    public void setCResolution2Array(int i, org.apache.xmlbeans.XmlObject cResolution2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CRESOLUTION2$5, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cResolution2);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CResolution2" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCResolution2(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CRESOLUTION2$5, CRESOLUTION2$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CResolution2" element
     */
    public org.apache.xmlbeans.XmlObject addNewCResolution2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CRESOLUTION2$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "CResolution2" element
     */
    public void removeCResolution2(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CRESOLUTION2$5, i);
        }
    }
    
    /**
     * Gets array of all "CSize2" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCSize2Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CSIZE2$7, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CSize2" element
     */
    public org.apache.xmlbeans.XmlObject getCSize2Array(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CSIZE2$7, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CSize2" element
     */
    public int sizeOfCSize2Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CSIZE2$7);
        }
    }
    
    /**
     * Sets array of all "CSize2" element
     */
    public void setCSize2Array(org.apache.xmlbeans.XmlObject[] cSize2Array)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cSize2Array, CSIZE2$6, CSIZE2$7);
        }
    }
    
    /**
     * Sets ith "CSize2" element
     */
    public void setCSize2Array(int i, org.apache.xmlbeans.XmlObject cSize2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CSIZE2$7, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cSize2);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CSize2" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCSize2(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CSIZE2$7, CSIZE2$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CSize2" element
     */
    public org.apache.xmlbeans.XmlObject addNewCSize2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CSIZE2$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "CSize2" element
     */
    public void removeCSize2(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CSIZE2$7, i);
        }
    }
    
    /**
     * Gets array of all "CPixSize2" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCPixSize2Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CPIXSIZE2$9, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CPixSize2" element
     */
    public org.apache.xmlbeans.XmlObject getCPixSize2Array(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CPIXSIZE2$9, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CPixSize2" element
     */
    public int sizeOfCPixSize2Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CPIXSIZE2$9);
        }
    }
    
    /**
     * Sets array of all "CPixSize2" element
     */
    public void setCPixSize2Array(org.apache.xmlbeans.XmlObject[] cPixSize2Array)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cPixSize2Array, CPIXSIZE2$8, CPIXSIZE2$9);
        }
    }
    
    /**
     * Sets ith "CPixSize2" element
     */
    public void setCPixSize2Array(int i, org.apache.xmlbeans.XmlObject cPixSize2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CPIXSIZE2$9, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cPixSize2);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CPixSize2" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCPixSize2(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CPIXSIZE2$9, CPIXSIZE2$8, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CPixSize2" element
     */
    public org.apache.xmlbeans.XmlObject addNewCPixSize2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CPIXSIZE2$8);
            return target;
        }
    }
    
    /**
     * Removes the ith "CPixSize2" element
     */
    public void removeCPixSize2(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CPIXSIZE2$9, i);
        }
    }
}
