/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParamHTTP.java,v 1.2 2007/01/04 16:26:25 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.v10.resource.dataservice.types.HTTPQueryType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * A service invoked via an HTTP Query (either Get or Post)
 *  with a set of arguments consisting of keyword name-value pairs.
 *  
 *  Note that the URL for help with this service can be put into
 *  the Service/ReferenceURL element.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:25 $
 */
public class ParamHTTP extends org.astrogrid.registry.beans.v10.resource.Interface 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The type of HTTP request, either GET or POST.
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.types.HTTPQueryType _qtype;

    /**
     * The MIME type of a document returned by an HTTP Get.
     *  
     */
    private java.lang.String _resultType;

    /**
     * a description of a input parameter. Each should be 
     *  rendered as name=value in the query URL's arguements. 
     *  
     */
    private java.util.ArrayList _paramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ParamHTTP() {
        super();
        _paramList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.ParamHTTP()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addParam
     * 
     * @param vParam
     */
    public void addParam(org.astrogrid.registry.beans.v10.resource.dataservice.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(vParam);
    } //-- void addParam(org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

    /**
     * Method addParam
     * 
     * @param index
     * @param vParam
     */
    public void addParam(int index, org.astrogrid.registry.beans.v10.resource.dataservice.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(index, vParam);
    } //-- void addParam(int, org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

    /**
     * Method clearParam
     */
    public void clearParam()
    {
        _paramList.clear();
    } //-- void clearParam() 

    /**
     * Method enumerateParam
     */
    public java.util.Enumeration enumerateParam()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_paramList.iterator());
    } //-- java.util.Enumeration enumerateParam() 

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
        
        if (obj instanceof ParamHTTP) {
        
            ParamHTTP temp = (ParamHTTP)obj;
            if (this._qtype != null) {
                if (temp._qtype == null) return false;
                else if (!(this._qtype.equals(temp._qtype))) 
                    return false;
            }
            else if (temp._qtype != null)
                return false;
            if (this._resultType != null) {
                if (temp._resultType == null) return false;
                else if (!(this._resultType.equals(temp._resultType))) 
                    return false;
            }
            else if (temp._resultType != null)
                return false;
            if (this._paramList != null) {
                if (temp._paramList == null) return false;
                else if (!(this._paramList.equals(temp._paramList))) 
                    return false;
            }
            else if (temp._paramList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getParam
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Param getParam(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Param) _paramList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Param getParam(int) 

    /**
     * Method getParam
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Param[] getParam()
    {
        int size = _paramList.size();
        org.astrogrid.registry.beans.v10.resource.dataservice.Param[] mArray = new org.astrogrid.registry.beans.v10.resource.dataservice.Param[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.dataservice.Param) _paramList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Param[] getParam() 

    /**
     * Method getParamCount
     */
    public int getParamCount()
    {
        return _paramList.size();
    } //-- int getParamCount() 

    /**
     * Returns the value of field 'qtype'. The field 'qtype' has
     * the following description: The type of HTTP request, either
     * GET or POST.
     *  
     * 
     * @return the value of field 'qtype'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.types.HTTPQueryType getQtype()
    {
        return this._qtype;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.HTTPQueryType getQtype() 

    /**
     * Returns the value of field 'resultType'. The field
     * 'resultType' has the following description: The MIME type of
     * a document returned by an HTTP Get.
     *  
     * 
     * @return the value of field 'resultType'.
     */
    public java.lang.String getResultType()
    {
        return this._resultType;
    } //-- java.lang.String getResultType() 

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
     * Method removeParam
     * 
     * @param vParam
     */
    public boolean removeParam(org.astrogrid.registry.beans.v10.resource.dataservice.Param vParam)
    {
        boolean removed = _paramList.remove(vParam);
        return removed;
    } //-- boolean removeParam(org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

    /**
     * Method setParam
     * 
     * @param index
     * @param vParam
     */
    public void setParam(int index, org.astrogrid.registry.beans.v10.resource.dataservice.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _paramList.set(index, vParam);
    } //-- void setParam(int, org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

    /**
     * Method setParam
     * 
     * @param paramArray
     */
    public void setParam(org.astrogrid.registry.beans.v10.resource.dataservice.Param[] paramArray)
    {
        //-- copy array
        _paramList.clear();
        for (int i = 0; i < paramArray.length; i++) {
            _paramList.add(paramArray[i]);
        }
    } //-- void setParam(org.astrogrid.registry.beans.v10.resource.dataservice.Param) 

    /**
     * Sets the value of field 'qtype'. The field 'qtype' has the
     * following description: The type of HTTP request, either GET
     * or POST.
     *  
     * 
     * @param qtype the value of field 'qtype'.
     */
    public void setQtype(org.astrogrid.registry.beans.v10.resource.dataservice.types.HTTPQueryType qtype)
    {
        this._qtype = qtype;
    } //-- void setQtype(org.astrogrid.registry.beans.v10.resource.dataservice.types.HTTPQueryType) 

    /**
     * Sets the value of field 'resultType'. The field 'resultType'
     * has the following description: The MIME type of a document
     * returned by an HTTP Get.
     *  
     * 
     * @param resultType the value of field 'resultType'.
     */
    public void setResultType(java.lang.String resultType)
    {
        this._resultType = resultType;
    } //-- void setResultType(java.lang.String) 

    /**
     * Method unmarshalParamHTTP
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.ParamHTTP unmarshalParamHTTP(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.ParamHTTP) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.ParamHTTP.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.ParamHTTP unmarshalParamHTTP(java.io.Reader) 

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
