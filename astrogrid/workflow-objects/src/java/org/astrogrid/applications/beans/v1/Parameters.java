/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Parameters.java,v 1.15 2004/03/11 11:54:13 pah Exp $
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
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Parameters.
 * 
 * @version $Revision: 1.15 $ $Date: 2004/03/11 11:54:13 $
 */
public class Parameters extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * A generalised parameter - this is a substitution point
     */
    private java.util.ArrayList _parameterList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Parameters() {
        super();
        _parameterList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.Parameters()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addParameter
     * 
     * @param vParameter
     */
    public void addParameter(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition vParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterList.add(vParameter);
    } //-- void addParameter(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method addParameter
     * 
     * @param index
     * @param vParameter
     */
    public void addParameter(int index, org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition vParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterList.add(index, vParameter);
    } //-- void addParameter(int, org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method clearParameter
     */
    public void clearParameter()
    {
        _parameterList.clear();
    } //-- void clearParameter() 

    /**
     * Method enumerateParameter
     */
    public java.util.Enumeration enumerateParameter()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_parameterList.iterator());
    } //-- java.util.Enumeration enumerateParameter() 

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
        
        if (obj instanceof Parameters) {
        
            Parameters temp = (Parameters)obj;
            if (this._parameterList != null) {
                if (temp._parameterList == null) return false;
                else if (!(this._parameterList.equals(temp._parameterList))) 
                    return false;
            }
            else if (temp._parameterList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getParameter
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition getParameter(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) _parameterList.get(index);
    } //-- org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition getParameter(int) 

    /**
     * Method getParameter
     */
    public org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[] getParameter()
    {
        int size = _parameterList.size();
        org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[] mArray = new org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) _parameterList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[] getParameter() 

    /**
     * Method getParameterCount
     */
    public int getParameterCount()
    {
        return _parameterList.size();
    } //-- int getParameterCount() 

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
     * Method removeParameter
     * 
     * @param vParameter
     */
    public boolean removeParameter(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition vParameter)
    {
        boolean removed = _parameterList.remove(vParameter);
        return removed;
    } //-- boolean removeParameter(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method setParameter
     * 
     * @param index
     * @param vParameter
     */
    public void setParameter(int index, org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition vParameter)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _parameterList.set(index, vParameter);
    } //-- void setParameter(int, org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method setParameter
     * 
     * @param parameterArray
     */
    public void setParameter(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[] parameterArray)
    {
        //-- copy array
        _parameterList.clear();
        for (int i = 0; i < parameterArray.length; i++) {
            _parameterList.add(parameterArray[i]);
        }
    } //-- void setParameter(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method unmarshalParameters
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.Parameters unmarshalParameters(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.Parameters) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.Parameters.class, reader);
    } //-- org.astrogrid.applications.beans.v1.Parameters unmarshalParameters(java.io.Reader) 

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
