/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: AuthorityType.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
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
 * Class AuthorityType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class AuthorityType extends ResourceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the organization that manages the current authority. 
     *  
     */
    private org.astrogrid.registry.generated.package.ManagingOrg _managingOrg;


      //----------------/
     //- Constructors -/
    //----------------/

    public AuthorityType() {
        super();
    } //-- org.astrogrid.registry.generated.package.AuthorityType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'managingOrg'. The field
     * 'managingOrg' has the following description: the
     * organization that manages the current authority. 
     *  
     * 
     * @return the value of field 'managingOrg'.
     */
    public org.astrogrid.registry.generated.package.ManagingOrg getManagingOrg()
    {
        return this._managingOrg;
    } //-- org.astrogrid.registry.generated.package.ManagingOrg getManagingOrg() 

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
    public void setManagingOrg(org.astrogrid.registry.generated.package.ManagingOrg managingOrg)
    {
        this._managingOrg = managingOrg;
    } //-- void setManagingOrg(org.astrogrid.registry.generated.package.ManagingOrg) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.AuthorityType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.AuthorityType.class, reader);
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
