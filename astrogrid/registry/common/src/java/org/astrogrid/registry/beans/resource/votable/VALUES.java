/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: VALUES.java,v 1.3 2004/03/05 09:52:02 KevinBenson Exp $
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
import org.astrogrid.registry.beans.resource.votable.types.VALUESTypeType;
import org.astrogrid.registry.beans.resource.votable.types.Yesno;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class VALUES.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:52:02 $
 */
public class VALUES extends org.astrogrid.common.bean.BaseBean 
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
     * Field _type
     */
    private org.astrogrid.registry.beans.resource.votable.types.VALUESTypeType _type = org.astrogrid.registry.beans.resource.votable.types.VALUESTypeType.valueOf("legal");

    /**
     * Field _null
     */
    private java.lang.String _null;

    /**
     * Field _invalid
     */
    private org.astrogrid.registry.beans.resource.votable.types.Yesno _invalid = org.astrogrid.registry.beans.resource.votable.types.Yesno.valueOf("no");

    /**
     * Field _MIN
     */
    private org.astrogrid.registry.beans.resource.votable.MIN _MIN;

    /**
     * Field _MAX
     */
    private org.astrogrid.registry.beans.resource.votable.MAX _MAX;

    /**
     * Field _OPTIONList
     */
    private java.util.ArrayList _OPTIONList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VALUES() {
        super();
        setType(org.astrogrid.registry.beans.resource.votable.types.VALUESTypeType.valueOf("legal"));
        setInvalid(org.astrogrid.registry.beans.resource.votable.types.Yesno.valueOf("no"));
        _OPTIONList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.VALUES()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addOPTION
     * 
     * @param vOPTION
     */
    public void addOPTION(org.astrogrid.registry.beans.resource.votable.OPTION vOPTION)
        throws java.lang.IndexOutOfBoundsException
    {
        _OPTIONList.add(vOPTION);
    } //-- void addOPTION(org.astrogrid.registry.beans.resource.votable.OPTION) 

    /**
     * Method addOPTION
     * 
     * @param index
     * @param vOPTION
     */
    public void addOPTION(int index, org.astrogrid.registry.beans.resource.votable.OPTION vOPTION)
        throws java.lang.IndexOutOfBoundsException
    {
        _OPTIONList.add(index, vOPTION);
    } //-- void addOPTION(int, org.astrogrid.registry.beans.resource.votable.OPTION) 

    /**
     * Method clearOPTION
     */
    public void clearOPTION()
    {
        _OPTIONList.clear();
    } //-- void clearOPTION() 

    /**
     * Method enumerateOPTION
     */
    public java.util.Enumeration enumerateOPTION()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_OPTIONList.iterator());
    } //-- java.util.Enumeration enumerateOPTION() 

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
     * Returns the value of field 'invalid'.
     * 
     * @return the value of field 'invalid'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.Yesno getInvalid()
    {
        return this._invalid;
    } //-- org.astrogrid.registry.beans.resource.votable.types.Yesno getInvalid() 

    /**
     * Returns the value of field 'MAX'.
     * 
     * @return the value of field 'MAX'.
     */
    public org.astrogrid.registry.beans.resource.votable.MAX getMAX()
    {
        return this._MAX;
    } //-- org.astrogrid.registry.beans.resource.votable.MAX getMAX() 

    /**
     * Returns the value of field 'MIN'.
     * 
     * @return the value of field 'MIN'.
     */
    public org.astrogrid.registry.beans.resource.votable.MIN getMIN()
    {
        return this._MIN;
    } //-- org.astrogrid.registry.beans.resource.votable.MIN getMIN() 

    /**
     * Returns the value of field 'null'.
     * 
     * @return the value of field 'null'.
     */
    public java.lang.String getNull()
    {
        return this._null;
    } //-- java.lang.String getNull() 

    /**
     * Method getOPTION
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.OPTION getOPTION(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _OPTIONList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.OPTION) _OPTIONList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.OPTION getOPTION(int) 

    /**
     * Method getOPTION
     */
    public org.astrogrid.registry.beans.resource.votable.OPTION[] getOPTION()
    {
        int size = _OPTIONList.size();
        org.astrogrid.registry.beans.resource.votable.OPTION[] mArray = new org.astrogrid.registry.beans.resource.votable.OPTION[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.OPTION) _OPTIONList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.OPTION[] getOPTION() 

    /**
     * Method getOPTIONCount
     */
    public int getOPTIONCount()
    {
        return _OPTIONList.size();
    } //-- int getOPTIONCount() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.VALUESTypeType getType()
    {
        return this._type;
    } //-- org.astrogrid.registry.beans.resource.votable.types.VALUESTypeType getType() 

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
     * Method removeOPTION
     * 
     * @param vOPTION
     */
    public boolean removeOPTION(org.astrogrid.registry.beans.resource.votable.OPTION vOPTION)
    {
        boolean removed = _OPTIONList.remove(vOPTION);
        return removed;
    } //-- boolean removeOPTION(org.astrogrid.registry.beans.resource.votable.OPTION) 

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
     * Sets the value of field 'invalid'.
     * 
     * @param invalid the value of field 'invalid'.
     */
    public void setInvalid(org.astrogrid.registry.beans.resource.votable.types.Yesno invalid)
    {
        this._invalid = invalid;
    } //-- void setInvalid(org.astrogrid.registry.beans.resource.votable.types.Yesno) 

    /**
     * Sets the value of field 'MAX'.
     * 
     * @param MAX the value of field 'MAX'.
     */
    public void setMAX(org.astrogrid.registry.beans.resource.votable.MAX MAX)
    {
        this._MAX = MAX;
    } //-- void setMAX(org.astrogrid.registry.beans.resource.votable.MAX) 

    /**
     * Sets the value of field 'MIN'.
     * 
     * @param MIN the value of field 'MIN'.
     */
    public void setMIN(org.astrogrid.registry.beans.resource.votable.MIN MIN)
    {
        this._MIN = MIN;
    } //-- void setMIN(org.astrogrid.registry.beans.resource.votable.MIN) 

    /**
     * Sets the value of field 'null'.
     * 
     * @param _null
     * @param null the value of field 'null'.
     */
    public void setNull(java.lang.String _null)
    {
        this._null = _null;
    } //-- void setNull(java.lang.String) 

    /**
     * Method setOPTION
     * 
     * @param index
     * @param vOPTION
     */
    public void setOPTION(int index, org.astrogrid.registry.beans.resource.votable.OPTION vOPTION)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _OPTIONList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _OPTIONList.set(index, vOPTION);
    } //-- void setOPTION(int, org.astrogrid.registry.beans.resource.votable.OPTION) 

    /**
     * Method setOPTION
     * 
     * @param OPTIONArray
     */
    public void setOPTION(org.astrogrid.registry.beans.resource.votable.OPTION[] OPTIONArray)
    {
        //-- copy array
        _OPTIONList.clear();
        for (int i = 0; i < OPTIONArray.length; i++) {
            _OPTIONList.add(OPTIONArray[i]);
        }
    } //-- void setOPTION(org.astrogrid.registry.beans.resource.votable.OPTION) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.astrogrid.registry.beans.resource.votable.types.VALUESTypeType type)
    {
        this._type = type;
    } //-- void setType(org.astrogrid.registry.beans.resource.votable.types.VALUESTypeType) 

    /**
     * Method unmarshalVALUES
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.VALUES unmarshalVALUES(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.VALUES) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.VALUES.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.VALUES unmarshalVALUES(java.io.Reader) 

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
