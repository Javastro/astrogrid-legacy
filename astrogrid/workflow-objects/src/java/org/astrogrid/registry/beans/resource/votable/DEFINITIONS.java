/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DEFINITIONS.java,v 1.10 2004/12/03 14:47:40 jdt Exp $
 */

package org.astrogrid.registry.beans.resource.votable;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class DEFINITIONS.
 * 
 * @version $Revision: 1.10 $ $Date: 2004/12/03 14:47:40 $
 */
public class DEFINITIONS extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _items
     */
    private java.util.ArrayList _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public DEFINITIONS() {
        super();
        _items = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.DEFINITIONS()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDEFINITIONSItem
     * 
     * @param vDEFINITIONSItem
     */
    public void addDEFINITIONSItem(org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem vDEFINITIONSItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(vDEFINITIONSItem);
    } //-- void addDEFINITIONSItem(org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem) 

    /**
     * Method addDEFINITIONSItem
     * 
     * @param index
     * @param vDEFINITIONSItem
     */
    public void addDEFINITIONSItem(int index, org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem vDEFINITIONSItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(index, vDEFINITIONSItem);
    } //-- void addDEFINITIONSItem(int, org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem) 

    /**
     * Method clearDEFINITIONSItem
     */
    public void clearDEFINITIONSItem()
    {
        _items.clear();
    } //-- void clearDEFINITIONSItem() 

    /**
     * Method enumerateDEFINITIONSItem
     */
    public java.util.Enumeration enumerateDEFINITIONSItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_items.iterator());
    } //-- java.util.Enumeration enumerateDEFINITIONSItem() 

    /**
     * Note: hashCode() has not been overriden
     * 
     * @param obj
     */
    public boolean equals(java.lang.Object obj)
    {
        if ( this == obj )
            return true;
        
        if (super.equals(obj)==false)
            return false;
        
        if (obj instanceof DEFINITIONS) {
        
            DEFINITIONS temp = (DEFINITIONS)obj;
            if (this._items != null) {
                if (temp._items == null) return false;
                else if (!(this._items.equals(temp._items))) 
                    return false;
            }
            else if (temp._items != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getDEFINITIONSItem
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem getDEFINITIONSItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem) _items.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem getDEFINITIONSItem(int) 

    /**
     * Method getDEFINITIONSItem
     */
    public org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem[] getDEFINITIONSItem()
    {
        int size = _items.size();
        org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem[] mArray = new org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem) _items.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem[] getDEFINITIONSItem() 

    /**
     * Method getDEFINITIONSItemCount
     */
    public int getDEFINITIONSItemCount()
    {
        return _items.size();
    } //-- int getDEFINITIONSItemCount() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeDEFINITIONSItem
     * 
     * @param vDEFINITIONSItem
     */
    public boolean removeDEFINITIONSItem(org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem vDEFINITIONSItem)
    {
        boolean removed = _items.remove(vDEFINITIONSItem);
        return removed;
    } //-- boolean removeDEFINITIONSItem(org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem) 

    /**
     * Method setDEFINITIONSItem
     * 
     * @param index
     * @param vDEFINITIONSItem
     */
    public void setDEFINITIONSItem(int index, org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem vDEFINITIONSItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.set(index, vDEFINITIONSItem);
    } //-- void setDEFINITIONSItem(int, org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem) 

    /**
     * Method setDEFINITIONSItem
     * 
     * @param DEFINITIONSItemArray
     */
    public void setDEFINITIONSItem(org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem[] DEFINITIONSItemArray)
    {
        //-- copy array
        _items.clear();
        for (int i = 0; i < DEFINITIONSItemArray.length; i++) {
            _items.add(DEFINITIONSItemArray[i]);
        }
    } //-- void setDEFINITIONSItem(org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem) 

    /**
     * Method unmarshalDEFINITIONS
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.DEFINITIONS unmarshalDEFINITIONS(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.DEFINITIONS) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.DEFINITIONS.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.DEFINITIONS unmarshalDEFINITIONS(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
