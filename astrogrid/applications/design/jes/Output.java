/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Output.java,v 1.1 2004/08/10 21:09:29 pah Exp $
 */

package jes;

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
 * the list of output paramters
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/10 21:09:29 $
 */
public class Output extends org.astrogrid.common.bean.BaseBean 
implements Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a parameter that should be based to an CEA tool
     */
    private ArrayList _parameterList;

    /**
     * @link aggregation
     * @supplierCardinality 0..* 
     */
    private ParameterValue lnkParameterValue;
//-- org.astrogrid.workflow.beans.v1.Output()



      //-----------/
     //- Methods -/
    //-----------/

//-- void addParameter(org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method addParameter
     * 
     * @param index
     * @param vParameter
     */
    public void addParameter(int index, ParameterValue vParameter)
        throws IndexOutOfBoundsException
    {
        _parameterList.add(index, vParameter);
    } //-- void addParameter(int, org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

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
    public Enumeration enumerateParameter()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_parameterList.iterator());
    }

 //-- java.util.Enumeration enumerateParameter() 

//-- boolean equals(java.lang.Object) 

    /**
     * Method getParameter
     * 
     * @param index
     */
    public ParameterValue getParameter(int index)
        throws IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.parameters.ParameterValue) _parameterList.get(index);
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue getParameter(int) 

    /**
     * Method getParameter
     */
    public ParameterValue[] getParameter()
    {
        int size = _parameterList.size();
        org.astrogrid.applications.beans.v1.parameters.ParameterValue[] mArray = new org.astrogrid.applications.beans.v1.parameters.ParameterValue[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.parameters.ParameterValue) _parameterList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue[] getParameter() 

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
    }

 //-- boolean isValid() 


//-- void marshal(java.io.Writer) 

//-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeParameter
     * 
     * @param vParameter
     */
    public boolean removeParameter(ParameterValue vParameter)
    {
        boolean removed = _parameterList.remove(vParameter);
        return removed;
    } //-- boolean removeParameter(org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method setParameter
     * 
     * @param index
     * @param vParameter
     */
    public void setParameter(int index, ParameterValue vParameter)
        throws IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _parameterList.set(index, vParameter);
    } //-- void setParameter(int, org.astrogrid.applications.beans.v1.parameters.ParameterValue) 

    /**
     * Method setParameter
     * 
     * @param parameterArray
     */
    public void setParameter(ParameterValue[] parameterArray)
    {
        //-- copy array
        _parameterList.clear();
        for (int i = 0; i < parameterArray.length; i++) {
            _parameterList.add(parameterArray[i]);
        }
    }

 //-- void setParameter(org.astrogrid.applications.beans.v1.parameters.ParameterValue) 


//-- org.astrogrid.workflow.beans.v1.Output unmarshalOutput(java.io.Reader) 

//-- void validate() 

}
