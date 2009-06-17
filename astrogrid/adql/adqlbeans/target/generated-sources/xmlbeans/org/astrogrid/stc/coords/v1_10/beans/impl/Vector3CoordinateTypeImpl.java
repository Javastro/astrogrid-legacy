/*
 * XML Type:  vector3CoordinateType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * An XML vector3CoordinateType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is a complex type.
 */
public class Vector3CoordinateTypeImpl extends org.astrogrid.stc.coords.v1_10.beans.impl.CoordinateTypeImpl implements org.astrogrid.stc.coords.v1_10.beans.Vector3CoordinateType
{
    
    public Vector3CoordinateTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CVALUE3$0 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CValue3");
    private static final org.apache.xmlbeans.QNameSet CVALUE3$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CValue3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Value3Ref"),
    });
    private static final javax.xml.namespace.QName CERROR3$2 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CError3");
    private static final org.apache.xmlbeans.QNameSet CERROR3$3 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error3Ref"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Error3Matrix"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CError3"),
    });
    private static final javax.xml.namespace.QName CRESOLUTION3$4 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CResolution3");
    private static final org.apache.xmlbeans.QNameSet CRESOLUTION3$5 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution3Matrix"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CResolution3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Resolution3Ref"),
    });
    private static final javax.xml.namespace.QName CSIZE3$6 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CSize3");
    private static final org.apache.xmlbeans.QNameSet CSIZE3$7 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size3Matrix"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size3Ref"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CSize3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "Size3"),
    });
    private static final javax.xml.namespace.QName CPIXSIZE3$8 = 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CPixSize3");
    private static final org.apache.xmlbeans.QNameSet CPIXSIZE3$9 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize3Matrix"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "CPixSize3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize3"),
        new javax.xml.namespace.QName("http://www.ivoa.net/xml/STC/STCcoords/v1.10", "PixSize3Ref"),
    });
    
    
    /**
     * Gets the "CValue3" element
     */
    public org.apache.xmlbeans.XmlObject getCValue3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CVALUE3$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "CValue3" element
     */
    public boolean isSetCValue3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CVALUE3$1) != 0;
        }
    }
    
    /**
     * Sets the "CValue3" element
     */
    public void setCValue3(org.apache.xmlbeans.XmlObject cValue3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CVALUE3$1, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CVALUE3$0);
            }
            target.set(cValue3);
        }
    }
    
    /**
     * Appends and returns a new empty "CValue3" element
     */
    public org.apache.xmlbeans.XmlObject addNewCValue3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CVALUE3$0);
            return target;
        }
    }
    
    /**
     * Unsets the "CValue3" element
     */
    public void unsetCValue3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CVALUE3$1, 0);
        }
    }
    
    /**
     * Gets array of all "CError3" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCError3Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CERROR3$3, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CError3" element
     */
    public org.apache.xmlbeans.XmlObject getCError3Array(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CERROR3$3, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CError3" element
     */
    public int sizeOfCError3Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CERROR3$3);
        }
    }
    
    /**
     * Sets array of all "CError3" element
     */
    public void setCError3Array(org.apache.xmlbeans.XmlObject[] cError3Array)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cError3Array, CERROR3$2, CERROR3$3);
        }
    }
    
    /**
     * Sets ith "CError3" element
     */
    public void setCError3Array(int i, org.apache.xmlbeans.XmlObject cError3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CERROR3$3, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cError3);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CError3" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCError3(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CERROR3$3, CERROR3$2, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CError3" element
     */
    public org.apache.xmlbeans.XmlObject addNewCError3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CERROR3$2);
            return target;
        }
    }
    
    /**
     * Removes the ith "CError3" element
     */
    public void removeCError3(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CERROR3$3, i);
        }
    }
    
    /**
     * Gets array of all "CResolution3" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCResolution3Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CRESOLUTION3$5, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CResolution3" element
     */
    public org.apache.xmlbeans.XmlObject getCResolution3Array(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CRESOLUTION3$5, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CResolution3" element
     */
    public int sizeOfCResolution3Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CRESOLUTION3$5);
        }
    }
    
    /**
     * Sets array of all "CResolution3" element
     */
    public void setCResolution3Array(org.apache.xmlbeans.XmlObject[] cResolution3Array)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cResolution3Array, CRESOLUTION3$4, CRESOLUTION3$5);
        }
    }
    
    /**
     * Sets ith "CResolution3" element
     */
    public void setCResolution3Array(int i, org.apache.xmlbeans.XmlObject cResolution3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CRESOLUTION3$5, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cResolution3);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CResolution3" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCResolution3(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CRESOLUTION3$5, CRESOLUTION3$4, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CResolution3" element
     */
    public org.apache.xmlbeans.XmlObject addNewCResolution3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CRESOLUTION3$4);
            return target;
        }
    }
    
    /**
     * Removes the ith "CResolution3" element
     */
    public void removeCResolution3(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CRESOLUTION3$5, i);
        }
    }
    
    /**
     * Gets array of all "CSize3" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCSize3Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CSIZE3$7, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CSize3" element
     */
    public org.apache.xmlbeans.XmlObject getCSize3Array(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CSIZE3$7, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CSize3" element
     */
    public int sizeOfCSize3Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CSIZE3$7);
        }
    }
    
    /**
     * Sets array of all "CSize3" element
     */
    public void setCSize3Array(org.apache.xmlbeans.XmlObject[] cSize3Array)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cSize3Array, CSIZE3$6, CSIZE3$7);
        }
    }
    
    /**
     * Sets ith "CSize3" element
     */
    public void setCSize3Array(int i, org.apache.xmlbeans.XmlObject cSize3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CSIZE3$7, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cSize3);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CSize3" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCSize3(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CSIZE3$7, CSIZE3$6, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CSize3" element
     */
    public org.apache.xmlbeans.XmlObject addNewCSize3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CSIZE3$6);
            return target;
        }
    }
    
    /**
     * Removes the ith "CSize3" element
     */
    public void removeCSize3(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CSIZE3$7, i);
        }
    }
    
    /**
     * Gets array of all "CPixSize3" elements
     */
    public org.apache.xmlbeans.XmlObject[] getCPixSize3Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CPIXSIZE3$9, targetList);
            org.apache.xmlbeans.XmlObject[] result = new org.apache.xmlbeans.XmlObject[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "CPixSize3" element
     */
    public org.apache.xmlbeans.XmlObject getCPixSize3Array(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CPIXSIZE3$9, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "CPixSize3" element
     */
    public int sizeOfCPixSize3Array()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CPIXSIZE3$9);
        }
    }
    
    /**
     * Sets array of all "CPixSize3" element
     */
    public void setCPixSize3Array(org.apache.xmlbeans.XmlObject[] cPixSize3Array)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(cPixSize3Array, CPIXSIZE3$8, CPIXSIZE3$9);
        }
    }
    
    /**
     * Sets ith "CPixSize3" element
     */
    public void setCPixSize3Array(int i, org.apache.xmlbeans.XmlObject cPixSize3)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().find_element_user(CPIXSIZE3$9, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(cPixSize3);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "CPixSize3" element
     */
    public org.apache.xmlbeans.XmlObject insertNewCPixSize3(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().insert_element_user(CPIXSIZE3$9, CPIXSIZE3$8, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "CPixSize3" element
     */
    public org.apache.xmlbeans.XmlObject addNewCPixSize3()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlObject target = null;
            target = (org.apache.xmlbeans.XmlObject)get_store().add_element_user(CPIXSIZE3$8);
            return target;
        }
    }
    
    /**
     * Removes the ith "CPixSize3" element
     */
    public void removeCPixSize3(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CPIXSIZE3$9, i);
        }
    }
}
