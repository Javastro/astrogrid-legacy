/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CommandLineExecutionControllerConfig.java,v 1.7 2004/03/03 19:54:55 nw Exp $
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
 * The configuration for a command line execution controller
 * 
 * @version $Revision: 1.7 $ $Date: 2004/03/03 19:54:55 $
 */
public class CommandLineExecutionControllerConfig extends org.astrogrid.applications.beans.v1.CommonExecutionConnectorConfigType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The definition of what an application is
     */
    private java.util.ArrayList _applicationList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CommandLineExecutionControllerConfig() {
        super();
        _applicationList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addApplication
     * 
     * @param vApplication
     */
    public void addApplication(org.astrogrid.applications.beans.v1.CommandLineApplication vApplication)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationList.add(vApplication);
    } //-- void addApplication(org.astrogrid.applications.beans.v1.CommandLineApplication) 

    /**
     * Method addApplication
     * 
     * @param index
     * @param vApplication
     */
    public void addApplication(int index, org.astrogrid.applications.beans.v1.CommandLineApplication vApplication)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationList.add(index, vApplication);
    } //-- void addApplication(int, org.astrogrid.applications.beans.v1.CommandLineApplication) 

    /**
     * Method clearApplication
     */
    public void clearApplication()
    {
        _applicationList.clear();
    } //-- void clearApplication() 

    /**
     * Method enumerateApplication
     */
    public java.util.Enumeration enumerateApplication()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_applicationList.iterator());
    } //-- java.util.Enumeration enumerateApplication() 

    /**
     * Method getApplication
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.CommandLineApplication getApplication(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.CommandLineApplication) _applicationList.get(index);
    } //-- org.astrogrid.applications.beans.v1.CommandLineApplication getApplication(int) 

    /**
     * Method getApplication
     */
    public org.astrogrid.applications.beans.v1.CommandLineApplication[] getApplication()
    {
        int size = _applicationList.size();
        org.astrogrid.applications.beans.v1.CommandLineApplication[] mArray = new org.astrogrid.applications.beans.v1.CommandLineApplication[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.CommandLineApplication) _applicationList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.CommandLineApplication[] getApplication() 

    /**
     * Method getApplicationCount
     */
    public int getApplicationCount()
    {
        return _applicationList.size();
    } //-- int getApplicationCount() 

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
     * Method removeApplication
     * 
     * @param vApplication
     */
    public boolean removeApplication(org.astrogrid.applications.beans.v1.CommandLineApplication vApplication)
    {
        boolean removed = _applicationList.remove(vApplication);
        return removed;
    } //-- boolean removeApplication(org.astrogrid.applications.beans.v1.CommandLineApplication) 

    /**
     * Method setApplication
     * 
     * @param index
     * @param vApplication
     */
    public void setApplication(int index, org.astrogrid.applications.beans.v1.CommandLineApplication vApplication)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _applicationList.set(index, vApplication);
    } //-- void setApplication(int, org.astrogrid.applications.beans.v1.CommandLineApplication) 

    /**
     * Method setApplication
     * 
     * @param applicationArray
     */
    public void setApplication(org.astrogrid.applications.beans.v1.CommandLineApplication[] applicationArray)
    {
        //-- copy array
        _applicationList.clear();
        for (int i = 0; i < applicationArray.length; i++) {
            _applicationList.add(applicationArray[i]);
        }
    } //-- void setApplication(org.astrogrid.applications.beans.v1.CommandLineApplication) 

    /**
     * Method unmarshalCommandLineExecutionControllerConfig
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig unmarshalCommandLineExecutionControllerConfig(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig.class, reader);
    } //-- org.astrogrid.applications.beans.v1.CommandLineExecutionControllerConfig unmarshalCommandLineExecutionControllerConfig(java.io.Reader) 

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
