/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: VALUES.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.astrogrid.registry.generated.package.types.VALUESTypeType;
import org.astrogrid.registry.generated.package.types.Yesno;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class VALUES.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class VALUES implements java.io.Serializable {


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
    private org.astrogrid.registry.generated.package.types.VALUESTypeType _type = org.astrogrid.registry.generated.package.types.VALUESTypeType.valueOf("legal");

    /**
     * Field _null
     */
    private java.lang.String _null;

    /**
     * Field _invalid
     */
    private org.astrogrid.registry.generated.package.types.Yesno _invalid = org.astrogrid.registry.generated.package.types.Yesno.valueOf("no");

    /**
     * Field _MIN
     */
    private org.astrogrid.registry.generated.package.MIN _MIN;

    /**
     * Field _MAX
     */
    private org.astrogrid.registry.generated.package.MAX _MAX;

    /**
     * Field _OPTIONList
     */
    private java.util.Vector _OPTIONList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VALUES() {
        super();
        setType(org.astrogrid.registry.generated.package.types.VALUESTypeType.valueOf("legal"));
        setInvalid(org.astrogrid.registry.generated.package.types.Yesno.valueOf("no"));
        _OPTIONList = new Vector();
    } //-- org.astrogrid.registry.generated.package.VALUES()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addOPTION
     * 
     * @param vOPTION
     */
    public void addOPTION(org.astrogrid.registry.generated.package.OPTION vOPTION)
        throws java.lang.IndexOutOfBoundsException
    {
        _OPTIONList.addElement(vOPTION);
    } //-- void addOPTION(org.astrogrid.registry.generated.package.OPTION) 

    /**
     * Method addOPTION
     * 
     * @param index
     * @param vOPTION
     */
    public void addOPTION(int index, org.astrogrid.registry.generated.package.OPTION vOPTION)
        throws java.lang.IndexOutOfBoundsException
    {
        _OPTIONList.insertElementAt(vOPTION, index);
    } //-- void addOPTION(int, org.astrogrid.registry.generated.package.OPTION) 

    /**
     * Method enumerateOPTION
     */
    public java.util.Enumeration enumerateOPTION()
    {
        return _OPTIONList.elements();
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
    public org.astrogrid.registry.generated.package.types.Yesno getInvalid()
    {
        return this._invalid;
    } //-- org.astrogrid.registry.generated.package.types.Yesno getInvalid() 

    /**
     * Returns the value of field 'MAX'.
     * 
     * @return the value of field 'MAX'.
     */
    public org.astrogrid.registry.generated.package.MAX getMAX()
    {
        return this._MAX;
    } //-- org.astrogrid.registry.generated.package.MAX getMAX() 

    /**
     * Returns the value of field 'MIN'.
     * 
     * @return the value of field 'MIN'.
     */
    public org.astrogrid.registry.generated.package.MIN getMIN()
    {
        return this._MIN;
    } //-- org.astrogrid.registry.generated.package.MIN getMIN() 

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
    public org.astrogrid.registry.generated.package.OPTION getOPTION(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _OPTIONList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.OPTION) _OPTIONList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.OPTION getOPTION(int) 

    /**
     * Method getOPTION
     */
    public org.astrogrid.registry.generated.package.OPTION[] getOPTION()
    {
        int size = _OPTIONList.size();
        org.astrogrid.registry.generated.package.OPTION[] mArray = new org.astrogrid.registry.generated.package.OPTION[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.OPTION) _OPTIONList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.OPTION[] getOPTION() 

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
    public org.astrogrid.registry.generated.package.types.VALUESTypeType getType()
    {
        return this._type;
    } //-- org.astrogrid.registry.generated.package.types.VALUESTypeType getType() 

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
     * Method removeAllOPTION
     */
    public void removeAllOPTION()
    {
        _OPTIONList.removeAllElements();
    } //-- void removeAllOPTION() 

    /**
     * Method removeOPTION
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.OPTION removeOPTION(int index)
    {
        java.lang.Object obj = _OPTIONList.elementAt(index);
        _OPTIONList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.OPTION) obj;
    } //-- org.astrogrid.registry.generated.package.OPTION removeOPTION(int) 

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
    public void setInvalid(org.astrogrid.registry.generated.package.types.Yesno invalid)
    {
        this._invalid = invalid;
    } //-- void setInvalid(org.astrogrid.registry.generated.package.types.Yesno) 

    /**
     * Sets the value of field 'MAX'.
     * 
     * @param MAX the value of field 'MAX'.
     */
    public void setMAX(org.astrogrid.registry.generated.package.MAX MAX)
    {
        this._MAX = MAX;
    } //-- void setMAX(org.astrogrid.registry.generated.package.MAX) 

    /**
     * Sets the value of field 'MIN'.
     * 
     * @param MIN the value of field 'MIN'.
     */
    public void setMIN(org.astrogrid.registry.generated.package.MIN MIN)
    {
        this._MIN = MIN;
    } //-- void setMIN(org.astrogrid.registry.generated.package.MIN) 

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
    public void setOPTION(int index, org.astrogrid.registry.generated.package.OPTION vOPTION)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _OPTIONList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _OPTIONList.setElementAt(vOPTION, index);
    } //-- void setOPTION(int, org.astrogrid.registry.generated.package.OPTION) 

    /**
     * Method setOPTION
     * 
     * @param OPTIONArray
     */
    public void setOPTION(org.astrogrid.registry.generated.package.OPTION[] OPTIONArray)
    {
        //-- copy array
        _OPTIONList.removeAllElements();
        for (int i = 0; i < OPTIONArray.length; i++) {
            _OPTIONList.addElement(OPTIONArray[i]);
        }
    } //-- void setOPTION(org.astrogrid.registry.generated.package.OPTION) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.astrogrid.registry.generated.package.types.VALUESTypeType type)
    {
        this._type = type;
    } //-- void setType(org.astrogrid.registry.generated.package.types.VALUESTypeType) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.VALUES) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.VALUES.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
