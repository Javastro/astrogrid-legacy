/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MakeQueryResponse.java,v 1.2 2003/11/19 15:26:41 nw Exp $
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
 * Class MakeQueryResponse.
 * 
 * @version $Revision: 1.2 $ $Date: 2003/11/19 15:26:41 $
 */
public class MakeQueryResponse implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _getQueryReturn
     */
    private java.lang.String _getQueryReturn;


      //----------------/
     //- Constructors -/
    //----------------/

    public MakeQueryResponse() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.MakeQueryResponse()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'getQueryReturn'.
     * 
     * @return the value of field 'getQueryReturn'.
     */
    public java.lang.String getGetQueryReturn()
    {
        return this._getQueryReturn;
    } //-- java.lang.String getGetQueryReturn() 

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
     * Sets the value of field 'getQueryReturn'.
     * 
     * @param getQueryReturn the value of field 'getQueryReturn'.
     */
    public void setGetQueryReturn(java.lang.String getQueryReturn)
    {
        this._getQueryReturn = getQueryReturn;
    } //-- void setGetQueryReturn(java.lang.String) 

    /**
     * Method unmarshalMakeQueryResponse
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.MakeQueryResponse unmarshalMakeQueryResponse(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.MakeQueryResponse) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.MakeQueryResponse.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.MakeQueryResponse unmarshalMakeQueryResponse(java.io.Reader) 

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
