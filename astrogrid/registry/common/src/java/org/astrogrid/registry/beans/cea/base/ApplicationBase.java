/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ApplicationBase.java,v 1.2 2004/03/19 08:16:47 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.cea.base;

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
 * Base Application Description
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/19 08:16:47 $
 */
public class ApplicationBase extends org.astrogrid.common.bean.BaseBean 
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
    private org.astrogrid.registry.beans.cea.base.Parameters _parameters;

    /**
     * Field _interfaces
     */
    private org.astrogrid.registry.beans.cea.base.InterfacesType _interfaces;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationBase() {
        super();
    } //-- org.astrogrid.registry.beans.cea.base.ApplicationBase()


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
    public org.astrogrid.registry.beans.cea.base.InterfacesType getInterfaces()
    {
        return this._interfaces;
    } //-- org.astrogrid.registry.beans.cea.base.InterfacesType getInterfaces() 

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
    public org.astrogrid.registry.beans.cea.base.Parameters getParameters()
    {
        return this._parameters;
    } //-- org.astrogrid.registry.beans.cea.base.Parameters getParameters() 

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
    public void setInterfaces(org.astrogrid.registry.beans.cea.base.InterfacesType interfaces)
    {
        this._interfaces = interfaces;
    } //-- void setInterfaces(org.astrogrid.registry.beans.cea.base.InterfacesType) 

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
    public void setParameters(org.astrogrid.registry.beans.cea.base.Parameters parameters)
    {
        this._parameters = parameters;
    } //-- void setParameters(org.astrogrid.registry.beans.cea.base.Parameters) 

    /**
     * Method unmarshalApplicationBase
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.cea.base.ApplicationBase unmarshalApplicationBase(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.cea.base.ApplicationBase) Unmarshaller.unmarshal(org.astrogrid.registry.beans.cea.base.ApplicationBase.class, reader);
    } //-- org.astrogrid.registry.beans.cea.base.ApplicationBase unmarshalApplicationBase(java.io.Reader) 

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
