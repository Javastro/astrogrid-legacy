/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Application.java,v 1.2 2007/01/04 16:26:20 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.cea.implementation;

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
 * The definition of what an application is
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:20 $
 */
public class Application extends org.astrogrid.applications.beans.v1.cea.implementation.CommandLineApplication 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * perhaps this is best as an attribute
     */
    private java.lang.String _version;


      //----------------/
     //- Constructors -/
    //----------------/

    public Application() {
        super();
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.Application()


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
        
        if (obj instanceof Application) {
        
            Application temp = (Application)obj;
            if (this._version != null) {
                if (temp._version == null) return false;
                else if (!(this._version.equals(temp._version))) 
                    return false;
            }
            else if (temp._version != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'version'. The field 'version'
     * has the following description: perhaps this is best as an
     * attribute
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
     * Sets the value of field 'version'. The field 'version' has
     * the following description: perhaps this is best as an
     * attribute
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(java.lang.String version)
    {
        this._version = version;
    } //-- void setVersion(java.lang.String) 

    /**
     * Method unmarshalApplication
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.cea.implementation.Application unmarshalApplication(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.cea.implementation.Application) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.cea.implementation.Application.class, reader);
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.Application unmarshalApplication(java.io.Reader) 

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
