/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ExtendedInterfaceType.java,v 1.2 2004/03/03 16:22:08 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.resource.AccessURLType;
import org.astrogrid.registry.beans.resource.types.InvocationType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * This is used as a base type for extended Interface types. 
 *  It simply restricts the value of Invocation to be "Extended".
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/03 16:22:08 $
 */
public class ExtendedInterfaceType extends org.astrogrid.registry.beans.resource.InterfaceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The type of interface used by the current Service,
     *  expressed as a controlled name. 
     *  
     */
    private org.astrogrid.registry.beans.resource.types.InvocationType _invocation;

    /**
     * An account of the nature of the resource
     *  
     */
    private java.lang.String _description;

    /**
     * A full or base URL that can be used to access data or a
     * service.
     *  
     */
    private org.astrogrid.registry.beans.resource.AccessURLType _accessURL;


      //----------------/
     //- Constructors -/
    //----------------/

    public ExtendedInterfaceType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.dataservice.ExtendedInterfaceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'accessURL'. The field
     * 'accessURL' has the following description: A full or base
     * URL that can be used to access data or a service.
     *  
     * 
     * @return the value of field 'accessURL'.
     */
    public org.astrogrid.registry.beans.resource.AccessURLType getAccessURL()
    {
        return this._accessURL;
    } //-- org.astrogrid.registry.beans.resource.AccessURLType getAccessURL() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'invocation'. The field
     * 'invocation' has the following description: The type of
     * interface used by the current Service,
     *  expressed as a controlled name. 
     *  
     * 
     * @return the value of field 'invocation'.
     */
    public org.astrogrid.registry.beans.resource.types.InvocationType getInvocation()
    {
        return this._invocation;
    } //-- org.astrogrid.registry.beans.resource.types.InvocationType getInvocation() 

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
     * Sets the value of field 'accessURL'. The field 'accessURL'
     * has the following description: A full or base URL that can
     * be used to access data or a service.
     *  
     * 
     * @param accessURL the value of field 'accessURL'.
     */
    public void setAccessURL(org.astrogrid.registry.beans.resource.AccessURLType accessURL)
    {
        this._accessURL = accessURL;
    } //-- void setAccessURL(org.astrogrid.registry.beans.resource.AccessURLType) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'invocation'. The field 'invocation'
     * has the following description: The type of interface used by
     * the current Service,
     *  expressed as a controlled name. 
     *  
     * 
     * @param invocation the value of field 'invocation'.
     */
    public void setInvocation(org.astrogrid.registry.beans.resource.types.InvocationType invocation)
    {
        this._invocation = invocation;
    } //-- void setInvocation(org.astrogrid.registry.beans.resource.types.InvocationType) 

    /**
     * Method unmarshalExtendedInterfaceType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.ExtendedInterfaceType unmarshalExtendedInterfaceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.ExtendedInterfaceType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.ExtendedInterfaceType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.ExtendedInterfaceType unmarshalExtendedInterfaceType(java.io.Reader) 

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
