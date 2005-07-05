/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Authority.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.registry;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.v10.resource.ResourceName;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * a naming authority; an assertion of control over a
 *  namespace represented by an authority identifier. 
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:01 $
 */
public class Authority extends org.astrogrid.registry.beans.v10.resource.Resource 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the organization that manages or owns the this authority.
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.ResourceName _managingOrg;


      //----------------/
     //- Constructors -/
    //----------------/

    public Authority() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.registry.Authority()


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
        
        if (obj instanceof Authority) {
        
            Authority temp = (Authority)obj;
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
     * organization that manages or owns the this authority.
     *  
     * 
     * @return the value of field 'managingOrg'.
     */
    public org.astrogrid.registry.beans.v10.resource.ResourceName getManagingOrg()
    {
        return this._managingOrg;
    } //-- org.astrogrid.registry.beans.v10.resource.ResourceName getManagingOrg() 

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
     * organization that manages or owns the this authority.
     *  
     * 
     * @param managingOrg the value of field 'managingOrg'.
     */
    public void setManagingOrg(org.astrogrid.registry.beans.v10.resource.ResourceName managingOrg)
    {
        this._managingOrg = managingOrg;
    } //-- void setManagingOrg(org.astrogrid.registry.beans.v10.resource.ResourceName) 

    /**
     * Method unmarshalAuthority
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.registry.Authority unmarshalAuthority(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.registry.Authority) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.registry.Authority.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.registry.Authority unmarshalAuthority(java.io.Reader) 

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
