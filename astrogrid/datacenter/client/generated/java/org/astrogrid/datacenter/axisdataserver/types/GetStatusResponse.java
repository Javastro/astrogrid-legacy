/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: GetStatusResponse.java,v 1.4 2003/11/21 17:30:19 nw Exp $
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
 * Class GetStatusResponse.
 * 
 * @version $Revision: 1.4 $ $Date: 2003/11/21 17:30:19 $
 */
public class GetStatusResponse implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _getStatusReturn
     */
    private java.lang.String _getStatusReturn;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetStatusResponse() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.GetStatusResponse()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'getStatusReturn'.
     * 
     * @return the value of field 'getStatusReturn'.
     */
    public java.lang.String getGetStatusReturn()
    {
        return this._getStatusReturn;
    } //-- java.lang.String getGetStatusReturn() 

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
     * Sets the value of field 'getStatusReturn'.
     * 
     * @param getStatusReturn the value of field 'getStatusReturn'.
     */
    public void setGetStatusReturn(java.lang.String getStatusReturn)
    {
        this._getStatusReturn = getStatusReturn;
    } //-- void setGetStatusReturn(java.lang.String) 

    /**
     * Method unmarshalGetStatusResponse
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.GetStatusResponse unmarshalGetStatusResponse(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.GetStatusResponse) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.GetStatusResponse.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.GetStatusResponse unmarshalGetStatusResponse(java.io.Reader) 

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
