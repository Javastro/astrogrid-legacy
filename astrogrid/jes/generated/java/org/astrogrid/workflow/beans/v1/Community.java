/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Community.java,v 1.2 2004/02/12 15:50:10 nw Exp $
 */

package org.astrogrid.workflow.beans.v1;

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
 * Class Community.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/02/12 15:50:10 $
 */
public class Community extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _token
     */
    private java.lang.String _token;

    /**
     * Field _credentials
     */
    private org.astrogrid.workflow.beans.v1.Credentials _credentials;


      //----------------/
     //- Constructors -/
    //----------------/

    public Community() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.Community()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'credentials'.
     * 
     * @return the value of field 'credentials'.
     */
    public org.astrogrid.workflow.beans.v1.Credentials getCredentials()
    {
        return this._credentials;
    } //-- org.astrogrid.workflow.beans.v1.Credentials getCredentials() 

    /**
     * Returns the value of field 'token'.
     * 
     * @return the value of field 'token'.
     */
    public java.lang.String getToken()
    {
        return this._token;
    } //-- java.lang.String getToken() 

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
     * Sets the value of field 'credentials'.
     * 
     * @param credentials the value of field 'credentials'.
     */
    public void setCredentials(org.astrogrid.workflow.beans.v1.Credentials credentials)
    {
        this._credentials = credentials;
    } //-- void setCredentials(org.astrogrid.workflow.beans.v1.Credentials) 

    /**
     * Sets the value of field 'token'.
     * 
     * @param token the value of field 'token'.
     */
    public void setToken(java.lang.String token)
    {
        this._token = token;
    } //-- void setToken(java.lang.String) 

    /**
     * Method unmarshalCommunity
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Community unmarshalCommunity(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Community) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Community.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Community unmarshalCommunity(java.io.Reader) 

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
