/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: InterfaceType.java,v 1.11 2005/01/23 12:51:45 jdt Exp $
 */

package org.astrogrid.registry.beans.resource;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.resource.types.InvocationType;
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
 * @version $Revision: 1.11 $ $Date: 2005/01/23 12:51:45 $
 */
public class InterfaceType extends org.astrogrid.common.bean.BaseBean 
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
     * Specifically, a textual description of the interface.
     *  
     */
    private java.lang.String _description;

    /**
     * Whether this refers to a base or full URL depends on
     *  the specific class of interface. If 
     *  
     */
    private org.astrogrid.registry.beans.resource.AccessURLType _accessURL;


      //----------------/
     //- Constructors -/
    //----------------/

    public InterfaceType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.InterfaceType()


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
        
        if (obj instanceof InterfaceType) {
        
            InterfaceType temp = (InterfaceType)obj;
            if (this._invocation != null) {
                if (temp._invocation == null) return false;
                else if (!(this._invocation.equals(temp._invocation))) 
                    return false;
            }
            else if (temp._invocation != null)
                return false;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
                return false;
            if (this._accessURL != null) {
                if (temp._accessURL == null) return false;
                else if (!(this._accessURL.equals(temp._accessURL))) 
                    return false;
            }
            else if (temp._accessURL != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'accessURL'. The field
     * 'accessURL' has the following description: Whether this
     * refers to a base or full URL depends on
     *  the specific class of interface. If 
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
     * has the following description: Whether this refers to a base
     * or full URL depends on
     *  the specific class of interface. If 
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
    public void setInvocation(org.astrogrid.registry.beans.resource.types.InvocationType invocation)
    {
        this._invocation = invocation;
    } //-- void setInvocation(org.astrogrid.registry.beans.resource.types.InvocationType) 

    /**
     * Method unmarshalInterfaceType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.InterfaceType unmarshalInterfaceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.InterfaceType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.InterfaceType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.InterfaceType unmarshalInterfaceType(java.io.Reader) 

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
