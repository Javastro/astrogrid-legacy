/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Input.java,v 1.4 2004/03/02 16:57:19 nw Exp $
 */

package org.astrogrid.applications.beans.v1;

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
 * Class Input.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/02 16:57:19 $
 */
public class Input extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a reference to a parameter
     */
    private java.util.ArrayList _prefList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Input() {
        super();
        _prefList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.Input()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPref
     * 
     * @param vPref
     */
    public void addPref(org.astrogrid.applications.beans.v1.Pref vPref)
        throws java.lang.IndexOutOfBoundsException
    {
        _prefList.add(vPref);
    } //-- void addPref(org.astrogrid.applications.beans.v1.Pref) 

    /**
     * Method addPref
     * 
     * @param index
     * @param vPref
     */
    public void addPref(int index, org.astrogrid.applications.beans.v1.Pref vPref)
        throws java.lang.IndexOutOfBoundsException
    {
        _prefList.add(index, vPref);
    } //-- void addPref(int, org.astrogrid.applications.beans.v1.Pref) 

    /**
     * Method clearPref
     */
    public void clearPref()
    {
        _prefList.clear();
    } //-- void clearPref() 

    /**
     * Method enumeratePref
     */
    public java.util.Enumeration enumeratePref()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_prefList.iterator());
    } //-- java.util.Enumeration enumeratePref() 

    /**
     * Method getPref
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.Pref getPref(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _prefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.Pref) _prefList.get(index);
    } //-- org.astrogrid.applications.beans.v1.Pref getPref(int) 

    /**
     * Method getPref
     */
    public org.astrogrid.applications.beans.v1.Pref[] getPref()
    {
        int size = _prefList.size();
        org.astrogrid.applications.beans.v1.Pref[] mArray = new org.astrogrid.applications.beans.v1.Pref[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.Pref) _prefList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.Pref[] getPref() 

    /**
     * Method getPrefCount
     */
    public int getPrefCount()
    {
        return _prefList.size();
    } //-- int getPrefCount() 

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
     * Method removePref
     * 
     * @param vPref
     */
    public boolean removePref(org.astrogrid.applications.beans.v1.Pref vPref)
    {
        boolean removed = _prefList.remove(vPref);
        return removed;
    } //-- boolean removePref(org.astrogrid.applications.beans.v1.Pref) 

    /**
     * Method setPref
     * 
     * @param index
     * @param vPref
     */
    public void setPref(int index, org.astrogrid.applications.beans.v1.Pref vPref)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _prefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _prefList.set(index, vPref);
    } //-- void setPref(int, org.astrogrid.applications.beans.v1.Pref) 

    /**
     * Method setPref
     * 
     * @param prefArray
     */
    public void setPref(org.astrogrid.applications.beans.v1.Pref[] prefArray)
    {
        //-- copy array
        _prefList.clear();
        for (int i = 0; i < prefArray.length; i++) {
            _prefList.add(prefArray[i]);
        }
    } //-- void setPref(org.astrogrid.applications.beans.v1.Pref) 

    /**
     * Method unmarshalInput
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.Input unmarshalInput(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.Input) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.Input.class, reader);
    } //-- org.astrogrid.applications.beans.v1.Input unmarshalInput(java.io.Reader) 

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
