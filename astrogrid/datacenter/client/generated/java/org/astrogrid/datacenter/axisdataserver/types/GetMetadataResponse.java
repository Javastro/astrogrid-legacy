/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: GetMetadataResponse.java,v 1.2 2003/11/19 15:26:41 nw Exp $
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
 * Class GetMetadataResponse.
 * 
 * @version $Revision: 1.2 $ $Date: 2003/11/19 15:26:41 $
 */
public class GetMetadataResponse implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _getMetadataReturn
     */
    private java.lang.String _getMetadataReturn;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetMetadataResponse() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.GetMetadataResponse()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'getMetadataReturn'.
     * 
     * @return the value of field 'getMetadataReturn'.
     */
    public java.lang.String getGetMetadataReturn()
    {
        return this._getMetadataReturn;
    } //-- java.lang.String getGetMetadataReturn() 

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
     * Sets the value of field 'getMetadataReturn'.
     * 
     * @param getMetadataReturn the value of field
     * 'getMetadataReturn'.
     */
    public void setGetMetadataReturn(java.lang.String getMetadataReturn)
    {
        this._getMetadataReturn = getMetadataReturn;
    } //-- void setGetMetadataReturn(java.lang.String) 

    /**
     * Method unmarshalGetMetadataResponse
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.GetMetadataResponse unmarshalGetMetadataResponse(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.GetMetadataResponse) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.GetMetadataResponse.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.GetMetadataResponse unmarshalGetMetadataResponse(java.io.Reader) 

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
