/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ServiceType.java,v 1.4 2004/03/09 09:45:24 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource;

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
 * Class ServiceType.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/09 09:45:24 $
 */
public class ServiceType extends org.astrogrid.registry.beans.resource.ResourceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a specific description of the context and capabilities of a
     * service.
     *  
     */
    private org.astrogrid.registry.beans.resource.CapabilityType _capability;

    /**
     * A description of a service interface.
     *  
     */
    private org.astrogrid.registry.beans.resource.InterfaceType _interface;


      //----------------/
     //- Constructors -/
    //----------------/

    public ServiceType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.ServiceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'capability'. The field
     * 'capability' has the following description: a specific
     * description of the context and capabilities of a service.
     *  
     * 
     * @return the value of field 'capability'.
     */
    public org.astrogrid.registry.beans.resource.CapabilityType getCapability()
    {
        return this._capability;
    } //-- org.astrogrid.registry.beans.resource.CapabilityType getCapability() 

    /**
     * Returns the value of field 'interface'. The field
     * 'interface' has the following description: A description of
     * a service interface.
     *  
     * 
     * @return the value of field 'interface'.
     */
    public org.astrogrid.registry.beans.resource.InterfaceType getInterface()
    {
        return this._interface;
    } //-- org.astrogrid.registry.beans.resource.InterfaceType getInterface() 

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
     * Sets the value of field 'capability'. The field 'capability'
     * has the following description: a specific description of the
     * context and capabilities of a service.
     *  
     * 
     * @param capability the value of field 'capability'.
     */
    public void setCapability(org.astrogrid.registry.beans.resource.CapabilityType capability)
    {
        this._capability = capability;
    } //-- void setCapability(org.astrogrid.registry.beans.resource.CapabilityType) 

    /**
     * Sets the value of field 'interface'. The field 'interface'
     * has the following description: A description of a service
     * interface.
     *  
     * 
     * @param _interface
     * @param interface the value of field 'interface'.
     */
    public void setInterface(org.astrogrid.registry.beans.resource.InterfaceType _interface)
    {
        this._interface = _interface;
    } //-- void setInterface(org.astrogrid.registry.beans.resource.InterfaceType) 

    /**
     * Method unmarshalServiceType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.ServiceType unmarshalServiceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.ServiceType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.ServiceType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.ServiceType unmarshalServiceType(java.io.Reader) 

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
