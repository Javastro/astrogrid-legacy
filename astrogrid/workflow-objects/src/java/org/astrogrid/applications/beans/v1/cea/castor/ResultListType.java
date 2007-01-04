/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ResultListType.java,v 1.13 2007/01/04 16:26:32 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.cea.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ResultListType.
 * 
 * @version $Revision: 1.13 $ $Date: 2007/01/04 16:26:32 $
 */
public class ResultListType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resultList
     */
    private java.util.ArrayList _resultList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResultListType() {
        super();
        _resultList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.cea.castor.ResultListType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addResult
     * 
     * @param vResult
     */
    public void addResult(org.astrogrid.applications.beans.v1.parameters.ParameterValue vResult)
        throws java.lang.IndexOutOfBoundsException
    {
        _resultList.add(vResult);
    } //-- void addResult(org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method addResult
     * 
     * @param index
     * @param vResult
     */
    public void addResult(int index, org.astrogrid.applications.beans.v1.parameters.ParameterValue vResult)
        throws java.lang.IndexOutOfBoundsException
    {
        _resultList.add(index, vResult);
    } //-- void addResult(int, org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method clearResult
     */
    public void clearResult()
    {
        _resultList.clear();
    } //-- void clearResult() 

    /**
     * Method enumerateResult
     */
    public java.util.Enumeration enumerateResult()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_resultList.iterator());
    } //-- java.util.Enumeration enumerateResult() 

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
        
        if (obj instanceof ResultListType) {
        
            ResultListType temp = (ResultListType)obj;
            if (this._resultList != null) {
                if (temp._resultList == null) return false;
                else if (!(this._resultList.equals(temp._resultList))) 
                    return false;
            }
            else if (temp._resultList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getResult
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.parameters.ParameterValue getResult(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resultList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.parameters.ParameterValue) _resultList.get(index);
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue getResult(int) 

    /**
     * Method getResult
     */
    public org.astrogrid.applications.beans.v1.parameters.ParameterValue[] getResult()
    {
        int size = _resultList.size();
        org.astrogrid.applications.beans.v1.parameters.ParameterValue[] mArray = new org.astrogrid.applications.beans.v1.parameters.ParameterValue[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.parameters.ParameterValue) _resultList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue[] getResult() 

    /**
     * Method getResultCount
     */
    public int getResultCount()
    {
        return _resultList.size();
    } //-- int getResultCount() 

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
     * Method removeResult
     * 
     * @param vResult
     */
    public boolean removeResult(org.astrogrid.applications.beans.v1.parameters.ParameterValue vResult)
    {
        boolean removed = _resultList.remove(vResult);
        return removed;
    } //-- boolean removeResult(org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method setResult
     * 
     * @param index
     * @param vResult
     */
    public void setResult(int index, org.astrogrid.applications.beans.v1.parameters.ParameterValue vResult)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resultList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _resultList.set(index, vResult);
    } //-- void setResult(int, org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method setResult
     * 
     * @param resultArray
     */
    public void setResult(org.astrogrid.applications.beans.v1.parameters.ParameterValue[] resultArray)
    {
        //-- copy array
        _resultList.clear();
        for (int i = 0; i < resultArray.length; i++) {
            _resultList.add(resultArray[i]);
        }
    } //-- void setResult(org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method unmarshalResultListType
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.cea.castor.ResultListType unmarshalResultListType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.cea.castor.ResultListType) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.cea.castor.ResultListType.class, reader);
    } //-- org.astrogrid.applications.beans.v1.cea.castor.ResultListType unmarshalResultListType(java.io.Reader) 

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
