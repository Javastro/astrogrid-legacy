/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: InterfaceType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.generated.package.types.InvocationType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * This is typically used as a base type for defining specific
 *  kinds of service interfaces. If this type is used
 *  directly, the Description element should be used to describe
 *  how to invoke the service.
 *  
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class InterfaceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The type of interface used by the current Service,
     *  expressed as a controlled name. 
     *  
     */
    private org.astrogrid.registry.generated.package.types.InvocationType _invocation;

    /**
     * Specifically, a textual description of the interface.
     *  
     */
    private java.lang.String _description;

    /**
     * Whether this refers to a base or full URL depends on
     *  the specific class of interface. If 
     *  
     */
    private org.astrogrid.registry.generated.package.AccessURL _accessURL;


      //----------------/
     //- Constructors -/
    //----------------/

    public InterfaceType() {
        super();
    } //-- org.astrogrid.registry.generated.package.InterfaceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'accessURL'. The field
     * 'accessURL' has the following description: Whether this
     * refers to a base or full URL depends on
     *  the specific class of interface. If 
     *  
     * 
     * @return the value of field 'accessURL'.
     */
    public org.astrogrid.registry.generated.package.AccessURL getAccessURL()
    {
        return this._accessURL;
    } //-- org.astrogrid.registry.generated.package.AccessURL getAccessURL() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: Specifically, a
     * textual description of the interface.
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
    public org.astrogrid.registry.generated.package.types.InvocationType getInvocation()
    {
        return this._invocation;
    } //-- org.astrogrid.registry.generated.package.types.InvocationType getInvocation() 

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
     * has the following description: Whether this refers to a base
     * or full URL depends on
     *  the specific class of interface. If 
     *  
     * 
     * @param accessURL the value of field 'accessURL'.
     */
    public void setAccessURL(org.astrogrid.registry.generated.package.AccessURL accessURL)
    {
        this._accessURL = accessURL;
    } //-- void setAccessURL(org.astrogrid.registry.generated.package.AccessURL) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: Specifically, a
     * textual description of the interface.
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
    public void setInvocation(org.astrogrid.registry.generated.package.types.InvocationType invocation)
    {
        this._invocation = invocation;
    } //-- void setInvocation(org.astrogrid.registry.generated.package.types.InvocationType) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.InterfaceType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.InterfaceType.class, reader);
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
