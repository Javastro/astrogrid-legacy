/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CommandLineApplication.java,v 1.19 2004/04/05 15:17:59 nw Exp $
 */

package org.astrogrid.applications.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Description of a command line application
 * 
 * @version $Revision: 1.19 $ $Date: 2004/04/05 15:17:59 $
 */
public class CommandLineApplication extends org.astrogrid.applications.beans.v1.ApplicationBase 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _executionPath
     */
    private java.lang.String _executionPath;


      //----------------/
     //- Constructors -/
    //----------------/

    public CommandLineApplication() {
        super();
    } //-- org.astrogrid.applications.beans.v1.CommandLineApplication()


      //-----------/
     //- Methods -/
    //-----------/

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
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'executionPath'.
     * 
     * @return the value of field 'executionPath'.
     */
    public java.lang.String getExecutionPath()
    {
        return this._executionPath;
    } //-- java.lang.String getExecutionPath() 

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
     * Sets the value of field 'executionPath'.
     * 
     * @param executionPath the value of field 'executionPath'.
     */
    public void setExecutionPath(java.lang.String executionPath)
    {
        this._executionPath = executionPath;
    } //-- void setExecutionPath(java.lang.String) 

    /**
     * Method unmarshalCommandLineApplication
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.CommandLineApplication unmarshalCommandLineApplication(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.CommandLineApplication) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.CommandLineApplication.class, reader);
    } //-- org.astrogrid.applications.beans.v1.CommandLineApplication unmarshalCommandLineApplication(java.io.Reader) 

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
