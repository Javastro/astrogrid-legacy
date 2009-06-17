/*
 * XML Type:  fitsType
 * Namespace: http://www.ivoa.net/xml/STC/STCcoords/v1.10
 * Java type: org.astrogrid.stc.coords.v1_10.beans.FitsType
 *
 * Automatically generated - do not modify.
 */
package org.astrogrid.stc.coords.v1_10.beans.impl;
/**
 * An XML fitsType(@http://www.ivoa.net/xml/STC/STCcoords/v1.10).
 *
 * This is an atomic type that is a restriction of org.astrogrid.stc.coords.v1_10.beans.FitsType.
 */
public class FitsTypeImpl extends org.apache.xmlbeans.impl.values.JavaUriHolderEx implements org.astrogrid.stc.coords.v1_10.beans.FitsType
{
    
    public FitsTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType, true);
    }
    
    protected FitsTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
    {
        super(sType, b);
    }
    
    private static final javax.xml.namespace.QName HDUNUM$0 = 
        new javax.xml.namespace.QName("", "hdu_num");
    private static final javax.xml.namespace.QName HDUNAME$2 = 
        new javax.xml.namespace.QName("", "hdu_name");
    
    
    /**
     * Gets the "hdu_num" attribute
     */
    public java.math.BigInteger getHduNum()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(HDUNUM$0);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "hdu_num" attribute
     */
    public org.apache.xmlbeans.XmlInteger xgetHduNum()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(HDUNUM$0);
            return target;
        }
    }
    
    /**
     * True if has "hdu_num" attribute
     */
    public boolean isSetHduNum()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(HDUNUM$0) != null;
        }
    }
    
    /**
     * Sets the "hdu_num" attribute
     */
    public void setHduNum(java.math.BigInteger hduNum)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(HDUNUM$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(HDUNUM$0);
            }
            target.setBigIntegerValue(hduNum);
        }
    }
    
    /**
     * Sets (as xml) the "hdu_num" attribute
     */
    public void xsetHduNum(org.apache.xmlbeans.XmlInteger hduNum)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInteger target = null;
            target = (org.apache.xmlbeans.XmlInteger)get_store().find_attribute_user(HDUNUM$0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInteger)get_store().add_attribute_user(HDUNUM$0);
            }
            target.set(hduNum);
        }
    }
    
    /**
     * Unsets the "hdu_num" attribute
     */
    public void unsetHduNum()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(HDUNUM$0);
        }
    }
    
    /**
     * Gets the "hdu_name" attribute
     */
    public java.lang.String getHduName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(HDUNAME$2);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "hdu_name" attribute
     */
    public org.apache.xmlbeans.XmlString xgetHduName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(HDUNAME$2);
            return target;
        }
    }
    
    /**
     * True if has "hdu_name" attribute
     */
    public boolean isSetHduName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().find_attribute_user(HDUNAME$2) != null;
        }
    }
    
    /**
     * Sets the "hdu_name" attribute
     */
    public void setHduName(java.lang.String hduName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(HDUNAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(HDUNAME$2);
            }
            target.setStringValue(hduName);
        }
    }
    
    /**
     * Sets (as xml) the "hdu_name" attribute
     */
    public void xsetHduName(org.apache.xmlbeans.XmlString hduName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(HDUNAME$2);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(HDUNAME$2);
            }
            target.set(hduName);
        }
    }
    
    /**
     * Unsets the "hdu_name" attribute
     */
    public void unsetHduName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_attribute(HDUNAME$2);
        }
    }
}
