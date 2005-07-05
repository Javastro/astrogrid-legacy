/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Contact.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource;

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
 * Information that can be used for contacting someone
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:56 $
 */
public class Contact extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the name or title of the contact person.
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.ResourceName _name;

    /**
     * the contact mailing address
     */
    private java.lang.String _address;

    /**
     * the contact email address
     */
    private java.lang.String _email;

    /**
     * the contact telephone number
     */
    private java.lang.String _telephone;


      //----------------/
     //- Constructors -/
    //----------------/

    public Contact() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.Contact()


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
        
        if (obj instanceof Contact) {
        
            Contact temp = (Contact)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._address != null) {
                if (temp._address == null) return false;
                else if (!(this._address.equals(temp._address))) 
                    return false;
            }
            else if (temp._address != null)
                return false;
            if (this._email != null) {
                if (temp._email == null) return false;
                else if (!(this._email.equals(temp._email))) 
                    return false;
            }
            else if (temp._email != null)
                return false;
            if (this._telephone != null) {
                if (temp._telephone == null) return false;
                else if (!(this._telephone.equals(temp._telephone))) 
                    return false;
            }
            else if (temp._telephone != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'address'. The field 'address'
     * has the following description: the contact mailing address
     * 
     * @return the value of field 'address'.
     */
    public java.lang.String getAddress()
    {
        return this._address;
    } //-- java.lang.String getAddress() 

    /**
     * Returns the value of field 'email'. The field 'email' has
     * the following description: the contact email address
     * 
     * @return the value of field 'email'.
     */
    public java.lang.String getEmail()
    {
        return this._email;
    } //-- java.lang.String getEmail() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: the name or title of the contact
     * person.
     *  
     * 
     * @return the value of field 'name'.
     */
    public org.astrogrid.registry.beans.v10.resource.ResourceName getName()
    {
        return this._name;
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName getName() 

    /**
     * Returns the value of field 'telephone'. The field
     * 'telephone' has the following description: the contact
     * telephone number
     * 
     * @return the value of field 'telephone'.
     */
    public java.lang.String getTelephone()
    {
        return this._telephone;
    } //-- java.lang.String getTelephone() 

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
     * Sets the value of field 'address'. The field 'address' has
     * the following description: the contact mailing address
     * 
     * @param address the value of field 'address'.
     */
    public void setAddress(java.lang.String address)
    {
        this._address = address;
    } //-- void setAddress(java.lang.String) 

    /**
     * Sets the value of field 'email'. The field 'email' has the
     * following description: the contact email address
     * 
     * @param email the value of field 'email'.
     */
    public void setEmail(java.lang.String email)
    {
        this._email = email;
    } //-- void setEmail(java.lang.String) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: the name or title of the contact
     * person.
     *  
     * 
     * @param name the value of field 'name'.
     */
    public void setName(org.astrogrid.registry.beans.v10.resource.ResourceName name)
    {
        this._name = name;
    } //-- void setName(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Sets the value of field 'telephone'. The field 'telephone'
     * has the following description: the contact telephone number
     * 
     * @param telephone the value of field 'telephone'.
     */
    public void setTelephone(java.lang.String telephone)
    {
        this._telephone = telephone;
    } //-- void setTelephone(java.lang.String) 

    /**
     * Method unmarshalContact
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.Contact unmarshalContact(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.Contact) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.Contact.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.Contact unmarshalContact(java.io.Reader) 

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
