/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WebHttpCall.java,v 1.2 2005/07/05 08:26:57 clq2 Exp $
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
 * Description of an HTTP get or post service
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:57 $
 */
public class WebHttpCall extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _URL
     */
    private org.astrogrid.applications.beans.v1.HttpURLType _URL;

    /**
     * Field _simpleParameterList
     */
    private java.util.ArrayList _simpleParameterList;


      //----------------/
     //- Constructors -/
    //----------------/

    public WebHttpCall() {
        super();
        _simpleParameterList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.WebHttpCall()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addSimpleParameter
     * 
     * @param vSimpleParameter
     */
    public void addSimpleParameter(org.astrogrid.applications.beans.v1.SimpleParameter vSimpleParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        _simpleParameterList.add(vSimpleParameter);
    } //-- void addSimpleParameter(org.astrogrid.applications.beans.v1.SimpleParameter) 

    /**
     * Method addSimpleParameter
     * 
     * @param index
     * @param vSimpleParameter
     */
    public void addSimpleParameter(int index, org.astrogrid.applications.beans.v1.SimpleParameter vSimpleParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        _simpleParameterList.add(index, vSimpleParameter);
    } //-- void addSimpleParameter(int, org.astrogrid.applications.beans.v1.SimpleParameter) 

    /**
     * Method clearSimpleParameter
     */
    public void clearSimpleParameter()
    {
        _simpleParameterList.clear();
    } //-- void clearSimpleParameter() 

    /**
     * Method enumerateSimpleParameter
     */
    public java.util.Enumeration enumerateSimpleParameter()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_simpleParameterList.iterator());
    } //-- java.util.Enumeration enumerateSimpleParameter() 

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
        
        if (obj instanceof WebHttpCall) {
        
            WebHttpCall temp = (WebHttpCall)obj;
            if (this._URL != null) {
                if (temp._URL == null) return false;
                else if (!(this._URL.equals(temp._URL))) 
                    return false;
            }
            else if (temp._URL != null)
                return false;
            if (this._simpleParameterList != null) {
                if (temp._simpleParameterList == null) return false;
                else if (!(this._simpleParameterList.equals(temp._simpleParameterList))) 
                    return false;
            }
            else if (temp._simpleParameterList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getSimpleParameter
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.SimpleParameter getSimpleParameter(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _simpleParameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.SimpleParameter) _simpleParameterList.get(index);
    } //-- org.astrogrid.applications.beans.v1.SimpleParameter getSimpleParameter(int) 

    /**
     * Method getSimpleParameter
     */
    public org.astrogrid.applications.beans.v1.SimpleParameter[] getSimpleParameter()
    {
        int size = _simpleParameterList.size();
        org.astrogrid.applications.beans.v1.SimpleParameter[] mArray = new org.astrogrid.applications.beans.v1.SimpleParameter[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.SimpleParameter) _simpleParameterList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.SimpleParameter[] getSimpleParameter() 

    /**
     * Method getSimpleParameterCount
     */
    public int getSimpleParameterCount()
    {
        return _simpleParameterList.size();
    } //-- int getSimpleParameterCount() 

    /**
     * Returns the value of field 'URL'.
     * 
     * @return the value of field 'URL'.
     */
    public org.astrogrid.applications.beans.v1.HttpURLType getURL()
    {
        return this._URL;
    } //-- org.astrogrid.applications.beans.v1.HttpURLType getURL() 

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
     * Method removeSimpleParameter
     * 
     * @param vSimpleParameter
     */
    public boolean removeSimpleParameter(org.astrogrid.applications.beans.v1.SimpleParameter vSimpleParameter)
    {
        boolean removed = _simpleParameterList.remove(vSimpleParameter);
        return removed;
    } //-- boolean removeSimpleParameter(org.astrogrid.applications.beans.v1.SimpleParameter) 

    /**
     * Method setSimpleParameter
     * 
     * @param index
     * @param vSimpleParameter
     */
    public void setSimpleParameter(int index, org.astrogrid.applications.beans.v1.SimpleParameter vSimpleParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _simpleParameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _simpleParameterList.set(index, vSimpleParameter);
    } //-- void setSimpleParameter(int, org.astrogrid.applications.beans.v1.SimpleParameter) 

    /**
     * Method setSimpleParameter
     * 
     * @param simpleParameterArray
     */
    public void setSimpleParameter(org.astrogrid.applications.beans.v1.SimpleParameter[] simpleParameterArray)
    {
        //-- copy array
        _simpleParameterList.clear();
        for (int i = 0; i < simpleParameterArray.length; i++) {
            _simpleParameterList.add(simpleParameterArray[i]);
        }
    } //-- void setSimpleParameter(org.astrogrid.applications.beans.v1.SimpleParameter) 

    /**
     * Sets the value of field 'URL'.
     * 
     * @param URL the value of field 'URL'.
     */
    public void setURL(org.astrogrid.applications.beans.v1.HttpURLType URL)
    {
        this._URL = URL;
    } //-- void setURL(org.astrogrid.applications.beans.v1.HttpURLType) 

    /**
     * Method unmarshalWebHttpCall
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.WebHttpCall unmarshalWebHttpCall(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.WebHttpCall) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.WebHttpCall.class, reader);
    } //-- org.astrogrid.applications.beans.v1.WebHttpCall unmarshalWebHttpCall(java.io.Reader) 

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
