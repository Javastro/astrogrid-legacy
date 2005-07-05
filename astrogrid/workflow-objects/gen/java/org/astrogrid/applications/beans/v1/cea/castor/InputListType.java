/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: InputListType.java,v 1.2 2005/07/05 08:26:58 clq2 Exp $
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
 * Class InputListType.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:58 $
 */
public class InputListType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _inputList
     */
    private java.util.ArrayList _inputList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InputListType() {
        super();
        _inputList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.cea.castor.InputListType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addInput
     * 
     * @param vInput
     */
    public void addInput(org.astrogrid.applications.beans.v1.parameters.ParameterValue vInput)
        throws java.lang.IndexOutOfBoundsException
    {
        _inputList.add(vInput);
    } //-- void addInput(org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method addInput
     * 
     * @param index
     * @param vInput
     */
    public void addInput(int index, org.astrogrid.applications.beans.v1.parameters.ParameterValue vInput)
        throws java.lang.IndexOutOfBoundsException
    {
        _inputList.add(index, vInput);
    } //-- void addInput(int, org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method clearInput
     */
    public void clearInput()
    {
        _inputList.clear();
    } //-- void clearInput() 

    /**
     * Method enumerateInput
     */
    public java.util.Enumeration enumerateInput()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_inputList.iterator());
    } //-- java.util.Enumeration enumerateInput() 

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
        
        if (obj instanceof InputListType) {
        
            InputListType temp = (InputListType)obj;
            if (this._inputList != null) {
                if (temp._inputList == null) return false;
                else if (!(this._inputList.equals(temp._inputList))) 
                    return false;
            }
            else if (temp._inputList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getInput
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.parameters.ParameterValue getInput(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _inputList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.parameters.ParameterValue) _inputList.get(index);
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue getInput(int) 

    /**
     * Method getInput
     */
    public org.astrogrid.applications.beans.v1.parameters.ParameterValue[] getInput()
    {
        int size = _inputList.size();
        org.astrogrid.applications.beans.v1.parameters.ParameterValue[] mArray = new org.astrogrid.applications.beans.v1.parameters.ParameterValue[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.parameters.ParameterValue) _inputList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue[] getInput() 

    /**
     * Method getInputCount
     */
    public int getInputCount()
    {
        return _inputList.size();
    } //-- int getInputCount() 

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
     * Method removeInput
     * 
     * @param vInput
     */
    public boolean removeInput(org.astrogrid.applications.beans.v1.parameters.ParameterValue vInput)
    {
        boolean removed = _inputList.remove(vInput);
        return removed;
    } //-- boolean removeInput(org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method setInput
     * 
     * @param index
     * @param vInput
     */
    public void setInput(int index, org.astrogrid.applications.beans.v1.parameters.ParameterValue vInput)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _inputList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _inputList.set(index, vInput);
    } //-- void setInput(int, org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method setInput
     * 
     * @param inputArray
     */
    public void setInput(org.astrogrid.applications.beans.v1.parameters.ParameterValue[] inputArray)
    {
        //-- copy array
        _inputList.clear();
        for (int i = 0; i < inputArray.length; i++) {
            _inputList.add(inputArray[i]);
        }
    } //-- void setInput(org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method unmarshalInputListType
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.cea.castor.InputListType unmarshalInputListType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.cea.castor.InputListType) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.cea.castor.InputListType.class, reader);
    } //-- org.astrogrid.applications.beans.v1.cea.castor.InputListType unmarshalInputListType(java.io.Reader) 

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
