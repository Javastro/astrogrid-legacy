/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: OptionList.java,v 1.8 2007/01/04 16:26:32 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.parameters;

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
 * For parameters that can only have one of a list of values
 * 
 * @version $Revision: 1.8 $ $Date: 2007/01/04 16:26:32 $
 */
public class OptionList extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _optionValList
     */
    private java.util.ArrayList _optionValList;


      //----------------/
     //- Constructors -/
    //----------------/

    public OptionList() {
        super();
        _optionValList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.parameters.OptionList()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addOptionVal
     * 
     * @param vOptionVal
     */
    public void addOptionVal(java.lang.String vOptionVal)
        throws java.lang.IndexOutOfBoundsException
    {
        _optionValList.add(vOptionVal);
    } //-- void addOptionVal(java.lang.String) 

    /**
     * Method addOptionVal
     * 
     * @param index
     * @param vOptionVal
     */
    public void addOptionVal(int index, java.lang.String vOptionVal)
        throws java.lang.IndexOutOfBoundsException
    {
        _optionValList.add(index, vOptionVal);
    } //-- void addOptionVal(int, java.lang.String) 

    /**
     * Method clearOptionVal
     */
    public void clearOptionVal()
    {
        _optionValList.clear();
    } //-- void clearOptionVal() 

    /**
     * Method enumerateOptionVal
     */
    public java.util.Enumeration enumerateOptionVal()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_optionValList.iterator());
    } //-- java.util.Enumeration enumerateOptionVal() 

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
        
        if (obj instanceof OptionList) {
        
            OptionList temp = (OptionList)obj;
            if (this._optionValList != null) {
                if (temp._optionValList == null) return false;
                else if (!(this._optionValList.equals(temp._optionValList))) 
                    return false;
            }
            else if (temp._optionValList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getOptionVal
     * 
     * @param index
     */
    public java.lang.String getOptionVal(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _optionValList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_optionValList.get(index);
    } //-- java.lang.String getOptionVal(int) 

    /**
     * Method getOptionVal
     */
    public java.lang.String[] getOptionVal()
    {
        int size = _optionValList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_optionValList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getOptionVal() 

    /**
     * Method getOptionValCount
     */
    public int getOptionValCount()
    {
        return _optionValList.size();
    } //-- int getOptionValCount() 

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
     * Method removeOptionVal
     * 
     * @param vOptionVal
     */
    public boolean removeOptionVal(java.lang.String vOptionVal)
    {
        boolean removed = _optionValList.remove(vOptionVal);
        return removed;
    } //-- boolean removeOptionVal(java.lang.String) 

    /**
     * Method setOptionVal
     * 
     * @param index
     * @param vOptionVal
     */
    public void setOptionVal(int index, java.lang.String vOptionVal)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _optionValList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _optionValList.set(index, vOptionVal);
    } //-- void setOptionVal(int, java.lang.String) 

    /**
     * Method setOptionVal
     * 
     * @param optionValArray
     */
    public void setOptionVal(java.lang.String[] optionValArray)
    {
        //-- copy array
        _optionValList.clear();
        for (int i = 0; i < optionValArray.length; i++) {
            _optionValList.add(optionValArray[i]);
        }
    } //-- void setOptionVal(java.lang.String) 

    /**
     * Method unmarshalOptionList
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.parameters.OptionList unmarshalOptionList(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.parameters.OptionList) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.parameters.OptionList.class, reader);
    } //-- org.astrogrid.applications.beans.v1.parameters.OptionList unmarshalOptionList(java.io.Reader) 

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
