/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: GetResultsAndCloseResponse.java,v 1.1 2003/11/18 14:22:14 nw Exp $
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
 * Class GetResultsAndCloseResponse.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/11/18 14:22:14 $
 */
public class GetResultsAndCloseResponse implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _getResultsAndCloseReturn
     */
    private java.lang.String _getResultsAndCloseReturn;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetResultsAndCloseResponse() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.GetResultsAndCloseResponse()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'getResultsAndCloseReturn'.
     * 
     * @return the value of field 'getResultsAndCloseReturn'.
     */
    public java.lang.String getGetResultsAndCloseReturn()
    {
        return this._getResultsAndCloseReturn;
    } //-- java.lang.String getGetResultsAndCloseReturn() 

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
     * Sets the value of field 'getResultsAndCloseReturn'.
     * 
     * @param getResultsAndCloseReturn the value of field
     * 'getResultsAndCloseReturn'.
     */
    public void setGetResultsAndCloseReturn(java.lang.String getResultsAndCloseReturn)
    {
        this._getResultsAndCloseReturn = getResultsAndCloseReturn;
    } //-- void setGetResultsAndCloseReturn(java.lang.String) 

    /**
     * Method unmarshalGetResultsAndCloseResponse
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.GetResultsAndCloseResponse unmarshalGetResultsAndCloseResponse(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.GetResultsAndCloseResponse) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.GetResultsAndCloseResponse.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.GetResultsAndCloseResponse unmarshalGetResultsAndCloseResponse(java.io.Reader) 

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
