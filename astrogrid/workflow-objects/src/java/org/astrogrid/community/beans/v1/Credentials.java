/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Credentials.java,v 1.4 2004/03/02 16:57:19 nw Exp $
 */

package org.astrogrid.community.beans.v1;

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
 * The full authorization and authentication credentials
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/02 16:57:19 $
 */
public class Credentials extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _account
     */
    private org.astrogrid.community.beans.v1.AccountType _account;

    /**
     * Field _group
     */
    private org.astrogrid.community.beans.v1.GroupType _group;

    /**
     * Field _securityToken
     */
    private java.lang.String _securityToken;


      //----------------/
     //- Constructors -/
    //----------------/

    public Credentials() {
        super();
    } //-- org.astrogrid.community.beans.v1.Credentials()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'account'.
     * 
     * @return the value of field 'account'.
     */
    public org.astrogrid.community.beans.v1.AccountType getAccount()
    {
        return this._account;
    } //-- org.astrogrid.community.beans.v1.AccountType getAccount() 

    /**
     * Returns the value of field 'group'.
     * 
     * @return the value of field 'group'.
     */
    public org.astrogrid.community.beans.v1.GroupType getGroup()
    {
        return this._group;
    } //-- org.astrogrid.community.beans.v1.GroupType getGroup() 

    /**
     * Returns the value of field 'securityToken'.
     * 
     * @return the value of field 'securityToken'.
     */
    public java.lang.String getSecurityToken()
    {
        return this._securityToken;
    } //-- java.lang.String getSecurityToken() 

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
     * Sets the value of field 'account'.
     * 
     * @param account the value of field 'account'.
     */
    public void setAccount(org.astrogrid.community.beans.v1.AccountType account)
    {
        this._account = account;
    } //-- void setAccount(org.astrogrid.community.beans.v1.AccountType) 

    /**
     * Sets the value of field 'group'.
     * 
     * @param group the value of field 'group'.
     */
    public void setGroup(org.astrogrid.community.beans.v1.GroupType group)
    {
        this._group = group;
    } //-- void setGroup(org.astrogrid.community.beans.v1.GroupType) 

    /**
     * Sets the value of field 'securityToken'.
     * 
     * @param securityToken the value of field 'securityToken'.
     */
    public void setSecurityToken(java.lang.String securityToken)
    {
        this._securityToken = securityToken;
    } //-- void setSecurityToken(java.lang.String) 

    /**
     * Method unmarshalCredentials
     * 
     * @param reader
     */
    public static org.astrogrid.community.beans.v1.Credentials unmarshalCredentials(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.community.beans.v1.Credentials) Unmarshaller.unmarshal(org.astrogrid.community.beans.v1.Credentials.class, reader);
    } //-- org.astrogrid.community.beans.v1.Credentials unmarshalCredentials(java.io.Reader) 

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
