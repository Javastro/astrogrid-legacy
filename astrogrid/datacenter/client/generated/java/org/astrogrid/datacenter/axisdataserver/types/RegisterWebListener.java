/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: RegisterWebListener.java,v 1.2 2003/11/19 15:26:41 nw Exp $
 */

package org.astrogrid.datacenter.axisdataserver.types;

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
 * Class RegisterWebListener.
 * 
 * @version $Revision: 1.2 $ $Date: 2003/11/19 15:26:41 $
 */
public class RegisterWebListener implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _queryId
     */
    private java.lang.String _queryId;

    /**
     * Field _url
     */
    private java.lang.String _url;


      //----------------/
     //- Constructors -/
    //----------------/

    public RegisterWebListener() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.RegisterWebListener()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'queryId'.
     * 
     * @return the value of field 'queryId'.
     */
    public java.lang.String getQueryId()
    {
        return this._queryId;
    } //-- java.lang.String getQueryId() 

    /**
     * Returns the value of field 'url'.
     * 
     * @return the value of field 'url'.
     */
    public java.lang.String getUrl()
    {
        return this._url;
    } //-- java.lang.String getUrl() 

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
     * Sets the value of field 'queryId'.
     * 
     * @param queryId the value of field 'queryId'.
     */
    public void setQueryId(java.lang.String queryId)
    {
        this._queryId = queryId;
    } //-- void setQueryId(java.lang.String) 

    /**
     * Sets the value of field 'url'.
     * 
     * @param url the value of field 'url'.
     */
    public void setUrl(java.lang.String url)
    {
        this._url = url;
    } //-- void setUrl(java.lang.String) 

    /**
     * Method unmarshalRegisterWebListener
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.RegisterWebListener unmarshalRegisterWebListener(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.RegisterWebListener) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.RegisterWebListener.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.RegisterWebListener unmarshalRegisterWebListener(java.io.Reader) 

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
