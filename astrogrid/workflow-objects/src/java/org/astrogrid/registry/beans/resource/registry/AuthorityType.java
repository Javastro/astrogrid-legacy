/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AuthorityType.java,v 1.9 2004/09/09 10:41:48 pah Exp $
 */

package org.astrogrid.registry.beans.resource.registry;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AuthorityType.
 * 
 * @version $Revision: 1.9 $ $Date: 2004/09/09 10:41:48 $
 */
public class AuthorityType extends org.astrogrid.registry.beans.resource.ResourceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the organization that manages the current authority. 
     *  
     */
    private org.astrogrid.registry.beans.resource.IdentifierType _managingOrg;


      //----------------/
     //- Constructors -/
    //----------------/

    public AuthorityType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.registry.AuthorityType()


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
        
        if (obj instanceof AuthorityType) {
        
            AuthorityType temp = (AuthorityType)obj;
            if (this._managingOrg != null) {
                if (temp._managingOrg == null) return false;
                else if (!(this._managingOrg.equals(temp._managingOrg))) 
                    return false;
            }
            else if (temp._managingOrg != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'managingOrg'. The field
     * 'managingOrg' has the following description: the
     * organization that manages the current authority. 
     *  
     * 
     * @return the value of field 'managingOrg'.
     */
    public org.astrogrid.registry.beans.resource.IdentifierType getManagingOrg()
    {
        return this._managingOrg;
    } //-- org.astrogrid.registry.beans.resource.IdentifierType getManagingOrg() 

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
     * Sets the value of field 'managingOrg'. The field
     * 'managingOrg' has the following description: the
     * organization that manages the current authority. 
     *  
     * 
     * @param managingOrg the value of field 'managingOrg'.
     */
    public void setManagingOrg(org.astrogrid.registry.beans.resource.IdentifierType managingOrg)
    {
        this._managingOrg = managingOrg;
    } //-- void setManagingOrg(org.astrogrid.registry.beans.resource.IdentifierType) 

    /**
     * Method unmarshalAuthorityType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.registry.AuthorityType unmarshalAuthorityType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.registry.AuthorityType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.registry.AuthorityType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.registry.AuthorityType unmarshalAuthorityType(java.io.Reader) 

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
