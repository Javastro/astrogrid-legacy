/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: OPTION.java,v 1.12 2005/04/25 12:09:29 clq2 Exp $
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
 * Class OPTION.
 * 
 * @version $Revision: 1.12 $ $Date: 2005/04/25 12:09:29 $
 */
public class OPTION extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _value
     */
    private java.lang.String _value;

    /**
     * Field _OPTIONList
     */
    private java.util.ArrayList _OPTIONList;


      //----------------/
     //- Constructors -/
    //----------------/

    public OPTION() {
        super();
        _OPTIONList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.OPTION()


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
        
        if (obj instanceof OPTION) {
        
            OPTION temp = (OPTION)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._value != null) {
                if (temp._value == null) return false;
                else if (!(this._value.equals(temp._value))) 
                    return false;
            }
            else if (temp._value != null)
                return false;
            if (this._OPTIONList != null) {
                if (temp._OPTIONList == null) return false;
                else if (!(this._OPTIONList.equals(temp._OPTIONList))) 
                    return false;
            }
            else if (temp._OPTIONList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public java.lang.String getValue()
    {
        return this._value;
    } //-- java.lang.String getValue() 

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
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

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
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value)
    {
        this._value = value;
    } //-- void setValue(java.lang.String) 

    /**
     * Method unmarshalOPTION
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.OPTION unmarshalOPTION(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.OPTION) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.OPTION.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.OPTION unmarshalOPTION(java.io.Reader) 

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
