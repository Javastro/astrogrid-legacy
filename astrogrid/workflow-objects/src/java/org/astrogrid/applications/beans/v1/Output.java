/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Output.java,v 1.14 2004/03/10 17:13:59 pah Exp $
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
 * Class Output.
 * 
 * @version $Revision: 1.14 $ $Date: 2004/03/10 17:13:59 $
 */
public class Output extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _prefList
     */
    private java.util.ArrayList _prefList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Output() {
        super();
        _prefList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.Output()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPref
     * 
     * @param vPref
     */
    public void addPref(org.astrogrid.applications.beans.v1.ParameterRef vPref)
        throws java.lang.IndexOutOfBoundsException
    {
        _prefList.add(vPref);
    } //-- void addPref(org.astrogrid.applications.beans.v1.ParameterRef) 

    /**
     * Method addPref
     * 
     * @param index
     * @param vPref
     */
    public void addPref(int index, org.astrogrid.applications.beans.v1.ParameterRef vPref)
        throws java.lang.IndexOutOfBoundsException
    {
        _prefList.add(index, vPref);
    } //-- void addPref(int, org.astrogrid.applications.beans.v1.ParameterRef) 

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
        
        if (obj instanceof Output) {
        
            Output temp = (Output)obj;
            if (this._prefList != null) {
                if (temp._prefList == null) return false;
                else if (!(this._prefList.equals(temp._prefList))) 
                    return false;
            }
            else if (temp._prefList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getPref
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.ParameterRef getPref(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _prefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.ParameterRef) _prefList.get(index);
    } //-- org.astrogrid.applications.beans.v1.ParameterRef getPref(int) 

    /**
     * Method getPref
     */
    public org.astrogrid.applications.beans.v1.ParameterRef[] getPref()
    {
        int size = _prefList.size();
        org.astrogrid.applications.beans.v1.ParameterRef[] mArray = new org.astrogrid.applications.beans.v1.ParameterRef[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.ParameterRef) _prefList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.ParameterRef[] getPref() 

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
    public boolean removePref(org.astrogrid.applications.beans.v1.ParameterRef vPref)
    {
        boolean removed = _prefList.remove(vPref);
        return removed;
    } //-- boolean removePref(org.astrogrid.applications.beans.v1.ParameterRef) 

    /**
     * Method setPref
     * 
     * @param index
     * @param vPref
     */
    public void setPref(int index, org.astrogrid.applications.beans.v1.ParameterRef vPref)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _prefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _prefList.set(index, vPref);
    } //-- void setPref(int, org.astrogrid.applications.beans.v1.ParameterRef) 

    /**
     * Method setPref
     * 
     * @param prefArray
     */
    public void setPref(org.astrogrid.applications.beans.v1.ParameterRef[] prefArray)
    {
        //-- copy array
        _prefList.clear();
        for (int i = 0; i < prefArray.length; i++) {
            _prefList.add(prefArray[i]);
        }
    } //-- void setPref(org.astrogrid.applications.beans.v1.ParameterRef) 

    /**
     * Method unmarshalOutput
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.Output unmarshalOutput(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.Output) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.Output.class, reader);
    } //-- org.astrogrid.applications.beans.v1.Output unmarshalOutput(java.io.Reader) 

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
