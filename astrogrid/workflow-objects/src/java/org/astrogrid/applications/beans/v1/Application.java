/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Application.java,v 1.8 2004/03/03 21:48:00 nw Exp $
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
 * Base Application definition
 * 
 * @version $Revision: 1.8 $ $Date: 2004/03/03 21:48:00 $
 */
public class Application extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _instanceClass
     */
    private java.lang.String _instanceClass;

    /**
     * Field _parameters
     */
    private org.astrogrid.applications.beans.v1.Parameters _parameters;

    /**
     * Field _interfaces
     */
    private org.astrogrid.applications.beans.v1.InterfacesType _interfaces;


      //----------------/
     //- Constructors -/
    //----------------/

    public Application() {
        super();
    } //-- org.astrogrid.applications.beans.v1.Application()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'instanceClass'.
     * 
     * @return the value of field 'instanceClass'.
     */
    public java.lang.String getInstanceClass()
    {
        return this._instanceClass;
    } //-- java.lang.String getInstanceClass() 

    /**
     * Returns the value of field 'interfaces'.
     * 
     * @return the value of field 'interfaces'.
     */
    public org.astrogrid.applications.beans.v1.InterfacesType getInterfaces()
    {
        return this._interfaces;
    } //-- org.astrogrid.applications.beans.v1.InterfacesType getInterfaces() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'parameters'.
     * 
     * @return the value of field 'parameters'.
     */
    public org.astrogrid.applications.beans.v1.Parameters getParameters()
    {
        return this._parameters;
    } //-- org.astrogrid.applications.beans.v1.Parameters getParameters() 

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
     * Sets the value of field 'instanceClass'.
     * 
     * @param instanceClass the value of field 'instanceClass'.
     */
    public void setInstanceClass(java.lang.String instanceClass)
    {
        this._instanceClass = instanceClass;
    } //-- void setInstanceClass(java.lang.String) 

    /**
     * Sets the value of field 'interfaces'.
     * 
     * @param interfaces the value of field 'interfaces'.
     */
    public void setInterfaces(org.astrogrid.applications.beans.v1.InterfacesType interfaces)
    {
        this._interfaces = interfaces;
    } //-- void setInterfaces(org.astrogrid.applications.beans.v1.InterfacesType) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'parameters'.
     * 
     * @param parameters the value of field 'parameters'.
     */
    public void setParameters(org.astrogrid.applications.beans.v1.Parameters parameters)
    {
        this._parameters = parameters;
    } //-- void setParameters(org.astrogrid.applications.beans.v1.Parameters) 

    /**
     * Method unmarshalApplication
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.Application unmarshalApplication(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.Application) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.Application.class, reader);
    } //-- org.astrogrid.applications.beans.v1.Application unmarshalApplication(java.io.Reader) 

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
