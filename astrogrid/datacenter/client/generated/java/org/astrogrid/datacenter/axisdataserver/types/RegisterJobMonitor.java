/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: RegisterJobMonitor.java,v 1.4 2003/11/21 17:30:19 nw Exp $
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
 * Class RegisterJobMonitor.
 * 
 * @version $Revision: 1.4 $ $Date: 2003/11/21 17:30:19 $
 */
public class RegisterJobMonitor implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _queryId
     */
    private org.astrogrid.datacenter.axisdataserver.types.QueryId _queryId;

    /**
     * Field _url
     */
    private java.lang.String _url;


      //----------------/
     //- Constructors -/
    //----------------/

    public RegisterJobMonitor() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.RegisterJobMonitor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'queryId'.
     * 
     * @return the value of field 'queryId'.
     */
    public org.astrogrid.datacenter.axisdataserver.types.QueryId getQueryId()
    {
        return this._queryId;
    } //-- org.astrogrid.datacenter.axisdataserver.types.QueryId getQueryId() 

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
    public void setQueryId(org.astrogrid.datacenter.axisdataserver.types.QueryId queryId)
    {
        this._queryId = queryId;
    } //-- void setQueryId(org.astrogrid.datacenter.axisdataserver.types.QueryId) 

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
     * Method unmarshalRegisterJobMonitor
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.RegisterJobMonitor unmarshalRegisterJobMonitor(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.RegisterJobMonitor) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.RegisterJobMonitor.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.RegisterJobMonitor unmarshalRegisterJobMonitor(java.io.Reader) 

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
