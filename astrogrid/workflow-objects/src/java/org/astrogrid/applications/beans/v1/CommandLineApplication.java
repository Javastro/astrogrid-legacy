/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CommandLineApplication.java,v 1.7 2004/03/03 19:54:55 nw Exp $
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
 * Class CommandLineApplication.
 * 
 * @version $Revision: 1.7 $ $Date: 2004/03/03 19:54:55 $
 */
public class CommandLineApplication extends org.astrogrid.applications.beans.v1.Application 
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
