/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AccountType.java,v 1.4 2004/03/02 16:57:19 nw Exp $
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
 * An account that can log into the system
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/02 16:57:19 $
 */
public class AccountType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _accountID
     */
    private java.lang.String _accountID;

    /**
     * Field _community
     */
    private org.astrogrid.community.beans.v1.Community _community;


      //----------------/
     //- Constructors -/
    //----------------/

    public AccountType() {
        super();
    } //-- org.astrogrid.community.beans.v1.AccountType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'accountID'.
     * 
     * @return the value of field 'accountID'.
     */
    public java.lang.String getAccountID()
    {
        return this._accountID;
    } //-- java.lang.String getAccountID() 

    /**
     * Returns the value of field 'community'.
     * 
     * @return the value of field 'community'.
     */
    public org.astrogrid.community.beans.v1.Community getCommunity()
    {
        return this._community;
    } //-- org.astrogrid.community.beans.v1.Community getCommunity() 

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
     * Sets the value of field 'accountID'.
     * 
     * @param accountID the value of field 'accountID'.
     */
    public void setAccountID(java.lang.String accountID)
    {
        this._accountID = accountID;
    } //-- void setAccountID(java.lang.String) 

    /**
     * Sets the value of field 'community'.
     * 
     * @param community the value of field 'community'.
     */
    public void setCommunity(org.astrogrid.community.beans.v1.Community community)
    {
        this._community = community;
    } //-- void setCommunity(org.astrogrid.community.beans.v1.Community) 

    /**
     * Method unmarshalAccountType
     * 
     * @param reader
     */
    public static org.astrogrid.community.beans.v1.AccountType unmarshalAccountType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.community.beans.v1.AccountType) Unmarshaller.unmarshal(org.astrogrid.community.beans.v1.AccountType.class, reader);
    } //-- org.astrogrid.community.beans.v1.AccountType unmarshalAccountType(java.io.Reader) 

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
