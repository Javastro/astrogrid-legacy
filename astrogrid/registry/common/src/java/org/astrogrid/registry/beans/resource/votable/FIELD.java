/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: FIELD.java,v 1.2 2004/03/03 16:22:08 KevinBenson Exp $
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
import org.astrogrid.registry.beans.resource.votable.types.DataType;
import org.astrogrid.registry.beans.resource.votable.types.FIELDItemTypeType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class FIELD.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/03 16:22:08 $
 */
public class FIELD extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ID
     */
    private java.lang.String _ID;

    /**
     * Field _unit
     */
    private java.lang.String _unit;

    /**
     * Field _datatype
     */
    private org.astrogrid.registry.beans.resource.votable.types.DataType _datatype;

    /**
     * Field _precision
     */
    private java.lang.String _precision;

    /**
     * Field _width
     */
    private int _width;

    /**
     * keeps track of state for field: _width
     */
    private boolean _has_width;

    /**
     * Field _ref
     */
    private java.lang.Object _ref;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _ucd
     */
    private java.lang.String _ucd;

    /**
     * Field _arraysize
     */
    private java.lang.String _arraysize;

    /**
     * Field _type
     */
    private org.astrogrid.registry.beans.resource.votable.types.FIELDItemTypeType _type;

    /**
     * Field _items
     */
    private java.util.ArrayList _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public FIELD() {
        super();
        _items = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.FIELD()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFIELDItem
     * 
     * @param vFIELDItem
     */
    public void addFIELDItem(org.astrogrid.registry.beans.resource.votable.FIELDItem vFIELDItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(vFIELDItem);
    } //-- void addFIELDItem(org.astrogrid.registry.beans.resource.votable.FIELDItem) 

    /**
     * Method addFIELDItem
     * 
     * @param index
     * @param vFIELDItem
     */
    public void addFIELDItem(int index, org.astrogrid.registry.beans.resource.votable.FIELDItem vFIELDItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.add(index, vFIELDItem);
    } //-- void addFIELDItem(int, org.astrogrid.registry.beans.resource.votable.FIELDItem) 

    /**
     * Method clearFIELDItem
     */
    public void clearFIELDItem()
    {
        _items.clear();
    } //-- void clearFIELDItem() 

    /**
     * Method deleteWidth
     */
    public void deleteWidth()
    {
        this._has_width= false;
    } //-- void deleteWidth() 

    /**
     * Method enumerateFIELDItem
     */
    public java.util.Enumeration enumerateFIELDItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_items.iterator());
    } //-- java.util.Enumeration enumerateFIELDItem() 

    /**
     * Returns the value of field 'arraysize'.
     * 
     * @return the value of field 'arraysize'.
     */
    public java.lang.String getArraysize()
    {
        return this._arraysize;
    } //-- java.lang.String getArraysize() 

    /**
     * Returns the value of field 'datatype'.
     * 
     * @return the value of field 'datatype'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.DataType getDatatype()
    {
        return this._datatype;
    } //-- org.astrogrid.registry.beans.resource.votable.types.DataType getDatatype() 

    /**
     * Method getFIELDItem
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.FIELDItem getFIELDItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.FIELDItem) _items.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.FIELDItem getFIELDItem(int) 

    /**
     * Method getFIELDItem
     */
    public org.astrogrid.registry.beans.resource.votable.FIELDItem[] getFIELDItem()
    {
        int size = _items.size();
        org.astrogrid.registry.beans.resource.votable.FIELDItem[] mArray = new org.astrogrid.registry.beans.resource.votable.FIELDItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.FIELDItem) _items.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.FIELDItem[] getFIELDItem() 

    /**
     * Method getFIELDItemCount
     */
    public int getFIELDItemCount()
    {
        return _items.size();
    } //-- int getFIELDItemCount() 

    /**
     * Returns the value of field 'ID'.
     * 
     * @return the value of field 'ID'.
     */
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'precision'.
     * 
     * @return the value of field 'precision'.
     */
    public java.lang.String getPrecision()
    {
        return this._precision;
    } //-- java.lang.String getPrecision() 

    /**
     * Returns the value of field 'ref'.
     * 
     * @return the value of field 'ref'.
     */
    public java.lang.Object getRef()
    {
        return this._ref;
    } //-- java.lang.Object getRef() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.FIELDItemTypeType getType()
    {
        return this._type;
    } //-- org.astrogrid.registry.beans.resource.votable.types.FIELDItemTypeType getType() 

    /**
     * Returns the value of field 'ucd'.
     * 
     * @return the value of field 'ucd'.
     */
    public java.lang.String getUcd()
    {
        return this._ucd;
    } //-- java.lang.String getUcd() 

    /**
     * Returns the value of field 'unit'.
     * 
     * @return the value of field 'unit'.
     */
    public java.lang.String getUnit()
    {
        return this._unit;
    } //-- java.lang.String getUnit() 

    /**
     * Returns the value of field 'width'.
     * 
     * @return the value of field 'width'.
     */
    public int getWidth()
    {
        return this._width;
    } //-- int getWidth() 

    /**
     * Method hasWidth
     */
    public boolean hasWidth()
    {
        return this._has_width;
    } //-- boolean hasWidth() 

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
     * Method removeFIELDItem
     * 
     * @param vFIELDItem
     */
    public boolean removeFIELDItem(org.astrogrid.registry.beans.resource.votable.FIELDItem vFIELDItem)
    {
        boolean removed = _items.remove(vFIELDItem);
        return removed;
    } //-- boolean removeFIELDItem(org.astrogrid.registry.beans.resource.votable.FIELDItem) 

    /**
     * Sets the value of field 'arraysize'.
     * 
     * @param arraysize the value of field 'arraysize'.
     */
    public void setArraysize(java.lang.String arraysize)
    {
        this._arraysize = arraysize;
    } //-- void setArraysize(java.lang.String) 

    /**
     * Sets the value of field 'datatype'.
     * 
     * @param datatype the value of field 'datatype'.
     */
    public void setDatatype(org.astrogrid.registry.beans.resource.votable.types.DataType datatype)
    {
        this._datatype = datatype;
    } //-- void setDatatype(org.astrogrid.registry.beans.resource.votable.types.DataType) 

    /**
     * Method setFIELDItem
     * 
     * @param index
     * @param vFIELDItem
     */
    public void setFIELDItem(int index, org.astrogrid.registry.beans.resource.votable.FIELDItem vFIELDItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.set(index, vFIELDItem);
    } //-- void setFIELDItem(int, org.astrogrid.registry.beans.resource.votable.FIELDItem) 

    /**
     * Method setFIELDItem
     * 
     * @param FIELDItemArray
     */
    public void setFIELDItem(org.astrogrid.registry.beans.resource.votable.FIELDItem[] FIELDItemArray)
    {
        //-- copy array
        _items.clear();
        for (int i = 0; i < FIELDItemArray.length; i++) {
            _items.add(FIELDItemArray[i]);
        }
    } //-- void setFIELDItem(org.astrogrid.registry.beans.resource.votable.FIELDItem) 

    /**
     * Sets the value of field 'ID'.
     * 
     * @param ID the value of field 'ID'.
     */
    public void setID(java.lang.String ID)
    {
        this._ID = ID;
    } //-- void setID(java.lang.String) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'precision'.
     * 
     * @param precision the value of field 'precision'.
     */
    public void setPrecision(java.lang.String precision)
    {
        this._precision = precision;
    } //-- void setPrecision(java.lang.String) 

    /**
     * Sets the value of field 'ref'.
     * 
     * @param ref the value of field 'ref'.
     */
    public void setRef(java.lang.Object ref)
    {
        this._ref = ref;
    } //-- void setRef(java.lang.Object) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.astrogrid.registry.beans.resource.votable.types.FIELDItemTypeType type)
    {
        this._type = type;
    } //-- void setType(org.astrogrid.registry.beans.resource.votable.types.FIELDItemTypeType) 

    /**
     * Sets the value of field 'ucd'.
     * 
     * @param ucd the value of field 'ucd'.
     */
    public void setUcd(java.lang.String ucd)
    {
        this._ucd = ucd;
    } //-- void setUcd(java.lang.String) 

    /**
     * Sets the value of field 'unit'.
     * 
     * @param unit the value of field 'unit'.
     */
    public void setUnit(java.lang.String unit)
    {
        this._unit = unit;
    } //-- void setUnit(java.lang.String) 

    /**
     * Sets the value of field 'width'.
     * 
     * @param width the value of field 'width'.
     */
    public void setWidth(int width)
    {
        this._width = width;
        this._has_width = true;
    } //-- void setWidth(int) 

    /**
     * Method unmarshalFIELD
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.FIELD unmarshalFIELD(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.FIELD) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.FIELD.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.FIELD unmarshalFIELD(java.io.Reader) 

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
