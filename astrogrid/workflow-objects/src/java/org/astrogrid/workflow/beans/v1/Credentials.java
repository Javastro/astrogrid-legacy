/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Credentials.java,v 1.1 2004/02/20 18:36:39 nw Exp $
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
 * Class Credentials.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/02/20 18:36:39 $
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
    private java.lang.String _account;

    /**
     * Field _group
     */
    private java.lang.String _group;


      //----------------/
     //- Constructors -/
    //----------------/

    public Credentials() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.Credentials()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'account'.
     * 
     * @return the value of field 'account'.
     */
    public java.lang.String getAccount()
    {
        return this._account;
    } //-- java.lang.String getAccount() 

    /**
     * Returns the value of field 'group'.
     * 
     * @return the value of field 'group'.
     */
    public java.lang.String getGroup()
    {
        return this._group;
    } //-- java.lang.String getGroup() 

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
    public void setAccount(java.lang.String account)
    {
        this._account = account;
    } //-- void setAccount(java.lang.String) 

    /**
     * Sets the value of field 'group'.
     * 
     * @param group the value of field 'group'.
     */
    public void setGroup(java.lang.String group)
    {
        this._group = group;
    } //-- void setGroup(java.lang.String) 

    /**
     * Method unmarshalCredentials
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Credentials unmarshalCredentials(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Credentials) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Credentials.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Credentials unmarshalCredentials(java.io.Reader) 

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
