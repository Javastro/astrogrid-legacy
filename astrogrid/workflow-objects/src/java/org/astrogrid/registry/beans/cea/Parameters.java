/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Parameters.java,v 1.1 2004/03/15 16:53:03 pah Exp $
 */

package org.astrogrid.registry.beans.cea;

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
 * @version $Revision: 1.1 $ $Date: 2004/03/15 16:53:03 $
 */
public class Parameters extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _parameterDefinitionList
     */
    private java.util.ArrayList _parameterDefinitionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Parameters() {
        super();
        _parameterDefinitionList = new ArrayList();
    } //-- org.astrogrid.registry.beans.cea.Parameters()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addParameterDefinition
     * 
     * @param vParameterDefinition
     */
    public void addParameterDefinition(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition vParameterDefinition)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterDefinitionList.add(vParameterDefinition);
    } //-- void addParameterDefinition(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method addParameterDefinition
     * 
     * @param index
     * @param vParameterDefinition
     */
    public void addParameterDefinition(int index, org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition vParameterDefinition)
        throws java.lang.IndexOutOfBoundsException
    {
        _parameterDefinitionList.add(index, vParameterDefinition);
    } //-- void addParameterDefinition(int, org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method clearParameterDefinition
     */
    public void clearParameterDefinition()
    {
        _parameterDefinitionList.clear();
    } //-- void clearParameterDefinition() 

    /**
     * Method enumerateParameterDefinition
     */
    public java.util.Enumeration enumerateParameterDefinition()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_parameterDefinitionList.iterator());
    } //-- java.util.Enumeration enumerateParameterDefinition() 

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
            if (this._parameterDefinitionList != null) {
                if (temp._parameterDefinitionList == null) return false;
                else if (!(this._parameterDefinitionList.equals(temp._parameterDefinitionList))) 
                    return false;
            }
            else if (temp._parameterDefinitionList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getParameterDefinition
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition getParameterDefinition(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterDefinitionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) _parameterDefinitionList.get(index);
    } //-- org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition getParameterDefinition(int) 

    /**
     * Method getParameterDefinition
     */
    public org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[] getParameterDefinition()
    {
        int size = _parameterDefinitionList.size();
        org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[] mArray = new org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) _parameterDefinitionList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[] getParameterDefinition() 

    /**
     * Method getParameterDefinitionCount
     */
    public int getParameterDefinitionCount()
    {
        return _parameterDefinitionList.size();
    } //-- int getParameterDefinitionCount() 

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
     * Method removeParameterDefinition
     * 
     * @param vParameterDefinition
     */
    public boolean removeParameterDefinition(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition vParameterDefinition)
    {
        boolean removed = _parameterDefinitionList.remove(vParameterDefinition);
        return removed;
    } //-- boolean removeParameterDefinition(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method setParameterDefinition
     * 
     * @param index
     * @param vParameterDefinition
     */
    public void setParameterDefinition(int index, org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition vParameterDefinition)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterDefinitionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _parameterDefinitionList.set(index, vParameterDefinition);
    } //-- void setParameterDefinition(int, org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method setParameterDefinition
     * 
     * @param parameterDefinitionArray
     */
    public void setParameterDefinition(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition[] parameterDefinitionArray)
    {
        //-- copy array
        _parameterDefinitionList.clear();
        for (int i = 0; i < parameterDefinitionArray.length; i++) {
            _parameterDefinitionList.add(parameterDefinitionArray[i]);
        }
    } //-- void setParameterDefinition(org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition) 

    /**
     * Method unmarshalParameters
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.cea.Parameters unmarshalParameters(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.cea.Parameters) Unmarshaller.unmarshal(org.astrogrid.registry.beans.cea.Parameters.class, reader);
    } //-- org.astrogrid.registry.beans.cea.Parameters unmarshalParameters(java.io.Reader) 

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
