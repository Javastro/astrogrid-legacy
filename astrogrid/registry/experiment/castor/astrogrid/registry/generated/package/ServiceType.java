/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: ServiceType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

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
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class ServiceType extends org.astrogrid.registry.generated.package.ResourceType 
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
    private org.astrogrid.registry.generated.package.Capability _capability;

    /**
     * A description of a service interface.
     *  
     */
    private org.astrogrid.registry.generated.package.Interface _interface;


      //----------------/
     //- Constructors -/
    //----------------/

    public ServiceType() {
        super();
    } //-- org.astrogrid.registry.generated.package.ServiceType()


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
    public org.astrogrid.registry.generated.package.Capability getCapability()
    {
        return this._capability;
    } //-- org.astrogrid.registry.generated.package.Capability getCapability() 

    /**
     * Returns the value of field 'interface'. The field
     * 'interface' has the following description: A description of
     * a service interface.
     *  
     * 
     * @return the value of field 'interface'.
     */
    public org.astrogrid.registry.generated.package.Interface getInterface()
    {
        return this._interface;
    } //-- org.astrogrid.registry.generated.package.Interface getInterface() 

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
    public void setCapability(org.astrogrid.registry.generated.package.Capability capability)
    {
        this._capability = capability;
    } //-- void setCapability(org.astrogrid.registry.generated.package.Capability) 

    /**
     * Sets the value of field 'interface'. The field 'interface'
     * has the following description: A description of a service
     * interface.
     *  
     * 
     * @param _interface
     * @param interface the value of field 'interface'.
     */
    public void setInterface(org.astrogrid.registry.generated.package.Interface _interface)
    {
        this._interface = _interface;
    } //-- void setInterface(org.astrogrid.registry.generated.package.Interface) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.ServiceType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.ServiceType.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
