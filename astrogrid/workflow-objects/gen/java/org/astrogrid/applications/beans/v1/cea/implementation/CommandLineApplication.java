/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CommandLineApplication.java,v 1.2 2005/07/05 08:26:57 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.cea.implementation;

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
 * Description of a command line application
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:57 $
 */
public class CommandLineApplication extends org.astrogrid.applications.beans.v1.ApplicationBase 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * This is the file path of the executable
     */
    private java.lang.String _executionPath;

    /**
     * Field _longName
     */
    private java.lang.String _longName;

    /**
     * this should probably be in the main schema somewhere...
     */
    private java.lang.String _version;

    /**
     * A description of the application that can become part of the
     * registry entry
     */
    private java.lang.String _description;

    /**
     * references to on-line information about the application
     */
    private java.util.ArrayList _referenceURLList;


      //----------------/
     //- Constructors -/
    //----------------/

    public CommandLineApplication() {
        super();
        _referenceURLList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.CommandLineApplication()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addReferenceURL
     * 
     * @param vReferenceURL
     */
    public void addReferenceURL(java.lang.String vReferenceURL)
        throws java.lang.IndexOutOfBoundsException
    {
        _referenceURLList.add(vReferenceURL);
    } //-- void addReferenceURL(java.lang.String) 

    /**
     * Method addReferenceURL
     * 
     * @param index
     * @param vReferenceURL
     */
    public void addReferenceURL(int index, java.lang.String vReferenceURL)
        throws java.lang.IndexOutOfBoundsException
    {
        _referenceURLList.add(index, vReferenceURL);
    } //-- void addReferenceURL(int, java.lang.String) 

    /**
     * Method clearReferenceURL
     */
    public void clearReferenceURL()
    {
        _referenceURLList.clear();
    } //-- void clearReferenceURL() 

    /**
     * Method enumerateReferenceURL
     */
    public java.util.Enumeration enumerateReferenceURL()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_referenceURLList.iterator());
    } //-- java.util.Enumeration enumerateReferenceURL() 

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
        
        if (obj instanceof CommandLineApplication) {
        
            CommandLineApplication temp = (CommandLineApplication)obj;
            if (this._executionPath != null) {
                if (temp._executionPath == null) return false;
                else if (!(this._executionPath.equals(temp._executionPath))) 
                    return false;
            }
            else if (temp._executionPath != null)
                return false;
            if (this._longName != null) {
                if (temp._longName == null) return false;
                else if (!(this._longName.equals(temp._longName))) 
                    return false;
            }
            else if (temp._longName != null)
                return false;
            if (this._version != null) {
                if (temp._version == null) return false;
                else if (!(this._version.equals(temp._version))) 
                    return false;
            }
            else if (temp._version != null)
                return false;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
                return false;
            if (this._referenceURLList != null) {
                if (temp._referenceURLList == null) return false;
                else if (!(this._referenceURLList.equals(temp._referenceURLList))) 
                    return false;
            }
            else if (temp._referenceURLList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: A description
     * of the application that can become part of the registry
     * entry
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'executionPath'. The field
     * 'executionPath' has the following description: This is the
     * file path of the executable
     * 
     * @return the value of field 'executionPath'.
     */
    public java.lang.String getExecutionPath()
    {
        return this._executionPath;
    } //-- java.lang.String getExecutionPath() 

    /**
     * Returns the value of field 'longName'.
     * 
     * @return the value of field 'longName'.
     */
    public java.lang.String getLongName()
    {
        return this._longName;
    } //-- java.lang.String getLongName() 

    /**
     * Method getReferenceURL
     * 
     * @param index
     */
    public java.lang.String getReferenceURL(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _referenceURLList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_referenceURLList.get(index);
    } //-- java.lang.String getReferenceURL(int) 

    /**
     * Method getReferenceURL
     */
    public java.lang.String[] getReferenceURL()
    {
        int size = _referenceURLList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_referenceURLList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getReferenceURL() 

    /**
     * Method getReferenceURLCount
     */
    public int getReferenceURLCount()
    {
        return _referenceURLList.size();
    } //-- int getReferenceURLCount() 

    /**
     * Returns the value of field 'version'. The field 'version'
     * has the following description: this should probably be in
     * the main schema somewhere...
     * 
     * @return the value of field 'version'.
     */
    public java.lang.String getVersion()
    {
        return this._version;
    } //-- java.lang.String getVersion() 

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
     * Method removeReferenceURL
     * 
     * @param vReferenceURL
     */
    public boolean removeReferenceURL(java.lang.String vReferenceURL)
    {
        boolean removed = _referenceURLList.remove(vReferenceURL);
        return removed;
    } //-- boolean removeReferenceURL(java.lang.String) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: A description
     * of the application that can become part of the registry
     * entry
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'executionPath'. The field
     * 'executionPath' has the following description: This is the
     * file path of the executable
     * 
     * @param executionPath the value of field 'executionPath'.
     */
    public void setExecutionPath(java.lang.String executionPath)
    {
        this._executionPath = executionPath;
    } //-- void setExecutionPath(java.lang.String) 

    /**
     * Sets the value of field 'longName'.
     * 
     * @param longName the value of field 'longName'.
     */
    public void setLongName(java.lang.String longName)
    {
        this._longName = longName;
    } //-- void setLongName(java.lang.String) 

    /**
     * Method setReferenceURL
     * 
     * @param index
     * @param vReferenceURL
     */
    public void setReferenceURL(int index, java.lang.String vReferenceURL)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _referenceURLList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _referenceURLList.set(index, vReferenceURL);
    } //-- void setReferenceURL(int, java.lang.String) 

    /**
     * Method setReferenceURL
     * 
     * @param referenceURLArray
     */
    public void setReferenceURL(java.lang.String[] referenceURLArray)
    {
        //-- copy array
        _referenceURLList.clear();
        for (int i = 0; i < referenceURLArray.length; i++) {
            _referenceURLList.add(referenceURLArray[i]);
        }
    } //-- void setReferenceURL(java.lang.String) 

    /**
     * Sets the value of field 'version'. The field 'version' has
     * the following description: this should probably be in the
     * main schema somewhere...
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(java.lang.String version)
    {
        this._version = version;
    } //-- void setVersion(java.lang.String) 

    /**
     * Method unmarshalCommandLineApplication
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.cea.implementation.CommandLineApplication unmarshalCommandLineApplication(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.cea.implementation.CommandLineApplication) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.cea.implementation.CommandLineApplication.class, reader);
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.CommandLineApplication unmarshalCommandLineApplication(java.io.Reader) 

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
