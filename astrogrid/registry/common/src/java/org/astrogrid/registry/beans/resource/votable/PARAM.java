/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: PARAM.java,v 1.8 2004/04/05 14:36:11 KevinBenson Exp $
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class PARAM.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/04/05 14:36:11 $
 */
public class PARAM extends org.astrogrid.common.bean.BaseBean 
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
     * Field _value
     */
    private java.lang.String _value;

    /**
     * Field _arraysize
     */
    private java.lang.String _arraysize;

    /**
     * Field _DESCRIPTION
     */
    private org.astrogrid.registry.beans.resource.votable.AnyTEXT _DESCRIPTION;

    /**
     * Field _VALUES
     */
    private org.astrogrid.registry.beans.resource.votable.VALUES _VALUES;

    /**
     * Field _LINKList
     */
    private java.util.ArrayList _LINKList;


      //----------------/
     //- Constructors -/
    //----------------/

    public PARAM() {
        super();
        _LINKList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.PARAM()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addLINK
     * 
     * @param vLINK
     */
    public void addLINK(org.astrogrid.registry.beans.resource.votable.LINK vLINK)
        throws java.lang.IndexOutOfBoundsException
    {
        _LINKList.add(vLINK);
    } //-- void addLINK(org.astrogrid.registry.beans.resource.votable.LINK) 

    /**
     * Method addLINK
     * 
     * @param index
     * @param vLINK
     */
    public void addLINK(int index, org.astrogrid.registry.beans.resource.votable.LINK vLINK)
        throws java.lang.IndexOutOfBoundsException
    {
        _LINKList.add(index, vLINK);
    } //-- void addLINK(int, org.astrogrid.registry.beans.resource.votable.LINK) 

    /**
     * Method clearLINK
     */
    public void clearLINK()
    {
        _LINKList.clear();
    } //-- void clearLINK() 

    /**
     * Method deleteWidth
     */
    public void deleteWidth()
    {
        this._has_width= false;
    } //-- void deleteWidth() 

    /**
     * Method enumerateLINK
     */
    public java.util.Enumeration enumerateLINK()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_LINKList.iterator());
    } //-- java.util.Enumeration enumerateLINK() 

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
     * Returns the value of field 'DESCRIPTION'.
     * 
     * @return the value of field 'DESCRIPTION'.
     */
    public org.astrogrid.registry.beans.resource.votable.AnyTEXT getDESCRIPTION()
    {
        return this._DESCRIPTION;
    } //-- org.astrogrid.registry.beans.resource.votable.AnyTEXT getDESCRIPTION() 

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
     * Returns the value of field 'ID'.
     * 
     * @return the value of field 'ID'.
     */
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

    /**
     * Method getLINK
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.LINK getLINK(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _LINKList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.LINK) _LINKList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.LINK getLINK(int) 

    /**
     * Method getLINK
     */
    public org.astrogrid.registry.beans.resource.votable.LINK[] getLINK()
    {
        int size = _LINKList.size();
        org.astrogrid.registry.beans.resource.votable.LINK[] mArray = new org.astrogrid.registry.beans.resource.votable.LINK[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.LINK) _LINKList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.LINK[] getLINK() 

    /**
     * Method getLINKCount
     */
    public int getLINKCount()
    {
        return _LINKList.size();
    } //-- int getLINKCount() 

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
     * Returns the value of field 'VALUES'.
     * 
     * @return the value of field 'VALUES'.
     */
    public org.astrogrid.registry.beans.resource.votable.VALUES getVALUES()
    {
        return this._VALUES;
    } //-- org.astrogrid.registry.beans.resource.votable.VALUES getVALUES() 

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public java.lang.String getValue()
    {
        return this._value;
    } //-- java.lang.String getValue() 

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
     * Method removeLINK
     * 
     * @param vLINK
     */
    public boolean removeLINK(org.astrogrid.registry.beans.resource.votable.LINK vLINK)
    {
        boolean removed = _LINKList.remove(vLINK);
        return removed;
    } //-- boolean removeLINK(org.astrogrid.registry.beans.resource.votable.LINK) 

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
     * Sets the value of field 'DESCRIPTION'.
     * 
     * @param DESCRIPTION the value of field 'DESCRIPTION'.
     */
    public void setDESCRIPTION(org.astrogrid.registry.beans.resource.votable.AnyTEXT DESCRIPTION)
    {
        this._DESCRIPTION = DESCRIPTION;
    } //-- void setDESCRIPTION(org.astrogrid.registry.beans.resource.votable.AnyTEXT) 

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
     * Sets the value of field 'ID'.
     * 
     * @param ID the value of field 'ID'.
     */
    public void setID(java.lang.String ID)
    {
        this._ID = ID;
    } //-- void setID(java.lang.String) 

    /**
     * Method setLINK
     * 
     * @param index
     * @param vLINK
     */
    public void setLINK(int index, org.astrogrid.registry.beans.resource.votable.LINK vLINK)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _LINKList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _LINKList.set(index, vLINK);
    } //-- void setLINK(int, org.astrogrid.registry.beans.resource.votable.LINK) 

    /**
     * Method setLINK
     * 
     * @param LINKArray
     */
    public void setLINK(org.astrogrid.registry.beans.resource.votable.LINK[] LINKArray)
    {
        //-- copy array
        _LINKList.clear();
        for (int i = 0; i < LINKArray.length; i++) {
            _LINKList.add(LINKArray[i]);
        }
    } //-- void setLINK(org.astrogrid.registry.beans.resource.votable.LINK) 

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
     * Sets the value of field 'VALUES'.
     * 
     * @param VALUES the value of field 'VALUES'.
     */
    public void setVALUES(org.astrogrid.registry.beans.resource.votable.VALUES VALUES)
    {
        this._VALUES = VALUES;
    } //-- void setVALUES(org.astrogrid.registry.beans.resource.votable.VALUES) 

    /**
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value)
    {
        this._value = value;
    } //-- void setValue(java.lang.String) 

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
     * Method unmarshalPARAM
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.PARAM unmarshalPARAM(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.PARAM) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.PARAM.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.PARAM unmarshalPARAM(java.io.Reader) 

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
