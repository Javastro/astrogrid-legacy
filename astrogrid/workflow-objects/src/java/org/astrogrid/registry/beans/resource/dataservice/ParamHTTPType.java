/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParamHTTPType.java,v 1.14 2007/01/04 16:26:07 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.resource.dataservice.types.HTTPQueryType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ParamHTTPType.
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:07 $
 */
public class ParamHTTPType extends org.astrogrid.registry.beans.resource.dataservice.ExtendedInterfaceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _qtype
     */
    private org.astrogrid.registry.beans.resource.dataservice.types.HTTPQueryType _qtype;

    /**
     * The MIME type of a document returned by an HTTP Get.
     *  
     */
    private java.lang.String _HTTPResults;

    /**
     * an input parameter. This should be rendered
     *  as name=value in the query URL's arguements. 
     *  
     */
    private java.util.ArrayList _paramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ParamHTTPType() {
        super();
        _paramList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.dataservice.ParamHTTPType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addParam
     * 
     * @param vParam
     */
    public void addParam(org.astrogrid.registry.beans.resource.dataservice.ParamType vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(vParam);
    } //-- void addParam(org.astrogrid.registry.beans.resource.dataservice.ParamType) 

    /**
     * Method addParam
     * 
     * @param index
     * @param vParam
     */
    public void addParam(int index, org.astrogrid.registry.beans.resource.dataservice.ParamType vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.add(index, vParam);
    } //-- void addParam(int, org.astrogrid.registry.beans.resource.dataservice.ParamType) 

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
        
        if (obj instanceof ParamHTTPType) {
        
            ParamHTTPType temp = (ParamHTTPType)obj;
            if (this._qtype != null) {
                if (temp._qtype == null) return false;
                else if (!(this._qtype.equals(temp._qtype))) 
                    return false;
            }
            else if (temp._qtype != null)
                return false;
            if (this._HTTPResults != null) {
                if (temp._HTTPResults == null) return false;
                else if (!(this._HTTPResults.equals(temp._HTTPResults))) 
                    return false;
            }
            else if (temp._HTTPResults != null)
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
     * Returns the value of field 'HTTPResults'. The field
     * 'HTTPResults' has the following description: The MIME type
     * of a document returned by an HTTP Get.
     *  
     * 
     * @return the value of field 'HTTPResults'.
     */
    public java.lang.String getHTTPResults()
    {
        return this._HTTPResults;
    } //-- java.lang.String getHTTPResults() 

    /**
     * Method getParam
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.dataservice.ParamType getParam(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.dataservice.ParamType) _paramList.get(index);
    } //-- org.astrogrid.registry.beans.resource.dataservice.ParamType getParam(int) 

    /**
     * Method getParam
     */
    public org.astrogrid.registry.beans.resource.dataservice.ParamType[] getParam()
    {
        int size = _paramList.size();
        org.astrogrid.registry.beans.resource.dataservice.ParamType[] mArray = new org.astrogrid.registry.beans.resource.dataservice.ParamType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.dataservice.ParamType) _paramList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.dataservice.ParamType[] getParam() 

    /**
     * Method getParamCount
     */
    public int getParamCount()
    {
        return _paramList.size();
    } //-- int getParamCount() 

    /**
     * Returns the value of field 'qtype'.
     * 
     * @return the value of field 'qtype'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.types.HTTPQueryType getQtype()
    {
        return this._qtype;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.HTTPQueryType getQtype() 

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
    public boolean removeParam(org.astrogrid.registry.beans.resource.dataservice.ParamType vParam)
    {
        boolean removed = _paramList.remove(vParam);
        return removed;
    } //-- boolean removeParam(org.astrogrid.registry.beans.resource.dataservice.ParamType) 

    /**
     * Sets the value of field 'HTTPResults'. The field
     * 'HTTPResults' has the following description: The MIME type
     * of a document returned by an HTTP Get.
     *  
     * 
     * @param HTTPResults the value of field 'HTTPResults'.
     */
    public void setHTTPResults(java.lang.String HTTPResults)
    {
        this._HTTPResults = HTTPResults;
    } //-- void setHTTPResults(java.lang.String) 

    /**
     * Method setParam
     * 
     * @param index
     * @param vParam
     */
    public void setParam(int index, org.astrogrid.registry.beans.resource.dataservice.ParamType vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _paramList.set(index, vParam);
    } //-- void setParam(int, org.astrogrid.registry.beans.resource.dataservice.ParamType) 

    /**
     * Method setParam
     * 
     * @param paramArray
     */
    public void setParam(org.astrogrid.registry.beans.resource.dataservice.ParamType[] paramArray)
    {
        //-- copy array
        _paramList.clear();
        for (int i = 0; i < paramArray.length; i++) {
            _paramList.add(paramArray[i]);
        }
    } //-- void setParam(org.astrogrid.registry.beans.resource.dataservice.ParamType) 

    /**
     * Sets the value of field 'qtype'.
     * 
     * @param qtype the value of field 'qtype'.
     */
    public void setQtype(org.astrogrid.registry.beans.resource.dataservice.types.HTTPQueryType qtype)
    {
        this._qtype = qtype;
    } //-- void setQtype(org.astrogrid.registry.beans.resource.dataservice.types.HTTPQueryType) 

    /**
     * Method unmarshalParamHTTPType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.ParamHTTPType unmarshalParamHTTPType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.ParamHTTPType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.ParamHTTPType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.ParamHTTPType unmarshalParamHTTPType(java.io.Reader) 

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
